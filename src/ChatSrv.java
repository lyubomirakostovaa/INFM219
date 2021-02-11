import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ChatSrv {
    static final int PORT = 9090;

    public static void main(String[] args) throws IOException {
        try (ServerSocket s = new ServerSocket(PORT)) {
            System.out.println("Server Started");
            Clients clt = new Clients();
            while (true) {
                // Blocks until a connection occurs:
                Socket socket = s.accept();
                try {
                    new ServeOneClient(socket, clt);
                    System.out.println("join a new client - total number " + clt.nCl());
                } catch (IOException e) {
                    // If it fails, close the socket,
                    // otherwise the thread will close it:
                    socket.close();
                }
            }
        }
    }
}