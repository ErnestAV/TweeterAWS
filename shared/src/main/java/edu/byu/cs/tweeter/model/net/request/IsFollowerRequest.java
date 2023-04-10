package edu.byu.cs.tweeter.model.net.request;

public class IsFollowerRequest {
    private String followerAlias;
    private String followeeAlias;
    private String authToken;


    private IsFollowerRequest() {}
    public IsFollowerRequest(String followerAlias, String followeeAlias, String authToken) {
        this.followerAlias = followerAlias;
        this.followeeAlias = followeeAlias;
        this.authToken = authToken;
    }


    public String getFollowerAlias() {
        return followerAlias;
    }

    public void setFollowerAlias(String followerAlias) {
        this.followerAlias = followerAlias;
    }

    public String getFolloweeAlias() {
        return followeeAlias;
    }

    public void setFolloweeAlias(String followeeAlias) {
        this.followeeAlias = followeeAlias;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}
