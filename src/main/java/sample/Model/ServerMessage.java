package sample.Model;

import java.util.HashMap;

public class ServerMessage {
    public static final String error = "error";
    public static final String successful = "successful";

    private final String type;
    HashMap<String, Object> data;

    public ServerMessage(String type, HashMap<String, Object> data) {
        this.type = type;
        this.data = data;
    }

    public String getType() {
        return type;
    }

    public HashMap<String, Object> getData() {
        return data;
    }
}
