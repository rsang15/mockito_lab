package mockingtest;
 
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
 
import org.junit.Before;
import org.junit.Test;

import mocking.*;
 
public class LoginServiceTest {
   private IAccount account;
   private IAccountRepository accountRepository;
   private LoginService service;
 
   @Before
   public void init() {
      account = mock(IAccount.class);
      when(account.getId()).thenReturn("brett");
      accountRepository = mock(IAccountRepository.class);
      when(accountRepository.find(anyString())).thenReturn(account);
      service = new LoginService(accountRepository);
   }
 
   private void willPasswordMatch(boolean value) {
      when(account.passwordMatches(anyString())).thenReturn(value);
   }
 
   @Test
   public void itShouldSetAccountToLoggedInWhenPasswordMatches() {
      willPasswordMatch(true);
      service.login("brett", "password");
      verify(account, times(1)).setLoggedIn(true);
   }
 
   @Test
   public void itShouldSetAccountToRevokedAfterThreeFailedLoginAttempts() {
      willPasswordMatch(false);
 
      for (int i = 0; i < 3; ++i)
         service.login("brett", "password");
 
      verify(account, times(1)).setRevoked(true);
   }
 
   @Test
   public void itShouldNotSetAccountLoggedInIfPasswordDoesNotMatch() {
      willPasswordMatch(false);
      service.login("brett", "password");
      verify(account, never()).setLoggedIn(true);
   }
 
   @Test
   public void itShouldNotRevokeSecondAccountAfterTwoFailedAttemptsFirstAccount() {
      willPasswordMatch(false);
 
      IAccount secondAccount = mock(IAccount.class);
      when(secondAccount.passwordMatches(anyString())).thenReturn(false);
      when(accountRepository.find("schuchert")).thenReturn(secondAccount);
 
      service.login("brett", "password");
      service.login("brett", "password");
      service.login("schuchert", "password");
 
      verify(secondAccount, never()).setRevoked(true);
   }
 
   @Test(expected = AccountLoginLimitReachedException.class)
   public void itShouldNowAllowConcurrentLogins() {
      willPasswordMatch(true);
      when(account.isLoggedIn()).thenReturn(true);
      service.login("brett", "password");
   }
 
   @Test(expected = AccountNotFoundException.class)
   public void itShouldThrowExceptionIfAccountNotFound() {
      when(accountRepository.find("schuchert")).thenReturn(null);
      service.login("schuchert", "password");
   }
 
   @Test(expected = AccountRevokedException.class)
   public void ItShouldNotBePossibleToLogIntoRevokedAccount() {
      willPasswordMatch(true);
      when(account.isRevoked()).thenReturn(true);
      service.login("brett", "password");
   }
 
   @Test
   public void itShouldResetBackToInitialStateAfterSuccessfulLogin() {
      willPasswordMatch(false);
      service.login("brett", "password");
      service.login("brett", "password");
      willPasswordMatch(true);
      service.login("brett", "password");
      willPasswordMatch(false);
      service.login("brett", "password");
      verify(account, never()).setRevoked(true);
   }
   /**
    * ADDED TESTS FOR LAB START HERE
    * names map to requirements respectively
    */
   
   @Test
   public void itShouldNotAllowLoginWithExpiredPassword(){
	   willPasswordMatch(false);
	   when(account.isLoggedIn()).thenReturn(true);
	   //set password to expired
	   account.setPasswordIsExpired(true);
	   //try to login
	   service.login("brett", "password");
	   //verify it didnt happen (not logged in"
	   verify(account, never()).isLoggedIn();
   }
   
   @Test
   public void itShouldAllowLoginWithExpiredPasswordAfterChangingPassword(){
	   willPasswordMatch(true);
	   //change password 
	   account.setPasswordReset(true);
	   service.changePasswordAfterExpiration("brett", "brettnewpassword", "expiredpassword");
	   //try to login 
	   service.login("brett", "brettnewpassword");
	   //verify it happened (logged in)
	   verify(account, times(1)).setLoggedIn(true);
   }
   
   @Test
   public void itShouldNotAllowLoginWithTemporaryPassword(){
	   willPasswordMatch(false);
	   when(account.isLoggedIn()).thenReturn(true);
	   //set temporary password
	   service.setAccountTemporaryPassword("brett", "tmppassword");
	   //try to login
	   service.login("brett","tmppassword");
	   //check it didnt happen
	   verify(account, never()).isLoggedIn();
   }
   @Test
   public void itShouldAllowLoginWithTempPasswordAfterChangingPassword(){
	   willPasswordMatch(true);
	   //key line here - the account has to be in that state
	   account.setPasswordReset(true);
	   service.setAccountTemporaryPassword("brett", "tmppassword");
	   //using temp password, change to new password
	   service.changePassword("brett", "newpassword", "tmppassword");
	   account.setLoggedIn(false);
	   //have user login with new password
	   service.login("brett","newpassword");
	   account.setLoggedIn(true);
	   //account.setLoggedIn(true);
	   verify(account, times(2)).setLoggedIn(true);
   }
   
   @Test
   public void itShouldNotAllowPasswordChangeToAnyOfPrev24Passwords(){
	   //try to change password after logging in
	   when(account.passwordIsChanged()).thenReturn(true);
	   service.setAccountTemporaryPassword("brett", "tmppassword");
	   //using temp password, change to new password
	   service.changePassword("brett","newpassword22", "tmppassword");
	   service.checkPasswordIsNew("brett", "newpassword22");//not true, run in to exception
	   verify(account, never()).passwordIsChanged();
   }
   
   @Test
   public void canChangePasswordToOneOfPreviousIfGreaterThan24ChangesSinceLastUse(){
	   when(account.passwordIsChanged()).thenReturn(true);
	   service.setAccountTemporaryPassword("brett", "tmppassword");
	   //use temp password to change password
	   service.changePassword("brett","newpassword25", "tmppassword");
	   service.checkPasswordIsNew("brett", "newpassword25");
	   account.setPasswordIsChanged(true);
	   service.login("brett", "newpassword25");
	   verify(account, times(1)).setPasswordIsChanged(true);
   }
}