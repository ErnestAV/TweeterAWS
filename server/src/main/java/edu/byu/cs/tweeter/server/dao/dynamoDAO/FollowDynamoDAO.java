package edu.byu.cs.tweeter.server.dao.dynamoDAO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.FollowRequest;
import edu.byu.cs.tweeter.model.net.request.FollowersRequest;
import edu.byu.cs.tweeter.model.net.request.FollowingRequest;
import edu.byu.cs.tweeter.model.net.request.IsFollowerRequest;
import edu.byu.cs.tweeter.model.net.request.UnfollowRequest;
import edu.byu.cs.tweeter.model.net.response.FollowResponse;
import edu.byu.cs.tweeter.model.net.response.FollowersResponse;
import edu.byu.cs.tweeter.model.net.response.FollowingResponse;
import edu.byu.cs.tweeter.model.net.response.IsFollowerResponse;
import edu.byu.cs.tweeter.model.net.response.UnfollowResponse;
import edu.byu.cs.tweeter.server.dao.FollowDAOInterface;
import edu.byu.cs.tweeter.server.dao.dynamoDAO.bean.FollowBean;
import software.amazon.awssdk.core.pagination.sync.SdkIterable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbIndex;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.BatchWriteItemEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.BatchWriteResult;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.WriteBatch;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;

public class FollowDynamoDAO implements FollowDAOInterface {

    //TODO: Redo DynamoDB exercise to have the follows table

    private static final String TableName = "follows";
    public static final String IndexName = "follows_index";

    private static final String FollowerAttr = "follower_handle";
    private static final String FolloweeAttr = "followee_handle";

    // TODO: Import aws library somehow
    Region region = Region.US_EAST_1;
    DynamoDbClient dynamoDbClient = DynamoDbClient.builder()
            .region(region)
            .build();
    DynamoDbEnhancedClient dynamoDbEnhancedClient = DynamoDbEnhancedClient.builder().dynamoDbClient(dynamoDbClient).build();

    private static boolean isNonEmptyString(String value) {
        return (value != null && value.length() > 0);
    }

    /** FOLLOWEES **/
    @Override
    public FollowingResponse getFollowees(FollowingRequest followingRequest) {
        assert followingRequest.getLimit() > 0;
        assert followingRequest.getFollowerAlias() != null;

        DynamoDbTable<FollowBean> table = dynamoDbEnhancedClient.table(TableName, TableSchema.fromBean(FollowBean.class));


        QueryConditional queryConditional = QueryConditional.keyEqualTo(Key.builder()
                .partitionValue(followingRequest.getFollowerAlias())
                .build());

        QueryEnhancedRequest.Builder queryEnhancedRequestBuilder = QueryEnhancedRequest.builder()
                .queryConditional(queryConditional)
                .scanIndexForward(false);

        if (isNonEmptyString(followingRequest.getLastFolloweeAlias())) {
            Map<String, AttributeValue> startKey = new HashMap<>();
            startKey.put(FollowerAttr, AttributeValue.builder().s(followingRequest.getFollowerAlias()).build());
            startKey.put(FolloweeAttr, AttributeValue.builder().s(followingRequest.getLastFolloweeAlias()).build());

            queryEnhancedRequestBuilder.exclusiveStartKey(startKey);
        }

        QueryEnhancedRequest queryEnhancedRequest = queryEnhancedRequestBuilder.build();

        List<FollowBean> results = table.query(queryEnhancedRequest).items().stream()
                .limit(followingRequest.getLimit()).collect(Collectors.toList());


        List<User> responseFollowees = new ArrayList<>();

        for (FollowBean followee : results) {
            try {
                User userToAdd = new User(followee.getFollowee_name(), followee.getFollowee_lastName(), followee.getFollowee_handle(), followee.getFollowee_image_url());
                responseFollowees.add(userToAdd);
            } catch (DynamoDbException e) {
                System.err.println(e.getMessage());
                throw new RuntimeException("[Internal Server Error]. " + e.getMessage());
            }
        }

        boolean hasMorePages = responseFollowees.size() >= followingRequest.getLimit();

        return new FollowingResponse(responseFollowees, hasMorePages);
    }

    /** FOLLOWERS **/

