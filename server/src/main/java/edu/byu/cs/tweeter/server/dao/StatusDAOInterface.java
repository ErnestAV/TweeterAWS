package edu.byu.cs.tweeter.server.dao;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.FeedRequest;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.request.StoryRequest;
import edu.byu.cs.tweeter.model.net.response.FeedResponse;
import edu.byu.cs.tweeter.model.net.response.PostStatusResponse;
import edu.byu.cs.tweeter.model.net.response.StoryResponse;

public interface StatusDAOInterface {
    public StoryResponse getStory(StoryRequest storyRequest, User targetUser);
    public FeedResponse getFeed(FeedRequest feedRequest);
    public PostStatusResponse postStatus(PostStatusRequest postStatusRequest);
    public void postStatusToFeed(List<String> followers, Status status);
    public String addFollowersToSQS(List<User> followers, Status status);
}
