package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;

import edu.byu.cs.tweeter.model.net.request.IsFollowerRequest;
import edu.byu.cs.tweeter.model.net.response.IsFollowerResponse;
import edu.byu.cs.tweeter.server.service.FollowService;

public class IsFollowerHandler extends BaseHandler<IsFollowerRequest, IsFollowerResponse> {

    @Override
    public IsFollowerResponse handleRequest(IsFollowerRequest isFollowerRequest, Context context) {
        FollowService followService = new FollowService(factoryDAO);
        return followService.isFollower(isFollowerRequest);
    }
}
