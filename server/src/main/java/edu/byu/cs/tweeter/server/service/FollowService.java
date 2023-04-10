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
import edu.byu.cs.tweeter.model.net.response.FollowResponse;
import edu.byu.cs.tweeter.model.net.response.FollowersCountResponse;
import edu.byu.cs.tweeter.model.net.response.FollowersResponse;
import edu.byu.cs.tweeter.model.net.response.FollowingCountResponse;
import edu.byu.cs.tweeter.model.net.response.FollowingResponse;
import edu.byu.cs.tweeter.model.net.response.IsFollowerResponse;
import edu.byu.cs.tweeter.model.net.response.UnfollowResponse;
import edu.byu.cs.tweeter.server.dao.FollowDAO;
import edu.byu.cs.tweeter.util.FakeData;
import edu.byu.cs.tweeter.util.Pair;

/**
 * Contains the business logic for getting the users a user is following.
 */
public class FollowService {

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
        User lastFollowee = FakeData.getInstance().findUserByAlias(request.getLastFolloweeAlias());
        Pair<List<User>, Boolean> result = FakeData.getInstance().getPageOfUsers(lastFollowee, request.getLimit(), null);
        return new FollowingResponse(result.getFirst(), result.getSecond());
    }

    /**
     * Returns an instance of {@link FollowDAO}. Allows mocking of the FollowDAO class
     * for testing purposes. All usages of FollowDAO should get their FollowDAO
     * instance from this method to allow for mocking of the instance.
     *
     * @return the instance.
     */
    FollowDAO getFollowingDAO() {
        return new FollowDAO();
    }

    public FollowersCountResponse getFollowersCount(FollowersCountRequest followersCountRequest) {
        if(followersCountRequest.getTargetUser() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a target user.");
        }
        return new FollowersCountResponse(20);
    }

    public FollowersResponse getFollowers(FollowersRequest followersRequest) {
        if(followersRequest.getFolloweeAlias() == null) {
            throw new RuntimeException("[Bad Request] Requests needs to have a followee alias");
        } else if(followersRequest.getLimit() <= 0) {
            throw new RuntimeException("[Bad Request] Request needs to have a positive limit");
        }
        User lastFollower = FakeData.getInstance().findUserByAlias(followersRequest.getLastFollowerAlias());
        Pair<List<User>, Boolean> result = FakeData.getInstance().getPageOfUsers(lastFollower, followersRequest.getLimit(), null);
        return new FollowersResponse(result.getFirst(), result.getSecond());
    }

    public FollowingCountResponse getFollowingCount(FollowingCountRequest followingCountRequest) {
        if(followingCountRequest.getTargetUser() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a target user.");
        }
        return new FollowingCountResponse(20);
    }

    public FollowResponse follow(FollowRequest followRequest) {
        if(followRequest.getToFollow() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a target user.");
        }
//        else if(followRequest.getAuthToken() == null) {
//            throw new RuntimeException("[Bad Request] Request needs to have an auth token.");
//        }
        return new FollowResponse();
    }

    public UnfollowResponse unfollow(UnfollowRequest unfollowRequest) {
        System.out.println(unfollowRequest.getToUnfollow());
        if (unfollowRequest.getToUnfollow() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a target user.");
        }
//        else if(unfollowRequest.getAuthToken() == null) {
//            throw new RuntimeException("[Bad Request] Request needs to have an auth token.");
//        }
        return new UnfollowResponse();
    }

    public IsFollowerResponse isFollower(IsFollowerRequest isFollowerRequest) {
        if (isFollowerRequest.getFollowerAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a user follower");
        } else if (isFollowerRequest.getFolloweeAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a user followee");
        }
        return new IsFollowerResponse(new Random().nextInt() > 0);
    }
}
