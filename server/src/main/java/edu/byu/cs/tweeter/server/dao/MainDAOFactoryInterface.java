package edu.byu.cs.tweeter.server.dao;

public interface MainDAOFactoryInterface {
    public FollowDAOInterface getFollowDAO();
    public StatusDAOInterface getStatusDAO();
    public UserDAOInterface getUserDAO();
}
