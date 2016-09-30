package util;

import java.io.File;
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

    //nome do folder onde os arquivos vao ficar
    public static final String SHAREDFOLDER = "sd_shared";

    //listar arquivos
    public static ArrayList<String> listFilesFromSharedFolder() {
        File file = new File(Utils.SHAREDFOLDER);
        if (!file.exists()) {
            throw new IllegalArgumentException("the shared folder doesn't exists");
        }
        if (!file.isDirectory()) {
            throw new IllegalArgumentException("This isn't a folder");
        }

        ArrayList<String> files = new ArrayList<String>();
        for (String s : file.list()) {
            if (new File(Utils.SHAREDFOLDER + File.pathSeparator + s).isFile()) {
                files.add(s);
            }
        }
        return files;
    }
}
