import java.util.ArrayList;

public class HashTable {

    private int size;
    private HashFunction function;
    private ArrayList<ArrayList<Tuple>> buckets;
    private int count;

    HashTable(int size) {
        function = new HashFunction(size);
        this.size = function.getPrime();
        buckets = new ArrayList<>(this.size);
        for (int i = 0; i < this.size; i++)
            buckets.add(i, null);
    }

    /**
     *
     * @param index index the bucket we want to get returned
     * @return bucket at the given index of buckets
     */
    public ArrayList<Tuple> getBucket(int index){
        // Returns an empty ArrayList if given an index which cannot exist or if cell at given index is null
        if (index >= size || buckets.get(index) == null)
            return new ArrayList<>();
        return buckets.get(index);
    }
    /**
     *
     * @return the maximum load of the hash table
     */
    public int maxLoad() {
        int max = buckets.get(0).size();
        for (ArrayList<Tuple> bucket : buckets)
            if (bucket.size() > max)
                max = bucket.size();
        return max;
    }

    /**
     *
     * @return the average load of the hash table
     */
    public float averageLoad() {
        int total = 0;
        for (ArrayList<Tuple> bucket : buckets)
            total += bucket.size();
        return total / buckets.size();
    }

    /**
     *
     * @return the current size of the hash table
     */
    public int size() {
        return size;
    }

    /**
     *
     * @return the number of distinct Tuples that are currently stored in the hash table
     */
    public int numElements() {
        return count;
    }

    /**
     *
     * @return the load factor which is numElements()/size()
     */
    public int loadFactor() {
        return numElements() / size();
    }

    /** Adds the given Tuple, t, to this hash table
     *  by placing t in the list pointed to by the cell hash(t.getKey())
     *  where hash is the hash function method from the class HashFunction
     *
     *  When the load factors becomes bigger than 0.7,
     *  then the hash table (approximately) doubles its size and rehashes all containing tuples.
     *  The new size must be the smallest prime integer whose value is at least twice the current size.
     *
     * @param t the tuple to be added to this hash table
     */
    public void add(Tuple t) {
        int key = function.hash(t.getKey());

        ArrayList<Tuple> bucket = buckets.get(key);
        if (bucket == null) {
            buckets.set(key, new ArrayList<>());
            bucket = buckets.get(key);
        }

        addTupleToBucket(t, bucket);

        if (loadFactor() > 0.7)
            resize();
    }

    private void resize() {
        function.reset(size * 2);
        size = function.getPrime();
        count = 0;

        ArrayList<ArrayList<Tuple>> new_buckets = new ArrayList<>(size);
        for (int i = 0; i < size; i++)
            new_buckets.add(i, null);

        for (ArrayList<Tuple> old_bucket : buckets) {
            if (old_bucket == null) continue;
            for (Tuple tuple : old_bucket) {
                int key = function.hash(tuple.getKey());
                ArrayList<Tuple> new_bucket = new_buckets.get(key);

                if (new_bucket == null) {
                    new_buckets.set(key, new ArrayList<>());
                    new_bucket = new_buckets.get(key);
                }

                tuple.frequency = 1;
                addTupleToBucket(tuple, new_bucket);
            }
        }

        buckets = new_buckets;
    }

    private void addTupleToBucket(Tuple t, ArrayList<Tuple> bucket) {
        boolean found = false;

        for (Tuple other : bucket) {
            if (other.equals(t)) {
                other.frequency++;
                found = true;
                break;
            }
        }
        if (!found) {
            bucket.add(t);
            count++;
        }
    }

    /**
     *
     * @param k the key in which all Tuples in the returned list will have
     * @return an array list of Tuples in this hash table whose key equals k
     *         if no such Tuples exist, returns an empty list
     */
    public ArrayList<Tuple> search(int k) {
        int key = function.hash(k);
        ArrayList<Tuple> bucket = buckets.get(key);

        if (bucket == null)
            return new ArrayList<>();
        return bucket;
    }

    /**
     *
     * @param t the tuple in which its frequency/occurrences will be returned
     * @return the number of times Tuple t appears in the hash table
     */
    public int search(Tuple t) {
        int key = function.hash(t.getKey());
        ArrayList<Tuple> bucket = buckets.get(key);

        if (bucket == null)
            return 0;

        for (Tuple other : bucket)
            if (other.equals(t))
                return other.frequency;

        return 0;
    }

    /** Removes one occurrence of Tuple t from the hash table
     *
     * @param t the tuple to be removed from this hash table
     */
    public void remove(Tuple t) {
        int key = function.hash(t.getKey());

        ArrayList<Tuple> bucket = buckets.get(key);
        if (bucket != null)
            for (Tuple other : bucket)
                if (other.equals(t)) {
                    if (other.frequency == 1) {
                        bucket.remove(other);
                        count--;
                    } else
                        other.frequency--;
                    return;
                }
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();

        for (int i = 0; i < buckets.size(); i++) {
            ArrayList<Tuple> bucket = buckets.get(i);
            if (bucket == null)
                continue;

            str.append("bucket ").append(i).append(":\n");

            for (Tuple t : bucket)
                str.append(t.getValue()).append("(").append(t.getKey()).append("), ");

            str.append("\n");
        }

        return str.toString();
    }

}
