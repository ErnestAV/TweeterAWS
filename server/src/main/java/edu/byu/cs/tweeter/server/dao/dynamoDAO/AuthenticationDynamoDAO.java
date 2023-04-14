package edu.byu.cs.tweeter.server.dao.dynamoDAO;

import java.util.Random;
import java.util.UUID;

import edu.byu.cs.tweeter.server.dao.AuthenticationDAOInterface;
import edu.byu.cs.tweeter.server.dao.dynamoDAO.bean.AuthBeanToken;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

public class AuthenticationDynamoDAO implements AuthenticationDAOInterface {


    static final DynamoDbClient dynamoDbClient = DynamoDbClient.builder()
            .credentialsProvider(ProfileCredentialsProvider.create()).region(Region.US_EAST_1).build();

    static final DynamoDbEnhancedClient dynamoDbEnhancedClient = DynamoDbEnhancedClient.builder().
            dynamoDbClient(dynamoDbClient).build();



}
