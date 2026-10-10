# hello — Coding Interview Prep (Principal Engineer, Backend)

This repo is a sandbox for backend interview prep at the Principal Engineer level —
LLD/DSA problems (e.g. `Design Twitter`) and concurrency exercises (`temp/` —
`CompletableFuture`/`Executor` fan-out patterns). Java 21, Maven.

## Interaction style
- **Direct solution + critique.** Write working code first, then critique it like an
  interviewer would — don't wait to be asked.
- **Critique lives in the file, not the console.** Put it in a header block comment at
  the top of the file (or inline at the relevant line if it's specific to one method).
  Console/chat prose is transient and not useful for later review — the file is the
  artifact that persists. Still give a short spoken summary, but the file is the
  source of truth.
- **Succinct.** No Feynman-style analogies, no multi-step teaching loops, no padding.
  Override the global explanatory/output-style defaults for this project.
- Every critique must cover, briefly:
  - Time/space complexity of the core operations.
  - Concurrency correctness (races, visibility, thread-safety) if the code touches threads/futures.
  - Edge cases an interviewer would probe (empty input, duplicate calls, ties, overflow).
  - What breaks at scale (millions of users/tweets) and the standard fix (fan-out-on-write
    vs fan-out-on-read, caching, sharding, etc.) — one line, not a system-design essay.
  - A better alternative if one exists, with the tradeoff it costs.

## Project state
- `docs/learning-plan.md` is the living study-order tracker (phases, dependencies, backlog).
  Update it whenever problems are added/solved or the recommended order changes.
- Topic packages: `dsa.<topic>` (arrays, hashing, slidingwindow, twopointers, binarysearch,
  linkedlist, stack, heap, tree, graph, backtracking, dp, datastructures) and `lld.cache`.
  Merged from `D:\aa-coding-prep-java` on 2026-10-10; those files are marked 📥 in the plan
  (compile, not yet critiqued). New problems go in the matching `dsa.<topic>` package.
  Package-private helper classes (`Node`, `Solution`, `App`) repeat across files, so problems
  that would clash get a subpackage (e.g. `dsa.tree.mindepth`, `dsa.backtracking.queens`).
- Root-level classes (`User`, `Tweet`, `UserTweet`, `Followers`, `SampleDataGenerator`,
  `TwitterApp`) are in the **default package** and compile/run standalone.
- `temp/` (`FreshUserDataApp`, `SimulatedUserDataService`, `UserDataSnapshot`) is a
  **separate, currently broken** concurrency exercise — it expects a `tweet` package
  (see the `import tweet.UserDataSnapshot;` in `SimulatedUserDataService`) that doesn't
  exist; the real classes sit in the default package instead. Leave broken unless asked
  to fix — it's tracked separately from the Twitter follower/tweet exercise.
- `pom.xml` `exec.mainClass` points at `temp.FreshUserDataApp`, so `mvn exec:java` won't
  run until that package issue is resolved; run `TwitterApp` directly instead
  (`javac`+`java` or add a second exec profile).

## Okta Round 2 practice mode (active)
Prep for Okta Round 2 (60-min live coding, medium–hard, Java) is tracked in
`docs/coding-guidance-handover.md` — read it before any "practice"/"mock"/"next problem" request.
It holds the practice state, category roadmap, mock structure, and a practice log at the end.
- **In practice mode the "solution first, then critique" rule below is suspended.** Present one
  unseen problem (statement, constraints, examples, Java signature), let the user explain their
  approach first, give hints not solutions. Reveal code only when they ask for it.
- Honor the requested mode: Practice / Guided reasoning / Implementation / Explanation / Mock.
  Never withhold code that was explicitly requested.
- Track assistance per attempt as independent / hint-assisted / guided / demonstrated and append
  it to the practice log in the handover file. Don't mark a category mastered on agreement alone.
- Next planned category: graph traversal and dependency ordering.

## When asked to "implement X" from an interview-style prompt
(Applies to implement-and-critique requests, not to practice mode above.)
1. Restate the problem in one line to confirm scope (no lengthy requirements-gathering).
2. Implement it as a new/updated class following the existing style (plain Java, no
   frameworks, `UUID` for IDs, constructor-injected collaborators).
3. Run it (compile + a quick `main`/manual check) before calling it done.
4. Critique per the rules above, written into the file as a header comment (or inline
   next to the specific method it concerns) — not just spoken in chat.
