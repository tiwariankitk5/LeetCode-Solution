import java.util.LinkedList;

class MyHashSet {
    private static final int BUCKET_SIZE = 769;
    private LinkedList<Integer>[] buckets;

    @SuppressWarnings("unchecked")
    public MyHashSet() {
        buckets = new LinkedList[BUCKET_SIZE];
        for (int i = 0; i < BUCKET_SIZE; i++) {
            buckets[i] = new LinkedList<>();
        }
    }

    private int hash(int key) {
        return key % BUCKET_SIZE;
    }

    public void add(int key) {
        int index = hash(key);
        if (!buckets[index].contains(key)) {
            buckets[index].add(key);
        }
    }

    public void remove(int key) {
        int index = hash(key);
        buckets[index].remove(Integer.valueOf(key));
    }

    public boolean contains(int key) {
        int index = hash(key);
        return buckets[index].contains(key);
    }
}