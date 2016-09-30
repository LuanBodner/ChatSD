package br.edu.utfpr.cm.sd.chat.util;

import java.io.File;
import java.util.ArrayList;

public class Utils {
    public static final String IP = "225.1.2.3";
    public static final int PORTTOMULTICASTMESSAGES = 6789;
    public static final int PORTTOPRIVATEMESSAGES = 6799;
    public static final String SHAREDFOLDER = "sd_shared";

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