    @Override
    public FollowersResponse getFollowers(FollowersRequest followersRequest) {
        assert followersRequest.getLimit() > 0;
        assert followersRequest.getFolloweeAlias() != null;

        DynamoDbIndex<FollowBean> index = dynamoDbEnhancedClient.table(TableName, TableSchema.fromBean(FollowBean.class)).index(IndexName);
        Key key = Key.builder()
                .partitionValue(followersRequest.getFolloweeAlias())
                .build();

        QueryEnhancedRequest.Builder requestBuilder = QueryEnhancedRequest.builder()
                .queryConditional(QueryConditional.keyEqualTo(key))
                .limit(followersRequest.getLimit());

        if (isNonEmptyString(followersRequest.getLastFollowerAlias())) {
            Map<String, AttributeValue> startKey = new HashMap<>();
            startKey.put(FollowerAttr, AttributeValue.builder().s(followersRequest.getLastFollowerAlias()).build());
            startKey.put(FolloweeAttr, AttributeValue.builder().s(followersRequest.getFolloweeAlias()).build());
            requestBuilder.exclusiveStartKey(startKey);
        }

        QueryEnhancedRequest enhancedRequest = requestBuilder.build();

        List<FollowBean> followers = new ArrayList<>();

        SdkIterable<Page<FollowBean>> results = index.query(enhancedRequest);
        PageIterable<FollowBean> pages = PageIterable.create(results);
        pages.stream()
                .limit(1)
                .forEach(visitsPage -> visitsPage.items().forEach(iterate -> followers.add(iterate)));

        List<User> responseFollowers = new ArrayList<>();

        for (FollowBean follower : followers) {
            try {
                User userToAdd = new User(follower.getFollower_name(), follower.getFollower_lastName(), follower.getFollower_handle(), follower.getFollower_image_url());
                responseFollowers.add(userToAdd);
            } catch (DynamoDbException e) {
                System.err.println(e.getMessage());
                throw new RuntimeException("[Internal Server Error]. " + e.getMessage());
            }
        }

        boolean hasMorePages = responseFollowers.size() >= followersRequest.getLimit();

        return new FollowersResponse(responseFollowers, hasMorePages);
    }

//    public List<User> getAllFollowers(String username) {
//        DynamoDbIndex<FollowBean> index = dynamoDbEnhancedClient.table(TableName, TableSchema.fromBean(FollowBean.class)).index(IndexName);
//        Key key = Key.builder()
//                .partitionValue(username)
//                .build();
//
//        QueryEnhancedRequest.Builder requestBuilder = QueryEnhancedRequest.builder()
//                .queryConditional(QueryConditional.keyEqualTo(key));
//
//
//        QueryEnhancedRequest queryRequest = requestBuilder.build();
//
//        List<FollowBean> follows = new ArrayList<>();
//
//        SdkIterable<Page<FollowBean>> results2 = index.query(queryRequest);
//        PageIterable<FollowBean> pages = PageIterable.create(results2);
//        // limit 1 page, with pageSize items
//        pages.stream()
//                .limit(1)
//                .forEach(visitsPage -> visitsPage.items().forEach(v -> follows.add(v)));
//
//        List<String> followers = new ArrayList<>();
//        for (FollowBean follow:
//                follows) {
//            followers.add(follow.getFollower_handle());
//        }
//
//        List<User> responseFollowers = new ArrayList<>();
//        for (String follower : followers) {
//            try {
//                User userToAdd = new User(follower(), follower.getFollower_lastName(), follower.getFollower_handle(), follower.getFollower_image_url());
//                responseFollowers.add(new UserDynamoDAO().getUser(follower));
//            } catch (DynamoDbException e) {
//                System.err.println(e.getMessage());
//                throw new RuntimeException("[Internal Server Error]. " + e.getMessage());
//            }
//        }
//
//        return responseFollowers;
//    }

//    public List<String> getAllFollowerUsernames(String username) {
//        DynamoDbIndex<FollowBean> index = client.table(TableName, TableSchema.fromBean(FollowBean.class)).index(IndexName);
//        Key key = Key.builder()
//                .partitionValue(username)
//                .build();
//
//        QueryEnhancedRequest.Builder requestBuilder = QueryEnhancedRequest.builder()
//                .queryConditional(QueryConditional.keyEqualTo(key));
//
//
//        QueryEnhancedRequest queryRequest = requestBuilder.build();
//
//        List<FollowBean> follows = new ArrayList<>();
//
//        SdkIterable<Page<FollowBean>> results2 = index.query(queryRequest);
//        PageIterable<FollowBean> pages = PageIterable.create(results2);
//        pages.stream()
//                .limit(1)
//                .forEach(visitsPage -> visitsPage.items().forEach(iterator -> follows.add(iterator)));
//
//        List<String> followers = new ArrayList<>();
//        for (FollowBean follow:
//                follows) {
//            followers.add(follow.getFollower_handle());
//        }
//
//        return followers;
//    }

