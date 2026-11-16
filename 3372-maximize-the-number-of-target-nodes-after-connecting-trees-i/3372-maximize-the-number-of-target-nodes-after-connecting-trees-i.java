class Solution {
    public int[] maxTargetNodes(int[][] edges1, int[][] edges2, int k) {
        int n = edges1.length + 1;
        int m = edges2.length + 1;

        List<List<Integer>> adj1 = buildGraph(n, edges1);
        List<List<Integer>> adj2 = buildGraph(m, edges2);

        // Find the best node in Tree2 to connect to (maximizing nodes within distance k - 1)
        int maxTree2 = 0;
        if (k > 0) {
            for (int v = 0; v < m; v++) {
                maxTree2 = Math.max(maxTree2, countNodesWithinDistance(v, -1, k - 1, adj2));
            }
        }

        // For each node in Tree 1, count nodes within distance k and add maxTree2
        int[] answer = new int[n];
        for (int u = 0; u < n; u++) {
            answer[u] = countNodesWithinDistance(u, -1, k, adj1) + maxTree2;
        }

        return answer;
    }

    // Simple DFS to count nodes at distance <= maxDist from curr
    private int countNodesWithinDistance(int curr, int parent, int maxDist, List<List<Integer>> adj) {
        if (maxDist < 0) {
            return 0;
        }
        int count = 1; // Including the  current node
        for (int neighbor : adj.get(curr)) {
            if (neighbor != parent) {
                count += countNodesWithinDistance(neighbor, curr, maxDist - 1, adj);
            }
        }
        return count;
    }

    private List<List<Integer>> buildGraph(int size, int[][] edges) {
        List<List<Integer>> adj = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            adj.add(new ArrayList<>());
        }
        for (int[] edge : edges) {
            adj.get(edge[0]).add(edge[1]);
            adj.get(edge[1]).add(edge[0]);
        }
        return adj;
    }
}