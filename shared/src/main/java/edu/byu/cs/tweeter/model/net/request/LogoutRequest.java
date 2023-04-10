package edu.byu.cs.tweeter.model.net.request;

public class LogoutRequest {
    private String authToken;

    private LogoutRequest() {}
    public LogoutRequest(String authToken) {
        this.authToken = authToken;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}
