package edu.byu.cs.tweeter.server.util;

import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.server.dao.FollowDAOInterface;
import edu.byu.cs.tweeter.server.dao.MainDAOFactoryInterface;
import edu.byu.cs.tweeter.server.dao.UserDAOInterface;
import edu.byu.cs.tweeter.server.dao.dynamoDAO.FactoryDynamoDAO;

public abstract class Filler {

    // How many follower users to add
    // We recommend you test this with a smaller number first, to make sure it works for you
    private final static int NUM_USERS = 10000;

    // The alias of the user to be followed by each user created
    // This example code does not add the target user, that user must be added separately.
    private final static String FOLLOW_TARGET = "@ernesto";

    public static void fillDatabase() {

        MainDAOFactoryInterface mainDAOFactoryInterface = new FactoryDynamoDAO();

        // Get instance of DAOs by way of the Abstract Factory Pattern
        UserDAOInterface userDAOInterface = mainDAOFactoryInterface.getUserDAO();
        FollowDAOInterface followDAOInterface = mainDAOFactoryInterface.getFollowDAO();

        List<String> followers = new ArrayList<>();
        List<User> users = new ArrayList<>();

        // Iterate over the number of users you will create
        for (int i = 1; i <= NUM_USERS; i++) {

            String name = "User " + i;
            String lastName = "Batch" + i;
            String alias = "@userbatch" + i;

            // Note that in this example, a UserDTO only has a name and an alias.
            // The url for the profile image can be derived from the alias in this example
            User user = new User(name, lastName,alias, null);
            user.setAlias(alias);
            user.setFirstName(name);
            users.add(user);

            // Note that in this example, to represent a follows relationship, only the aliases
            // of the two users are needed
            followers.add(alias);
        }

        // Call the DAOs for the database logic
        if (users.size() > 0) {
            userDAOInterface.addUserBatch(users);
        }
        if (followers.size() > 0) {
            followDAOInterface.addFollowersBatch(followers, FOLLOW_TARGET);
        }
    }

    public static void main(String[] args) {
        fillDatabase();
    }
}
