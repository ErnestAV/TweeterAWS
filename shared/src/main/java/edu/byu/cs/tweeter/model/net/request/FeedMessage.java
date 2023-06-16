package edu.byu.cs.tweeter.model.net.request;

import java.util.List;

public class FeedMessage {
    private String status;
    private List<String> followersAliases;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<String> getFollowersAliases() {
        return followersAliases;
    }

    public void setFollowersAliases(List<String> followersAliases) {
        this.followersAliases = followersAliases;
    }
}
