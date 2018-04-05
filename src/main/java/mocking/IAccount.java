package mocking;

public interface IAccount {
	boolean passwordMatches(String candiate);
    void setLoggedIn(boolean value);
    void setRevoked(boolean value);
    boolean isLoggedIn();
    boolean isRevoked();
    String getId();
}
