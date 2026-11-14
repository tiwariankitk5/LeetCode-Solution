import java.util.*;

class Solution {
    public int minimumThreshold(int n, int[][] edges, int source, int destination, int k) {
        if (source == destination) {
            return 0;
        }

        // Build adjacency list
        List<int[]>[] adj = new ArrayList[n];
        for (int i = 0; i < n; i++) {
            adj[i] = new ArrayList<>();
        }
        
        int maxWeight = 0;
        for (int[] edge : edges) {
            int u = edge[0], v = edge[1], w = edge[2];
            adj[u].add(new int[]{v, w});
            adj[v].add(new int[]{u, w}); // Remove this line if the graph is directed
            maxWeight = Math.max(maxWeight, w);
        }

        int low = 0;
        int high = maxWeight;
        int ans = -1;

        // Binary search for the minimum valid threshold
        while (low <= high) {
            int mid = low + (high - low) / 2;
            if (canReach(n, adj, source, destination, k, mid)) {
                ans = mid;
                high = mid - 1;
            } else {
                low = mid + 1;
            }
        }

        return ans;
    }

    // 0-1 BFS to check if destination is reachable from source using <= k heavy edges
    private boolean canReach(int n, List<int[]>[] adj, int source, int destination, int k, int threshold) {
        int[] heavyCount = new int[n];
        Arrays.fill(heavyCount, Integer.MAX_VALUE);
        heavyCount[source] = 0;

        Deque<Integer> deque = new ArrayDeque<>();
        deque.offerFirst(source);

        while (!deque.isEmpty()) {
            int u = deque.pollFirst();

            if (u == destination) {
                return heavyCount[u] <= k;
            }

            for (int[] neighbor : adj[u]) {
                int v = neighbor[0];
                int weight = neighbor[1];
                int cost = (weight > threshold) ? 1 : 0;

                if (heavyCount[u] + cost < heavyCount[v]) {
                    heavyCount[v] = heavyCount[u] + cost;
                    
                    if (v == destination && heavyCount[v] <= k) {
                        return true;
                    }

                    if (cost == 0) {
                        deque.offerFirst(v);
                    } else {
                        deque.offerLast(v);
                    }
                }
            }
        }

        return heavyCount[destination] <= k;
    }
}