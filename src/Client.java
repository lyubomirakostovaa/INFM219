import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class Client {
    private BufferedReader in;
    private PrintWriter out;
    private ObjectOutputStream objectOutputStream;
    private Socket socket;
    private String name;
    private Gui g;

    private Client(JFrame f) {
        init();
        g = new Gui(f);
    }

    class Gui {
        JTextArea serv;
        JTextField cl;

        Gui(JFrame f) {
            f.setLayout(new BorderLayout());
            serv = new JTextArea(20, 10);
            serv.setEditable(false);
            serv.setText("Please provide the following information: \r\n" +
                    "phone; message\r\n" +
                    "Please keep the format :)\r\n" +
                    "----------------------------\r\n");
            serv.setBackground(new Color(230, 230, 230));
            serv.setFont(new Font("SANS_SERIF", Font.BOLD, 14));
            cl = new JTextField(30);
            f.add("Center", new JScrollPane(serv));
            f.add("South", cl);
            cl.addActionListener(new SrvL());
            (new Rcv()).start();
        }

        class SrvL implements ActionListener {
            public void actionPerformed(ActionEvent e) {
                try {
                    String st = cl.getText();
                    send(st);
                    cl.setText("");
                } catch (Exception ex) {
                    System.out.println("exception: " + ex);
                    System.out.println("closing...");
                    try {
                        socket.close();
                    } catch (Exception expt) {
                        System.out.println(expt);
                    }
                }
            }
        }

        class Rcv extends Thread {
            public void run() {
                for (; ; ) {
                    try {
                        sleep(400);
                    } catch (InterruptedException e) {
                    }
                    try {
                        serv.append(in.readLine() + "\n");
                        serv.setCaretPosition(serv.getDocument().getLength());
                    } catch (IOException e1) {
                        break;
                    }
                }
                System.out.println(" closing reading thread...");
                try {
                    socket.close();
                } catch (Exception expt) {
                    System.out.println(expt);
                }
                System.exit(0);
            }
        }
    }

    private void init() {
        try {
            do {
                name = JOptionPane.showInputDialog("Please enter your name");
            } while ((name == null) || (name.length() == 0));
            String server = null;
            InetAddress addr = InetAddress.getByName(server);
            System.out.println("addr = " + addr);
            socket = new Socket(addr, ChatSrv.PORT);
            System.out.println("socket = " + socket);
            //BufferedReader sin = new BufferedReader(
            // new InputStreamReader(System.in));
            in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            // Output is automatically flushed
            // by PrintWriter:
            out = new PrintWriter(new BufferedWriter(
                    new OutputStreamWriter(socket.getOutputStream())), true);
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        } catch (Exception e) {
            System.out.println("exception: " + e);
            System.out.println("closing...");
            try {
                socket.close();
            } catch (Exception e2) {
                System.out.println("no server running");
                System.exit(5);
            }
        }
    }

    private void send(String s) {
        if (s.length() == 0) {
            int quit = JOptionPane.showConfirmDialog(null, "Exit chat");
            if (quit == 0) {
                out.println("END");
                System.out.println("closing...");
                try {
                    socket.close();
                } catch (Exception expt) {
                    System.out.println(expt);
                }
                System.exit(0);
            }
        } else {
            String[] st = s.split(";");
            Training training = new Training(name, st[0].trim(), st[1].trim());

            try {
                // sending the object
                objectOutputStream.writeObject(training);
            } catch (IOException e) {
            }
        }
    }

    public static void main(String[] args) throws IOException {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        Client cl = new Client(frame);
        frame.setTitle(cl.name + "  (empty line to exit)");
        frame.setSize(500, 300);
        frame.setVisible(true);
    }
}