package edu.byu.cs.tweeter.client.model.service.backgroundTasks;

import android.os.Handler;
import android.util.Log;

import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.FollowersCountRequest;
import edu.byu.cs.tweeter.model.net.response.FollowersCountResponse;

/**
 * Background task that queries how many followers a user has.
 */
public class GetFollowersCountTask extends GetCountTask {

    private static final String LOG_TAG = "GetFollowersCountTask" ;

    public GetFollowersCountTask(AuthToken authToken, User targetUser, Handler messageHandler) {
        super(authToken, targetUser, messageHandler);
    }

    @Override
    protected int runCountTask() {
        return 20;
    }

    @Override
    protected void runTask() { // TODO: Check variables
        try {
            FollowersCountRequest request = new FollowersCountRequest(getTargetUser().getAlias(), authToken.getToken());
            FollowersCountResponse response = facade.getFollowersCount(request, "\\getfollowerscount");
            if (response.isSuccess()) {
                count = response.getCount();
                sendSuccessMessage();
            } else {
                sendFailedMessage(response.getMessage());
            }
        } catch (IOException | TweeterRemoteException e) {
            Log.e(LOG_TAG, "Failed to get Followers count.", e);
            sendExceptionMessage(e);
        }
    }
}
