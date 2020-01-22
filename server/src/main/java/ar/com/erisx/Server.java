package ar.com.erisx;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.*;

import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.concurrent.BlockingQueue;

public class Server{

    private Selector selector;
    private InetSocketAddress listenAddress;
    private Map<SocketChannel,List> dataMapper;
    private BlockingQueue<SelectionKey> queue;
    private int cont =0;
    private Map<String,Statistics> statisticsMap = new HashMap<String, Statistics>();
    private Map<String,CheckMessage> checkMessages = new HashMap<String,CheckMessage>();

    public static void main (String [] args) throws IOException {

        new Server();

    }


    //accept a connection made to this channel's socket
    private void accept(SelectionKey key) throws IOException {
        ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
        SocketChannel channel = serverChannel.accept();
        channel.configureBlocking(false);
        Socket socket = channel.socket();
        SocketAddress remoteAddr = socket.getRemoteSocketAddress();
        System.out.println("Connected to: " + remoteAddr);

        // register channel with selector for further IO
        dataMapper.put(channel, new ArrayList());
        channel.register(this.selector, SelectionKey.OP_READ);
    }


    public Server() throws IOException{
        Timer timer = new Timer();
        CronStatistics  cron = new CronStatistics(statisticsMap);
        timer.scheduleAtFixedRate(cron,0,5000);
        listenAddress = new InetSocketAddress("localhost", 8090);
        dataMapper = new HashMap<SocketChannel,List>();
        this.selector = Selector.open();
        ServerSocketChannel serverChannel = ServerSocketChannel.open();
        serverChannel.configureBlocking(false);

        serverChannel.socket().bind(listenAddress);
        serverChannel.register(this.selector,SelectionKey.OP_ACCEPT);
        System.out.println("server is started");


        while(true){

            this.selector.select();
            long start = System.currentTimeMillis();

            Iterator keys = this.selector.selectedKeys().iterator();

            while (keys.hasNext()){
                SelectionKey key = (SelectionKey) keys.next();
                if(!key.isValid())
                    continue;
                if(key.isAcceptable()){
                    this.accept(key);
                }else{
                    if(key.isReadable()){
                        try  {
                            this.read(key);
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                }
                keys.remove();
            }
        }

    }

    //read from the socket channel
    private void read(SelectionKey key) throws IOException {

        SocketChannel channel = (SocketChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        int numRead = channel.read(buffer);
        String clientUrl = channel.socket().getRemoteSocketAddress().toString();
        if (statisticsMap.get(clientUrl) == null) {
            statisticsMap.put(clientUrl, new Statistics());
            checkMessages.put(clientUrl, new CheckMessage());
        }

        if (numRead == -1) {
            this.dataMapper.remove(channel);
            Socket socket = channel.socket();
            SocketAddress remoteAddr = socket.getRemoteSocketAddress();
            System.out.println("Connection closed by client: " + remoteAddr);
            channel.close();
            key.cancel();
            statisticsMap.get(channel.socket().getRemoteSocketAddress().toString()).end();
            checkMessages.remove(clientUrl);
            return;
        }

        byte[] data = new byte[numRead];
        System.arraycopy(buffer.array(), 0, data, 0, numRead);
        String message = new String(data);
        System.out.println(contador++);

        List<String> wrappedMessages = checkMessages.get(clientUrl).check(message);
        statisticsMap.get(clientUrl).messagesProcessed(wrappedMessages.size());
        wrappedMessages.forEach(System.out::println);


        buffer.flip();
        channel.write(buffer);
        buffer.compact();

        buffer.clear();
    }
    int contador = 0;



}


