package proyecto4client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.StringReader;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

public class MyClient extends Thread {

    private int socketPortNumber;

    public MyClient() {
        this.socketPortNumber = utilities.Constants.socketPortNumber;
    } // constructor

    @Override
    public void run() {
        try {
            InetAddress address = InetAddress.getByName(utilities.Constants.address);
            Socket socket = new Socket(address, this.socketPortNumber);

            PrintStream send = new PrintStream(socket.getOutputStream());
            BufferedReader receive = new BufferedReader(
                    new InputStreamReader(socket.getInputStream())
            );
            
            send.println("Yer");

            socket.close();
        } catch (UnknownHostException ex) {
            Logger.getLogger(MyClient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MyClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    } // run

} // end class
