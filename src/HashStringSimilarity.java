public class HashStringSimilarity extends HashSimilarity {

    HashStringSimilarity(String s1, String s2, int sLength) {
        super(s1, s2, sLength);
    }

    @Override
    void fillHashMap(HashTable table, String s) {
        StringIterator iterator = new StringIterator(s, sLength);

        String curString = iterator.next();
        char previousFirstChar = curString.charAt(0);
        int curHash = calculateHashString(curString);

        // HashStringSimilarity DOES account for string hash function collisions
        // therefore the added tuple must store the original sub-string
        table.add(new Tuple(curHash, curString));

        while (iterator.hasNext()) {
            curString = iterator.next();

            // calculate current hash based on last sub-string hash code
            curHash = (curHash - previousFirstChar * lastFactor) * alpha + curString.charAt(sLength - 1);

            previousFirstChar = curString.charAt(0);
            table.add(new Tuple(curHash, curString));
        }
    }

}
