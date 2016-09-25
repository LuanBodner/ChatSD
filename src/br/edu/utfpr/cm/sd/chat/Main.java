package src.br.edu.utfpr.cm.sd.chat;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
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
                messageIn = new DatagramPacket(buffer, buffer.length);
                multicastListener.receive(messageIn);

                System.out.println("Recebido:" + new String(messageIn.getData()));
            } catch (IOException ex) {
                Logger.getLogger(MulticastListener.class.getName()).log(Level.SEVERE, null, ex);
            }
        } while (messageIn.getData().toString().equals("EXIT"));
    }
}

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

        do {
            Scanner sc = new Scanner(System.in);
            buffer = sc.toString().getBytes();
            messageOut = new DatagramPacket(buffer, buffer.length, address, 6789);

            try {
                multicastSender.send(messageOut);
            } catch (IOException ex) {
                Logger.getLogger(MulticastSender.class.getName()).log(Level.SEVERE, null, ex);
            }
        } while (buffer.equals("EXIT"));

    }
}

public class Main {

    public static void main() throws IOException {

        MulticastSocket s = null;
        try {
            // cria um grupo multicast 
            InetAddress group = null;
            group = InetAddress.getByName("235.1.2.3");

            // cria um socket multicast 
            s = new MulticastSocket(6789);

            // adiciona o host ao grupo 
            s.joinGroup(group);

            MulticastListener ml = new MulticastListener(s);
            MulticastSender ms = new MulticastSender(s, group);
            ml.run();
            ms.run();
        } catch (SocketException e) {
            System.out.println("Socket: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO: " + e.getMessage());
        } finally {
            if (s != null) {
                s.close(); //fecha o socket
            }
        }
    }
}
