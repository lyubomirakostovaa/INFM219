import java.io.*;
import java.net.Socket;

class ServeOneClient extends Thread {
    private Socket socket;
    private PrintWriter out;
    private Clients clt;
    private ObjectInputStream objectInputStream;

    public ServeOneClient(Socket s, Clients clt) throws IOException {
        socket = s;
        this.clt = clt;
        // Enable auto-flush:
        out = new PrintWriter(new BufferedWriter(
                new OutputStreamWriter(
                        socket.getOutputStream()
                )), true);

        objectInputStream = new ObjectInputStream(socket.getInputStream());
        clt.addC(out);
        start();    // Calls run()
    }

    public void run() {
        try {
            while (true) {
                Training training = null;
                try {
                    // receive object
                    training = (Training) objectInputStream.readObject();
                } catch (ClassNotFoundException e) {
                }
                String str = training.toString();
                if (str.equals("END")) break;
                System.out.println(str); // Write to the server console
                clt.sendC(str); // Sending to clients
            }

        } catch (IOException e) {
        } finally {
            try {
                clt.rmvC(out);
                System.out.println("disconect a client. Total number " + clt.nCl());
                socket.close();
            } catch (IOException e) {
            }
        }
    }
}