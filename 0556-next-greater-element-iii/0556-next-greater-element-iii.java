class Solution {
    public int nextGreaterElement(int n) {
        char[] digits = String.valueOf(n).toCharArray();
        int len = digits.length;
        
        // 1. Find the first decreasing digit from the right (pivot)
        int i = len - 2;
        while (i >= 0 && digits[i] >= digits[i + 1]) {
            i--;
        }
        
        // If digits are in descending order, no greater permutation exists
        if (i < 0) {
            return -1;
        }
        
        // 2. Find the smallest digit to the right of i that is strictly greater than digits[i]
        int j = len - 1;
        while (digits[j] <= digits[i]) {
            j--;
        }
        
        // 3. Swap pivot with that successor
        swap(digits, i, j);
        
        // 4. Reverse the suffix right after index i to make it as small as possible
        reverse(digits, i + 1, len - 1);
        
        // 5. Parse as long to prevent 32-bit signed integer overflow
        long result = Long.parseLong(new String(digits));
        return (result > Integer.MAX_VALUE) ? -1 : (int) result;
    }
    
    private void swap(char[] arr, int i, int j) {
        char temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
    
    private void reverse(char[] arr, int start, int end) {
        while (start < end) {
            swap(arr, start++, end--);
        }
    }
}