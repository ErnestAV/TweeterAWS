package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.server.dao.MainDAOFactoryInterface;
import edu.byu.cs.tweeter.server.dao.dynamoDAO.FactoryDynamoDAO;

public abstract class BaseHandler<T, O> implements RequestHandler<T, O> {
    protected MainDAOFactoryInterface factoryDAO = new FactoryDynamoDAO();
}
