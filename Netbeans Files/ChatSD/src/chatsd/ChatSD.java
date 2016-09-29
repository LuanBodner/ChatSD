package chatsd;

import java.io.IOException;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Scanner;
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

            //inicializa a lista de usuários conectados
            Utils.CONNECTED = new ArrayList();

            //lê o apelido do usuário
            System.out.print("Set Nickname: ");
            Scanner sc = new Scanner(System.in);

            Utils.NICKNAME = sc.next();

            //inicializa as threads de escuta e envio de mensagens
            Listener ml = new Listener(s, group);
            Sender ms = new Sender(s, group);

        } catch (SocketException e) {
            System.out.println("Socket: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO: " + e.getMessage());
        }
    }
}
