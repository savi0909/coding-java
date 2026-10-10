package temp;

import tweet.UserDataSnapshot;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class SimulatedUserDataService {

    private final String name;
    private final Executor executor;
    private final Random random;
    private final int minLatencyMs;
    private final int maxLatencyMs;

    public SimulatedUserDataService(String name, Executor executor, long seed,
                                    int minLatencyMs, int maxLatencyMs) {
        this.name = name;
        this.executor = executor;
        this.random = new Random(seed);
        this.minLatencyMs = minLatencyMs;
        this.maxLatencyMs = maxLatencyMs;
    }

    public CompletableFuture<UserDataSnapshot> fetch(UUID userId, String userName) {
        int latency = pickLatency();
        int updatedMinutesAgo = pickUpdatedMinutesAgo();
        return CompletableFuture.supplyAsync(() -> {
            sleepQuietly(latency);
            LocalDateTime updated = LocalDateTime.now().minusMinutes(updatedMinutesAgo);
            String payload = name + "-payload-for-" + userName;
            return new UserDataSnapshot(userId, userName, payload, updated, name);
        }, executor);
    }

    public String getName() {
        return name;
    }

    private synchronized int pickLatency() {
        return minLatencyMs + random.nextInt(Math.max(1, maxLatencyMs - minLatencyMs));
    }

    private synchronized int pickUpdatedMinutesAgo() {
        return random.nextInt(60 * 24);
    }

    private static void sleepQuietly(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("interrupted while simulating service latency", e);
        }
    }
}
