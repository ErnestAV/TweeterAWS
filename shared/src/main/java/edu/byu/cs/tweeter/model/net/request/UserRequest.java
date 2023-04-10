package edu.byu.cs.tweeter.model.net.request;

public class UserRequest {
    private String userAlias;
    private String authToken;

    private UserRequest() {}

    public UserRequest(String userAlias, String authToken) {

        this.userAlias = userAlias;
        this.authToken = authToken;
    }

    public String getUserAlias() {
        return userAlias;
    }

    public void setUserAlias(String userAlias) {
        this.userAlias = userAlias;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}
