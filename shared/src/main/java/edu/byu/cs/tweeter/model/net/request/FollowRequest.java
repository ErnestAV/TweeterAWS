package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;

public class FollowRequest {

    private String currentUser;
    private String toFollow;
    private AuthToken authToken;

    private FollowRequest() {}

    public FollowRequest(String currentUser, String toFollow, AuthToken authToken) {
        this.currentUser = currentUser;
        this.toFollow = toFollow;
        this.authToken = authToken;
    }

    public String getCurrentUser() { return currentUser; }

    public void setCurrentUser(String currentUser) { this.currentUser = currentUser; }

    public String getToFollow() {
        return toFollow;
    }

    public void setToFollow(String toFollow) {
        this.toFollow = toFollow;
    }

    public AuthToken getAuthToken() {
        return authToken;
    }

    public void setAuthToken(AuthToken authToken) {
        this.authToken = authToken;
    }
}
