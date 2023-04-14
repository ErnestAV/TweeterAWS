package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;

import edu.byu.cs.tweeter.model.net.request.UserRequest;
import edu.byu.cs.tweeter.model.net.response.UserResponse;
import edu.byu.cs.tweeter.server.service.UserService;

public class GetUserHandler extends BaseHandler<UserRequest, UserResponse> {
    @Override
    public UserResponse handleRequest(UserRequest userRequest, Context context) {
        UserService userService = new UserService(factoryDAO);
        return userService.getUser(userRequest);
    }
}
