package tweet;

import java.util.UUID;

public class User {

    private final UUID id;

    public User() {
        this.id = UUID.randomUUID();
    }

    public User(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }
}
