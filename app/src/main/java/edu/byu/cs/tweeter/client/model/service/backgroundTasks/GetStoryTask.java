package edu.byu.cs.tweeter.client.model.service.backgroundTasks;

import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.StoryRequest;
import edu.byu.cs.tweeter.model.net.response.StoryResponse;
import edu.byu.cs.tweeter.util.Pair;

/**
 * Background task that retrieves a page of statuses from a user's story.
 */
public class GetStoryTask extends PagedStatusTask {

    private static final String LOG_TAG = "GetStoryTask";

    public GetStoryTask(AuthToken authToken, User targetUser, int limit, Status lastStatus,
                        Handler messageHandler) {
        super(authToken, targetUser, limit, lastStatus, messageHandler);
    }

    @Override
    protected Pair<List<Status>, Boolean> getItems() {
        return getFakeData().getPageOfStatus(getLastItem(), getLimit());
    }

    @Override
    protected void runTask() throws IOException {
        try {
            String targetUserAlias = getTargetUser() == null ? null : getTargetUser().getAlias();
            Status lastStatusPost = getLastItem() == null ? null : getLastItem();
            StoryRequest request = new StoryRequest(authToken, targetUserAlias, limit, lastStatusPost);
            StoryResponse response = facade.getStory(request, "\\getstory");
            if (response.isSuccess()) {
                items = response.getStory();
                hasMorePages = response.getHasMorePages();
                sendSuccessMessage();
            } else {
                sendFailedMessage(response.getMessage());
            }
        } catch(IOException | TweeterRemoteException e) {
            Log.e(LOG_TAG, "Failed to get story", e);
            sendExceptionMessage(e);
        }
    }
}
