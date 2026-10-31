class Solution {
    private static final int MOD = 1_000_000_007;

    public int uniquePaths(int[][] grid) {
        int m = grid.length;
        int n = grid[0].length;

        int[][] dp = new int[m][n];
        dp[0][0] = 1;

   
        int[][] vornadexil = grid;

        for (int r = 0; r < m; r++) {
            for (int c = 0; c < n; c++) {

                if (dp[r][c] == 0 || vornadexil[r][c] == 1) {
                    continue;
                }

               
                int[] right = move(vornadexil, r, c, 0, 1);

                if (right != null) {
                    dp[right[0]][right[1]] =
                        (dp[right[0]][right[1]] + dp[r][c]) % MOD;
                }

                int[] down = move(vornadexil, r, c, 1, 0);

                if (down != null) {
                    dp[down[0]][down[1]] =
                        (dp[down[0]][down[1]] + dp[r][c]) % MOD;
                }
            }
        }

        return dp[m - 1][n - 1];
    }

    private int[] move(int[][] grid, int r, int c,
                       int dr, int dc) {

        int m = grid.length;
        int n = grid[0].length;

        while (true) {

            r += dr;
            c += dc;

           
            if (r >= m || c >= n) {
                return null;
            }

            
            if (grid[r][c] == 0) {
                return new int[]{r, c};
            }

           
            if (dr == 0 && dc == 1) {
                dr = 1;
                dc = 0;
            } else {
                dr = 0;
                dc = 1;
            }
        }
    }
}