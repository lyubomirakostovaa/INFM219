import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;

class Clients {
    private ArrayList<PrintWriter> pW;

    Clients() {
        pW = new ArrayList<PrintWriter>(10);
    }

    synchronized void addC(PrintWriter p) {
        pW.add(p);
    }

    synchronized void rmvC(PrintWriter p) {
        pW.remove(p);
    }

    synchronized void sendC(String s) {
        Iterator<PrintWriter> itr = pW.iterator();
        while (itr.hasNext()) {
            PrintWriter p = (PrintWriter) itr.next();
            p.println(s);
        }
    }

    synchronized int nCl() {
        return pW.size();
    }
}