package ar.com.erisx;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.LinkedList;
import java.util.List;

public class ReadThread  implements Runnable {
    private SocketChannel client;
    private int messagesSended;
    private int cont = 0;
    private StringBuilder stringBuilder = new StringBuilder();
    private List<String> messages = new LinkedList<String>();
    private String message = "";

    public ReadThread(SocketChannel client,int messagesSended){
        this.client=client;
        this.messagesSended=messagesSended;
    }
    @Override
    public void run() {

        try {
            int contMessageReceived = 0;
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            while (contMessageReceived < messagesSended) {
                buffer.clear();
                int numRead = client.read(buffer);
                byte[] data = new byte[numRead];
                System.arraycopy(buffer.array(), 0, data, 0, numRead);
                String parsedData = new String(data);
                checkMessage(parsedData);
                contMessageReceived = contMessageReceived + messages.size();


                System.out.println("messages received:" + contMessageReceived);
                messages.stream().forEach(x -> {
                    System.out.println("received: " + x);
                });
            }

            client.close();
        }catch(IOException e){
            e.printStackTrace();
        }

    }


    private List<String> checkMessage(String incomingMessage) {

        this.message = this.message +incomingMessage;
        messages.clear();
        cont =0;
        stringBuilder = new StringBuilder();

        int index = 0;
        do {
            index = message.indexOf("|");
            if (index != -1) {
                stringBuilder.append(message.substring(0, index) + "|"  );
                cont++;
                message = message.substring(index+1, message.length());
            }else {
                stringBuilder.append(message);
            }

            if (cont == 16) {
                messages.add(stringBuilder.toString());
                stringBuilder = new StringBuilder();
                cont = 0;
            }
        } while (index != -1);

        message = stringBuilder.toString();
        return messages;

    }
}
