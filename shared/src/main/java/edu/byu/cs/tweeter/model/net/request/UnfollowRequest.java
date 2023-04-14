package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.util.FakeData;

public class UnfollowRequest {

    private String currentUser;
    private String toUnfollow;
    private String authToken;

    private UnfollowRequest() {}

    public UnfollowRequest(String currentUser, String toUnfollow, String authToken) { // TODO: ADD THE CURRENT USER
        this.currentUser = currentUser;
        this.toUnfollow = toUnfollow;
        this.authToken = authToken;
    }

    public String getCurrentUser() { return currentUser; }

    public String getToUnfollow() {
        return toUnfollow;
    }

    public void setToUnfollow(String toUnfollow) {
        this.toUnfollow = toUnfollow;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}
