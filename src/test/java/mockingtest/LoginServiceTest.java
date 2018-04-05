package mockingtest;

import org.junit.Test;

import mocking.IAccount;
import mocking.IAccountRepository;
import mocking.LoginService;

import static org.mockito.Mockito.*;
 
public class LoginServiceTest {
 
   @Test
   public void itShouldSetAccountToLoggedInWhenPasswordMatches() {
      IAccount account = mock(IAccount.class);
      when(account.passwordMatches(anyString())).thenReturn(true);
 
      IAccountRepository accountRepository = mock(IAccountRepository.class);
      when(accountRepository.find(anyString())).thenReturn(account);
 
      LoginService service = new LoginService(accountRepository);
 
      service.login("brett", "password");
 
      verify(account, times(1)).setLoggedIn(true);
   }
}