package edu.byu.cs.tweeter.server.dao.dynamoDAO;

import java.util.List;
import java.util.UUID;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.FollowersCountRequest;
import edu.byu.cs.tweeter.model.net.request.FollowingCountRequest;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.request.LogoutRequest;
import edu.byu.cs.tweeter.model.net.request.RegisterRequest;
import edu.byu.cs.tweeter.model.net.request.UserRequest;
import edu.byu.cs.tweeter.model.net.response.FollowersCountResponse;
import edu.byu.cs.tweeter.model.net.response.FollowingCountResponse;
import edu.byu.cs.tweeter.model.net.response.LoginResponse;
import edu.byu.cs.tweeter.model.net.response.LogoutResponse;
import edu.byu.cs.tweeter.model.net.response.RegisterResponse;
import edu.byu.cs.tweeter.model.net.response.UserResponse;
import edu.byu.cs.tweeter.server.dao.UserDAOInterface;
import edu.byu.cs.tweeter.server.dao.dynamoDAO.bean.AuthBeanToken;
import edu.byu.cs.tweeter.server.dao.dynamoDAO.bean.UserBean;
import edu.byu.cs.tweeter.server.util.PBKDF2WithHmacSHA1Hashing;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.BatchWriteItemEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.BatchWriteResult;
import software.amazon.awssdk.enhanced.dynamodb.model.WriteBatch;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;

public class UserDynamoDAO implements UserDAOInterface {
    private static final String TableName = "users";
    private static final String UserAttr = "username";
    private static final String AuthenticationTableName = "[insert here when created]";
    private static final String TimeStampAttr = "timestamp";
    private final PBKDF2WithHmacSHA1Hashing encryptor = new PBKDF2WithHmacSHA1Hashing();
    static Region region = Region.US_EAST_1;
    static DynamoDbClient dynamoDbClient = DynamoDbClient.builder()
            .region(region)
            .build();
    static DynamoDbEnhancedClient dynamoDbEnhancedClient = DynamoDbEnhancedClient.builder().dynamoDbClient(dynamoDbClient).build();

    static final DynamoDbTable<AuthBeanToken> authBeanTokenDynamoDbTable = dynamoDbEnhancedClient.table(AuthenticationTableName,
            TableSchema.fromBean(AuthBeanToken.class));

    static final DynamoDbTable<UserBean> userBeanDynamoDbTable = dynamoDbEnhancedClient.table(TableName, TableSchema.fromBean(UserBean.class));

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        Key key = Key.builder()
                .partitionValue(loginRequest.getUsername()).build();
        UserBean userBean = userBeanDynamoDbTable.getItem(key);

        if (userBean == null) {
            throw new RuntimeException("[Bad Request] invalid username or password");
        }

