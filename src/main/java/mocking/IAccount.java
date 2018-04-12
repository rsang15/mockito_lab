package mocking;

public interface IAccount {
	boolean passwordMatches(String candidate);
	
    void setLoggedIn(boolean value);
    void setRevoked(boolean value);
    void setPasswordReset(boolean value);
	void setPasswordIsChanged(boolean value);
	void setPasswordIsExpired(boolean b);
	void setLoggedOut(boolean b);
	
    boolean isLoggedIn();
    boolean isLoggedOut();
    boolean isRevoked();
    boolean passwordIsExpired();
    Boolean passwordIsChanged();
    String getId();

	
}
