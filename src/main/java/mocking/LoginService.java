package mocking;

public class LoginService extends LoginServiceContext {
    private final IAccountRepository accountRepository;
    
    public LoginService(IAccountRepository accountRepository) {
       super(new AwaitingFirstLoginAttempt());
       this.accountRepository = accountRepository;
    }
  
    public void login(String accountId, String password) {
       IAccount account = accountRepository.find(accountId);
  
       if (account == null)
          throw new AccountNotFoundException();
  
       getState().login(this, account, password);
    }

	public void setAccountTemporaryPassword(String string, String string2) {
		// TODO Auto-generated method stub
		
	}

	public void changePassword(String string, String string2, String string3) {
		// TODO Auto-generated method stub
		
	}

	public void checkPasswordIsNew(String string, String string2) {
		// TODO Auto-generated method stub
		
	}

	public void changePasswordAfterExpiration(String string, String string2, String string3) {
		// TODO Auto-generated method stub
		
	}
    
//    public void changePoassword(String accountId, String newpassword){
//    	IAccount account = accountRepository.find(accountId);
//    	if (account == null)
//            throw new AccountNotFoundException();
//    	
//    	
//    }
}
