
class Solution {
    private PriorityQueue<Integer> small; // Max-heap for the smaller half
    private PriorityQueue<Integer> large; // Min-heap for the larger half
    private Map<Integer, Integer> delayed; 
    private int smallSize = 0;
    private int largeSize = 0;

    public double[] medianSlidingWindow(int[] nums, int k) {
        // Use Integer.compare to avoid integer overflow with extreme values like Integer.MIN_VALUE
        small = new PriorityQueue<>((a, b) -> Integer.compare(b, a));
        large = new PriorityQueue<>();
        delayed = new HashMap<>();

        int n = nums.length;
        double[] result = new double[n - k + 1];

        for (int i = 0; i < n; i++) {
            addNum(nums[i]);

            // Remove the element that slid out of the window
            if (i >= k) {
                removeNum(nums[i - k]);
            }

            // Record median once the first full window of size k is formed
            if (i >= k - 1) {
                result[i - k + 1] = getMedian(k);
            }
        }

        return result;
    }

    private void addNum(int num) {
        if (small.isEmpty() || num <= small.peek()) {
            small.offer(num);
            smallSize++;
        } else {
            large.offer(num);
            largeSize++;
        }
        rebalance();
    }

    private void removeNum(int num) {
        delayed.put(num, delayed.getOrDefault(num, 0) + 1);
        if (num <= small.peek()) {
            smallSize--;
            if (num == small.peek()) {
                prune(small);
            }
        } else {
            largeSize--;
            if (!large.isEmpty() && num == large.peek()) {
                prune(large);
            }
        }
        rebalance();
    }

    private void rebalance() {
        // Maintain invariant: smallSize == largeSize OR smallSize == largeSize + 1
        if (smallSize > largeSize + 1) {
            large.offer(small.poll());
            smallSize--;
            largeSize++;
            prune(small);
        } else if (smallSize < largeSize) {
            small.offer(large.poll());
            largeSize--;
            smallSize++;
            prune(large);
        }
    }

    private void prune(PriorityQueue<Integer> heap) {
        while (!heap.isEmpty() && delayed.containsKey(heap.peek())) {
            int top = heap.poll();
            int count = delayed.get(top);
            if (count == 1) {
                delayed.remove(top);
            } else {
                delayed.put(top, count - 1);
            }
        }
    }

    private double getMedian(int k) {
        if ((k & 1) == 1) {
            return (double) small.peek();
        }
        return ((double) small.peek() + (double) large.peek()) / 2.0;
    }
}