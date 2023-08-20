package hello.jdbc.repository;

import hello.jdbc.domain.Member;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.NoSuchElementException;
import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.support.JdbcUtils;

/**
 * JDBC - Connection Param
 */
@Slf4j
public class MemberRepositoryV2 {

  private final DataSource dataSource;

  public MemberRepositoryV2(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  public Member save(Member member) throws SQLException {
    String sql = "insert into member(member_id, money) values (?, ?)"; // (?, ?) 파라미터 바인딩 사용해야함
    //문자열로 할 경우 SQL Injection 공격에 취약

    Connection con = null;
    PreparedStatement pstmt = null;

    try {
      con = getConnection();
      pstmt = con.prepareStatement(sql);
      pstmt.setString(1, member.getMemberId());
      pstmt.setInt(2, member.getMoney());
      pstmt.executeUpdate();

      return member;
    }catch (SQLException e) {
      log.error("db error", e);
      throw e;
    }finally {
      close(con, pstmt, null);
    }
  }

  public void delete(String memberId) throws SQLException {
    String sql = "delete member where member_id=?";

    Connection con = null;
    PreparedStatement pstmt = null;

    try {
      con = getConnection();
      pstmt = con.prepareStatement(sql);
      pstmt.setString(1, memberId);
      pstmt.executeUpdate();
    }catch (SQLException e) {
      log.error("db error", e);
      throw e;
    }finally {
      close(con, pstmt, null);
    }
  }

  public void update(String memberId, int money) throws SQLException {
    String sql = "update member set money=? where member_id=?";

    Connection con = null;
    PreparedStatement pstmt = null;

    try {
      con = getConnection();
      pstmt = con.prepareStatement(sql);
      pstmt.setInt(1, money);
      pstmt.setString(2, memberId);
      int resultSize = pstmt.executeUpdate();
    }catch (SQLException e) {
      log.error("db error", e);
      throw e;
    }finally {
      close(con, pstmt, null);
    }
  }

  public void update(Connection con, String memberId, int money) throws SQLException {
    String sql = "update member set money=? where member_id=?";

    PreparedStatement pstmt = null;

    try {
      pstmt = con.prepareStatement(sql);
      pstmt.setInt(1, money);
      pstmt.setString(2, memberId);
      int resultSize = pstmt.executeUpdate();
    }catch (SQLException e) {
      log.error("db error", e);
      throw e;
    }finally {
      JdbcUtils.closeStatement(pstmt);
    }
  }

  public Member findById(String memberId) throws SQLException {
    String sql = "select * from member where member_id = ?";

    Connection con = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;

    try
    {
      con = getConnection();
      pstmt = con.prepareStatement(sql);
      pstmt.setString(1, memberId);
      rs = pstmt.executeQuery();

      if(rs.next()) {
        Member member = new Member();
        member.setMemberId(rs.getString("member_id"));
        member.setMoney(rs.getInt("money") );
        return member;
      } else{
        throw new NoSuchElementException("member not found memberId =" + memberId);
      }

    }catch (SQLException e){
      log.error("db error", e);
      throw e;
    }finally {
      close(con, pstmt, rs);
    }
  }

  public Member findById(Connection con, String memberId) throws SQLException {
    String sql = "select * from member where member_id = ?";

    PreparedStatement pstmt = null;
    ResultSet rs = null;

    try
    {
      pstmt = con.prepareStatement(sql);
      pstmt.setString(1, memberId);
      rs = pstmt.executeQuery();

      if(rs.next()) {
        Member member = new Member();
        member.setMemberId(rs.getString("member_id"));
        member.setMoney(rs.getInt("money") );
        return member;
      } else{
        throw new NoSuchElementException("member not found memberId =" + memberId);
      }

    }catch (SQLException e){
      log.error("db error", e);
      throw e;
    }finally {
      JdbcUtils.closeResultSet(rs);
    }

  }

  private void close(Connection con, Statement stmt, ResultSet rs) throws SQLException {
    JdbcUtils.closeResultSet(rs);
    JdbcUtils.closeStatement(stmt);
    JdbcUtils.closeConnection(con);

  }

  private Connection getConnection() throws SQLException {
    return dataSource.getConnection();
  }

}