package hello.jdbc.repository;

import hello.jdbc.connection.DBConnectionUtil;
import hello.jdbc.domain.Member;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import lombok.extern.slf4j.Slf4j;

/**
 * JDBC - DriverManager 사용
 */
@Slf4j
public class MemberRepositoryV0 {

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

  private void close(Connection con, Statement stmt, ResultSet rs) throws SQLException {
    if (rs != null) {
      try {
        rs.close();
      } catch (SQLException e) {
        log.info("error", e);
      }
    }
    if (stmt != null) {
      try {
        stmt.close();
      } catch (SQLException e) {
        log.info("error", e);
      }
    }
    if (con != null) {
      try {
        con.close();
      } catch (SQLException e) {
        log.info("error", e);
      }
    }
  }

  private Connection getConnection(){
    return DBConnectionUtil.getConnection();
  }

}