        boolean passwordCorrect;
        try {
            passwordCorrect = encryptor.validatePassword(loginRequest.getPassword(), userBean.getPassword());
        } catch (Exception e) {
            e.printStackTrace();
            return new LoginResponse(e.getMessage());
        }
        if (passwordCorrect) {
            User returnUser = new User(userBean.getFirstName(), userBean.getLastName(), userBean.getUsername(),
                    userBean.getImageUrl());
            AuthToken authToken = putAuthentication(returnUser.getAlias());
            return new LoginResponse(returnUser, authToken);
        }
        throw new RuntimeException("[Bad Request] Invalid username or password.");
    }

    @Override
    public RegisterResponse register(RegisterRequest registerRequest) {
        String imageURL = new S3DynamoBucket().storeImage(registerRequest.getImage(), registerRequest.getUsername());

        String hashed;
        try {
            hashed = encryptor.generateStrongPasswordHash(registerRequest.getPassword());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("[Internal Server Error]" + e.getMessage());
        }

        // Setting up the user object
        UserBean userBean = new UserBean();
        userBean.setFirstName(registerRequest.getFirstName());
        userBean.setLastName(registerRequest.getLastName());
        userBean.setUsername(registerRequest.getUsername());
        userBean.setImageUrl(imageURL);
        userBean.setPassword(hashed);

        // Set to 0 because it is a new user (duh)
        userBean.setFollowersCount(0);
        userBean.setFolloweesCount(0);

        try {
            userBeanDynamoDbTable.putItem(userBean);

            User responseUser = new User(userBean.getFirstName(), userBean.getLastName(),
                    userBean.getUsername(), userBean.getImageUrl());

            AuthToken responseToken = putAuthentication(userBean.getUsername());

            return new RegisterResponse(responseUser, responseToken);
        } catch (DynamoDbException e) {
            e.printStackTrace();
            throw new RuntimeException("[Bad Request] " + e.getMessage());
        }
    }

    @Override
    public LogoutResponse logout(LogoutRequest logoutRequest) {
        assert logoutRequest.getAuthToken() != null;
        AuthBeanToken authBeanToken = new AuthBeanToken();
        authBeanToken.setToken(logoutRequest.getAuthToken());
        deleteAuthentication(authBeanToken);
        return new LogoutResponse();
    }

    @Override
    public FollowersCountResponse getFollowersCount(FollowersCountRequest followersCountRequest) {
        Key key = Key.builder()
                .partitionValue(followersCountRequest.getAuthToken())
                .build();
        UserBean userBean = userBeanDynamoDbTable.getItem(key);
        return new FollowersCountResponse(userBean.getFollowersCount());
    }

    @Override
    public FollowingCountResponse getFolloweeCount(FollowingCountRequest followingCountRequest) {
        Key key = Key.builder()
                .partitionValue(followingCountRequest.getAuthToken())
                .build();
        UserBean userBean = userBeanDynamoDbTable.getItem(key);
        return new FollowingCountResponse(userBean.getFolloweesCount());
    }

    @Override
    public UserResponse getUser(UserRequest userRequest) {
        DynamoDbTable<UserBean> table = dynamoDbEnhancedClient.table(TableName, TableSchema.fromBean(UserBean.class));
        Key key = Key.builder()
                .partitionValue(userRequest.getUserAlias())
                .build();
        UserBean userBean;
        try {
            userBean = table.getItem(key);
        } catch (DynamoDbException e) {
            e.printStackTrace();
            throw new RuntimeException("[Internal Server Error] " + e.getMessage()); // Would it be internal server error or bad request?
        }

        if (userBean == null) throw new RuntimeException("[Bad Request]. User does not exist"); // Would it be internal server error or bad request?

        if (!isTokenStillValid(userRequest.getAuthToken())) {
            return new UserResponse("Session is expired.");
        }

        User user = new User(userBean.getFirstName(), userBean.getLastName(), userBean.getUsername(),
                userBean.getImageUrl());

        AuthToken authToken = new AuthToken();
        authToken.setToken(userRequest.getAuthToken());

        return new UserResponse(user, authToken);
    }

    public boolean isTokenStillValid(String authToken) {
        Key key = Key.builder()
                .partitionValue(authToken)
                .build();
        AuthBeanToken authBeanToken = authBeanTokenDynamoDbTable.getItem(key);

        if (System.currentTimeMillis() - authBeanToken.getTimeStamp() < 120000) { // 2 minutes to be able to test it
            authBeanToken.setTimeStamp(System.currentTimeMillis());
            authBeanTokenDynamoDbTable.putItem(authBeanToken);
            return true;
        }

        return false;
    }

//    public void writePortionOfUserBeans(List<UserBean> userBeans) {
//        if(userBeans.size() > 25)
//            throw new RuntimeException("Too many users to write");
//
//
//        DynamoDbTable<UserBean> table = client.table(TableName, TableSchema.fromBean(UserBean.class));
//        WriteBatch.Builder<UserBean> writeBuilder = WriteBatch.builder(UserBean.class).mappedTableResource(table);
//        for (UserBean item : userBeans) {
//            writeBuilder.addPutItem(builder -> builder.item(item));
//        }
//        BatchWriteItemEnhancedRequest batchWriteItemEnhancedRequest = BatchWriteItemEnhancedRequest.builder()
//                .writeBatches(writeBuilder.build()).build();
//
//        try {
//            BatchWriteResult result = client.batchWriteItem(batchWriteItemEnhancedRequest);
//
//            // just hammer dynamodb again with anything that didn't get written this time
//            if (result.unprocessedPutItemsForTable(table).size() > 0) {
//                writePortionOfUserBeans(result.unprocessedPutItemsForTable(table));
//            }
//
//        } catch (DynamoDbException e) {
//            System.err.println(e.getMessage());
//            System.exit(1);
//        }
//    }

    public AuthToken putAuthentication(String userAlias) {
        AuthBeanToken authBeanToken = new AuthBeanToken();
        authBeanToken.setUserAlias(userAlias);
        authBeanToken.setToken(generateRandomUUIDString());
        authBeanToken.setTimeStamp(System.currentTimeMillis());

        authBeanTokenDynamoDbTable.putItem(authBeanToken);
        return new AuthToken(authBeanToken.getToken(), String.valueOf(authBeanToken.getTimeStamp()));
    }

    public void deleteAuthentication(AuthBeanToken authBeanToken) {
        Key key = Key.builder()
                .partitionValue(authBeanToken.getToken())
                .build();
        authBeanTokenDynamoDbTable.deleteItem(key);
    }

    public String generateRandomUUIDString() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }
}
