package chatsd;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

class MulticastListener extends Thread {

    MulticastSocket multicastListener;
    byte[] buffer;
    DatagramPacket messageIn;

    public MulticastListener(MulticastSocket multicastSocket) {

        multicastListener = multicastSocket;
        buffer = new byte[1000];
        this.start();
    }

    public void run() {

        do {
            try {
                buffer = new byte[1000];
                messageIn = new DatagramPacket(buffer, buffer.length);
                multicastListener.receive(messageIn);

                System.out.println("Recebido:" + new String(messageIn.getData()));

            } catch (IOException ex) {
                Logger.getLogger(MulticastListener.class.getName()).log(Level.SEVERE, null, ex);
            }
        } while (true);

    }
}
