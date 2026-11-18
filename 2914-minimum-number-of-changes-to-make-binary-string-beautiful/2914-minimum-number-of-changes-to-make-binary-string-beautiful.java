class Solution {
    public int minChanges(String s) {
        int changes = 0;
        
        // Iterate through the string in steps of 2 (checking adjacent pairs)
        for (int i = 0; i < s.length(); i += 2) {
            if (s.charAt(i) != s.charAt(i + 1)) {
                changes++;
            }
        }
        
        return changes;
    }
}