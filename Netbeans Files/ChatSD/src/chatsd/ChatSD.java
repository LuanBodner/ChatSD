package chatsd;

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
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.Utils;

public class ChatSD {

    public static void main(String[] args) {

        MulticastSocket s = null;
        try {
            // cria um grupo multicast 
            InetAddress group = null;
            group = InetAddress.getByName(Utils.IP);

            // cria um socket multicast 
            s = new MulticastSocket(Utils.PORTTOMULTICASTMESSAGES);

            // adiciona o host ao grupo 
            s.joinGroup(group);

            Utils.CONNECTED = new ArrayList();

            System.out.print("Set Nickname: ");
            Scanner sc = new Scanner(System.in);

            Utils.NICKNAME = sc.next();

            MulticastListener ml = new MulticastListener(s, group);
            MulticastSender ms = new MulticastSender(s, group);

        } catch (SocketException e) {
            System.out.println("Socket: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO: " + e.getMessage());
        }
    }
}
