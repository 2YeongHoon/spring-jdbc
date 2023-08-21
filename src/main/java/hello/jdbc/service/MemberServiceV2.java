package hello.jdbc.service;

import hello.jdbc.domain.Member;
import hello.jdbc.repository.MemberRepositoryV2;
import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 트랜잭션 - 파라미터 연동, 풀 고려한 종료
 */
@Slf4j
@RequiredArgsConstructor
public class MemberServiceV2 {

  private final DataSource dataSource;
  private final MemberRepositoryV2 memberRepository;

  public void accountTransfer(String fromId, String toId, int money) throws SQLException {
    Connection con = dataSource.getConnection();

    try
    {
      con.setAutoCommit(false);
      vizLogin(con, fromId, toId, money);
      con.commit();
    }catch (Exception e) {
      con.rollback();
      throw new IllegalStateException(e);
    } finally {
      release(con);
    }
  }

  private void release(Connection con) {
    if (con != null) {
      try {
        con.setAutoCommit(true); // 풀에 반환하기전에 돌려놔야함. 안그러면 풀에 false상태로 반환됨.
        con.close();
      } catch (Exception e) {
        log.info("exception:", e);
      }
    }
  }

  private void vizLogin(Connection con, String fromId, String toId, int money) throws SQLException {
    Member fromMember = memberRepository.findById(con, fromId);
    Member toMember = memberRepository.findById(con, toId);

    memberRepository.update(con, fromMember.getMemberId(), fromMember.getMoney() - money);
    validation(toMember);
    memberRepository.update(con, toMember.getMemberId(), toMember.getMoney() + money);
  }

  private void validation(Member member) {
    if(member.getMemberId().equals("ex")){
      throw new IllegalStateException("이체중 예외 발생");
    }
  }

}
