import java.util.Iterator;

class TupleIterator implements Iterator<Tuple> {

    private HashTable hashTable;
    private int cell;
    private int index;
    private int count;

    TupleIterator(HashTable table) {
        hashTable = table;
        cell = 0;
        index = 0;
        count = 0;
    }

    @Override
    public boolean hasNext() {
        return count < hashTable.numElements();
    }

    @Override
    public Tuple next() {
        if (cell >= hashTable.size())
            throw new IllegalArgumentException("TupleIterator.hasNext() has not been called.");

        if (index < hashTable.getBucket(cell).size()) {
            count++;
            return hashTable.getBucket(cell).get(index++);
        }

        cell++;
        index = 0;

        return next();
    }

}
