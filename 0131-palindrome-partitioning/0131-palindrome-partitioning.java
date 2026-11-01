import java.util.ArrayList;
import java.util.List;

class Solution {
    public List<List<String>> partition(String s) {
        List<List<String>> result = new ArrayList<>();
        List<String> currentPath = new ArrayList<>();
        backtrack(0, s, currentPath, result);
        return result;
    }

    private void backtrack(int start, String s, List<String> currentPath, List<List<String>> result) {
        if (start == s.length()) {
            result.add(new ArrayList<>(currentPath));
            return;
        }

        for (int end = start + 1; end <= s.length(); end++) {
            if (isPalindrome(s, start, end - 1)) {
                currentPath.add(s.substring(start, end));
                backtrack(end, s, currentPath, result);
                currentPath.remove(currentPath.size() - 1);
            }
        }
    }

    private boolean isPalindrome(String s, int left, int right) {
        while (left < right) {
            if (s.charAt(left++) != s.charAt(right--)) {
                return false;
            }
        }
        return true;
    }
}