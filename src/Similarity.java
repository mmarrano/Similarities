import java.util.ArrayList;
import java.util.Iterator;

abstract class Similarity {

    String s1, s2;
    int sLength;

    /**
     * cached s1 substring set to improve performance in similarity() algorithm
     */
    ArrayList<Tuple> substringSetS1;

    /**
     * cached s2 substring set to improve performance in similarity() algorithm
     */
    ArrayList<Tuple> substringSetS2;

    /**
     * cached union set of s1 and s2
     */
    ArrayList<Tuple> unionSet;

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
    Similarity(String s1, String s2, int sLength) {
        this.s1 = s1;
        this.s2 = s2;
        this.sLength = sLength;

        substringSetS1 = new ArrayList<>();
        substringSetS2 = new ArrayList<>();
        unionSet = new ArrayList<>();
    }

    /**
     *
     * Note: Let S be the multi-set of all shingles (of length sLength) of the string s1
     *
     * @return the VectorLength of S
     */
    public float lengthOfS1() {
        float length = 0f;
        for (Tuple n : getSubstringSetS1())
            length += (float) Math.pow(n.frequency, 2);
        return (float) Math.sqrt(length);
    }

    /**
     *
     * Note: Let T is the multi-set of all shingles (of length sLength) of the string s2
     *
     * @return the VectorLength of T
     */
    public float lengthOfS2() {
        float length = 0f;
        for (Tuple n : getSubstringSetS2())
            length += (float) Math.pow(n.frequency, 2);
        return (float) Math.sqrt(length);
    }

    /**
     *
     * @return Similarity(S, T) as described in the assignment description
     */
    public float similarity() {
        float length = 0f;
        for (Tuple t : getSubstringSetUnion())
            length += getFrequencyInSet(substringSetS1, t) * getFrequencyInSet(substringSetS2, t);
        return length / ( lengthOfS1() * lengthOfS2() );
    }

    /**
     *
     * @return a list of non-unique substrings in s1
     */
    abstract ArrayList<Tuple> getSubstringSetS1();

    /**
     *
     * @return a list of non-unique substrings in s2
     */
    abstract ArrayList<Tuple> getSubstringSetS2();

    /**
     *
     * @return a list of unique substrings s1 and s2 have in common
     */
    abstract ArrayList<Tuple> getSubstringSetUnion();

    /**
     *
     * @param set the set in which to look for given t
     * @param t the tuple to find in given set
     * @return frequency of given tuple, t, in given set, set
     */
    private int getFrequencyInSet(ArrayList<Tuple> set, Tuple t) {
        for (Tuple o : set)
            if (o.equals(t))
                return o.frequency;
        return 0;
    }

    /**
     * Iterates the substrings of a given length of a given string
     */
    class StringIterator implements Iterator {
        String s;
        int len, index;

        StringIterator(String s, int len) {
            this.s = s;
            this.len = len;
            index = 0;
        }

        @Override
        public boolean hasNext() {
            return index <= s.length() - len;
        }

        @Override
        public String next() {
            String str = s.substring(index, index + len);
            index++;
            return str;
        }

    }

}
