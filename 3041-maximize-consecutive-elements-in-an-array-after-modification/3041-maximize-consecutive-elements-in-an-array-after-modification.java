class Solution {
    public int maxSelectedElements(int[] nums) {
        Arrays.sort(nums);
        
        // dp.get(x) stores the max length of a consecutive sequence ending at x
        Map<Integer, Integer> dp = new HashMap<>();
        int maxLen = 0;
        
        for (int num : nums) {
            int lenPlusOne = dp.getOrDefault(num, 0) + 1;
            int lenCurrent = dp.getOrDefault(num - 1, 0) + 1;
            
            dp.put(num + 1, lenPlusOne);
            dp.put(num, lenCurrent);
            
            maxLen = Math.max(maxLen, Math.max(lenCurrent, lenPlusOne));
        }
        
        return maxLen;
    }
}