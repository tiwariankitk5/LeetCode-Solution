class FrontMiddleBackQueue {
    private final Deque<Integer> left;
    private final Deque<Integer> right;

    public FrontMiddleBackQueue() {
        left = new ArrayDeque<>();
        right = new ArrayDeque<>();
    }

    public void pushFront(int val) {
        left.addFirst(val);
        rebalance();
    }

    public void pushMiddle(int val) {
        left.addLast(val);
        rebalance();
    }

    public void pushBack(int val) {
        right.addLast(val);
        rebalance();
    }

    public int popFront() {
        if (right.isEmpty()) {
            return -1;
        }
        int val = left.isEmpty() ? right.pollFirst() : left.pollFirst();
        rebalance();
        return val;
    }

    public int popMiddle() {
        if (right.isEmpty()) {
            return -1;
        }
        int val = (left.size() == right.size()) ? left.pollLast() : right.pollFirst();
        rebalance();
        return val;
    }

    public int popBack() {
        if (right.isEmpty()) {
            return -1;
        }
        int val = right.pollLast();
        rebalance();
        return val;
    }

    private void rebalance() {
        if (left.size() > right.size()) {
            right.addFirst(left.pollLast());
        } else if (right.size() > left.size() + 1) {
            left.addLast(right.pollFirst());
        }
    }
}