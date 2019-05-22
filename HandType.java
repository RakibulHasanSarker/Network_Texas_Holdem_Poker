public class HandType {
    private String type;
    private int value;

    public HandType(String type, int value) {
        this.type = type;
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public String getType() {
        return type;
    }
}