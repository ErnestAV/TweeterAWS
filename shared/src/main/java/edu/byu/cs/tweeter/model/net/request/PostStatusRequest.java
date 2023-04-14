package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.Status;

public class PostStatusRequest {
    private Status status;
    // Pass of Status Object
    private String authToken;

    private PostStatusRequest() {}
    public PostStatusRequest(Status status, String authToken) {
        this.status = status;
        this.authToken = authToken;
    }
    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}
