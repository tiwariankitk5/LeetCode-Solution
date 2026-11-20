
class Solution {
    public List<List<String>> findLadders(String beginWord, String endWord, List<String> wordList) {
        List<List<String>> results = new ArrayList<>();
        Set<String> dict = new HashSet<>(wordList);
        if (!dict.contains(endWord)) {
            return results;
        }

        // Maps each word to all words that can reach it on a shortest path
        Map<String, List<String>> parents = new HashMap<>();
        Set<String> currentLevel = new HashSet<>();
        currentLevel.add(beginWord);
        dict.remove(beginWord);

        boolean found = false;

        // 1. Level-order BFS to construct the shortest-path DAG
        while (!currentLevel.isEmpty() && !found) {
            // Remove all words at the current level from the dictionary to prevent cycles
            // while still allowing multiple parents from the SAME level to reach the same child
            dict.removeAll(currentLevel);
            Set<String> nextLevel = new HashSet<>();

            for (String word : currentLevel) {
                char[] chars = word.toCharArray();
                for (int i = 0; i < chars.length; i++) {
                    char originalChar = chars[i];
                    for (char c = 'a'; c <= 'z'; c++) {
                        if (c == originalChar) continue;
                        chars[i] = c;
                        String nextWord = new String(chars);

                        if (dict.contains(nextWord)) {
                            nextLevel.add(nextWord);
                            parents.computeIfAbsent(nextWord, k -> new ArrayList<>()).add(word);
                            if (nextWord.equals(endWord)) {
                                found = true;
                            }
                        }
                    }
                    chars[i] = originalChar;
                }
            }
            currentLevel = nextLevel;
        }

        // 2. Backtrack from endWord to beginWord to collect all shortest paths
        if (found) {
            LinkedList<String> path = new LinkedList<>();
            path.add(endWord);
            backtrack(endWord, beginWord, parents, path, results);
        }

        return results;
    }

    private void backtrack(String current, String beginWord, Map<String, List<String>> parents,
                           LinkedList<String> path, List<List<String>> results) {
        if (current.equals(beginWord)) {
            results.add(new ArrayList<>(path));
            return;
        }

        if (!parents.containsKey(current)) return;

        for (String parent : parents.get(current)) {
            path.addFirst(parent);
            backtrack(parent, beginWord, parents, path, results);
            path.removeFirst();
        }
    }
}