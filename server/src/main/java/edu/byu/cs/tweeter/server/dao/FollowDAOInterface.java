package edu.byu.cs.tweeter.server.dao;


import java.util.List;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.FollowRequest;
import edu.byu.cs.tweeter.model.net.request.FollowersRequest;
import edu.byu.cs.tweeter.model.net.request.FollowingRequest;
import edu.byu.cs.tweeter.model.net.request.IsFollowerRequest;
import edu.byu.cs.tweeter.model.net.request.UnfollowRequest;
import edu.byu.cs.tweeter.model.net.response.FollowResponse;
import edu.byu.cs.tweeter.model.net.response.FollowersResponse;
import edu.byu.cs.tweeter.model.net.response.FollowingResponse;
import edu.byu.cs.tweeter.model.net.response.IsFollowerResponse;
import edu.byu.cs.tweeter.model.net.response.UnfollowResponse;
import edu.byu.cs.tweeter.server.dao.dynamoDAO.bean.FollowBean;

public interface FollowDAOInterface {
    // Followers
    FollowersResponse getFollowers(FollowersRequest followersRequest);

    // Followees
    FollowingResponse getFollowees(FollowingRequest followingRequest);

    // Follow – Unfollow - IsFollow
    FollowResponse follow(FollowRequest followRequest, User follower, User followee);
    UnfollowResponse unfollow(UnfollowRequest unfollowRequest);
    IsFollowerResponse isFollower(IsFollowerRequest isFollowerRequest);

    //TODO: FIGURE THIS OUT
    public void addFollowersBatch(List<String> followers, String followTarget);
    //TODO: FIGURE THIS OUT
    public void writePortionOfFollowBeans(List<FollowBean> followers);
}
