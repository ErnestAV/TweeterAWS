package edu.byu.cs.tweeter.client.model.net;

import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.FeedRequest;
import edu.byu.cs.tweeter.model.net.request.FollowRequest;
import edu.byu.cs.tweeter.model.net.request.FollowersCountRequest;
import edu.byu.cs.tweeter.model.net.request.FollowersRequest;
import edu.byu.cs.tweeter.model.net.request.FollowingCountRequest;
import edu.byu.cs.tweeter.model.net.request.FollowingRequest;
import edu.byu.cs.tweeter.model.net.request.IsFollowerRequest;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.request.LogoutRequest;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.request.RegisterRequest;
import edu.byu.cs.tweeter.model.net.request.StoryRequest;
import edu.byu.cs.tweeter.model.net.request.UnfollowRequest;
import edu.byu.cs.tweeter.model.net.request.UserRequest;
import edu.byu.cs.tweeter.model.net.response.FeedResponse;
import edu.byu.cs.tweeter.model.net.response.FollowResponse;
import edu.byu.cs.tweeter.model.net.response.FollowersCountResponse;
import edu.byu.cs.tweeter.model.net.response.FollowersResponse;
import edu.byu.cs.tweeter.model.net.response.FollowingCountResponse;
import edu.byu.cs.tweeter.model.net.response.FollowingResponse;
import edu.byu.cs.tweeter.model.net.response.IsFollowerResponse;
import edu.byu.cs.tweeter.model.net.response.LoginResponse;
import edu.byu.cs.tweeter.model.net.response.LogoutResponse;
import edu.byu.cs.tweeter.model.net.response.PostStatusResponse;
import edu.byu.cs.tweeter.model.net.response.RegisterResponse;
import edu.byu.cs.tweeter.model.net.response.StoryResponse;
import edu.byu.cs.tweeter.model.net.response.UnfollowResponse;
import edu.byu.cs.tweeter.model.net.response.UserResponse;

/**
 * Acts as a Facade to the Tweeter server. All network requests to the server should go through
 * this class.
 */
public class ServerFacade {

    // TODO: Set this to the invoke URL of your API. Find it by going to your API in AWS, clicking
    //  on stages in the right-side menu, and clicking on the stage you deployed your API to.
    private static final String SERVER_URL = "https://bocrnek5th.execute-api.us-east-1.amazonaws.com/Milestone-3";

    private final ClientCommunicator clientCommunicator = new ClientCommunicator(SERVER_URL);

    /**
     * Performs a login and if successful, returns the logged in user and an auth token.
     *
     * @param request contains all information needed to perform a login.
     * @return the login response.
     */
    public LoginResponse login(LoginRequest request, String urlPath) throws IOException, TweeterRemoteException {
        return clientCommunicator.doPost(urlPath, request, null, LoginResponse.class);
    }

    /**
     * Returns the users that the user specified in the request is following. Uses information in
     * the request object to limit the number of followees returned and to return the next set of
     * followees after any that were returned in a previous request.
     *
     * @param request contains information about the user whose followees are to be returned and any
     *                other information required to satisfy the request.
     * @return the followees.
     */
    public FollowingResponse getFollowees(FollowingRequest request, String urlPath)
            throws IOException, TweeterRemoteException {
        return clientCommunicator.doPost(urlPath, request, null, FollowingResponse.class);
    }

    public FeedResponse getFeed(FeedRequest feedRequest, String urlPath) throws IOException, TweeterRemoteException {
        return clientCommunicator.doPost(urlPath, feedRequest, null, FeedResponse.class);
    }

    public FollowersResponse getFollowers(FollowersRequest followersRequest, String urlPath) throws IOException, TweeterRemoteException {
        return clientCommunicator.doPost(urlPath, followersRequest, null, FollowersResponse.class);
    }

    public FollowersCountResponse getFollowersCount(FollowersCountRequest followersCountRequest, String urlPath) throws IOException, TweeterRemoteException {
        return clientCommunicator.doPost(urlPath, followersCountRequest, null, FollowersCountResponse.class);
    }

    public FollowingCountResponse getFolloweesCount(FollowingCountRequest followingCountRequest, String urlPath) throws IOException, TweeterRemoteException {
        return clientCommunicator.doPost(urlPath, followingCountRequest, null, FollowingCountResponse.class);
    }

    public FollowResponse follow(FollowRequest followRequest, String urlPath) throws IOException, TweeterRemoteException {
        return clientCommunicator.doPost(urlPath, followRequest, null, FollowResponse.class);
    }

    public IsFollowerResponse getIsFollower(IsFollowerRequest isFollowerRequest, String urlPath) throws IOException, TweeterRemoteException {
        return clientCommunicator.doPost(urlPath, isFollowerRequest, null, IsFollowerResponse.class);
    }

    public LogoutResponse logout(LogoutRequest logoutRequest, String urlPath) throws IOException, TweeterRemoteException {
        return clientCommunicator.doPost(urlPath, logoutRequest, null, LogoutResponse.class);
    }

    public PostStatusResponse postStatus(PostStatusRequest postStatusRequest, String urlPath) throws IOException, TweeterRemoteException {
        return clientCommunicator.doPost(urlPath, postStatusRequest, null, PostStatusResponse.class);
    }

    public RegisterResponse register(RegisterRequest registerRequest, String urlPath) throws IOException, TweeterRemoteException {
        return clientCommunicator.doPost(urlPath, registerRequest, null, RegisterResponse.class);
    }

    public StoryResponse getStory(StoryRequest storyRequest, String urlPath) throws IOException, TweeterRemoteException {
        return clientCommunicator.doPost(urlPath, storyRequest, null, StoryResponse.class);
    }

    public UnfollowResponse unfollow(UnfollowRequest unfollowRequest, String urlPath) throws IOException, TweeterRemoteException {
        return clientCommunicator.doPost(urlPath, unfollowRequest, null, UnfollowResponse.class);
    }

    public UserResponse getUser(UserRequest userRequest, String urlPath) throws IOException, TweeterRemoteException {
        return clientCommunicator.doPost(urlPath, userRequest, null, UserResponse.class);
    }
}
