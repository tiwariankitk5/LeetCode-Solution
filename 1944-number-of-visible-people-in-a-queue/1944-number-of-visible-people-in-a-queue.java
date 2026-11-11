import java.util.ArrayDeque;
import java.util.Deque;

class Solution {
    public int[] canSeePersonsCount(int[] heights) {
        int n = heights.length;
        int[] ans = new int[n];
        Deque<Integer> stack = new ArrayDeque<>();

        for (int i = n - 1; i >= 0; i--) {
          
            while (!stack.isEmpty() && stack.peek() < heights[i]) {
                ans[i]++;
                stack.pop();
            }

            if (!stack.isEmpty()) {
                ans[i]++;
            }

            stack.push(heights[i]);
        }

        return ans;
    }
}