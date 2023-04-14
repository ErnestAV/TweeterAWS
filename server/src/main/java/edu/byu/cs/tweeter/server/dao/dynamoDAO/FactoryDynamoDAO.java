package edu.byu.cs.tweeter.server.dao.dynamoDAO;

import edu.byu.cs.tweeter.server.dao.FollowDAOInterface;
import edu.byu.cs.tweeter.server.dao.MainDAOFactoryInterface;
import edu.byu.cs.tweeter.server.dao.StatusDAOInterface;
import edu.byu.cs.tweeter.server.dao.UserDAOInterface;

public class FactoryDynamoDAO implements MainDAOFactoryInterface {
    @Override
    public FollowDAOInterface getFollowDAO() {
        return new FollowDynamoDAO();
    }

    @Override
    public StatusDAOInterface getStatusDAO() {
        return new StatusDynamoDAO();
    }

    @Override
    public UserDAOInterface getUserDAO() {
        return new UserDynamoDAO();
    }
}
