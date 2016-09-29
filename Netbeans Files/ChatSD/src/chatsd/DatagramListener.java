package chatsd;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatagramListener extends Thread {

    DatagramSocket datagramSocket;
    DatagramPacket message;
    byte[] buffer;

    public DatagramListener() throws SocketException {
        datagramSocket = new DatagramSocket();
        buffer = new byte[1000];
        this.run();
    }

    public void run() {

        do {

            message = new DatagramPacket(buffer, buffer.length);

            try {
                datagramSocket.receive(message);
            } catch (IOException ex) {
                Logger.getLogger(DatagramListener.class.getName()).log(Level.SEVERE, null, ex);
            }

            System.out.println(message.toString());

        } while (true);
    }
}
