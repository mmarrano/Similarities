import java.util.ArrayList;

abstract class HashSimilarity extends Similarity {

    private HashTable hashSet1;
    private HashTable hashSet2;

    int alpha = 31;
    int lastFactor = 1;

    /**
     *
     * Note: it is assumed that s1 and s2 have been pre-processed in such a way that:
     *         - all punctuation symbols have been removed
     *         - all white space has been removed
     *         - all letters are lowercase
     *
     * @param s1 a string to be compared to s2
     * @param s2 a string to be compared to s1
     * @param sLength the shingle length to be used to compute the similarity between s1 and s2
     */
    HashSimilarity(String s1, String s2, int sLength) {
        super(s1, s2, sLength);

        hashSet1 = new HashTable(s1.length() - sLength);
        fillHashMap(hashSet1, s1);

        hashSet2 = new HashTable(s2.length() - sLength);
        fillHashMap(hashSet2, s2);
    }

    @Override
    ArrayList<Tuple> getSubstringSetS1() {
        if (substringSetS1.isEmpty())
            substringSetS1 = getSubstringSet(hashSet1);

        return substringSetS1;
    }

    @Override
    ArrayList<Tuple> getSubstringSetS2() {
        if (substringSetS2.isEmpty())
            substringSetS2 = getSubstringSet(hashSet2);

        return substringSetS2;
    }

    private ArrayList<Tuple> getSubstringSet(HashTable table) {
        ArrayList<Tuple> set = new ArrayList<>();
        TupleIterator iterator = new TupleIterator(table);

        while(iterator.hasNext())
            set.add(iterator.next());

        return set;
    }

    @Override
    ArrayList<Tuple> getSubstringSetUnion() {
        unionSet = new ArrayList<>();

        // for every tuple, t, in set 1, add t to the union set
        TupleIterator iterator = new TupleIterator(hashSet1);
        while (iterator.hasNext())
            unionSet.add(iterator.next());

        // for every tuple, t, in set 2, if t did not exist in set 1, add t to the union set
        iterator = new TupleIterator(hashSet2);
        while (iterator.hasNext()) {
            Tuple t = iterator.next();

            if (hashSet1.search(t) == 0)
                unionSet.add(t);
        }

        return unionSet;
    }

    /**
     * Calculates the hash string of a string
     * @param s initial substring to hash
     * @return hash string of s
     */
    int calculateHashString(String s) {
        int v = 0;
        int factor = 1;
        for (int i = s.length() - 1; i >= 0; i--) {
            // In last iteration of this, last factor is set to alpha raised to the length of the s-1 (m-1)
            lastFactor = factor;
            v += s.charAt(i) * factor;
            factor *= alpha;
        }
        return v;
    }

    /**
     * Fills the given hash table with values. It uses roll over hashing
     * @param table table to fill
     * @param s document string to iterate through to hashcode substrings
     */
    abstract void fillHashMap(HashTable table, String s);

    @Override
    public float similarity() {
        float length = 0f;
        for (Tuple t : getSubstringSetUnion())
            length += hashSet1.search(t) * hashSet2.search(t);
        return length / ( lengthOfS1() * lengthOfS2() );
    }
}
