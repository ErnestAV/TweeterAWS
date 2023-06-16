package edu.byu.cs.tweeter.client.model.service.backgroundTasks;

import android.os.Handler;
import android.util.Log;

import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.UnfollowRequest;
import edu.byu.cs.tweeter.model.net.response.UnfollowResponse;

/**
 * Background task that removes a following relationship between two users.
 */
public class UnfollowTask extends AuthenticatedTask {

    private static final String LOG_TAG = "UnfollowTask";
    /**
     * The user that is being followed.
     */

    private final User currentUser;
    private final User followee;

    public UnfollowTask(AuthToken authToken, User currentUser, User followee, Handler messageHandler) {
        super(authToken, messageHandler);
        this.currentUser = currentUser;
        this.followee = followee;
    }

    @Override
    protected void runTask() {
        try {
            // PASS CURRENT USER INTO UNFOLLOW REQUEST
            System.out.println("From unfollow task RUN TASK: " + currentUser);
            UnfollowRequest request = new UnfollowRequest(currentUser.getAlias(), followee.getAlias(), authToken);
            UnfollowResponse response = facade.unfollow(request, "\\getunfollow");
            if (response.isSuccess()) {
                sendSuccessMessage();
            } else {
                sendFailedMessage(response.getMessage());
            }
        } catch(IOException | TweeterRemoteException e) {
            Log.e(LOG_TAG, "Failed to unfollow user.", e);
            sendExceptionMessage(e);
        }
    }
}
