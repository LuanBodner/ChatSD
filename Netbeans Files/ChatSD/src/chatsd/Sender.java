package chatsd;

import file.FileSender;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.Utils;

class Sender extends Thread {

    byte[] buffer;
    InetAddress address;
    DatagramPacket messageOut;
    MulticastSocket multicastSender;

    public Sender(MulticastSocket multicastSocket, InetAddress address) throws UnknownHostException {

        //inicializa as variaveis da classe
        multicastSender = multicastSocket;
        buffer = new byte[1000];
        this.address = address;

        // cria a mensagem padrão de join
        String joinack = "JOIN [" + Utils.NICKNAME + "]" + Inet4Address.getLocalHost().getHostAddress().toString();
        buffer = joinack.getBytes();
        messageOut = new DatagramPacket(buffer, buffer.length, address, Utils.PORTTOMULTICASTMESSAGES);
        try {
            //envia mensagem para o grupo que um novo user entrou
            multicastSender.send(messageOut);
        } catch (IOException ex) {
            Logger.getLogger(Sender.class.getName()).log(Level.SEVERE, null, ex);
        }

        //inicializa thread
        this.start();
    }

    public void run() {

        String message;

        do {

            //le o comando + mensagem enviada pelo usuario
            Scanner sc = new Scanner(System.in);
            message = sc.nextLine();

            //se o comando for SAIR, termina o programa
            if (message.equals("LEAVE [" + Utils.NICKNAME + "]")) {
                System.exit(0);
            }

            if (message.contains("DOWNFILE ")) {

                //DO NOTHING
                //Interface incompleta
            }
            buffer = message.getBytes();

            messageOut = new DatagramPacket(buffer, buffer.length, address, Utils.PORTTOMULTICASTMESSAGES);

            try {
                multicastSender.send(messageOut);
            } catch (IOException ex) {
                Logger.getLogger(Sender.class.getName()).log(Level.SEVERE, null, ex);
            }
        } while (true);

    }
}
