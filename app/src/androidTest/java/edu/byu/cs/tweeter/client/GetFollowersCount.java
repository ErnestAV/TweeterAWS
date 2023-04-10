package edu.byu.cs.tweeter.client;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.FollowersCountRequest;
import edu.byu.cs.tweeter.model.net.response.FollowersCountResponse;

public class GetFollowersCount {
    private FollowersCountRequest followersCountRequest;
    private FollowersCountResponse followersCountResponse;
    private ServerFacade facade;

    @BeforeEach
    public void setup() {
        followersCountRequest = new FollowersCountRequest("@FakeTarget", "FakeAuthToken");

        facade = new ServerFacade();
    }

    @Test
    public void testGetFollowersCount() throws IOException, TweeterRemoteException {
        followersCountResponse = facade.getFollowersCount(followersCountRequest, "\\getfollowerscount");

        Assertions.assertTrue(followersCountResponse.isSuccess());
        Assertions.assertNotNull(followersCountResponse.getCount());
    }
}
