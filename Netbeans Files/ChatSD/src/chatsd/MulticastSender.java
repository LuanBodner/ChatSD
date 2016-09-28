package chatsd;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.Utils;

class MulticastSender extends Thread {

    byte[] buffer;
    InetAddress address;
    DatagramPacket messageOut;
    MulticastSocket multicastSender;

    public MulticastSender(MulticastSocket multicastSocket, InetAddress address) {
        multicastSender = multicastSocket;
        buffer = new byte[1000];
        this.address = address;
        this.start();
    }

    public void run() {

        String message;

        do {
            Scanner sc = new Scanner(System.in);

            message = sc.nextLine();

            if (message.contains("LEAVE [*]")) {
                System.exit(0);
            }

            buffer = message.getBytes();
            messageOut = new DatagramPacket(buffer, buffer.length, address, Utils.PORTTOMULTICASTMESSAGES);

            try {
                multicastSender.send(messageOut);
            } catch (IOException ex) {
                Logger.getLogger(MulticastSender.class.getName()).log(Level.SEVERE, null, ex);
            }
        } while (true);

    }
}
