package util;

import java.util.ArrayList;
import java.util.Hashtable;

public class Utils {

    //valor padrão para IP
    public static final String IP = "225.1.2.3";
    //valor padrão para porta MULTICAST
    public static final int PORTTOMULTICASTMESSAGES = 6789;
    //valor padrão para porta privada
    public static final int PORTTOPRIVATEMESSAGES = 6799;
    //apelido do usuário
    public static String NICKNAME;
    //lista de usuários conectados
    public static ArrayList<String> CONNECTED;

    public static Hashtable<String, String> USERS;
}
