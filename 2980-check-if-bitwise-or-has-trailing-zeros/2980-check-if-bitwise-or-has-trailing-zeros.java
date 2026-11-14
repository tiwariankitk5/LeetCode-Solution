class Solution {
    public boolean hasTrailingZeros(int[] nums) {
        int evenCount = 0;
        
        for (int num : nums) {
            // Check if the least significant bit is 0 (i.e., num is even)
            if ((num & 1) == 0) {
                evenCount++;
                // Early exit as soon as we find two even numbers
                if (evenCount == 2) {
                    return true;
                }
            }
        }
        
        return false;
    }
}