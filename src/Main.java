import java.io.*;
import java.util.ArrayList;

public class Main {

    private static boolean verbose = false;


    public static void main(String[] args) {
        if (args.length > 0 && args[0].equals("-v"))
            verbose = true;

        run();
    }

    private static void run() {
        String s1 = getStringFromFile("src/shak1");
        String s2 = getStringFromFile("src/shak2");
        int sLength = 8;

        BruteForceSimilarity b = new BruteForceSimilarity(s1, s2, sLength);
        HashStringSimilarity s = new HashStringSimilarity(s1, s2, sLength);
        HashCodeSimilarity   c = new   HashCodeSimilarity(s1, s2, sLength);

        testSimilarity(b);
        testSimilarity(s);
        testSimilarity(c);


        if (verbose) {
            testHashTable();

            sLength = 4;
            s1 = getProcessedString("A rose is a rose is a rose.");
            s2 = getProcessedString("A rose is a flower, which is a rose.");

            b = new BruteForceSimilarity(s1, s2, sLength);
            s = new HashStringSimilarity(s1, s2, sLength);
            c = new   HashCodeSimilarity(s1, s2, sLength);

            // similarity should be 0.686 for all algorithms
            testSimilarity(b);
            testSimilarity(s);
            testSimilarity(c);
        }
    }

    private static void testSimilarity(Similarity similarity) {
        System.out.println(String.format("\n------ %s ------", similarity.getClass().toString().substring(6)));

        long start = System.nanoTime();
        float s = similarity.similarity();
        long delta = System.nanoTime() - start;

        System.out.println("similarity: " + s);
        System.out.println("time elapse (nanoseconds): " + delta);

        if (verbose) {
            System.out.println("s1: " + similarity.s1);
            System.out.println("s1 length  : " + similarity.lengthOfS1());
            System.out.println(getTupleArrayString(similarity.substringSetS1, true, false));
            System.out.println("---");

            System.out.println("s2: " + similarity.s1);
            System.out.println("s2 length  : " + similarity.lengthOfS2());
            System.out.println(getTupleArrayString(similarity.substringSetS2, true, false));
            System.out.println("---");

            System.out.println("union set:");
            System.out.println(getTupleArrayString(similarity.unionSet, false, false));
        }
    }

    private static void testHashTable() {
        System.out.println("\n------ Hash Table ------");

        HashTable table = new HashTable(4);
        System.out.println("table size: " + table.size());

        for (int i = 0; i < 20; i++) {
            Tuple t = new Tuple(i % 5, "foo_" + i);
            table.add(t);
            System.out.print("add tuple(" + (i % 5) + "), ");
        }

        System.out.println("\ntable size: " + table.size());
        System.out.println(table.toString());
    }

    private static String getTupleArrayString(ArrayList<Tuple> set, boolean frequency, boolean hashCode) {
        StringBuilder str = new StringBuilder();
        for (Tuple t : set)
            if (frequency)
                if (hashCode)
                    str.append(t.getKey()).append("(").append(t.frequency).append("), ");
                else
                    str.append(t.getValue()).append("(").append(t.frequency).append("), ");
            else
                if(hashCode)
                    str.append(t.getKey()).append(", ");
                else
                    str.append(t.getValue()).append(", ");
        return str.toString();
    }
    
    private static String getProcessedString(String s) {
        s = s.replaceAll("[ \\n\\t\\-'\";:+.,\\[\\]]*", "");
        s = s.toLowerCase();
        return s;
    }

    private static String getStringFromFile(String fileName) {
        StringBuilder str = new StringBuilder();
        String line;
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));

            while(!(line = bufferedReader.readLine()).isEmpty()) {
                line = line.replaceAll("[ \\n\\t\\-'\";:+.,\\[\\]]*", "");
                line = line.toLowerCase();
                str.append(line);
            }
            bufferedReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("Error: file not found: " + fileName);
            System.exit(1);
        } catch (IOException e) {
            System.out.println("Error: I/O exception: " + fileName);
            System.exit(1);
        }
        return str.toString();
    }

}

