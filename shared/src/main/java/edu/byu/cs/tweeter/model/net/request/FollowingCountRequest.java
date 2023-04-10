package edu.byu.cs.tweeter.model.net.request;

public class FollowingCountRequest {
    private String targetUser;
    private String authToken;

    private FollowingCountRequest() {}

    public FollowingCountRequest(String targetUser, String authToken) {

        this.targetUser = targetUser;
        this.authToken = authToken;
    }

    public String getTargetUser() {
        return targetUser;
    }

    public void setTargetUser(String targetUser) {
        this.targetUser = targetUser;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}
