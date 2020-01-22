package ar.com.erisx;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class ServerV2 {
    private static final int PORT_NUMBER = 8090;
    private static final int BUFFER_SIZE = 1024;

    public static void main(String[] args) throws IOException {
        ServerSocketChannel server = ServerSocketChannel.open();
        server.socket().bind(new InetSocketAddress(PORT_NUMBER));
        server.socket().setReuseAddress(true);
        server.configureBlocking(false);

        Selector selector = Selector.open();
        server.register(selector, SelectionKey.OP_ACCEPT);

        ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
        while (true) {
            int channelCount = selector.select();
            if (channelCount > 0) {
                Set<SelectionKey> keys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = keys.iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    iterator.remove();

                    if (key.isAcceptable()) {
                        SocketChannel client = server.accept();
                        client.configureBlocking(false);
                        client.register(selector, SelectionKey.OP_READ, client.socket().getPort());
                    } else if (key.isReadable()) {
                        SocketChannel client = (SocketChannel) key.channel();
                        System.out.println("port: " + key.attachment());
                        if (client.read(buffer) < 0) {
                            key.cancel();
                            client.close();
                        } else {
                            buffer.flip(); // read from the buffer
                            /*
                             * byte[] received = new byte[buffer.remaining()];
                             * buffer.get(received); buffer.clear(); // write into the buffer
                             * buffer.put(received); buffer.flip(); // read from the buffer
                             */
                            client.write(buffer);
                            buffer.clear(); // write into the buffer            }
                        }
                    }
                }
            }
        }

    }
}
