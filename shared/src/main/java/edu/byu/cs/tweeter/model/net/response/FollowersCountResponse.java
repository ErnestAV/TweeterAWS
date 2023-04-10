package edu.byu.cs.tweeter.model.net.response;

public class FollowersCountResponse extends Response {
    private int count;

    public FollowersCountResponse(int count) {
        super(true);
        this.count = count;
    }
    public FollowersCountResponse(String message) {
        super(false, message);
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
