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

class Listener extends Thread {

    MulticastSocket multicastListener;
    byte[] buffer;
    DatagramPacket messageIn;
    InetAddress address;

    public Listener(MulticastSocket multicastSocket, InetAddress address) {

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
                    String joinAck = new String("JOINACK [" + Utils.NICKNAME + "]");

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
                } else if (receivedMessage.contains("JOINACK ")) {

                    //se chegou um joinack
                    String nick = receivedMessage.substring(9, receivedMessage.length() - 1);
                    String[] token = nick.split("[\\]]+");

                    //adiciona os nicks na lista de membros conhecidos
                    if (!token[0].equals(Utils.NICKNAME)) {
                        Utils.CONNECTED.add(token[0]);
                    }
                    //printa a mensagem de joinack
                    System.out.println(receivedMessage);

                } else if (receivedMessage.contains("MSG ")) {

                    //se for uma mensagem simples, printa na tela
                    System.out.println(receivedMessage);
                } else if (receivedMessage.contains("MSGIDV FROM ")) {

                    //se for uma mensagem privada, faz um parse na string
                    String[] token = receivedMessage.split("[' '\\[\\]]+");

                    //verifica se é o usuário correto
                    if (token[4].equals(Utils.NICKNAME)) {
                        //printa a mensagem
                        System.out.println(receivedMessage);
                    }
                }

            } catch (IOException ex) {
                Logger.getLogger(Listener.class.getName()).log(Level.SEVERE, null, ex);
            }
        } while (true);

    }
}