package chatsd;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.Utils;

class MulticastListener extends Thread {

    MulticastSocket multicastListener;
    byte[] buffer;
    DatagramPacket messageIn;
    InetAddress address;

    public MulticastListener(MulticastSocket multicastSocket, InetAddress address) {

        multicastListener = multicastSocket;
        buffer = new byte[1000];
        this.address = address;

        this.start();
    }

    public void run() {

        do {
            try {
                buffer = new byte[1000];
                messageIn = new DatagramPacket(buffer, buffer.length);
                multicastListener.receive(messageIn);

                String receivedMessage = new String(messageIn.getData());

                if (receivedMessage.contains("JOIN ")) {

                    String joinAck = new String("JOINACK [" + Utils.NICKNAME + "]");

                    buffer = joinAck.getBytes();
                    DatagramPacket messageOut = new DatagramPacket(buffer, buffer.length, address, Utils.PORTTOMULTICASTMESSAGES);
                    multicastListener.send(messageOut);

                    String joinned = receivedMessage.substring(6, receivedMessage.length() - 1);
                    String[] token = joinned.split("[\\]]+");

                    if (!token[0].equals(Utils.NICKNAME)) {
                        Utils.CONNECTED.add(token[0]);
                    }
                } else if (receivedMessage.contains("JOINACK ")) {

                    String nick = receivedMessage.substring(9, receivedMessage.length() - 1);
                    String[] token = nick.split("[\\]]+");

                    if (!token[0].equals(Utils.NICKNAME)) {
                        Utils.CONNECTED.add(token[0]);
                    }
                    System.out.println(receivedMessage);

                } else if (receivedMessage.contains("MSG ")) {

                    String nick = receivedMessage.substring(9, receivedMessage.length() - 1);
                    String[] token = nick.split("[\\]]+");

                    System.out.println(receivedMessage);
                } else if (receivedMessage.contains("MSGIDV FROM ")) {

                    String[] token = receivedMessage.split("[' '\\[\\]]+");

                    if (token[4].equals(Utils.NICKNAME)) {

                        System.out.println(receivedMessage);
                    }
                }

            } catch (IOException ex) {
                Logger.getLogger(MulticastListener.class.getName()).log(Level.SEVERE, null, ex);
            }
        } while (true);

    }
}
