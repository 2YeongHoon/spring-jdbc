package hello.jdbc.service;

import hello.jdbc.domain.Member;
import hello.jdbc.repository.MemberRepositoryV3;
import java.sql.SQLException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

/**
 * 트랜잭션 - 트랜잭션 매니저
 */
@Slf4j
@RequiredArgsConstructor
public class MemberServiceV3_1 {

  private final PlatformTransactionManager transactionManager;
  private final MemberRepositoryV3 memberRepository;

  public void accountTransfer(String fromId, String toId, int money) {

    TransactionStatus status = transactionManager.getTransaction(
        new DefaultTransactionDefinition());

    try {
      bizLogic(fromId, toId, money);
      transactionManager.commit(status);
    } catch (Exception e) {
      transactionManager.rollback(status);
      throw new IllegalStateException(e);
    }
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
