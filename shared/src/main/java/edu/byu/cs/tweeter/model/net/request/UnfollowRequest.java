package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.util.FakeData;

public class UnfollowRequest {

    private String currentUser;
    private String toUnfollow;
    private AuthToken authToken;

    private UnfollowRequest() {}

    public UnfollowRequest(String currentUser, String toUnfollow, AuthToken authToken) { // TODO: ADD THE CURRENT USER
        this.currentUser = currentUser;
        this.toUnfollow = toUnfollow;
        this.authToken = authToken;
    }

    public String getCurrentUser() { return currentUser; }

    public void setCurrentUser(String currentUser) { this.currentUser = currentUser; }

    public String getToUnfollow() {
        return toUnfollow;
    }

    public void setToUnfollow(String toUnfollow) {
        this.toUnfollow = toUnfollow;
    }

    public AuthToken getAuthToken() {
        return authToken;
    }

    public void setAuthToken(AuthToken authToken) {
        this.authToken = authToken;
    }
}
