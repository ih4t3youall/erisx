package ar.com.erisx;



import java.io.IOException;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class ReadThread  implements Runnable{
    private SocketChannel socketChannel;

    public ReadThread(SocketChannel socketChannel){

        System.out.println("thread created");
        this.socketChannel = socketChannel;
    }
    public void run(){

        String remoteName = socketChannel.socket().getRemoteSocketAddress().toString();

        while(true) {
            int numRead = -1;
            ByteBuffer buffer = ByteBuffer.allocate(1024);

            try {
                numRead = socketChannel.read(buffer);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (numRead == -1) {
                Socket socket = socketChannel.socket();
                SocketAddress remoteAddr = socket.getRemoteSocketAddress();
                System.out.println("Connection closed by client: " + remoteAddr);
                try {
                    socketChannel.close();
                    break;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if(numRead > 0) {
                byte[] data = new byte[numRead];
                System.arraycopy(buffer.array(), 0, data, 0, numRead);
                String string = new String(data);
                if(string != "") {
                    System.out.println("Got: " + string + " from " + remoteName);
                    buffer.flip();
                    try {
                        socketChannel.write(buffer);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                buffer.clear();
            }
        }

    }
}

