package edu.byu.cs.tweeter.server.dao.dynamoDAO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.FeedRequest;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.request.StoryRequest;
import edu.byu.cs.tweeter.model.net.response.FeedResponse;
import edu.byu.cs.tweeter.model.net.response.PostStatusResponse;
import edu.byu.cs.tweeter.model.net.response.StoryResponse;
import edu.byu.cs.tweeter.server.dao.StatusDAOInterface;
import edu.byu.cs.tweeter.server.dao.dynamoDAO.bean.FeedBean;
import edu.byu.cs.tweeter.server.dao.dynamoDAO.bean.StoryBean;
import edu.byu.cs.tweeter.server.dao.dynamoDAO.bean.UserBean;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.BatchWriteItemEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.BatchWriteResult;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.WriteBatch;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;

public class StatusDynamoDAO implements StatusDAOInterface {
    private static final String FeedTable = "feed";
    private static final String StoryTable = "story";

    private static final String UsernameAttr = "authorAlias";
    private static final String DatetimeAttr = "timeStamp";
    private static final String FeedAttr = "currentUserAlias";

    Region region = Region.US_EAST_1;
    DynamoDbClient ddb = DynamoDbClient.builder()
            .region(region)
            .build();
    DynamoDbEnhancedClient client = DynamoDbEnhancedClient.builder().dynamoDbClient(ddb).build();

    DynamoDbTable<StoryBean> storyTable = client.table(StoryTable, TableSchema.fromBean(StoryBean.class));

    DynamoDbTable<FeedBean> feedTable = client.table(FeedTable, TableSchema.fromBean(FeedBean.class));


    // TODO: Convert the objects returning from the requests
    @Override
    public StoryResponse getStory(StoryRequest storyRequest, User targetUser) {
        assert storyRequest.getLimit() > 0;
        assert storyRequest.getTargetUser() != null;


        try {
            Key key = Key.builder().partitionValue(storyRequest.getTargetUser()).build();

            QueryConditional queryConditional = QueryConditional.keyEqualTo(key);

            QueryEnhancedRequest.Builder queryEnhancedRequestBuilder = QueryEnhancedRequest.builder()
                    .queryConditional(queryConditional)
                    .scanIndexForward(false);

            System.out.println("Last status: " + storyRequest.getLastStatus());
            if (storyRequest.getLastStatus() != null) {
                Map<String, AttributeValue> startKey = new HashMap<>();
                startKey.put(UsernameAttr, AttributeValue.builder().s(storyRequest.getTargetUser()).build());
                startKey.put(DatetimeAttr, AttributeValue.builder().s(String.valueOf(storyRequest.getLastStatus().timestamp)).build());
                System.out.println("Last status: " + storyRequest.getLastStatus());

                queryEnhancedRequestBuilder.exclusiveStartKey(startKey);
            }

            QueryEnhancedRequest queryEnhancedRequest = queryEnhancedRequestBuilder.build();

            List<StoryBean> storyBeans = storyTable.query(queryEnhancedRequest).items().stream()
                    .limit(storyRequest.getLimit()).collect(Collectors.toList());

            List<Status> statuses = new ArrayList<>(); // Convert beans to statuses
            for (StoryBean bean : storyBeans) {
                Status status = new Status(bean.getPost(), bean.getTimeStamp(),
                        bean.getUrls(), bean.getMentions(),  bean.getAuthorAlias());
                status.setUser(targetUser);
                statuses.add(status);
            }

            boolean hasMorePages = true;

            if (storyBeans.size() < storyRequest.getLimit()) {
                hasMorePages = false;
            }

            return new StoryResponse(statuses, hasMorePages);
        } catch (Exception e) {
            return new StoryResponse(e.getMessage());
        }
    }

    @Override
    public FeedResponse getFeed(FeedRequest feedRequest) {
        assert feedRequest.getLimit() > 0;
        assert feedRequest.getTargetUser() != null;

        try {
            QueryConditional queryConditional = QueryConditional.keyEqualTo(Key.builder()
                    .partitionValue(feedRequest.getTargetUser())
                    .build());

            QueryEnhancedRequest.Builder queryEnhancedRequestBuilder = QueryEnhancedRequest.builder()
                    .queryConditional(queryConditional)
                    .scanIndexForward(false);


            if (feedRequest.getLastStatus() != null) {
                Map<String, AttributeValue> startKey = new HashMap<>();
                startKey.put(FeedAttr, AttributeValue.builder().s(feedRequest.getTargetUser()).build());
                startKey.put(DatetimeAttr, AttributeValue.builder().s(String.valueOf(feedRequest.getLastStatus().timestamp)).build());

                queryEnhancedRequestBuilder.exclusiveStartKey(startKey);
            }

            QueryEnhancedRequest queryEnhancedRequest = queryEnhancedRequestBuilder.build();

            List<FeedBean> results = feedTable.query(queryEnhancedRequest).items().stream()
                    .limit(feedRequest.getLimit()).collect(Collectors.toList());


            List<Status> statuses = new ArrayList<>();

            for (FeedBean bean : results) {
                Status status = new Status(bean.getPost(), bean.getTimeStamp(),
                        bean.getUrls(), bean.getMentions(), bean.getAuthorAlias());
                statuses.add(status);
            }

            boolean hasMorePages = true;

            if (results.size() < feedRequest.getLimit()) {
                hasMorePages = false;
            }

            return new FeedResponse(statuses, hasMorePages);
        } catch (Exception e) {
            return new FeedResponse(e.getMessage());
        }
    }

