package edu.byu.cs.tweeter.model.net.request;

public class FollowRequest {

    private String currentUser;
    private String toFollow;
    private String authToken;

    private FollowRequest() {}

    public FollowRequest(String currentUser, String toFollow, String authToken) {
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

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}
