import java.util.Arrays;

class Solution {
    public int[] countServers(int n, int[][] logs, int x, int[] queries) {
        int qLen = queries.length;
        
        // Store queries along with their original indices: [time, original_index]
        int[][] sortedQueries = new int[qLen][2];
        for (int i = 0; i < qLen; i++) {
            sortedQueries[i][0] = queries[i];
            sortedQueries[i][1] = i;
        }
        
        // Sort queries by timestamp
        Arrays.sort(sortedQueries, (a, b) -> Integer.compare(a[0], b[0]));
        
        // Sort logs by timestamp
        Arrays.sort(logs, (a, b) -> Integer.compare(a[1], b[1]));
        
        int[] result = new int[qLen];
        int[] serverCount = new int[n + 1]; // Tracks active request counts for each server
        int activeServers = 0;
        
        int left = 0;
        int right = 0;
        int logsCount = logs.length;
        
        for (int i = 0; i < qLen; i++) {
            int queryTime = sortedQueries[i][0];
            int queryIdx = sortedQueries[i][1];
            int startTime = queryTime - x;
            
            // Expand right boundary: add logs that occurred <= queryTime
            while (right < logsCount && logs[right][1] <= queryTime) {
                int server = logs[right][0];
                if (serverCount[server] == 0) {
                    activeServers++;
                }
                serverCount[server]++;
                right++;
            }
            
            // Shrink left boundary: remove logs that occurred < startTime
            while (left < logsCount && logs[left][1] < startTime) {
                int server = logs[left][0];
                serverCount[server]--;
                if (serverCount[server] == 0) {
                    activeServers--;
                }
                left++;
            }
            
            // Total servers minus active servers gives servers with zero requests
            result[queryIdx] = n - activeServers;
        }
        
        return result;
    }
}