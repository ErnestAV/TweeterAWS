package edu.byu.cs.tweeter.client.model.service.backgroundTasks;

import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.FeedRequest;
import edu.byu.cs.tweeter.model.net.response.FeedResponse;
import edu.byu.cs.tweeter.util.Pair;

/**
 * Background task that retrieves a page of statuses from a user's feed.
 */
public class GetFeedTask extends PagedStatusTask {

    private static final String LOG_TAG = "GetFeedTask" ;

    public GetFeedTask(AuthToken authToken, User targetUser, int limit, Status lastStatus,
                       Handler messageHandler) {
        super(authToken, targetUser, limit, lastStatus, messageHandler);
    }

    @Override
    protected Pair<List<Status>, Boolean> getItems() {
        return getFakeData().getPageOfStatus(getLastItem(), getLimit());
    }

    @Override
    protected final void runTask() throws IOException { // TODO: Get authToken
        try {
            String targetUserAlias = getTargetUser() == null ? null : getTargetUser().getAlias();
            Status lastStatusPost = getLastItem() == null ? null : getLastItem();
            FeedRequest request = new FeedRequest(authToken, targetUserAlias, limit, lastStatusPost);
            FeedResponse response = facade.getFeed(request, "\\getfeed");
            if (response.isSuccess()) {
                items = response.getFeed();
                hasMorePages = response.getHasMorePages();
                sendSuccessMessage();
            } else {
                sendFailedMessage(response.getMessage());
            }
        } catch(IOException | TweeterRemoteException e) {
            Log.e(LOG_TAG, "Failed to get Feed", e);
            sendExceptionMessage(e);
        }
    }
}
