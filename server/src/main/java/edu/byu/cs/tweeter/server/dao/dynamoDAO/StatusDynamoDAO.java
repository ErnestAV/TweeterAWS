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

    private static final String UsernameAttr = "username";
    private static final String DatetimeAttr = "dateTime";

    Region region = Region.US_EAST_1;
    DynamoDbClient ddb = DynamoDbClient.builder()
            .region(region)
            .build();
    DynamoDbEnhancedClient client = DynamoDbEnhancedClient.builder().dynamoDbClient(ddb).build();

    // TODO: Convert the objects returning from the requests
    @Override
    public StoryResponse getStory(StoryRequest storyRequest) {
        assert storyRequest.getLimit() > 0;
        assert storyRequest.getTargetUser() != null;

        DynamoDbTable<StoryBean> table = client.table(StoryTable, TableSchema.fromBean(StoryBean.class));

        try {
            QueryConditional queryConditional = QueryConditional.keyEqualTo(Key.builder()
                    .partitionValue(storyRequest.getTargetUser())
                    .build());

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

            List<StoryBean> results = table.query(queryEnhancedRequest).items().stream()
                    .limit(storyRequest.getLimit()).collect(Collectors.toList());

            List<Status> statuses = new ArrayList<>();

            for (StoryBean bean : results) {
                Status status = new Status(bean.getPost(), bean.getTimeStamp(),
                        bean.getUrls(), bean.getMentions(),  bean.getAuthorAlias());
                statuses.add(status);
            }

            boolean hasMorePages = statuses.size() >= storyRequest.getLimit();

            return new StoryResponse(statuses, hasMorePages);
        } catch (DynamoDbException e) {
            e.printStackTrace();
            throw new RuntimeException("[Bad Request]. " + e.getMessage());
        }
    }

    @Override
    public FeedResponse getFeed(FeedRequest feedRequest) {
        assert feedRequest.getLimit() > 0;
        assert feedRequest.getTargetUser() != null;


        DynamoDbTable<FeedBean> table = client.table(FeedTable, TableSchema.fromBean(FeedBean.class));

        try {
            QueryConditional queryConditional = QueryConditional.keyEqualTo(Key.builder()
                    .partitionValue(feedRequest.getTargetUser())
                    .build());

            QueryEnhancedRequest.Builder queryEnhancedRequestBuilder = QueryEnhancedRequest.builder()
                    .queryConditional(queryConditional)
                    .scanIndexForward(false);


            if (feedRequest.getLastStatus() != null) {
                Map<String, AttributeValue> startKey = new HashMap<>();
                startKey.put(UsernameAttr, AttributeValue.builder().s(feedRequest.getTargetUser()).build());
                startKey.put(DatetimeAttr, AttributeValue.builder().s(String.valueOf(feedRequest.getLastStatus().timestamp)).build());

                queryEnhancedRequestBuilder.exclusiveStartKey(startKey);
            }

            QueryEnhancedRequest queryEnhancedRequest = queryEnhancedRequestBuilder.build();

            List<FeedBean> results = table.query(queryEnhancedRequest).items().stream()
                    .limit(feedRequest.getLimit()).collect(Collectors.toList());


            List<Status> statuses = new ArrayList<>();

            for (FeedBean bean : results) {
                Status status = new Status(bean.getPost(), bean.getTimeStamp(),
                        bean.getUrls(), bean.getMentions(), bean.getAuthorAlias());
                statuses.add(status);
            }

            boolean hasMorePages = statuses.size() >= feedRequest.getLimit();

            return new FeedResponse(statuses, hasMorePages);

        } catch (DynamoDbException e) {
            e.printStackTrace();
            throw new RuntimeException("[Bad Request]. " + e.getMessage());
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
        storyBeanToAdd.setAuthorAlias(postStatus.getUserAlias());
        storyBeanToAdd.setPost(postStatus.getPost());
        storyBeanToAdd.setMentions(postStatus.getMentions());
        storyBeanToAdd.setUrls(postStatus.getUrls());
        storyBeanToAdd.setTimeStamp(postStatus.timestamp);

        try {
            DynamoDbTable<StoryBean> storyTable = client.table(StoryTable, TableSchema.fromBean(StoryBean.class));
            System.out.println("story bean " + storyBeanToAdd.getPost());
            storyTable.putItem(storyBeanToAdd);
        } catch (DynamoDbException e) {
            e.printStackTrace();
            throw new RuntimeException("[Bad Request] " + e.getMessage());
        }

        return new PostStatusResponse();
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
