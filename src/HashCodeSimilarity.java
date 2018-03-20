public class HashCodeSimilarity extends HashSimilarity {

    HashCodeSimilarity(String s1, String s2, int sLength) {
        super(s1, s2, sLength);
    }

    @Override
    void fillHashMap(HashTable table, String s) {
        int curHash = calculateHashString(s.substring(0, sLength));

        // HashCodeSimilarity does not account for string hash function collisions
        // therefore the tuple value can be empty string for all tuples in the table
        table.add(new Tuple(curHash, ""));

        int index = 0;
        while (index < s.length() - sLength) {
            char previousFirstChar = s.charAt(index);
            char curLastChar = s.charAt(index + sLength);

            // calculate current hash based on last sub-string hash code
            curHash = (curHash - previousFirstChar * lastFactor) * alpha + curLastChar;
            table.add(new Tuple(curHash, ""));

            index++;
        }
    }

}
