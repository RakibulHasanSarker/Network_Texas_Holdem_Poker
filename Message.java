import java.util.*;

public class Message {
    private Vector<String> message = new Vector<>();

    public Vector<String> getMessage() {
        return message;
    }

    public void add(String s) {
        message.add(0, s);
    }

    public void clear()
    {
        message.clear();
    }
}
