public class Tuple {

    private int key;
    private String value;

    int frequency = 1;

    public Tuple(int keyP, String valueP) {
        this.key = keyP;
        this.value = valueP;
    }

    public Integer getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        return o != null && o.getClass() == getClass() &&
                this.key == ((Tuple) o).key && this.value.equals(((Tuple) o).value);
    }

}
