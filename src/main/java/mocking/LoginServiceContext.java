package mocking;

public class LoginServiceContext {
	private LoginServiceState state;
	 
	   public LoginServiceContext(LoginServiceState state) {
	      this.state = state;
	   }
	 
	   public void setState(LoginServiceState state) {
	      this.state = state;
	   }
	 
	   public LoginServiceState getState() {
	      return state;
	   }
	   
}
