class Solution {
    public String digitSum(String s, int k) {
        while (s.length() > k) {
            StringBuilder nextRound = new StringBuilder();
            
            // Process the string in groups of size k
            for (int i = 0; i < s.length(); i += k) {
                int sum = 0;
                // Sum digits in the current group, stopping at i + k or end of string
                for (int j = i; j < Math.min(i + k, s.length()); j++) {
                    sum += s.charAt(j) - '0';
                }
                nextRound.append(sum);
            }
            
            s = nextRound.toString();
        }
        
        return s;
    }
}