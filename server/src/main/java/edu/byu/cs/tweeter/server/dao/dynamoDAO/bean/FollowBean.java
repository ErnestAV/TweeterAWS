package edu.byu.cs.tweeter.server.dao.dynamoDAO.bean;

import edu.byu.cs.tweeter.server.dao.dynamoDAO.FollowDynamoDAO;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondaryPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondarySortKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

@DynamoDbBean
public class FollowBean {
    private String follower_handle;
    private String followee_handle;

    private String follower_name;
    private String followee_name;

    private String follower_lastName;
    private String followee_lastName;

    private String follower_image_url;
    private String followee_image_url;

    public FollowBean() {
    }

    public String getFollower_image_url() {
        return follower_image_url;
    }

    public void setFollower_image_url(String follower_image_url) {
        this.follower_image_url = follower_image_url;
    }

    public String getFollowee_image_url() {
        return followee_image_url;
    }

    public void setFollowee_image_url(String followee_image_url) {
        this.followee_image_url = followee_image_url;
    }

    public String getFollowee_lastName() {
        return followee_lastName;
    }

    public void setFollowee_lastName(String followee_lastName) {
        this.followee_lastName = followee_lastName;
    }

    public String getFollower_lastName() {
        return follower_lastName;
    }

    public void setFollower_lastName(String follower_lastName) {
        this.follower_lastName = follower_lastName;
    }

    public String getFollowee_name() {
        return followee_name;
    }

    public void setFollowee_name(String followee_name) {
        this.followee_name = followee_name;
    }

    public String getFollower_name() {
        return follower_name;
    }

    public void setFollower_name(String follower_name) {
        this.follower_name = follower_name;
    }

    @DynamoDbSortKey
    @DynamoDbSecondaryPartitionKey(indexNames =  FollowDynamoDAO.IndexName)
    public String getFollowee_handle() {
        return followee_handle;
    }

    public void setFollowee_handle(String followee_handle) {
        this.followee_handle = followee_handle;
    }

    @DynamoDbPartitionKey
    @DynamoDbSecondarySortKey(indexNames = FollowDynamoDAO.IndexName)
    public String getFollower_handle() {
        return follower_handle;
    }

    public void setFollower_handle(String follower_handle) {
        this.follower_handle = follower_handle;
    }
}
