package tweet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.UUID;

public class UserTweet {

    private final Map<UUID, PriorityQueue<Tweet>> tweetStorage = new HashMap<>();

    public Tweet postTweet(UUID userId, Tweet tweet) {
        PriorityQueue<Tweet> userTweets = tweetStorage.computeIfAbsent(
                userId, k -> new PriorityQueue<>(10, new TweetComparatorWithTime()));
        userTweets.add(tweet);
        return tweet;
    }

    /** Most recent {@code count} tweets for a user, newest first. */
    public List<Tweet> getLatestTweets(UUID userId, int count) {
        PriorityQueue<Tweet> userTweets = tweetStorage.get(userId);
        if (userTweets == null) {
            return Collections.emptyList();
        }
        List<Tweet> latest = new ArrayList<>();
        for (int i = 0; i < count && !userTweets.isEmpty(); i++) {
            latest.add(userTweets.poll());
        }
        // Re-add them back to preserve all tweets
        for (Tweet tweet : latest) {
            userTweets.add(tweet);
        }
        return latest;
    }
}
