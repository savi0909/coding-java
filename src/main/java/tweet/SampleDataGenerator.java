package tweet;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

public class SampleDataGenerator {

    private static final String[] FIRST_NAMES = {
            "Alice", "Bob", "Carol", "Dan", "Eve", "Frank", "Grace", "Heidi",
            "Ivan", "Judy", "Kevin", "Laura", "Mallory", "Niaj", "Olivia", "Peggy",
            "Quentin", "Rupert", "Sybil", "Trent", "Uma", "Victor", "Walter", "Xena",
            "Yara", "Zane"
    };

    private static final String[] TWEET_TEMPLATES = {
            "just shipped a new feature today",
            "coffee count: %d cups and still going",
            "anyone else struggling with %s?",
            "TIL: %s is way more interesting than I thought",
            "hot take: %s is overrated",
            "looking for recommendations on %s",
            "remember when %s was the hot new thing?",
            "deployed to prod on a Friday, what could go wrong",
            "%d lines of code deleted, %d added — feels good",
            "rewriting it in Rust this weekend, send help"
    };

    private static final String[] TOPICS = {
            "Kubernetes", "GraphQL", "Rust", "tabs vs spaces", "monorepos", "microservices",
            "Postgres", "Redis", "TypeScript", "vim", "emacs", "regex", "SQL window functions",
            "CRDTs", "event sourcing", "DDD", "TDD"
    };

    private final Random random;
    private final UserTweet userTweet;
    private final Followers followers;

    private final List<User> users = new ArrayList<>();
    private final Map<UUID, String> userNames = new HashMap<>();

    public SampleDataGenerator(long seed, UserTweet userTweet, Followers followers) {
        this.random = new Random(seed);
        this.userTweet = userTweet;
        this.followers = followers;
    }

    public List<User> generateUsers(int count) {
        for (int i = 0; i < count; i++) {
            User u = new User();
            users.add(u);
            String name = FIRST_NAMES[i % FIRST_NAMES.length] + (i / FIRST_NAMES.length + 1);
            userNames.put(u.getId(), name);
        }
        return users;
    }

    public void generateTweets(int count) {
        if (users.isEmpty()) {
            throw new IllegalStateException("call generateUsers(...) first");
        }
        LocalDateTime now = LocalDateTime.now();
        for (int i = 0; i < count; i++) {
            User author = users.get(random.nextInt(users.size()));
            int minutesAgo = random.nextInt(30 * 24 * 60);
            LocalDateTime when = now.minusMinutes(minutesAgo);
            String text = randomTweetText();
            userTweet.postTweet(author.getId(), new Tweet(text, when));
        }
    }

    public void generateFollowGraph(int avgFollowsPerUser) {
        if (users.isEmpty()) {
            throw new IllegalStateException("call generateUsers(...) first");
        }
        for (User u : users) {
            int target = Math.max(1, avgFollowsPerUser / 2 + random.nextInt(avgFollowsPerUser + 1));
            Set<Integer> picked = new LinkedHashSet<>();
            int attempts = 0;
            while (picked.size() < target && attempts < target * 5) {
                int idx = random.nextInt(users.size());
                User candidate = users.get(idx);
                if (!candidate.getId().equals(u.getId())) {
                    picked.add(idx);
                }
                attempts++;
            }
            for (int idx : picked) {
                followers.follow(u, users.get(idx));
            }
        }
    }

    public List<User> getUsers() {
        return users;
    }

    public String nameOf(UUID id) {
        return userNames.getOrDefault(id, id.toString());
    }

    private String randomTweetText() {
        String template = TWEET_TEMPLATES[random.nextInt(TWEET_TEMPLATES.length)];
        long count = template.chars().filter(c -> c == '%').count();
        if (count == 0) return template;
        if (template.contains("%d") && template.contains("%s")) {
            return String.format(template, randomCount(), randomTopic());
        }
        if (template.contains("%d") && count == 2) {
            return String.format(template, randomCount(), randomCount());
        }
        if (template.contains("%d")) {
            return String.format(template, randomCount());
        }
        return String.format(template, randomTopic());
    }

    private int randomCount() {
        return 1 + random.nextInt(500);
    }

    private String randomTopic() {
        return TOPICS[random.nextInt(TOPICS.length)];
    }
}
