package edu.byu.cs.tweeter.server.dao;

import edu.byu.cs.tweeter.model.net.request.FeedRequest;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.request.StoryRequest;
import edu.byu.cs.tweeter.model.net.response.FeedResponse;
import edu.byu.cs.tweeter.model.net.response.PostStatusResponse;
import edu.byu.cs.tweeter.model.net.response.StoryResponse;

public interface StatusDAOInterface {
    public StoryResponse getStory(StoryRequest storyRequest);
    public FeedResponse getFeed(FeedRequest feedRequest);
    public PostStatusResponse postStatus(PostStatusRequest postStatusRequest);
}
