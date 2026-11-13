class Solution {
    public long countNonDecreasingSubarrays(int[] nums, int k) {
        int n = nums.length;
        long ans = 0;
        long currentCost = 0;
        int r = n - 1;
        Deque<Integer> dq = new ArrayDeque<>();
        for (int l = n - 1; l >= 0; l--) {
            // Absorbing  peaks smaller than or equal to nums[l]
            while (!dq.isEmpty() && nums[dq.peekFirst()] <= nums[l]) {
                int p = dq.pollFirst();
                int nxt = dq.isEmpty() ? r + 1 : dq.peekFirst();
                currentCost += (long) (nxt - p) * (nums[l] - nums[p]);
            }

            dq.offerFirst(l);

            // Shrink the  window from the right until currentCost <= k
            while (currentCost > k) {
                currentCost -= nums[dq.peekLast()] - nums[r];
                if (dq.peekLast() == r) {
                    dq.pollLast();
                }
                r--;
            }

            ans += (r - l + 1);
        }

        return ans;
    }
}