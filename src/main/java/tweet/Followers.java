package tweet;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class Followers {

    private final Map<UUID, Set<UUID>> followingMap = new HashMap<>();

    public UUID follow(User user1, User user2) {
        followingMap.computeIfAbsent(user1.getId(), k -> new HashSet<>()).add(user2.getId());
        return user2.getId();
    }

    public UUID unFollow(User user1, User user2) {
        Set<UUID> following = followingMap.get(user1.getId());
        if (following == null) {
            return null;
        }
        following.remove(user2.getId());
        return user2.getId();
    }

    public boolean isFollower(User u1, User u2) {
        Set<UUID> following = followingMap.get(u1.getId());
        return following != null && following.contains(u2.getId());
    }

    /** Users that {@code u} follows. */
    public Set<UUID> getFollowing(User u) {
        Set<UUID> following = followingMap.get(u.getId());
        return following == null ? Collections.emptySet() : Collections.unmodifiableSet(following);
    }

    /** Users that follow {@code u} — reverse scan since we only keep the forward index. */
    public Set<UUID> getFollowers(User u) {
        UUID target = u.getId();
        Set<UUID> followers = new HashSet<>();
        for (Map.Entry<UUID, Set<UUID>> e : followingMap.entrySet()) {
            if (e.getValue().contains(target)) {
                followers.add(e.getKey());
            }
        }
        return followers;
    }
}
