package edu.byu.cs.tweeter.model.net.request;

public class PostStatusRequest {
    private String status;
    private String authToken;


    private PostStatusRequest() {}
    public PostStatusRequest(String status, String authToken) {
        this.status = status;
        this.authToken = authToken;
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}
