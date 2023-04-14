package edu.byu.cs.tweeter.server.dao.dynamoDAO.bean;

import java.io.Serializable;
import java.util.List;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

public class StoryBean {
    private String authorAlias;
    private long timeStamp;
    private String post;
    private List<String> urls;
    private List<String> mentions;

    public List<String> getUrls() {
        return urls;
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
    }

    public List<String> getMentions() {
        return mentions;
    }

    public void setMentions(List<String> mentions) {
        this.mentions = mentions;
    }

    @DynamoDbPartitionKey
    public String getAuthorAlias() {
        return authorAlias;
    }

    public void setAuthorAlias(String authorAlias) {
        this.authorAlias = authorAlias;
    }

    @DynamoDbSortKey
    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }
}
