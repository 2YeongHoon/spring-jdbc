package hello.jdbc.repository;

import com.zaxxer.hikari.HikariDataSource;
import hello.jdbc.connection.ConnectionConst;
import hello.jdbc.domain.Member;
import java.sql.SQLException;
import java.util.NoSuchElementException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MemberRepositoryV1Test {

  MemberRepositoryV1 memberRepository;

  @BeforeEach
  void beforeEach() throws SQLException {
//    DriverManagerDataSource dataSource = new DriverManagerDataSource(
//        ConnectionConst.URL,
//        ConnectionConst.USERNAME,
//        ConnectionConst.PASSWORD);

    HikariDataSource dataSource = new HikariDataSource();
    dataSource.setJdbcUrl(ConnectionConst.URL);
    dataSource.setUsername(ConnectionConst.USERNAME);
    dataSource.setPassword(ConnectionConst.PASSWORD);
    dataSource.setMaximumPoolSize(10);
    dataSource.setPoolName("MyPool");

    memberRepository = new MemberRepositoryV1(dataSource);
  }

  @Test
  void crud() throws SQLException {
    // save
    Member member = new Member("memberV100", 10000);
    memberRepository.save(member);

    // findById
    Member findMember = memberRepository.findById(member.getMemberId());
    Assertions.assertThat(member).isEqualTo(findMember);

    // update
    memberRepository.update(member.getMemberId(), 20000);
    Member updateMember = memberRepository.findById(member.getMemberId());
    Assertions.assertThat(updateMember.getMoney()).isEqualTo(20000);

    // delete
    memberRepository.delete(member.getMemberId());
    Assertions.assertThatThrownBy(() -> memberRepository.findById(member.getMemberId()))
        .isInstanceOf(NoSuchElementException.class);
  }

}