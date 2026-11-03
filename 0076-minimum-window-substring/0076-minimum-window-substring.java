import java.util.HashMap;
import java.util.Map;

class Solution {
    public String minWindow(String s, String t) {
        if (s.length() < t.length()) return "";

        Map<Character, Integer> targetCount = new HashMap<>();
        for (char c : t.toCharArray()) {
            targetCount.put(c, targetCount.getOrDefault(c, 0) + 1);
        }

        int required = targetCount.size();
        int formed = 0;

        Map<Character, Integer> windowCount = new HashMap<>();
        int left = 0, right = 0;
        
        // ans[0] = window length, ans[1] = left, ans[2] = right
        int[] ans = {-1, 0, 0};

        while (right < s.length()) {
            char c = s.charAt(right);
            windowCount.put(c, windowCount.getOrDefault(c, 0) + 1);

            if (targetCount.containsKey(c) && 
                windowCount.get(c).intValue() == targetCount.get(c).intValue()) {
                formed++;
            }

            // Shrink window from the left while it is valid
            while (left <= right && formed == required) {
                c = s.charAt(left);

                // Update min window result
                if (ans[0] == -1 || right - left + 1 < ans[0]) {
                    ans[0] = right - left + 1;
                    ans[1] = left;
                    ans[2] = right;
                }

                windowCount.put(c, windowCount.get(c) - 1);
                if (targetCount.containsKey(c) && 
                    windowCount.get(c) < targetCount.get(c)) {
                    formed--;
                }

                left++;
            }

            right++;
        }

        return ans[0] == -1 ? "" : s.substring(ans[1], ans[2] + 1);
    }
}