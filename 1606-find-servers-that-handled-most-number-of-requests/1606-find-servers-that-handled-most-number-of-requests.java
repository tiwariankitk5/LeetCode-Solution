import java.util.*;

class Solution {
    public List<Integer> busiestServers(int k, int[] arrival, int[] load) {
        TreeSet<Integer> freeServers = new TreeSet<>();
        for (int i = 0; i < k; i++) {
            freeServers.add(i);
        }

        // Min-heap storing int[]{end_time, server_id} ka value 
        PriorityQueue<int[]> busyHeap = new PriorityQueue<>((a, b) -> Integer.compare(a[0], b[0]));
        int[] requestCounts = new int[k];

        for (int i = 0; i < arrival.length; i++) {
            int start = arrival[i];
            int end = start + load[i];

            // 1. Release free servers -important hai
            while (!busyHeap.isEmpty() && busyHeap.peek()[0] <= start) {
                freeServers.add(busyHeap.poll()[1]);
            }

            // 2. Drop request if all servers busy - this is for emptying 
            if (freeServers.isEmpty()) continue;

            // 3. Find server >= (i % k) or wrap around
            Integer server = freeServers.ceiling(i % k);
            if (server == null) {
                server = freeServers.first();
            }

            // 4. Assign request
            requestCounts[server]++;
            freeServers.remove(server);
            busyHeap.offer(new int[]{end, server});
        }

        int maxReqs = 0;
        for (int count : requestCounts) {
            maxReqs = Math.max(maxReqs, count);
        }

        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < k; i++) {
            if (requestCounts[i] == maxReqs) {
                result.add(i);
            }
        }
        return result;
    }
}