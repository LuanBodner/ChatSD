package chatsd;

import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.Utils;

class MulticastListener extends Thread {

    MulticastSocket multicastListener;
    byte[] buffer;
    DatagramPacket messageIn;
    InetAddress address;

    public MulticastListener(MulticastSocket multicastSocket, InetAddress address) {

        // inicializa valores da classe
        multicastListener = multicastSocket;
        buffer = new byte[1000];
        this.address = address;

        //inicializa a thread
        this.start();
    }

    public void run() {

        do {
            try {
                buffer = new byte[1000];
                messageIn = new DatagramPacket(buffer, buffer.length);
                //recebo a mensagem vinda de um multicast
                multicastListener.receive(messageIn);

                //parse do buffer para string
                String receivedMessage = new String(messageIn.getData());

                //se chegou um comando JOIN
                if (receivedMessage.contains("JOIN ")) {

                    //envia um JOINACK para o grupo
                    String joinAck = new String("JOINACK [" + Utils.NICKNAME + "] " + Inet4Address.getLocalHost().getHostAddress().toString());

                    buffer = joinAck.getBytes();
                    DatagramPacket messageOut = new DatagramPacket(buffer, buffer.length, address, Utils.PORTTOMULTICASTMESSAGES);
                    multicastListener.send(messageOut);

                    //parse do nome do join
                    String joinned = receivedMessage.substring(6, receivedMessage.length() - 1);
                    String[] token = joinned.split("[\\]]+");

                    //adiciona o novo membro na lista de usuários conhecidos
                    if (!token[0].equals(Utils.NICKNAME)) {

                        Utils.CONNECTED.add(token[0]);
                    }

                    Utils.USERS.put(token[0], token[1]);

                } else if (receivedMessage.contains("JOINACK ")) {

                    //se chegou um joinack
                    String nick = receivedMessage.substring(9, receivedMessage.length() - 1);
                    String[] token = nick.split("[\\]]+");

                    //adiciona os nicks na lista de membros conhecidos
                    if (!token[0].equals(Utils.NICKNAME)) {
                        Utils.CONNECTED.add(token[0]);
                    }

                    Utils.USERS.put(token[0], token[1]);

                    //printa a mensagem de joinack
                    System.out.println(receivedMessage);

                } else if (receivedMessage.contains("MSG ")) {

                    //se for uma mensagem simples, printa na tela
                    System.out.println(receivedMessage);
                }

            } catch (IOException ex) {
                Logger.getLogger(MulticastListener.class.getName()).log(Level.SEVERE, null, ex);
            }
        } while (true);

    }
}
