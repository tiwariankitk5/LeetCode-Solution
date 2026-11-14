class Solution {
    public int minimumSumSubarray(List<Integer> nums, int l, int r) {
        int n = nums.size();
        int[] prefix = new int[n + 1];
        
        // Build prefix sum array
        for (int i = 0; i < n; i++) {
            prefix[i + 1] = prefix[i] + nums.get(i);
        }
        
        int minSum = Integer.MAX_VALUE;
        
        // Iterate over all valid subarray lengths from l to r
        for (int len = l; len <= r; len++) {
            // Slide a window of size 'len' across the array
            for (int i = 0; i + len <= n; i++) {
                int currentSum = prefix[i + len] - prefix[i];
                if (currentSum > 0) {
                    minSum = Math.min(minSum, currentSum);
                }
            }
        }
        
        return minSum == Integer.MAX_VALUE ? -1 : minSum;
    }
}