package tweet;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserDataSnapshot(
        UUID userId,
        String name,
        String payload,
        LocalDateTime lastUpdated,
        String source
) {
}
