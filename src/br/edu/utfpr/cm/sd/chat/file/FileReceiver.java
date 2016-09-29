package br.edu.utfpr.cm.sd.chat.file;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class FileReceiver implements Runnable {
    private String host;
    private int port;
    private String fileName;

    public FileReceiver(String host, int port, String fileName) {
        super();
        this.host = host;
        this.port = port;
        this.fileName = fileName;
    }

    @Override
    public void run() {
        try {
            Socket socket = new Socket(host, port);
            InputStream socketInput = socket.getInputStream();
            BufferedInputStream input = new BufferedInputStream(socketInput);
            FileOutputStream fileOutput = new FileOutputStream(fileName);
            BufferedOutputStream output = new BufferedOutputStream(fileOutput);
            int i = 0;
            while ((i = input.read()) != -1) {
                output.write(i);
            }
            input.close();
            output.flush();
            output.close();
            socket.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
    }

}
