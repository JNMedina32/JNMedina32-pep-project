package DAO;

import Model.Account;
import Util.ConnectionUtil;
import java.sql.*;

public class AccountDAO {
  /**
   * @param account to be inserted into account table
   * @return new Account with auto-generated account_id
   */
  public Account createAccount(Account account) {
      Connection connection = ConnectionUtil.getConnection();
      try {
          String sql = "INSERT INTO account(username, password) VALUES (?, ?);";
          PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
          preparedStatement.setString(1, account.username);
          preparedStatement.setString(2, account.password);
          preparedStatement.executeUpdate();
          ResultSet resultSet = preparedStatement.getGeneratedKeys();
          if (resultSet.next()) {
              int generated_account_id = (int) resultSet.getLong("account_id");
              return new Account(generated_account_id, account.getUsername(), account.getPassword());
          }
      } catch (SQLException e) {
          System.out.println(e.getMessage());
      }
      return null;
  }
  /**
   * @param username
   * @param password
   * @return account_id
   */
  public Account userLogin(Account account) {
      Connection connection = ConnectionUtil.getConnection();
      try {
          String sql = "SELECT * FROM account WHERE username = ? AND password = ?;";
          PreparedStatement preparedStatement = connection.prepareStatement(sql);
          preparedStatement.setString(1, account.getUsername());
          preparedStatement.setString(2, account.getPassword());
          preparedStatement.executeQuery();
          ResultSet resultSet = preparedStatement.getResultSet();
          if (resultSet.next()) {
              int account_id = resultSet.getInt("account_id");
              String username = resultSet.getString("username");
              String password = resultSet.getString("password");
              return new Account(account_id, username, password);
          }
      } catch (SQLException e) {
          System.out.println(e.getMessage());
      }
      return null;
  }






  
  /**
   * Checks if username is already in use
   * @param username
   * @return the .execute() will return true if username exists, else false
   */
  public boolean usernameAlreadyExists(String username) {
      Connection connection = ConnectionUtil.getConnection();
      try {
          String sql = "SELECT username FROM account WHERE username = ?;";
          PreparedStatement preparedStatement = connection.prepareStatement(sql);
          preparedStatement.setString(1, username);
          return preparedStatement.execute();
      } catch (SQLException e) {
          System.out.println(e.getMessage());
        }
        return false;
    }
}
