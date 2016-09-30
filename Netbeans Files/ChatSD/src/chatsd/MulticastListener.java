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

                    //pega nome + msg
                    String[] token = receivedMessage.split("[' ']+");
                    String[] msg = receivedMessage.split("[\"\"]+");

                    //se for uma mensagem simples, printa na tela
                    System.err.println(token[1] + ": " + msg[1]);

                } else if (receivedMessage.contains("MSGIDV FROM ")) {

                    //se for uma mensagem privada, faz um parse na string
                    String[] token = receivedMessage.split("[' '\\[\\]]+");
                    String[] msg = receivedMessage.split("[\"\"]+");

                    //verifica se é o usuário correto
                    if (token[4].equals(Utils.NICKNAME)) {
                        //printa a mensagem
                        System.out.println(token[2] + ": " + msg[1]);
                    } else {
                        System.out.println("Wrong User");
                    }
                } else {
                    System.out.println("Wrong Protocol");
                }

            } catch (IOException ex) {
                Logger.getLogger(MulticastListener.class.getName()).log(Level.SEVERE, null, ex);
            }
        } while (true);

    }
}
