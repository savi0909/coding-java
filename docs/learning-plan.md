# Learning Plan — Principal Engineer Backend Interview Prep

Living document. Study order groups problems by dependency — each phase leans on a
pattern introduced in the phase before it. Update this file whenever new problems are
added or the order changes; don't let it go stale.

Status legend: ✅ solved+verified in repo · 📥 imported from `D:\aa-coding-prep-java` (compiles,
not yet reviewed/critiqued here) · 🔲 not yet added

> **Caveat:** ✅ means a reference solution exists and was verified in this repo — it does **not**
> mean you solved it independently (see `coding-guidance-handover.md` §5). Count a category as
> done only after a blind re-solve with no help; the 📥 "twin" drills below are for that.

## Topic map (where code lives)
All paths under `src/main/java/`. `dsa.*` = algorithm drills, `lld.*` = low-level design.

| Topic | Packages |
|---|---|
| Hashing | `dsa.hashing`, `interviews.Prices` |
| Sliding window | `arrays.ref` (A/B sets), `arrays.sliding`, `dsa.slidingwindow` |
| Two pointers / prefix | `arrays.ref` (C set), `dsa.twopointers` |
| Array basics / binary search | `dsa.arrays`, `dsa.binarysearch` |
| Linked list / stack | `dsa.linkedlist`, `dsa.stack`, `dsa.datastructures` |
| Heaps | `arrays.ref` (D set), `dsa.heap` |
| Trees | `dsa.tree` (+ `.mindepth`, `.rightview`) |
| Graphs | `dsa.graph` |
| Backtracking | `dsa.backtracking` (+ `.queens`, `.sudoku`, `.maze`, `.knighttour`, `.graphcoloring`, `.hamiltonian`) |
| DP | `dsa.dp`, `arrays.ref.LongestIncreasingSubsequence` |
| LLD | `lld.cache` (+ `.lfu`, `.listbased`), `tweet`, `bank`, `access` |

