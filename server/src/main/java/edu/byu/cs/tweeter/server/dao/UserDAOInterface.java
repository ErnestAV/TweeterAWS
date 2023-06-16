package edu.byu.cs.tweeter.server.dao;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.AuthToken;
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
import edu.byu.cs.tweeter.server.dao.dynamoDAO.bean.UserBean;

public interface UserDAOInterface {
    public LoginResponse login(LoginRequest loginRequest);
    public RegisterResponse register(RegisterRequest registerRequest);
    public LogoutResponse logout(LogoutRequest logoutRequest);
    public FollowersCountResponse getFollowersCount(FollowersCountRequest followersCountRequest);
    public FollowingCountResponse getFolloweeCount(FollowingCountRequest followingCountRequest);
    public UserResponse getUser(UserRequest username);
    public void updateFollowersCount(String userAlias, int toAdd);
    public void updateFolloweesCount(String userAlias, int toAdd);

    public boolean isTokenStillValid(AuthToken authToken);
    public void addUserBatch(List<User> users);
    public void writePortionOfUserBeans(List<UserBean> userBeans);
}
