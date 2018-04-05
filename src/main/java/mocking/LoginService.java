package mocking;

public class LoginService {
	private final IAccountRepository accountRepository;
	 
    public LoginService(IAccountRepository accountRepository) {
      this.accountRepository = accountRepository;
    }
 
    public void login(String accountId, String password) {
      IAccount account = accountRepository.find(accountId);
      account.setLoggedIn(true);
    }
}
