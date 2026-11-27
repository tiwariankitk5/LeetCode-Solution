import heapq

class Solution(object):
    def trapRainWater(self, heightMap):
        if not heightMap or not heightMap[0]:
            return 0
        
        m, n = len(heightMap), len(heightMap[0])
        if m < 3 or n < 3:
            return 0
        
        visited = [[False] * n for _ in range(m)]
        min_heap = []
        
        for r in range(m):
            for c in (0, n - 1):
                heapq.heappush(min_heap, (heightMap[r][c], r, c))
                visited[r][c] = True
                
        for c in range(1, n - 1):
            for r in (0, m - 1):
                heapq.heappush(min_heap, (heightMap[r][c], r, c))
                visited[r][c] = True
                
        water_trapped = 0
        directions = ((1, 0), (-1, 0), (0, 1), (0, -1))
        
        while min_heap:
            height, r, c = heapq.heappop(min_heap)
            
            for dr, dc in directions:
                nr, nc = r + dr, c + dc
                if 0 <= nr < m and 0 <= nc < n and not visited[nr][nc]:
                    visited[nr][nc] = True
                    neighbor_height = heightMap[nr][nc]
                    
                    if neighbor_height < height:
                        water_trapped += height - neighbor_height
                        heapq.heappush(min_heap, (height, nr, nc))
                    else:
                        heapq.heappush(min_heap, (neighbor_height, nr, nc))
                        
        return water_trapped