package edu.byu.cs.tweeter.server.service;

import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.FeedRequest;
import edu.byu.cs.tweeter.model.net.request.FollowersRequest;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.request.StoryRequest;
import edu.byu.cs.tweeter.model.net.request.UserRequest;
import edu.byu.cs.tweeter.model.net.response.FeedResponse;
import edu.byu.cs.tweeter.model.net.response.FollowResponse;
import edu.byu.cs.tweeter.model.net.response.FollowersResponse;
import edu.byu.cs.tweeter.model.net.response.FollowingResponse;
import edu.byu.cs.tweeter.model.net.response.PostStatusResponse;
import edu.byu.cs.tweeter.model.net.response.StoryResponse;
import edu.byu.cs.tweeter.model.net.response.UserResponse;
import edu.byu.cs.tweeter.server.dao.FollowDAOInterface;
import edu.byu.cs.tweeter.server.dao.MainDAOFactoryInterface;
import edu.byu.cs.tweeter.server.dao.StatusDAOInterface;
import edu.byu.cs.tweeter.server.dao.UserDAOInterface;
import edu.byu.cs.tweeter.util.FakeData;

public class StatusService {
    private StatusDAOInterface statusDAO;
    private UserDAOInterface userDAO;
    private FollowDAOInterface followDAO;

    public StatusService(MainDAOFactoryInterface mainDAOFactoryInterface) {
        this.statusDAO = mainDAOFactoryInterface.getStatusDAO();
        this.userDAO = mainDAOFactoryInterface.getUserDAO();
        this.followDAO = mainDAOFactoryInterface.getFollowDAO();
    }

    public FeedResponse getFeed(FeedRequest feedRequest) {
        if (feedRequest.getTargetUser() == null) {
            throw new RuntimeException("[Bad Request] Request needs a Target User.");
        } else if(feedRequest.getLimit() <= 0) {
            throw new RuntimeException("[Bad Request] Request needs to have a positive limit.");
        } else if (!userDAO.isTokenStillValid(feedRequest.getAuthToken())) {
            return new FeedResponse("Session expired. Log out.");
        }
        FeedResponse feedResponse = statusDAO.getFeed(feedRequest);
        for (Status status : feedResponse.getFeed()) {
            User author = userDAO.getUser(new UserRequest(status.userAlias, feedRequest.getAuthToken())).getUser();
            status.setUser(author);
        }
        return feedResponse;
    }

    public PostStatusResponse postStatus(PostStatusRequest postStatusRequest) {
        if(postStatusRequest.getAuthToken() == null) {
            throw new RuntimeException("[Bad Request] Missing an AuthToken.");
        }
        else if (postStatusRequest.getStatus() == null) {
            throw new RuntimeException("[Bad Request] Missing a Status.");
        } else if (!userDAO.isTokenStillValid(postStatusRequest.getAuthToken())) {
            return new PostStatusResponse("Session expired. Log out.");
        }

        PostStatusResponse postStatusResponse = statusDAO.postStatus(postStatusRequest);

        // This is to update the feeds of the followers
        Status status = postStatusRequest.getStatus();
        FollowersRequest followersRequest = new FollowersRequest(null, status.user.getAlias(), 25, null);
        FollowersResponse followersResponse = followDAO.getFollowers(followersRequest);
        String lastFollowerAlias;
        boolean hasMorePages;
        do {
            hasMorePages = followersResponse.getHasMorePages();
            lastFollowerAlias = statusDAO.postStatusToFeed(followersResponse.getFollowers(), status);
            followersResponse = followDAO.getFollowers(new FollowersRequest(null, status.user.getAlias(), 25, lastFollowerAlias));
        } while (hasMorePages);
        return postStatusResponse;
    }

    public StoryResponse getStory(StoryRequest storyRequest) {
        if(storyRequest.getTargetUser() == null) {
            throw new RuntimeException("[Bad Request] Request needs a Target User.");
        } else if(storyRequest.getLimit() <= 0) {
            throw new RuntimeException("[Bad Request] Request needs to have a positive limit.");
        } else if (!userDAO.isTokenStillValid(storyRequest.getAuthToken())) {
            return new StoryResponse("Session expired. Log out.");
        }
        UserRequest userRequest = new UserRequest(storyRequest.getTargetUser(), storyRequest.getAuthToken());
        UserResponse userResponse = userDAO.getUser(userRequest);
        User user = userResponse.getUser();
        return statusDAO.getStory(storyRequest, user);
    }
}
