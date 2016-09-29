package br.edu.utfpr.cm.sd.chat.chatsd;

import java.io.IOException;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;

import br.edu.utfpr.cm.sd.chat.util.Utils;

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
            System.out.println("Initial command : JOIN [nickname]");

            MulticastListener ml = new MulticastListener(s);
            MulticastSender ms = new MulticastSender(s, group);
        } catch (SocketException e) {
            System.out.println("Socket: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO: " + e.getMessage());
        }
    }
}
