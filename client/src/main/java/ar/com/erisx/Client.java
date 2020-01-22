package ar.com.erisx;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.file.Path;
import java.util.List;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

public class Client{

    public static void main(String []args){

        try{
            new Client();
        }catch(IOException e){
            e.printStackTrace();
        }catch(InterruptedException e){
            e.printStackTrace();

        }

    }



    public Client() throws InterruptedException,IOException{

        FileGenerator fileGenerator = new FileGenerator();
        List<Path> path = readFiles(fileGenerator.getFileLocation());

        if (path == null){
            System.exit(1);
        }
        path.stream().forEach(file->{
            Thread t =  new Thread(new Sender(file.toString()));
            t.start();
        });


    }

    public List<Path> readFiles(String dirName) {
        try {
            return Files.list(new File(dirName).toPath()).collect(Collectors.toList());
        }catch(IOException e){
            e.printStackTrace();
            return null;
        }
    }




}
