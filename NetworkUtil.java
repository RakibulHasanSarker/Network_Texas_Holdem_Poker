import java.io.*;
import java.net.*;

public class NetworkUtil {
    private Socket socket;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;

    public NetworkUtil(String s, int port) {
        try {
            this.socket = new Socket(s, port);
            oos = new ObjectOutputStream(socket.getOutputStream());
            ois = new ObjectInputStream(socket.getInputStream());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public NetworkUtil(Socket s) {
        try {
            this.socket = s;
            oos = new ObjectOutputStream(socket.getOutputStream());
            ois = new ObjectInputStream(socket.getInputStream());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Object read() {
        Object o = null;
        try {
            o = ois.readObject();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return o;
    }

    public void write(Object o) {
        try {
            oos.reset();
            oos.writeObject(o);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closeConnection() {
        try {
            ois.close();
            oos.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}