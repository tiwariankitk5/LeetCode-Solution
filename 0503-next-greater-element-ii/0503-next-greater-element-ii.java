class Solution {
    public int[] nextGreaterElements(int[] nums) {
        int n = nums.length;
        int[] result = new int[n];
        Arrays.fill(result, -1);
        
        // Stack stores indices of elements waiting for their next greater element
        Deque<Integer> stack = new ArrayDeque<>();
        
        // Loop twice through the array to simulate circular traversal
        for (int i = 0; i < 2 * n; i++) {
            int currentVal = nums[i % n];
            
            // Resolve all previous smaller elements on the stack
            while (!stack.isEmpty() && nums[stack.peek()] < currentVal) {
                result[stack.pop()] = currentVal;
            }
            
            // Only push indices during the first pass
            if (i < n) {
                stack.push(i);
            }
        }
        
        return result;
    }
}