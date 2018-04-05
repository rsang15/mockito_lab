package mocking;

public interface IAccount {
	void setLoggedIn(boolean value);
	boolean passwordMatches(String candidate);
}
