package edu.byu.cs.tweeter.server.service;

import java.util.List;
import java.util.Random;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.FollowRequest;
import edu.byu.cs.tweeter.model.net.request.FollowersCountRequest;
import edu.byu.cs.tweeter.model.net.request.FollowersRequest;
import edu.byu.cs.tweeter.model.net.request.FollowingCountRequest;
import edu.byu.cs.tweeter.model.net.request.FollowingRequest;
import edu.byu.cs.tweeter.model.net.request.IsFollowerRequest;
import edu.byu.cs.tweeter.model.net.request.UnfollowRequest;
import edu.byu.cs.tweeter.model.net.request.UserRequest;
import edu.byu.cs.tweeter.model.net.response.FollowResponse;
import edu.byu.cs.tweeter.model.net.response.FollowersCountResponse;
import edu.byu.cs.tweeter.model.net.response.FollowersResponse;
import edu.byu.cs.tweeter.model.net.response.FollowingCountResponse;
import edu.byu.cs.tweeter.model.net.response.FollowingResponse;
import edu.byu.cs.tweeter.model.net.response.IsFollowerResponse;
import edu.byu.cs.tweeter.model.net.response.UnfollowResponse;
import edu.byu.cs.tweeter.server.dao.FollowDAO;
import edu.byu.cs.tweeter.server.dao.FollowDAOInterface;
import edu.byu.cs.tweeter.server.dao.MainDAOFactoryInterface;
import edu.byu.cs.tweeter.server.dao.UserDAOInterface;
import edu.byu.cs.tweeter.server.dao.dynamoDAO.FollowDynamoDAO;
import edu.byu.cs.tweeter.util.FakeData;
import edu.byu.cs.tweeter.util.Pair;

/**
 * Contains the business logic for getting the users a user is following.
 */
public class FollowService {
    private FollowDAOInterface followDAO;
    private UserDAOInterface userDAO;

    public FollowService(MainDAOFactoryInterface mainDAOFactory) {
        this.followDAO = mainDAOFactory.getFollowDAO();
        this.userDAO = mainDAOFactory.getUserDAO();

    }

    /**
     * Returns the users that the user specified in the request is following. Uses information in
     * the request object to limit the number of followees returned and to return the next set of
     * followees after any that were returned in a previous request. Uses the {@link FollowDAO} to
     * get the followees.
     *
     * @param request contains the data required to fulfill the request.
     * @return the followees.
     */
    public FollowingResponse getFollowees(FollowingRequest request) {
        if(request.getFollowerAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a follower alias");
        } else if(request.getLimit() <= 0) {
            throw new RuntimeException("[Bad Request] Request needs to have a positive limit");
        }
        return followDAO.getFollowees(request);
    }

    public FollowersCountResponse getFollowersCount(FollowersCountRequest followersCountRequest) {
        if(followersCountRequest.getTargetUser() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a target user.");
        }
        return userDAO.getFollowersCount(followersCountRequest);
    }

    public FollowersResponse getFollowers(FollowersRequest followersRequest) {
        if(followersRequest.getFolloweeAlias() == null) {
            throw new RuntimeException("[Bad Request] Requests needs to have a followee alias");
        } else if(followersRequest.getLimit() <= 0) {
            throw new RuntimeException("[Bad Request] Request needs to have a positive limit");
        }

        return followDAO.getFollowers(followersRequest);
    }

    public FollowingCountResponse getFollowingCount(FollowingCountRequest followingCountRequest) {
        if(followingCountRequest.getTargetUser() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a target user.");
        }
        return userDAO.getFolloweeCount(followingCountRequest);
    }

    public FollowResponse follow(FollowRequest followRequest) {
        if(followRequest.getToFollow() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a target user.");
        }
        else if(followRequest.getAuthToken() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have an auth token.");
        }
        User toFollow = userDAO.getUser(new UserRequest(followRequest.getToFollow(), followRequest.getAuthToken())).getUser();
        User userThatFollows = userDAO.getUser(new UserRequest(followRequest.getCurrentUser(), followRequest.getAuthToken())).getUser();
        return followDAO.follow(followRequest, userThatFollows, toFollow);
    }

    public UnfollowResponse unfollow(UnfollowRequest unfollowRequest) {
        if (unfollowRequest.getToUnfollow() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a target user.");
        }
        else if(unfollowRequest.getAuthToken() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have an auth token.");
        }
        return followDAO.unfollow(unfollowRequest);
    }

    public IsFollowerResponse isFollower(IsFollowerRequest isFollowerRequest) {
        if (isFollowerRequest.getFollowerAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a user follower");
        } else if (isFollowerRequest.getFolloweeAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a user followee");
        }
        return followDAO.isFollower(isFollowerRequest);
    }
}
