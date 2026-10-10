package tweet;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class TwitterApp {

    private static final DateTimeFormatter TS = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static void main(String[] args) {
        System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));

        UserTweet userTweet = new UserTweet();
        Followers followers = new Followers();

        SampleDataGenerator gen = new SampleDataGenerator(42L, userTweet, followers);
        List<User> users = gen.generateUsers(100);
        gen.generateTweets(1000);
        gen.generateFollowGraph(8);

        System.out.println("Generated " + users.size() + " users, 1000 tweets, follow graph seeded.");
        System.out.println();

        User sample = users.get(7);
        String sampleName = gen.nameOf(sample.getId());

        runLatestTweetsTest(sample, sampleName, userTweet);
        System.out.println();
        runFollowersTest(sample, sampleName, followers, gen);
        System.out.println();
        runFollowingTest(sample, sampleName, followers, gen);
    }

    private static void runLatestTweetsTest(User user, String name, UserTweet userTweet) {
        System.out.println("=== Latest 10 tweets for " + name + " ===");
        List<Tweet> latest = userTweet.getLatestTweets(user.getId(), 10);
        if (latest.isEmpty()) {
            System.out.println("(no tweets)");
            return;
        }
        for (Tweet t : latest) {
            System.out.println("  " + TS.format(t.getTweetTime()) + "  " + t.getTweet());
        }
    }

    private static void runFollowersTest(User user, String name, Followers followers, SampleDataGenerator gen) {
        Set<UUID> followerIds = followers.getFollowers(user);
        System.out.println("=== tweet.Followers of " + name + " (" + followerIds.size() + ") ===");
        printNames(followerIds, gen);
    }

    private static void runFollowingTest(User user, String name, Followers followers, SampleDataGenerator gen) {
        Set<UUID> followingIds = followers.getFollowing(user);
        System.out.println("=== " + name + " follows (" + followingIds.size() + ") ===");
        printNames(followingIds, gen);
    }

    private static void printNames(Set<UUID> ids, SampleDataGenerator gen) {
        if (ids.isEmpty()) {
            System.out.println("  (none)");
            return;
        }
        StringBuilder line = new StringBuilder("  ");
        int onLine = 0;
        for (UUID id : ids) {
            String name = gen.nameOf(id);
            if (onLine > 0) line.append(", ");
            line.append(name);
            onLine++;
            if (onLine == 8) {
                System.out.println(line);
                line = new StringBuilder("  ");
                onLine = 0;
            }
        }
        if (onLine > 0) System.out.println(line);
    }
}
