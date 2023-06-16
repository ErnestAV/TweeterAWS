package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;

public class FollowersCountRequest {
    private String targetUser;
    private AuthToken authToken;

    private FollowersCountRequest() {}

    public FollowersCountRequest(String targetUser, AuthToken authToken) {

        this.targetUser = targetUser;
        this.authToken = authToken;
    }

    public String getTargetUser() {
        return targetUser;
    }

    public void setTargetUser(String targetUser) {
        this.targetUser = targetUser;
    }

    public AuthToken getAuthToken() {
        return authToken;
    }

    public void setAuthToken(AuthToken authToken) {
        this.authToken = authToken;
    }
}
