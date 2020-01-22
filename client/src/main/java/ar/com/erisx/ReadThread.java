package ar.com.erisx;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.LinkedList;
import java.util.List;

public class ReadThread  implements Runnable {
    private SocketChannel client;
    private int messagesSended;

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
                System.out.println("before buffer");
                int numRead = client.read(buffer);
                System.out.println("after buffer " + contadeur);
                contadeur++;
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

    private int contadeur = 0;

    private int cont = 0;
    private StringBuilder stringBuilder = new StringBuilder();
    private List<String> messages = new LinkedList<String>();

    String message = "";

    private List<String> checkMessage(String incomingMessage) {

        this.message = this.message +incomingMessage;
        messages.clear();
        System.out.println(incomingMessage);
        System.out.println(message);
        System.out.println(cont);
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
