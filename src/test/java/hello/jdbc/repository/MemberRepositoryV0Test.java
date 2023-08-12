package hello.jdbc.repository;

import hello.jdbc.domain.Member;
import java.sql.SQLException;
import java.util.NoSuchElementException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class MemberRepositoryV0Test {

  MemberRepositoryV0 memberRepository = new MemberRepositoryV0();

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