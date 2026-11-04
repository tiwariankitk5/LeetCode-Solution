import java.util.*;

class Solution {
    public long minOperations(int[] nums, int x, int k) {
        int n = nums.length;
        long[] minOps = getMinOps(nums, x);

        // memo[i][remK] stores the min cost starting at index i with remK subarrays remaining
        long[][] memo = new long[n + 1][k + 1];
        for (long[] row : memo) {
            Arrays.fill(row, -1);
        }

        return solve(0, k, nums, x, minOps, memo);
    }

    private long solve(int i, int remK, int[] nums, int x, long[] minOps, long[][] memo) {
        int n = nums.length;
        if (remK == 0) return 0;
        if (i > n || (long) (n - i) < (long) remK * x) return (long) 1e18; // Impossible state
        if (memo[i][remK] != -1) return memo[i][remK];

        // Option 1: Skip index i
        long res = solve(i + 1, remK, nums, x, minOps, memo);

        // Option 2: Pick a subarray of size x starting at i
        if (i + x <= n) {
            res = Math.min(res, minOps[i] + solve(i + x, remK - 1, nums, x, minOps, memo));
        }

        return memo[i][remK] = res;
    }

    private long[] getMinOps(int[] nums, int x) {
        int n = nums.length;
        long[] minOps = new long[n - x + 1];

        // TreeMap-based Multiset to track median accurately with balance maintainance
        TreeMap<Integer, Integer> lower = new TreeMap<>();
        TreeMap<Integer, Integer> upper = new TreeMap<>();
        int lowerSize = 0, upperSize = 0;
        long lowerSum = 0, upperSum = 0;

        for (int i = 0; i < n; i++) {
            // Insert element
            if (lower.isEmpty() || nums[i] <= lower.lastKey()) {
                lower.put(nums[i], lower.getOrDefault(nums[i], 0) + 1);
                lowerSize++;
                lowerSum += nums[i];
            } else {
                upper.put(nums[i], upper.getOrDefault(nums[i], 0) + 1);
                upperSize++;
                upperSum += nums[i];
            }

            // Balance size: lowerSize should be upperSize or upperSize + 1
            while (lowerSize < upperSize) {
                int val = upper.firstKey();
                removeMap(upper, val);
                upperSize--;
                upperSum -= val;

                lower.put(val, lower.getOrDefault(val, 0) + 1);
                lowerSize++;
                lowerSum += val;
            }
            while (lowerSize > upperSize + 1) {
                int val = lower.lastKey();
                removeMap(lower, val);
                lowerSize--;
                lowerSum -= val;

                upper.put(val, upper.getOrDefault(val, 0) + 1);
                upperSize++;
                upperSum += val;
            }

            // Remove outgoing element from window
            if (i >= x) {
                int outNum = nums[i - x];
                if (lower.containsKey(outNum)) {
                    removeMap(lower, outNum);
                    lowerSize--;
                    lowerSum -= outNum;
                } else {
                    removeMap(upper, outNum);
                    upperSize--;
                    upperSum -= outNum;
                }

                // Rebalance after removal
                while (lowerSize < upperSize) {
                    int val = upper.firstKey();
                    removeMap(upper, val);
                    upperSize--;
                    upperSum -= val;

                    lower.put(val, lower.getOrDefault(val, 0) + 1);
                    lowerSize++;
                    lowerSum += val;
                }
                while (lowerSize > upperSize + 1) {
                    int val = lower.lastKey();
                    removeMap(lower, val);
                    lowerSize--;
                    lowerSum -= val;

                    upper.put(val, upper.getOrDefault(val, 0) + 1);
                    upperSize++;
                    upperSum += val;
                }
            }

            // Record minimum operations for current window
            if (i >= x - 1) {
                long median = lower.lastKey();
                long ops = (median * lowerSize - lowerSum) + (upperSum - median * upperSize);
                minOps[i - x + 1] = ops;
            }
        }

        return minOps;
    }

    private void removeMap(TreeMap<Integer, Integer> map, int key) {
        int cnt = map.get(key);
        if (cnt == 1) {
            map.remove(key);
        } else {
            map.put(key, cnt - 1);
        }
    }
}