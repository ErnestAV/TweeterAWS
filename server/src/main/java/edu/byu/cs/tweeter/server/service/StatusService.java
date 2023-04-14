package edu.byu.cs.tweeter.server.service;

import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.net.request.FeedRequest;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.request.StoryRequest;
import edu.byu.cs.tweeter.model.net.response.FeedResponse;
import edu.byu.cs.tweeter.model.net.response.PostStatusResponse;
import edu.byu.cs.tweeter.model.net.response.StoryResponse;
import edu.byu.cs.tweeter.server.dao.MainDAOFactoryInterface;
import edu.byu.cs.tweeter.server.dao.StatusDAOInterface;
import edu.byu.cs.tweeter.util.FakeData;

public class StatusService {
    private StatusDAOInterface statusDAO;

    public StatusService(MainDAOFactoryInterface mainDAOFactoryInterface) {
        this.statusDAO = mainDAOFactoryInterface.getStatusDAO();
    }

    public FeedResponse getFeed(FeedRequest feedRequest) {
        if (feedRequest.getTargetUser() == null) {
            throw new RuntimeException("[Bad Request] Request needs a Target User.");
        } else if(feedRequest.getLimit() <= 0) {
            throw new RuntimeException("[Bad Request] Request needs to have a positive limit.");
        }
        return statusDAO.getFeed(feedRequest);
    }

    public PostStatusResponse postStatus(PostStatusRequest postStatusRequest) {
        if(postStatusRequest.getAuthToken() == null) {
            throw new RuntimeException("[Bad Request] Missing an AuthToken.");
        }
        else if (postStatusRequest.getStatus() == null) {
            throw new RuntimeException("[Bad Request] Missing a Status.");
        }
        return statusDAO.postStatus(postStatusRequest);
    }

    public StoryResponse getStory(StoryRequest storyRequest) {
        if(storyRequest.getTargetUser() == null) {
            throw new RuntimeException("[Bad Request] Request needs a Target User.");
        } else if(storyRequest.getLimit() <= 0) {
            throw new RuntimeException("[Bad Request] Request needs to have a positive limit.");
        }
        return statusDAO.getStory(storyRequest);
    }
}
