package tweet;

import java.time.LocalDateTime;

public class Tweet {

    private final String tweet;
    private final LocalDateTime tweetTime;

    public Tweet(String tweet, LocalDateTime tweetTime) {
        this.tweet = tweet;
        this.tweetTime = tweetTime;
    }

    public String getTweet() {
        return tweet;
    }

    public LocalDateTime getTweetTime() {
        return tweetTime;
    }
}
