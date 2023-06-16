package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;

public class PostStatusRequest {
    private Status status;
    // Pass of Status Object
    private AuthToken authToken;

    private PostStatusRequest() {}
    public PostStatusRequest(Status status, AuthToken authToken) {
        this.status = status;
        this.authToken = authToken;
    }
    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public AuthToken getAuthToken() {
        return authToken;
    }

    public void setAuthToken(AuthToken authToken) {
        this.authToken = authToken;
    }
}
