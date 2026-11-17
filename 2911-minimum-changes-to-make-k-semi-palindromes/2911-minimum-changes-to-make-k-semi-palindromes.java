class Solution {
    public int minimumChanges(String s, int k) {
        int n = s.length();
        int INF = 1_000_000_000;
        char[] chars = s.toCharArray();

        // Precompute proper divisors for every possible substring length
        List<Integer>[] divisors = new ArrayList[n + 1];
        for (int len = 2; len <= n; len++) {
            divisors[len] = new ArrayList<>();
            for (int d = 1; d < len; d++) {
                if (len % d == 0) {
                    divisors[len].add(d);
                }
            }
        }

        // cost[i][j] = min changes to make s[i..j] a semi-palindrome
        int[][] cost = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                cost[i][j] = getSemiPalindromeCost(chars, i, j, divisors[j - i + 1], INF);
            }
        }

        // dp[p][i] = min changes to partition s[0..i-1] into p semi-palindromes
        int[][] dp = new int[k + 1][n + 1];
        for (int[] row : dp) {
            Arrays.fill(row, INF);
        }

        for (int i = 2; i <= n; i++) {
            dp[1][i] = cost[0][i - 1];
        }

        for (int p = 2; p <= k; p++) {
            // Each of the p parts requires at least 2 characters
            for (int i = 2 * p; i <= n; i++) {
                for (int m = 2 * (p - 1); m <= i - 2; m++) {
                    if (dp[p - 1][m] != INF) {
                        dp[p][i] = Math.min(dp[p][i], dp[p - 1][m] + cost[m][i - 1]);
                    }
                }
            }
        }

        return dp[k][n];
    }

    private int getSemiPalindromeCost(char[] chars, int l, int r, List<Integer> divs, int INF) {
        int minChanges = INF;

        for (int d : divs) {
            int changes = 0;
            // Check each residue group mod d
            for (int rem = 0; rem < d; rem++) {
                int left = l + rem;
                int right = r - (d - 1 - rem);
                while (left < right) {
                    if (chars[left] != chars[right]) {
                        changes++;
                    }
                    left += d;
                    right -= d;
                }
            }
            if (changes < minChanges) {
                minChanges = changes;
            }
        }

        return minChanges;
    }
}