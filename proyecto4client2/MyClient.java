package proyecto4client2;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MyClient extends Thread {

    private int socketPortNumber;
    private String action;
    private String namePlayer;
    private int numberPlayer;
    public static DataOutputStream send;

    public MyClient() {
        this.socketPortNumber = utilities.Constants.socketPortNumber;
        this.action = "";
        this.namePlayer = "";
        this.numberPlayer = -1;
    } // constructor

    @Override
    public void run() {
        try {
            InetAddress address = InetAddress.getByName(utilities.Constants.address);
            Socket socket = new Socket(address, this.socketPortNumber);
            String adress=String.valueOf(InetAddress.getLocalHost());
            send = new DataOutputStream(socket.getOutputStream());
            DataInputStream receive = new DataInputStream(socket.getInputStream());

            // 2 com: recibo nombre del server
            System.out.println("The server sent me: " + receive.readUTF());
            
            if (this.action.equals("chat")) {
                // 3 com: envio accion, nombre y mensaje
                while (true) {
                    String line = receive.readUTF();
                    if (line.startsWith("MESSAGE")) {
                        System.out.println(line);
                    }
                }
            }else if(this.action.equals("log")){
                // Envio accion y nombre de jugador y recibo mi numero de jugador
                this.send.writeUTF(this.action+"&"+this.namePlayer);
                String respuesta = receive.readUTF();
                if(respuesta.equals("Connection refused")){
                    System.err.println(respuesta);
                    System.exit(0);
                }
                this.numberPlayer = Integer.parseInt(respuesta);
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
    
    public void setNamePlayer(String name){
        this.namePlayer = name;
    }
    
    public int getNumberPlayer(){
        return this.numberPlayer;
    }
    
//    public static void main(String[] args) {
//        String s = JOptionPane.showInputDialog("direc");
//        utilities.Constants.address = s;
//        MyClient client = new MyClient();
//        client.setAction("log");
//        client.setNamePlayer("Yer");
//        client.start();
//    }

} // end class