    /** FOLLOW / UNFOLLOW / ISFOLLOW **/

    @Override
    public FollowResponse follow(FollowRequest followRequest, User follower, User followee) {
        DynamoDbTable<FollowBean> dynamoDbTable = dynamoDbEnhancedClient.table(TableName, TableSchema.fromBean(FollowBean.class));
        FollowBean followBean = new FollowBean();

        // Follower
        followBean.setFollower_handle(followRequest.getCurrentUser());
        followBean.setFollower_name(follower.getFirstName());
        followBean.setFollower_lastName(follower.getLastName());
        followBean.setFollower_image_url(follower.getImageUrl());

        // Followee
        followBean.setFollowee_handle(followRequest.getToFollow());
        followBean.setFollowee_name(followee.getFirstName());
        followBean.setFollowee_lastName(followee.getLastName());
        followBean.setFollowee_image_url(followee.getImageUrl());

        try {
            dynamoDbTable.putItem(followBean);
        } catch (DynamoDbException e) {
            e.printStackTrace();
            throw new RuntimeException("[Bad Request] " + e.getMessage());
        }
        return new FollowResponse();
    }

    @Override
    public UnfollowResponse unfollow(UnfollowRequest unfollowRequest) {
        DynamoDbTable<FollowBean> dynamoDbTable = dynamoDbEnhancedClient.table(TableName, TableSchema.fromBean(FollowBean.class));

        try {
            Key key = Key.builder()
                    .partitionValue(unfollowRequest.getCurrentUser()).sortValue(unfollowRequest.getToUnfollow())
                    .build();
            dynamoDbTable.deleteItem(key);
        } catch (DynamoDbException e) {
            e.printStackTrace();
            throw new RuntimeException("[Bad Request] " + e.getMessage());
        }
        return new UnfollowResponse();
    }

    @Override
    public IsFollowerResponse isFollower(IsFollowerRequest isFollowerRequest) {
        assert isFollowerRequest.getFolloweeAlias() != null;
        assert isFollowerRequest.getFollowerAlias() != null;

        DynamoDbTable<FollowBean> table = dynamoDbEnhancedClient.table(TableName, TableSchema.fromBean(FollowBean.class));

        try {
            Key key = Key.builder()
                    .partitionValue(isFollowerRequest.getFollowerAlias()).sortValue(isFollowerRequest.getFolloweeAlias())
                    .build();
            FollowBean bean = table.getItem(key);
            if (bean == null) {
                return new IsFollowerResponse(false);
            }
            return new IsFollowerResponse(true);

        } catch (DynamoDbException e) {
            e.printStackTrace();
            throw new RuntimeException("[Bad Request] " + e.getMessage());
        }
    }

//    public void writePortionOfFollowBeans(List<FollowBean> followBeans) {
//        if(followBeans.size() > 25)
//            throw new RuntimeException("Too many follows to write");
//
//        DynamoDbTable<FollowBean> table = client.table(TableName, TableSchema.fromBean(FollowBean.class));
//        WriteBatch.Builder<FollowBean> writeBuilder = WriteBatch.builder(FollowBean.class).mappedTableResource(table);
//        for (FollowBean item : followBeans) {
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
//                writePortionOfFollowBeans(result.unprocessedPutItemsForTable(table));
//            }
//
//        } catch (DynamoDbException e) {
//            System.err.println(e.getMessage());
//            System.exit(1);
//        }
//    }
}
