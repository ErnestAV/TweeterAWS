package edu.byu.cs.tweeter.server.dao;
public interface AuthenticationDAOInterface {
    public void putAuthentication(String userAlias);
    public void deleteAuthentication();
}
