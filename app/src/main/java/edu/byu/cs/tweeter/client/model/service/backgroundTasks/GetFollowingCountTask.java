package edu.byu.cs.tweeter.client.model.service.backgroundTasks;

import android.os.Handler;
import android.util.Log;

import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.FollowingCountRequest;
import edu.byu.cs.tweeter.model.net.response.FollowingCountResponse;

/**
 * Background task that queries how many other users a specified user is following.
 */
public class GetFollowingCountTask extends GetCountTask {

    private static final String LOG_TAG = "GetFollowingCountTask";

    public GetFollowingCountTask(AuthToken authToken, User targetUser, Handler messageHandler) {
        super(authToken, targetUser, messageHandler);
    }

    @Override
    protected int runCountTask() {
        return 20;
    }

    @Override
    protected void runTask() throws IOException {
        try {
            FollowingCountRequest request = new FollowingCountRequest(getTargetUser().getAlias(), authToken);
            FollowingCountResponse response = facade.getFolloweesCount(request, "\\getfollowingcount");
            if (response.isSuccess()) {
                count = response.getCount();
                sendSuccessMessage();
            } else {
                sendFailedMessage(response.getMessage());
            }
        } catch (IOException | TweeterRemoteException e) {
            Log.e(LOG_TAG, "Failed to get Followees count.", e);
            sendExceptionMessage(e);
        }
    }
}
