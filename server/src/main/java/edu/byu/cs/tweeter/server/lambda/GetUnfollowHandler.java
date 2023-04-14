package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;

import edu.byu.cs.tweeter.model.net.request.UnfollowRequest;
import edu.byu.cs.tweeter.model.net.response.UnfollowResponse;
import edu.byu.cs.tweeter.server.service.FollowService;

public class GetUnfollowHandler extends BaseHandler<UnfollowRequest, UnfollowResponse> {
    @Override
    public UnfollowResponse handleRequest(UnfollowRequest unfollowRequest, Context context) {
        System.out.println(unfollowRequest.getToUnfollow());
        FollowService followService = new FollowService(factoryDAO);
        return followService.unfollow(unfollowRequest);
    }
}
