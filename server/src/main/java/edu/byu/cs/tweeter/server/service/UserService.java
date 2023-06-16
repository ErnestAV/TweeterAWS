package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.request.LogoutRequest;
import edu.byu.cs.tweeter.model.net.request.RegisterRequest;
import edu.byu.cs.tweeter.model.net.request.UserRequest;
import edu.byu.cs.tweeter.model.net.response.LoginResponse;
import edu.byu.cs.tweeter.model.net.response.LogoutResponse;
import edu.byu.cs.tweeter.model.net.response.RegisterResponse;
import edu.byu.cs.tweeter.model.net.response.UserResponse;
import edu.byu.cs.tweeter.server.dao.MainDAOFactoryInterface;
import edu.byu.cs.tweeter.server.dao.UserDAOInterface;

public class UserService {

    private UserDAOInterface userDAO;

    public UserService(MainDAOFactoryInterface mainDAOFactoryInterface) {
        this.userDAO = mainDAOFactoryInterface.getUserDAO();
    }

    public LoginResponse login(LoginRequest loginRequest) {
        if (loginRequest.getUsername() == null) {
            throw new RuntimeException("[Bad Request] Missing a username");
        } else if (loginRequest.getPassword() == null) {
            throw new RuntimeException("[Bad Request] Missing a password");
        }

        return userDAO.login(loginRequest);
    }

    public UserResponse getUser(UserRequest userRequest) {
        if(userRequest.getUserAlias() == null) {
            throw new RuntimeException("[Bad Request] Missing a username.");
        }
        else if(userRequest.getAuthToken() == null) {
            throw new RuntimeException("[Bad Request] Missing an Authtoken.");
        } else if (!userDAO.isTokenStillValid(userRequest.getAuthToken())) {
            return new UserResponse("Session expired. Log out.");
        }
        return userDAO.getUser(userRequest);
    }

    public LogoutResponse logout(LogoutRequest logoutRequest) {
        if (!userDAO.isTokenStillValid(logoutRequest.getAuthToken())) {
            return new LogoutResponse("Session expired. Log out.");
        }
        return userDAO.logout(logoutRequest);
    }

    public RegisterResponse register(RegisterRequest registerRequest) {
        if (registerRequest.getUsername() == null) {
            throw new RuntimeException("[Bad Request] Missing a username");
        } else if (registerRequest.getPassword() == null) {
            throw new RuntimeException("[Bad Request] Missing a password");
        } else if (registerRequest.getImage() == null) {
            throw new RuntimeException("[Bad Request] Missing a profile picture");
        }
        return userDAO.register(registerRequest);
    }
}
