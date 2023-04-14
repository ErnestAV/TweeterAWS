package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;

import edu.byu.cs.tweeter.model.net.request.FeedRequest;
import edu.byu.cs.tweeter.model.net.response.FeedResponse;
import edu.byu.cs.tweeter.server.service.StatusService;

public class GetFeedHandler extends BaseHandler<FeedRequest, FeedResponse> {
    @Override
    public FeedResponse handleRequest(FeedRequest feedRequest, Context context) {
        StatusService statusService = new StatusService(factoryDAO);
        return statusService.getFeed(feedRequest);
    }
}