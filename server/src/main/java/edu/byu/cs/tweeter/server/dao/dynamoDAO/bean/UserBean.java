package edu.byu.cs.tweeter.server.dao.dynamoDAO.bean;

import java.io.Serializable;

import edu.byu.cs.tweeter.model.net.response.FollowersCountResponse;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

public class UserBean {

    private String firstName;
    private String lastName;
    private String username;
    private String imageUrl;
    private String password;
    private int followersCount;
    private int followeesCount;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @DynamoDbPartitionKey
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getFollowersCount() {
        return followersCount;
    }

    public void setFollowersCount(int followersCount) {
        this.followersCount = followersCount;
    }

    public int getFolloweesCount() {
        return followeesCount;
    }

    public void setFolloweesCount(int followeesCount) {
        this.followeesCount = followeesCount;
    }
}
