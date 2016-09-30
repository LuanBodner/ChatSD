package br.edu.utfpr.cm.sd.chat.file;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Inet4Address;
import java.net.ServerSocket;
import java.net.Socket;

import br.edu.utfpr.cm.sd.chat.util.Utils;

public class FileSender implements Runnable {
    private String ownHost;
    private int ownPort;
    private File file;
    private long fileLength;
    private ServerSocket serverSocket;

    public FileSender(String fileName) {
        loadFile(Utils.SHAREDFOLDER + File.pathSeparator + fileName);
        createSocket();
    }

    @Override
    protected void finalize() throws Throwable {
        if (serverSocket != null) {
            this.serverSocket.close();
        }
        super.finalize();
    }

    private void createSocket() {
        try {
            this.serverSocket = new ServerSocket(4000);
            this.ownHost = Inet4Address.getLocalHost().getHostAddress();
            this.ownPort = 4000;

        } catch (IOException e) {

            e.printStackTrace();
        }

    }

    private void loadFile(String fileName) {
        file = new File(fileName);
        if (!file.exists()) {
            throw new IllegalArgumentException("The file " + fileName + " doesn't exist.");
        }
        if (!file.isFile()) {
            throw new IllegalArgumentException("This isn't a file: " + fileName);
        }
        if (!file.canRead()) {
            throw new IllegalArgumentException("The file " + fileName + " can't be read.");
        }

        this.fileLength = file.length();
    }

    @Override
    public void run() {
        BufferedInputStream input = null;
        FileInputStream fileInput = null;
        OutputStream socketOutput = null;
        BufferedOutputStream output = null;

        try {
            Socket socket = serverSocket.accept();
            fileInput = new FileInputStream(file);
            input = new BufferedInputStream(fileInput);
            socketOutput = socket.getOutputStream();
            output = new BufferedOutputStream(socketOutput);
            int data = 0;
            while ((data = input.read()) != -1) {
                output.write(data);
            }
            input.close();
            output.flush();
            output.close();
            socket.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + file.getName());
        } catch (IOException e2) {
            e2.printStackTrace();
        }

    }

    /**
     * @return the ownHost
     */
    public String getOwnHost() {
        return ownHost;
    }

    /**
     * @param ownHost
     *            the ownHost to set
     */
    public void setOwnHost(String ownHost) {
        this.ownHost = ownHost;
    }

    /**
     * @return the ownPort
     */
    public int getOwnPort() {
        return ownPort;
    }

    /**
     * @param ownPort
     *            the ownPort to set
     */
    public void setOwnPort(int ownPort) {
        this.ownPort = ownPort;
    }

    /**
     * @return the fileLength
     */
    public long getFileLength() {
        return fileLength;
    }

    /**
     * @param fileLength
     *            the fileLength to set
     */
    public void setFileLength(long fileLength) {
        this.fileLength = fileLength;
    }

    /*
     * public static void main(String[] args) { FileSender fs = new
     * FileSender("a.txt"); fs.run(); }
     */
}