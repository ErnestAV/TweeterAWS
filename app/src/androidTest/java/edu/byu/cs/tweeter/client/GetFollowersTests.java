package edu.byu.cs.tweeter.client;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.FollowersRequest;
import edu.byu.cs.tweeter.model.net.response.FollowersResponse;

public class GetFollowersTests {
    private FollowersRequest followersRequest;
    private AuthToken authToken;
    private FollowersResponse followersResponse;
    private ServerFacade facade;

    @BeforeEach
    public void setup() {
        // AuthToken authToken, String followeeAlias, int limit, String lastFollowerAlias
        authToken = new AuthToken();
        followersRequest = new FollowersRequest(authToken, "@FakeAlias", 20, "@LastFakeAlias");

        facade = new ServerFacade();
    }

    @Test
    public void testGetFollowers() throws IOException, TweeterRemoteException {
        followersResponse = facade.getFollowers(followersRequest, "\\getfollowers");

        Assertions.assertTrue(followersResponse.isSuccess());
        Assertions.assertNotNull(followersResponse.getFollowers());
    }
}
