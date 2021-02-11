import java.io.Serializable;

public class Training implements Serializable {
    private String name;
    private String phone;
    private String message;

    public Training(String name, String phone, String message) {
        this.name = name;
        this.phone = phone;
        this.message = message;
    }

    @Override
    public String toString() {
        return "Training message\r\n" +
                "Name: '" + name + '\'' +
                "\r\nPhone: '" + phone + '\'' +
                "\r\nMessage: '" + message + '\'' +
                "\r\n----------------------------";
    }
}
