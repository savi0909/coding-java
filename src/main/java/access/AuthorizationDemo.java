package access;

import java.util.*;

// ---- domain (records: immutable, minimal boilerplate) ----
record User(int id, String name, List<String> roles) {}
record Page(int id, String path, List<String> requiredRoles) {}

/**
 * The single seam that makes this "easy to maintain when new permission types arrive."
 * A new permission type = a new AccessRule implementation, never an edit to the loop below.
 */
@FunctionalInterface
interface AccessRule {
    boolean grants(Set<String> userRoles, Page page);
}

/** Default policy: open if the page requires no roles, OR the user holds at least one. */
final class AnyRequiredRoleRule implements AccessRule {
    @Override
    public boolean grants(Set<String> userRoles, Page page) {
        List<String> required = page.requiredRoles();
        // Empty/null requiredRoles == public. This branch MUST come first, or a
        // zero-role user (Charlie) wrongly loses /dashboard.   <-- interviewer probes this
        if (required == null || required.isEmpty()) return true;
        // Iterate the small list, O(1) membership on the set.  <-- hot path
        for (String role : required) {
            if (userRoles.contains(role)) return true;
        }
        return false;
    }
}

final class Authorizer {
    private final AccessRule rule;

    Authorizer()                 { this(new AnyRequiredRoleRule()); }
    Authorizer(AccessRule rule)  { this.rule = rule; }   // inject a different rule later — no loop rewrite

    List<String> getAccessiblePages(User user, List<Page> pages) {
        if (pages == null) return List.of();
        // Build the role set ONCE (null-safe). Turns a naive O(P*R*U) into O(P*R).  <-- hot-path prep
        Set<String> userRoles = (user == null || user.roles() == null)
                ? Set.of()
                : new HashSet<>(user.roles());

        List<String> accessible = new ArrayList<>();   // preserves page order -> deterministic output
        for (Page page : pages) {
            if (rule.grants(userRoles, page)) {
                accessible.add(page.path());
            }
        }
        return accessible;
    }
}

public class AuthorizationDemo {
    public static void main(String[] args) {
        List<Page> pages = List.of(
            new Page(1, "/dashboard", List.of()),
            new Page(2, "/payroll",   List.of("HR")),
            new Page(3, "/admin",     List.of("ADMIN")),
            new Page(4, "/tickets",   List.of("SUPPORT", "ADMIN"))
        );

        Authorizer auth = new Authorizer();

        User alice   = new User(1, "Alice",   List.of("ADMIN", "HR"));
        User bob     = new User(2, "Bob",     List.of("SUPPORT"));
        User charlie = new User(3, "Charlie", List.of());

        System.out.println(auth.getAccessiblePages(alice,   pages)); // [/dashboard, /payroll, /admin, /tickets]
        System.out.println(auth.getAccessiblePages(bob,     pages)); // [/dashboard, /tickets]
        System.out.println(auth.getAccessiblePages(charlie, pages)); // [/dashboard]  <- zero-role user keeps public

        // edge cases
        System.out.println(auth.getAccessiblePages(new User(4,"Dana",List.of("FINANCE")), pages)); // [/dashboard]
        System.out.println(auth.getAccessiblePages(alice, List.of()));                              // []
        System.out.println(auth.getAccessiblePages(new User(5,"Eve",null), pages));                 // [/dashboard]
    }
}