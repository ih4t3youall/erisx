package ar.com.erisx;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.LinkedList;
import java.util.List;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.StringTokenizer;

public class Sender implements Runnable {

    private String path;

    public Sender(String path) {
        this.path = path;
    }

    public void run() {

        InetSocketAddress hostAddress = new InetSocketAddress("localhost", 8090);
        SocketChannel client = null;
        List<String> allLines = null;
        try {
            client = SocketChannel.open(hostAddress);
            allLines = Files.readAllLines(Paths.get(path));

        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Client... started");
        int messagesSended = allLines.size();
        ReadThread readThread = new ReadThread(client,messagesSended);

        Thread read = new Thread(readThread);
        read.start();

        try {
            for (String pureMessage : allLines) {
                System.out.println(pureMessage);
                byte[] message = new String(pureMessage).getBytes();
                ByteBuffer buffer = ByteBuffer.wrap(message);
                client.write(buffer);
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}





