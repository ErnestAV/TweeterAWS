package edu.byu.cs.tweeter.client;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.RegisterRequest;
import edu.byu.cs.tweeter.model.net.response.RegisterResponse;

public class RegisterTests {
    private RegisterRequest registerRequest;
    private RegisterResponse registerResponse;
    private ServerFacade facade;


    @BeforeEach
    public void setup() {
        registerRequest = new RegisterRequest("@FakeUsername", "FakePassword",
                "FakeName", "FakeLastName", "Profile");

        facade = new ServerFacade();
    }

    @Test
    public void testRegister() throws IOException, TweeterRemoteException {
        registerResponse = facade.register(registerRequest, "\\register");

        Assertions.assertTrue(registerResponse.isSuccess());
        Assertions.assertNotNull(registerResponse.getUser());
    }
}
