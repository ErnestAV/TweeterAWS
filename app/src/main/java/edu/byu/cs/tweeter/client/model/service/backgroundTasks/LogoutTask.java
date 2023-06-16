package edu.byu.cs.tweeter.client.model.service.backgroundTasks;

import android.os.Handler;
import android.util.Log;

import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.LogoutRequest;
import edu.byu.cs.tweeter.model.net.response.LogoutResponse;

/**
 * Background task that logs out a user (i.e., ends a session).
 */
public class LogoutTask extends AuthenticatedTask {

    private static final String LOG_TAG = "LogoutTask";

    public LogoutTask(AuthToken authToken, Handler messageHandler) {
        super(authToken, messageHandler);
    }

    @Override
    protected void runTask() {
        try {
            LogoutRequest request = new LogoutRequest(authToken);
            LogoutResponse response = facade.logout(request, "\\logout");
            if (response.isSuccess()) {
                sendSuccessMessage();
            } else {
                sendFailedMessage(response.getMessage());
            }
        } catch(IOException | TweeterRemoteException e) {
            Log.e(LOG_TAG, "Could not Log out the application.", e);
            sendExceptionMessage(e);
        }
    }
}
