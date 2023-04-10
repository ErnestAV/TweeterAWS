package edu.byu.cs.tweeter.model.net.request;

public class FollowRequest {
    private String toFollow;
    private String authToken;

    private FollowRequest() {}

    public FollowRequest(String toFollow, String authToken) {
        this.toFollow = toFollow;
        this.authToken = authToken;
    }

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
