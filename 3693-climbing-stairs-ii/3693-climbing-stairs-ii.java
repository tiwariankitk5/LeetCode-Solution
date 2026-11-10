class Solution {
    public int climbStairs(int n, int[] costs) {
        int[] dp = new int[n + 1];
        
        for (int i = 1; i <= n; i++) {
            int minCost = Integer.MAX_VALUE;
            
            // Checking  jumps of length 1, 2, and 3
            for (int k = 1; k <= 3; k++) {
                if (i - k >= 0) {
                    int jumpCost = k * k;
                    int stepCost = costs[i - 1]; 
                    minCost = Math.min(minCost, dp[i - k] + stepCost + jumpCost);
                }
            }
            
            dp[i] = minCost;
        }
        
        return dp[n];
    }
}