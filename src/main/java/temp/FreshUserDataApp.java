package temp;

import tweet.*;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class FreshUserDataApp {

    private static final DateTimeFormatter TS = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static void main(String[] args) {
        System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));

        ExecutorService pool = Executors.newFixedThreadPool(8);
        try {
            SimulatedUserDataService service1 =
                    new SimulatedUserDataService("ServiceA", pool, 1L, 80, 300);
            SimulatedUserDataService service2 =
                    new SimulatedUserDataService("ServiceB", pool, 2L, 80, 300);



            UserTweet ut = new UserTweet();
            Followers fl = new Followers();
            SampleDataGenerator gen = new SampleDataGenerator(42L, ut, fl);
            List<User> users = gen.generateUsers(10);

            long startNs = System.nanoTime();
            List<CompletableFuture<ComparisonResult>> pipelines = new ArrayList<>();
            for (User u : users) {
                String name = gen.nameOf(u.getId());

                CompletableFuture<UserDataSnapshot> f1 = service1.fetch(u.getId(), name);
                CompletableFuture<UserDataSnapshot> f2 = service2.fetch(u.getId(), name);

                CompletableFuture<ComparisonResult> compared = f1
                        .thenCombine(f2, FreshUserDataApp::compare)
                        .orTimeout(2, TimeUnit.SECONDS)
                        .exceptionally(t -> ComparisonResult.failed(u.getId(), name, t));

                pipelines.add(compared);
            }

            CompletableFuture.allOf(pipelines.toArray(new CompletableFuture[0])).join();
            long elapsedMs = (System.nanoTime() - startNs) / 1_000_000;

            printResults(pipelines);
            System.out.println();
            System.out.printf("Compared 2 services for %d users in %d ms (sequential would be ~%d ms).%n",
                    users.size(), elapsedMs, users.size() * 2 * 190);
        } finally {
            pool.shutdown();
            try {
                pool.awaitTermination(5, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private static ComparisonResult compare(UserDataSnapshot a, UserDataSnapshot b) {
        UserDataSnapshot winner = a.lastUpdated().isAfter(b.lastUpdated()) ? a : b;
        return new ComparisonResult(a.userId(), a.name(), a, b, winner, null);
    }

    private static void printResults(List<CompletableFuture<ComparisonResult>> pipelines) {
        System.out.printf("%-10s %-22s %-22s %-10s%n",
                "user", "ServiceA lastUpdated", "ServiceB lastUpdated", "winner");
        System.out.println("-".repeat(70));
        for (CompletableFuture<ComparisonResult> p : pipelines) {
            ComparisonResult r = p.join();
            if (r.error() != null) {
                System.out.printf("%-10s  ERROR: %s%n", r.userName(), r.error().getMessage());
                continue;
            }
            System.out.printf("%-10s %-22s %-22s %-10s%n",
                    r.userName(),
                    TS.format(r.serviceA().lastUpdated()),
                    TS.format(r.serviceB().lastUpdated()),
                    r.winner().source());
        }
    }

    record ComparisonResult(
            UUID userId,
            String userName,
            UserDataSnapshot serviceA,
            UserDataSnapshot serviceB,
            UserDataSnapshot winner,
            Throwable error
    ) {
        static ComparisonResult failed(UUID id, String name, Throwable t) {
            return new ComparisonResult(id, name, null, null, null, t);
        }
    }
}
