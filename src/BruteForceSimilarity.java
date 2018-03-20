import java.util.ArrayList;

public class BruteForceSimilarity extends Similarity {

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
    BruteForceSimilarity(String s1, String s2, int sLength) {
        super(s1, s2, sLength);
    }

    @Override
    ArrayList<Tuple> getSubstringSetS1() {
        if (substringSetS1.isEmpty())
            substringSetS1 = getSubstringSet(s1, sLength);
        return substringSetS1;
    }

    @Override
    ArrayList<Tuple> getSubstringSetS2() {
        if (substringSetS2.isEmpty())
            substringSetS2 = getSubstringSet(s2, sLength);
        return substringSetS2;
    }

    private ArrayList<Tuple> getSubstringSet(String s, int len) {
        ArrayList<Tuple> set = new ArrayList<>(s.length() - len);
        StringIterator iterator = new StringIterator(s, len);

        while (iterator.hasNext()) {
            String next = iterator.next();
            boolean found = false;
            for (Tuple t : set) {
                if (t.getValue().equals(next)) {
                    t.frequency++;
                    found = true;
                    break;
                }
            }

            if (!found)
                set.add(new Tuple(0, next));
        }

        return set;
    }

    @Override
    ArrayList<Tuple> getSubstringSetUnion() {
        if (substringSetS1.isEmpty()) getSubstringSetS1();
        if (substringSetS2.isEmpty()) getSubstringSetS2();

        if (unionSet == null || unionSet.size() == 0){
            union(unionSet, substringSetS1);
            union(unionSet, substringSetS2);
        }

        return unionSet;
    }

    /**
     *
     * @param union the set that will include all elements of the given set, set
     * @param set the set in which each element must appear in the union set
     */
    private void union(ArrayList<Tuple> union, ArrayList<Tuple> set) {
        for (Tuple set_tuple : set) {
            boolean found = false;
            for (Tuple union_tuple : union) {
                if (union_tuple.equals(set_tuple)) {
                    found = true;
                    break;
                }
            }

            if (!found)
                union.add(set_tuple);
        }
    }

}
