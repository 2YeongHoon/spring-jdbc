package hello.jdbc.repository;

import hello.jdbc.domain.Member;
import java.sql.SQLException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class MemberRepositoryV0Test {

  MemberRepositoryV0 memberRepository = new MemberRepositoryV0();

  @Test
  void crud() throws SQLException {
    // save
    Member member = new Member("memberV2", 10000);
    memberRepository.save(member);

    // findById
    Member findMember = memberRepository.findById(member.getMemberId());
    Assertions.assertThat(member).isEqualTo(findMember);
  }

}