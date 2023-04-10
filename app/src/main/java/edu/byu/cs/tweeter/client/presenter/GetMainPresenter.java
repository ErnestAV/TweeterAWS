package edu.byu.cs.tweeter.client.presenter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.model.service.observer.CountObserver;
import edu.byu.cs.tweeter.client.presenter.view.BaseView;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class GetMainPresenter extends Presenter {

    public GetMainPresenter(View view) {
        super(view);
    }

    public interface View extends BaseView {
        void showFollowButton(boolean value);

        void isFollowing(boolean value);

        void updateFollowButton(boolean value);

        void setFollowButtonEnabled(boolean value);

        void logoutUser();

        void followed();

        void unFollowed();

        void setFollowerCount(int count);

        void setFollowingCount(int count);
    }

    public void isAbleToFollow(User user) {
        if (user.compareTo(Cache.getInstance().getCurrUser()) == 0) {
            ((View) view).showFollowButton(false);
        } else {
            ((View) view).showFollowButton(true);
            new FollowService().getIsFollowerTask(Cache.getInstance().getCurrUser(), user, new IsFollowingObserver());
        }
    }

    public void followButtonWasClicked(User user, String followButtonString) {
        if (followButtonString.equals("Following")) {
            new FollowService().unfollowTask(user, new UnfollowObserver());
            view.displayMessage("Removing " + user.getName() + "...");
        } else {
            new FollowService().followTask(user, new FollowObserver());
            new FollowService().updateFollowersAndFollowingTask(user, new FollowerCountObserver(), new FollowingCountObserver());
            view.displayMessage("Adding + " + user.getName() + "...");
            ((View) view).updateFollowButton(false);
        }
    }

    public void logoutTask() {
        view.displayMessage("Logging Out...");
        new UserService().logoutTask(new LogoutObserver());
    }

    public StatusService getStatusService() {
        return new StatusService();
    }

    public void getStatusPostedTask(String post) throws ParseException {
        view.displayMessage("Posting Status...");
        Status status = new Status(post, Cache.getInstance().getCurrUser(), System.currentTimeMillis(), parseURLs(post), parseMentions(post));
        getStatusService().getStatusTask(status, new PostStatusObserver());
    }

    public void updateASelectedUserFollowingAndFollowers(User selectedUser) {
        new FollowService().updateFollowersAndFollowingTask(selectedUser, new FollowerCountObserver(), new FollowingCountObserver());
    }

    public String getFormattedDateTime() throws ParseException {
        SimpleDateFormat userFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        SimpleDateFormat statusFormat = new SimpleDateFormat("MMM d yyyy h:mm aaa");

        return statusFormat.format(userFormat.parse(LocalDate.now().toString() + " " + LocalTime.now().toString().substring(0, 8)));
    }

    public List<String> parseURLs(String post) {
        List<String> containedUrls = new ArrayList<>();
        for (String word : post.split("\\s")) {
            if (word.startsWith("http://") || word.startsWith("https://")) {

                int index = findUrlEndIndex(word);

                word = word.substring(0, index);

                containedUrls.add(word);
            }
        }

        return containedUrls;
    }

    public List<String> parseMentions(String post) {
        List<String> containedMentions = new ArrayList<>();

        for (String word : post.split("\\s")) {
            if (word.startsWith("@")) {
                word = word.replaceAll("[^a-zA-Z0-9]", "");
                word = "@".concat(word);

                containedMentions.add(word);
            }
        }

        return containedMentions;
    }

    public int findUrlEndIndex(String word) {
        if (word.contains(".com")) {
            int index = word.indexOf(".com");
            index += 4;
            return index;
        } else if (word.contains(".org")) {
            int index = word.indexOf(".org");
            index += 4;
            return index;
        } else if (word.contains(".edu")) {
            int index = word.indexOf(".edu");
            index += 4;
            return index;
        } else if (word.contains(".net")) {
            int index = word.indexOf(".net");
            index += 4;
            return index;
        } else if (word.contains(".mil")) {
            int index = word.indexOf(".mil");
            index += 4;
            return index;
        } else {
            return word.length();
        }
    }

    /**
     * Observers
     */

    private class IsFollowingObserver implements FollowService.IsFollowingObserver {

        @Override
        public void handleSuccess(boolean value) {
            ((View) view).isFollowing(value);
        }

        @Override
        public void handleFailure(String message) {
            ((View) view).displayMessage("Could not get IsFollower: " + message);
        }

        @Override
        public void handleException(Exception ex) {
            ((View) view).displayMessage("Could not get IsFollower due to exception: " + ex.getMessage());
        }
    }

    private class UnfollowObserver implements FollowService.UnfollowObserver {

        @Override
        public void handleSuccess() {
            ((View) view).unFollowed();
            ((View) view).setFollowButtonEnabled(true);
        }
        @Override
        public void handleFailure(String message) {
            ((View) view).displayMessage("Could not unfollow: " + message);
        }

        @Override
        public void handleException(Exception exception) {
            ((View) view).displayMessage("Could not unfollow because of exception: " + exception.getMessage());
        }
    }

    private class FollowObserver implements FollowService.FollowObserver {
        @Override
        public void handleSuccess() {
            ((View) view).followed();
            ((View) view).setFollowButtonEnabled(true);
        }

        @Override
        public void handleFailure(String message) {
            ((View) view).displayMessage("Could not follow: " + message);
        }

        @Override
        public void handleException(Exception exception) {
            ((View) view).displayMessage("Could not follow because of exception:" + exception.getMessage());
        }
    }

    public class FollowerCountObserver implements CountObserver {

        @Override
        public void handleSuccess(int count) {
            ((View) view).setFollowerCount(count);
        }

        @Override
        public void handleFailure(String message) {
            ((View) view).displayMessage("Could not get follower count: " + message);
        }

        @Override
        public void handleException(Exception exception) {
            ((View) view).displayMessage("Could not get follower count due to exception: " + exception.getMessage());
        }
    }

    public class FollowingCountObserver implements CountObserver {

        @Override
        public void handleSuccess(int count) {
            ((View) view).setFollowingCount(count);
        }

        @Override
        public void handleFailure(String message) {
            ((View) view).displayMessage("Could not get following count: " + message);
        }

        @Override
        public void handleException(Exception ex) {
            ((View) view).displayMessage("Could not get following count due to exception: " + ex.getMessage());
        }
    }

    private class LogoutObserver implements UserService.LogoutObserver {

        @Override
        public void handleSuccess() {
            Cache.getInstance().clearCache();
            ((View) view).logoutUser();
        }

        @Override
        public void handleFailure(String message) {
            ((View) view).displayMessage("Could not logout: " + message);
        }

        @Override
        public void handleException(Exception exception) {
            ((View) view).displayMessage("Could not logout due to exception: " + exception.getMessage());
        }
    }

    public class PostStatusObserver implements StatusService.PostStatusObserver {

        @Override
        public void handleSuccess() {
            ((View) view).clearMessage();
            ((View) view).displayMessage("Successfully Posted!");
        }

        @Override
        public void handleFailure(String message) {
            ((View) view).displayMessage("Failed to post status: " + message);
        }

        @Override
        public void handleException(Exception exception) {
            ((View) view).displayMessage("Failed to post status due to exception: " + exception);
        }
    }
}