## Phase 0 — Array basics (optional warm-up, ~1 session)
- 📥 `dsa.arrays` — `AlternatingSums`, `AddBorder`, `AllLongestStrings`, `AlmostIncreasingSequence`,
  `PopularityAnalysis` (monotonic check), `FindMaxUsingDivideAndConquer`, `CountSubarrays`
  (left/right run-length arrays — preview of Phase 4's prefix/suffix pass).
- 📥 `dsa.binarysearch.BinarySearch` — classic + boundary variants. Do before heaps; the
  `lo <= hi` vs `lo < hi` invariant comes up again in "search answer space" problems.

## Phase 1 — Warm-up / hashmap keying
1. ✅ `GroupAnagrams.java` — simplest hashmap-signature problem, no window concept yet.
2. ✅ `LongestSubstringWithoutRepeating.java` — first sliding window (shrinks on a
   "no duplicates" invariant). Bridges into Phase 2/3.
   - ✅ `interviews.Prices#solution` — first non-repeating character; `LinkedHashMap` for
     insertion order + a `-1` sentinel to mark "seen twice". Same hashmap-keying muscle as
     GroupAnagrams, no window yet.
- Drills: 📥 `dsa.hashing` — `FirstDuplicateInArray`, `FirstNotRepeatingCharacterInString`
  (compare with `Prices#solution`), `GroupSimilarTitles` (= GroupAnagrams, Netflix framing),
  `PossiblesumsGoogle` (set-of-reachable-sums; really a bounded-knapsack DP — revisit in Phase 10).

## Phase 2 — Fixed window
3. ✅ `arrays.ref.MaxSumSubarraySizeK.java` (A1) — simplest window: slide, no shrink logic.
4. ✅ `arrays.ref.FirstNegativeInEachWindow.java` (A3) — same fixed window + a monotonic deque.
5. ✅ `arrays.ref.FindAnagramIndices.java` (A2) — match-count trick (`satisfied` counter), reused in B7.
6. ✅ `arrays.sliding.MaxSum.java` — A1 re-solved from scratch, also returns the window's start index.
7. ✅ `arrays.sliding.MaxIndexPerWindow.java` — sliding-window maximum returning indices; shows why
   the reversible-sum trick fails for max and a monotonic deque is required (same deque as A3).
8. ✅ `arrays.sliding.MaxInEveryWindow.java` — same window, returns max values. Carries the O(n*k)
   brute force as a differential-test oracle, plus a deliberately-bad ArrayList deque + benchmark
   showing why `remove(0)` (O(size) shift) makes ArrayDeque non-negotiable here.
- Drill: 📥 `dsa.slidingwindow.MaxSumSubArrayOfSizeK` — Grokking version of A1; diff against yours.

## Phase 3 — Variable window
6. ✅ `arrays.ref.MinSubArrayLen.java` (B1) — canonical shrink-while-valid loop.
7. ✅ `arrays.ref.MaxConsecutiveOnesIII.java` (B4) — simplest "budget" variant of the same loop.
8. ✅ `arrays.ref.LongestKDistinctSubstring.java` (B2) — frequency-map version of the invariant.
9. ✅ `arrays.ref.FruitIntoBaskets.java` (B3) — B2 with k=2; confirms pattern recognition under a new name.
10. ✅ `arrays.ref.LongestRepeatingCharReplacement.java` (B5) — hardest single-invariant window
    (stale-max correctness trick).
11. ✅ `arrays.ref.SubarrayProductLessThanK.java` (B6) — same shrink loop, product instead of sum.
12. ✅ `arrays.ref.MinimumWindowSubstring.java` (B7) — closer: A2's match-counting + B1's shrink loop.
- Drills (📥 `dsa.slidingwindow`, Grokking twins of the above — re-solve blind, then diff):
  `MinSizeSubArraySum`↔B1, `ReplacingOnes`↔B4, `LongestSubstringKDistinct`↔B2,
  `CharacterReplacement`↔B5, `NoRepeatSubstring`↔Phase 1 #2,
  `FindLongestSubarraySums` (CodeSignal longest/shortest subarray by sum, positives only).

## Phase 4 — Two-pointer & prefix
13. ✅ `arrays.ref.SubarraySumEqualsK.java` (C4) — do right after B7: looks like a window problem but
    isn't (negatives break monotonicity) — contrast lands best while windows are fresh.
14. ✅ `arrays.ref.TwoSumSorted.java` (C1) — simplest two-pointer.
15. ✅ `arrays.ref.ContainerWithMostWater.java` (C2) — two-pointer + a greedy argument to justify out loud.
16. ✅ `arrays.ref.TrapRainWater.java` (C3) — harder two-pointer, same greedy family.
17. ✅ `arrays.ref.ProductExceptSelf.java` (C5) — prefix/suffix pass, standalone technique.
18. ✅ `arrays.ref.MaxDistinctWindowSum.java` (C6) — capstone: A1's fixed window + B2/B3's distinctness tracking.
    - ✅ `interviews.Prices#solutionPrices` — best time to buy/sell stock (LC 121). Running-minimum
      single pass; sits next to C5 as the other "carry one extremum forward" problem. Its delta-array
      framing *is* Kadane, so treat it as the on-ramp to the Phase 10 DP set (LC 122 → sum of positive
      deltas; LC 123/188 → k-transaction DP). `solutionPricesWithDays` covers the "which days?" follow-up.
- Drills (📥 `dsa.twopointers`), in order:
  `PairWithTargetSum`↔C1 then `PairWithTargetSumUsingMap` (unsorted → hashmap tradeoff),
  `RemoveDuplicates`, `RemoveElement`, `MoveZerostoLeft` (read/write-pointer compaction family),
  `SortedArraySquares` (converge from both ends), `TripletSumToZero` → `TripletSumCloseToTarget`
  (sort + fix one + two-pointer), `Sort0s1s` → `DutchFlag` (3-way partition; same as quicksort's).

## Phase 5 — Heaps / priority queues
19. ✅ `arrays.ref.KthLargestElement.java` (D1) — foundational bounded min-heap.
20. ✅ `arrays.ref.KthLargestInStream.java` (D2) — same heap, now stateful/online.
21. ✅ `arrays.ref.TopKFrequentElements.java` (D3) — heap keyed on map entries.
22. ✅ `arrays.ref.MeetingRoomsII.java` (D5) — heap + greedy interval scheduling.
23. ✅ `arrays.ref.MergeKSortedLists.java` (D4) — k-way merge, comparator on objects instead of primitives.
24. ✅ `arrays.ref.MedianFinder.java` (D6) — hardest: two synchronized heaps, saved for last.
- Drills (📥 `dsa.heap`): `KLargestNumbers`↔D1, `KthLargestNumberInStream`↔D2,
  `TopKFrequentNumbers`↔D3, `FrequencySort` (D3 + rebuild string), `KClosestElements`
  (heap on |x−X|; binary-search + two-pointer alternative is O(log n + k)),
  `MedianOfAStream` / `MedianOfAges`↔D6 (same two-heap code twice — pick one, delete the other later).

## Phase 6 — Linked lists & stacks (📥 new)
Do before revisiting D4 — `MergeKSortedLists` here is the node-based version.
25. 📥 `dsa.linkedlist.LinkedListNode` — shared node + `LinkedList` helpers (insert/print). Read first.
26. 📥 `dsa.linkedlist.RemoveNodesWothValFromLinkedList` — dummy-head trick.
27. 📥 `dsa.linkedlist.LinkedListPalindrome` — fast/slow pointer + reverse second half.
28. 📥 `dsa.linkedlist.MergeSortList` — split at middle + merge; O(n log n), O(log n) stack.
29. 📥 `dsa.linkedlist.MergeKSortedLists` — heap of list heads (compare with `arrays.ref` D4).
30. 📥 `dsa.stack.Stack` → `MinStack` → `maxStack` (+ `MaxStackDemo` driver) — auxiliary-stack
    invariant: top of aux = min/max of everything below.
31. 📥 `dsa.datastructures.Bag` — Sedgewick resizing-array bag + `Iterable`; amortized O(1) add.

## Phase 7 — Trees (📥 new)
BFS first (queue + level-size loop), then DFS.
32. 📥 `dsa.tree.InOrderTraversal` — iterative inorder with explicit stack.
33. 📥 `dsa.tree.LevelOrderTraversal` — the level-size BFS template everything below reuses.
34. 📥 `dsa.tree.AverageOfLevel`, `dsa.tree.ZigZagLevelOrderTraversal`,
    `dsa.tree.rightview.Solution`, `dsa.tree.mindepth.Solution` (early exit on first leaf).
35. 📥 `dsa.tree.CountAllPathSum` — DFS with path list; count sub-paths summing to S
    (prefix-sum-on-path is the O(n) upgrade — same idea as C4).

## Phase 8 — Graphs (📥 new)
36. 📥 `dsa.graph.FriendCircleCountInMatrixUsingDFS` — connected components on adjacency matrix
    (union-find is the alternative worth naming).
37. 📥 `dsa.graph.CloneGraph` + `dsa.graph.Node` (holds `Solution#clone`) — DFS with
    old→new map to handle cycles.

## Phase 9 — Backtracking (📥 new)
Choose → explore → un-choose. Easiest to hardest:
38. 📥 `dsa.backtracking.PermutationsOfLetters` — the template (used[] + path copy).
39. 📥 `dsa.backtracking.PhoneNumbertoLetter` / `PhoneNumbertoLetterEdu` — product of choices.
40. 📥 `dsa.backtracking.queens` (N-Queens), `.sudoku`, `.maze` — constraint check before recurse.
    Note: `queens.App` runs N=100 and does not finish in reasonable time — lower N to ~8–20.
41. 📥 `dsa.backtracking.knighttour`, `.graphcoloring`, `.hamiltonian` — graph-flavored backtracking.

## Phase 10 — Dynamic programming (📥 new)
42. 📥 `dsa.dp.ClimbingStairs` — Fibonacci recurrence. Currently O(n) array and **throws
    AIOOBE for n=1**; fix + reduce to O(1) rolling vars as the exercise.
43. 📥 `dsa.dp.HouseRobber` / `HouseThief` — take/skip recurrence; same problem twice.
44. 📥 `dsa.dp.DecodeNumbersToLetters` — top-down memo (decode ways).
45. 📥 `dsa.dp.RodCutter` — unbounded knapsack shape, top-down memo. **`main` NPEs on line 15**
    (leftover `Node` scratch code, pre-existing in source) — delete those lines to run it.
46. ✅ `arrays.ref.LongestIncreasingSubsequence` — O(n²) DP vs patience-sort O(n log n).
47. 📥 `dsa.dp.DpScratchpad` — scratch file (rod-cut variant, 2D `f(n,k)` table, a productExceptSelf
    attempt). Mine for practice, don't treat as reference.

## Phase 11 — LLD: caches (📥 new)
48. 📥 `lld.cache.listbased.LRUCache` — naive: `java.util.LinkedList` remove is O(n). Start here to
    articulate *why* it's wrong.
49. 📥 `lld.cache.LRUCacheBasic` — HashMap + hand-rolled doubly linked list, O(1) get/put.
50. 📥 `lld.cache.LRUCache` — generic, thread-safe version (`ConcurrentHashMap`) — critique its
    locking granularity.
51. 📥 `lld.cache.lfu.LFUCache` — frequency buckets; O(1) LFU needs freq→list map + minFreq.
- Then `tweet.TwitterApp` (fan-out read vs write), `bank`, `access`.

## Backlog (not yet added)
- 🔲 Graphs: course schedule / topological sort, number of islands, Dijkstra, union-find.
- 🔲 Trees: BST validation, LCA, serialize/deserialize.
- 🔲 DP: 0/1 knapsack, edit distance, LCS.
- 🔲 Review pass: critique each 📥 file per CLAUDE.md rules and flip it to ✅.

## Okta Round 2 practice track (parallel to the phases above)
State, roadmap, and attempt log live in `coding-guidance-handover.md` (same folder). Problems are
presented one at a time and solved by the user; each gets a scaffold (statement, stub, verified
test scenarios) in the matching `dsa.<topic>` package and flips to ✅ here once solved + critiqued.

**Order for this track is priority-driven (handover §7), not the phase order above** — the
phases are a dependency map for the whole repo; Okta Round 2 needs the medium tier of the
Revisit/Next categories first.

Done at easy level (self-reported 2026-10-10, assistance not recorded): longest substring
without repeating characters; merge overlapping intervals. Intervals had no entry in this plan
before — treat Merge Intervals as the on-ramp to the intervals rungs below.

Medium ladder (tentative — adjust from observed results; one problem active at a time):

| # | Category | Medium problem | Status |
|---|---|---|---|
| 1 | Sliding window (extremum invariant) | Longest subarray with bounded spread (max − min ≤ limit) | 🔲 `dsa.slidingwindow.LongestBoundedSpreadSubarray` — active |
| 2 | Intervals | Insert interval, or min removals to make non-overlapping | not presented |
| 3 | Hash aggregation + bounded heap | Top-K with tie-break comparator | not presented |
| 4 | Graph / dependency ordering | Min rounds to finish tasks (cycle → -1) | 🔲 `dsa.graph.MinRoundsToFinishTasks` — scaffolded, queued |
| 5 | Graph traversal | Grid components (islands) or shortest steps in a grid | not presented |
| 6 | Composite data structures | O(1) insert/delete/getRandom, or LRU variant | not presented |
| 7 | Binary search on answer | Capacity / rate feasibility search | not presented |

Later (only after the rungs above are independent): stacks/queues, trees, prefix sums, greedy,
basic DP, backtracking. Repo gaps this track exposes: no intervals set, thin graph set (2 files),
binary search is classic-only, and composite structures sit late (Phase 11) despite being "Next".

## Source material
- `docs/coding-guidance-handover.md` — Okta Round 2 prep handover (copied from Downloads 2026-10-10).
- `C:\Users\abhis\Downloads\array-window-practice.md` — origin of the A/B/C problem set.
- Heap/PQ problems (D1-D6) were proposed, not sourced from a file.
- `D:\aa-coding-prep-java` (git repo, `com.abhi.*`) — merged 2026-10-10 into `dsa.*` / `lld.*`.
  Source left untouched. Changes made during merge: package lines rewritten; renames
  `dsa/arrays/Main`→`CountSubarrays`, `dsa/Main`→`DpScratchpad` (slf4j logger → `System.out`),
  `netflix/Solution`→`MaxStackDemo`, `tree/dfs/TreeNode.java`→`CountAllPathSum.java`;
  `LinkedListNode` made `public` so `lld.cache.listbased.LRUCache` can import it.
