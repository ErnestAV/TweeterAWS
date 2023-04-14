package edu.byu.cs.tweeter.server.dao;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.FollowersCountRequest;
import edu.byu.cs.tweeter.model.net.request.FollowingCountRequest;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.request.LogoutRequest;
import edu.byu.cs.tweeter.model.net.request.RegisterRequest;
import edu.byu.cs.tweeter.model.net.request.UserRequest;
import edu.byu.cs.tweeter.model.net.response.FollowersCountResponse;
import edu.byu.cs.tweeter.model.net.response.FollowingCountResponse;
import edu.byu.cs.tweeter.model.net.response.LoginResponse;
import edu.byu.cs.tweeter.model.net.response.LogoutResponse;
import edu.byu.cs.tweeter.model.net.response.RegisterResponse;
import edu.byu.cs.tweeter.model.net.response.UserResponse;

public interface UserDAOInterface {
    public LoginResponse login(LoginRequest loginRequest);
    public RegisterResponse register(RegisterRequest registerRequest);
    public LogoutResponse logout(LogoutRequest logoutRequest);
    public FollowersCountResponse getFollowersCount(FollowersCountRequest followersCountRequest);
    public FollowingCountResponse getFolloweeCount(FollowingCountRequest followingCountRequest);
    public UserResponse getUser(UserRequest username);
}
