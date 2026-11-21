class Solution(object):
    def findSubtreeSizes(self, parent, s):
        n = len(parent)
        adj = [[] for _ in range(n)]
        for i in range(1, n):
            adj[parent[i]].append(i)
            
        new_parent = list(parent)
        last_seen = [-1] * 26
        
        def dfs_reparent(u):
            char_idx = ord(s[u]) - ord('a')
            if last_seen[char_idx] != -1:
                new_parent[u] = last_seen[char_idx]
                
            prev_ancestor = last_seen[char_idx]
            last_seen[char_idx] = u
            
            for v in adj[u]:
                dfs_reparent(v)
                
            last_seen[char_idx] = prev_ancestor

        dfs_reparent(0)
        
        new_adj = [[] for _ in range(n)]
        for i in range(1, n):
            new_adj[new_parent[i]].append(i)
            
        ans = [1] * n
        
        def dfs_size(u):
            for v in new_adj[u]:
                ans[u] += dfs_size(v)
            return ans[u]
            
        dfs_size(0)
        return ans