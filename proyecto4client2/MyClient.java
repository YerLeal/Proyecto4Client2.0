package proyecto4client2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MyClient extends Thread {

    private int socketPortNumber;
    private String action;
    public static PrintStream send;

    public MyClient() {
        this.socketPortNumber = utilities.Constants.socketPortNumber;
    } // constructor

    @Override
    public void run() {
        try {
            InetAddress address = InetAddress.getByName(utilities.Constants.address);
            Socket socket = new Socket(address, this.socketPortNumber);

            send = new PrintStream(socket.getOutputStream());
            BufferedReader receive = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // 2 com: recibo nombre del server
            System.out.println("The server sent me: " + receive.readLine());

            send.println(action+"&Yer&Hola");
            
            if (this.action.equals("CHAT")) {
                // 3 com: envio accion, nombre y mensaje
                while (true) {
                    String line = receive.readLine();
                    if (line.startsWith("MESSAGE")) {
                        System.out.println(line);
                    }
                }
            }
            socket.close();
        } catch (UnknownHostException ex) {
            Logger.getLogger(MyClient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MyClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    } // run

    public void setAction(String action) {
        this.action = action;
    }

} // end class
