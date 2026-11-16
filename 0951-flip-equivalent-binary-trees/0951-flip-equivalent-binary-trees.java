class Solution {
    public boolean flipEquiv(TreeNode root1, TreeNode root2) {
        // Base cases
        if (root1 == null && root2 == null) {
            return true;
        }
        if (root1 == null || root2 == null || root1.val != root2.val) {
            return false;
        }

        // Check both possibilities: subtrees are unchanged OR subtrees are flipped
        boolean noFlip = flipEquiv(root1.left, root2.left) && flipEquiv(root1.right, root2.right);
        boolean flipped = flipEquiv(root1.left, root2.right) && flipEquiv(root1.right, root2.left);

        return noFlip || flipped;
    }
}