package hello.jdbc.service;

import hello.jdbc.domain.Member;
import hello.jdbc.repository.MemberRepositoryV3;
import java.sql.SQLException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * 트랜잭션 - @Transactional - AOP
 */
@Slf4j
@Transactional
public class MemberServiceV3_3 {

  private final MemberRepositoryV3 memberRepository;

  public MemberServiceV3_3(MemberRepositoryV3 memberRepository) {
    this.memberRepository = memberRepository;
  }

  public void accountTransfer(String fromId, String toId, int money) throws SQLException {
    bizLogic(fromId, toId, money);
  }

  private void bizLogic(String fromId, String toId, int money) throws SQLException {
    Member fromMember = memberRepository.findById(fromId);
    Member toMember = memberRepository.findById(toId);

    memberRepository.update(fromMember.getMemberId(), fromMember.getMoney() - money);
    validation(toMember);
    memberRepository.update(toMember.getMemberId(), toMember.getMoney() + money);
  }

  private void validation(Member member) {
    if (member.getMemberId().equals("ex")) {
      throw new IllegalStateException("이체중 예외 발생");
    }
  }

}