    @Override
    public PostStatusResponse postStatus(PostStatusRequest postStatusRequest) {
        assert postStatusRequest.getStatus() != null;

        System.out.println(postStatusRequest.getStatus().getPost());
        Status postStatus = postStatusRequest.getStatus();

        System.out.println("request status: " + postStatusRequest.getStatus());
        User postUser = postStatusRequest.getStatus().getUser();
        assert postUser != null;

        UserBean userBean = new UserBean();
        userBean.setFirstName(postUser.getFirstName());
        userBean.setLastName(postUser.getLastName());
        userBean.setUsername(postUser.getAlias());
        userBean.setImageUrl(postUser.getImageUrl());

        System.out.println("Timestamp class : " + postStatusRequest.getStatus().timestamp.getClass());

        StoryBean storyBeanToAdd = new StoryBean();
        System.out.println("User alias: " + postStatus.getUser().getAlias());
        storyBeanToAdd.setAuthorAlias(postStatus.getUser().getAlias());
        storyBeanToAdd.setPost(postStatus.getPost());
        storyBeanToAdd.setMentions(postStatus.getMentions());
        storyBeanToAdd.setUrls(postStatus.getUrls());
        storyBeanToAdd.setTimeStamp(postStatus.timestamp);

        try {
            System.out.println("story bean " + storyBeanToAdd.getPost());
            storyTable.putItem(storyBeanToAdd);
        } catch (DynamoDbException e) {
            e.printStackTrace();
            throw new RuntimeException("[Bad Request] " + e.getMessage());
        }

        return new PostStatusResponse();
    }

    @Override
    public String postStatusToFeed(List<User> followers, Status status) {
        String lastAlias = null;
        FeedBean feedBeanToAdd = new FeedBean();
        System.out.println("User alias: " + status.getUser().getAlias());
        feedBeanToAdd.setAuthorAlias(status.getUser().getAlias());
        feedBeanToAdd.setPost(status.getPost());
        feedBeanToAdd.setMentions(status.getMentions());
        feedBeanToAdd.setUrls(status.getUrls());
        feedBeanToAdd.setTimeStamp(status.timestamp);

        for (User follower : followers) {
            try {
                feedBeanToAdd.setCurrentUserAlias(follower.getAlias());
                feedTable.putItem(feedBeanToAdd);
                lastAlias = follower.getAlias();
            } catch (DynamoDbException e) {
                e.printStackTrace();
                throw new RuntimeException("[Bad Request] " + e.getMessage());
            }
        }

        return lastAlias;
    }

//    public void postStatusToFeed(FeedBean statusToPost) {
//        DynamoDbTable<FeedBean> feedTable = client.table(FeedTable, TableSchema.fromBean(FeedBean.class));
//        try {
//            feedTable.putItem(statusToPost);
//        } catch (DynamoDbException e) {
//            e.printStackTrace();
//            throw new RuntimeException("[Bad Request] " + e.getMessage());
//        }
//    }

//    public void writePortionOfFeedBeans(List<FeedBean> feedBeans) {
//        if(feedBeans.size() > 25)
//            throw new RuntimeException("Too many users to write");
//
//
//        DynamoDbTable<FeedBean> table = client.table(FeedTable, TableSchema.fromBean(FeedBean.class));
//        WriteBatch.Builder<FeedBean> writeBuilder = WriteBatch.builder(FeedBean.class).mappedTableResource(table);
//        for (FeedBean item : feedBeans) {
//            writeBuilder.addPutItem(builder -> builder.item(item));
//        }
//        BatchWriteItemEnhancedRequest batchWriteItemEnhancedRequest = BatchWriteItemEnhancedRequest.builder()
//                .writeBatches(writeBuilder.build()).build();
//
//        try {
//            BatchWriteResult result = client.batchWriteItem(batchWriteItemEnhancedRequest);
//            if (result.unprocessedPutItemsForTable(table).size() > 0) {
//                writePortionOfFeedBeans(result.unprocessedPutItemsForTable(table));
//            }
//
//        } catch (DynamoDbException e) {
//            System.err.println(e.getMessage());
//            System.exit(1);
//        }
//    }
}
