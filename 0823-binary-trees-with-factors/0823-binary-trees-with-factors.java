import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

class Solution {
    public int numFactoredBinaryTrees(int[] arr) {
        int MOD = 1_000_000_007;
        Arrays.sort(arr);
        
        // Map: element value -> number of valid binary trees rooted at this element
        Map<Integer, Long> dp = new HashMap<>();
        
        for (int i = 0; i < arr.length; i++) {
            long count = 1L; // A single-node tree of value arr[i]
            
            for (int j = 0; j < i; j++) {
                // arr[j] is candidate for left child
                if (arr[i] % arr[j] == 0) {
                    int rightChild = arr[i] / arr[j];
                    
                    if (dp.containsKey(rightChild)) {
                        long leftTrees = dp.get(arr[j]);
                        long rightTrees = dp.get(rightChild);
                        
                        count = (count + (leftTrees * rightTrees)) % MOD;
                    }
                }
            }
            
            dp.put(arr[i], count);
        }
        
        long totalTrees = 0;
        for (long count : dp.values()) {
            totalTrees = (totalTrees + count) % MOD;
        }
        
        return (int) totalTrees;
    }
}