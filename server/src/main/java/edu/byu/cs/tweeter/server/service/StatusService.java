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
import edu.byu.cs.tweeter.util.FakeData;

public class StatusService {
    public FeedResponse getFeed(FeedRequest feedRequest) {
        if(feedRequest.getTargetUser() == null) {
            throw new RuntimeException("[Bad Request] Request needs a Target User.");
        } else if(feedRequest.getLimit() <= 0) {
            throw new RuntimeException("[Bad Request] Request needs to have a positive limit.");
        }
        // TODO: Change this in milestone 4.
        assert feedRequest.getLimit() > 0;
        assert feedRequest.getTargetUser() != null;

        List<Status> allFeed = FakeData.getInstance().getFakeStatuses();
        List<Status> responseFeed = new ArrayList<>(feedRequest.getLimit());

        boolean hasMorePages = false;

        if(feedRequest.getLimit() > 0) {
            if (allFeed != null) {
                int feedIndex = getStatusStartingIndex(feedRequest.getLastStatus(),allFeed);

                for(int limitCounter = 0; feedIndex < allFeed.size() && limitCounter < feedRequest.getLimit(); feedIndex++, limitCounter++) {
                    responseFeed.add(allFeed.get(feedIndex));
                }

                hasMorePages = feedIndex < allFeed.size();
            }
        }

        return new FeedResponse(responseFeed, hasMorePages);
    }

    public PostStatusResponse postStatus(PostStatusRequest postStatusRequest) {
//        if(postStatusRequest.getAuthToken() == null) {
//            throw new RuntimeException("[Bad Request] Missing an AuthToken.");
        if (postStatusRequest.getStatus() == null) { // TODO: Change this to else if once auth token is done
            throw new RuntimeException("[Bad Request] Missing a Status.");
        }
        return new PostStatusResponse();
    }

    public StoryResponse getStory(StoryRequest storyRequest) {
        if(storyRequest.getTargetUser() == null) {
            throw new RuntimeException("[Bad Request] Request needs a Target User.");
        } else if(storyRequest.getLimit() <= 0) {
            throw new RuntimeException("[Bad Request] Request needs to have a positive limit.");
        }
        // TODO: Change this in milestone 4.

        //Pair<List<Status>, Boolean> storyPair = FakeData.getInstance().getPageOfStatus(request.getLastStatus(), request.getLimit());
        assert storyRequest.getLimit() > 0;
        assert storyRequest.getTargetUser() != null;

        List<Status> allStatuses = FakeData.getInstance().getFakeStatuses();
        List<Status> responseStory = new ArrayList<>(storyRequest.getLimit());

        boolean hasMorePages = false;

        if(storyRequest.getLimit() > 0) {
            if (allStatuses != null) {
                int storyIndex = getStatusStartingIndex(storyRequest.getLastStatus(),allStatuses);

                for(int limitCounter = 0; storyIndex < allStatuses.size() && limitCounter < storyRequest.getLimit(); storyIndex++, limitCounter++) {
                    responseStory.add(allStatuses.get(storyIndex));
                }

                hasMorePages = storyIndex < allStatuses.size();
            }
        }

        return new StoryResponse(responseStory, hasMorePages);
    }

    private int getStatusStartingIndex(String lastStatus, List<Status> allStatuses) {

        int storyIndex = 0;

        if(lastStatus != null) {
            // This is a paged request for something after the first page. Find the first item
            // we should return
            for (int i = 0; i < allStatuses.size(); i++) {
                if(lastStatus.equals(allStatuses.get(i).toString())) {
                    // We found the index of the last item returned last time. Increment to get
                    // to the first one we should return
                    storyIndex = i + 1;
                    break;
                }
            }
        }

        return storyIndex;
    }
}
