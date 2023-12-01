package Service;

import DAO.AccountDAO;
import Model.Account;

public class AccountService {
  public AccountDAO accountDAO;

  public AccountService(){
    accountDAO = new AccountDAO();
  }

  /**
   * @param account obj with username & password to insert
   * @return created account
   */
  public Account createAccount(Account account){
    if(verifyUsernameAndPassword(account.getUsername(), account.getPassword()) == false){
      return null;
    }
    return accountDAO.createAccount(account);
  }

  /**
   * @param account obj 
   * @return users account
   */
  public Account userLogin(Account account){
    return accountDAO.userLogin(account);
  }

  /**
   * This method checks if: 
   *   username is blank OR 
   *   password.length < 4 OR 
   *   username already exists
   * @param username
   * @param password
   * @return boolean
   */
  public boolean verifyUsernameAndPassword(String username, String password){
    boolean usernameAlreadyExists = accountDAO.usernameAlreadyExists(username);
    if(username == "" || password.length() < 4 || usernameAlreadyExists == false){
      return false;
    }
    return true;
  }
  
}


