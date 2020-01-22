package ar.com.erisx;
import javax.swing.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.time.LocalDateTime;


public class FileGenerator {


    String filename = "file.txt";
    String dirName = System.getProperty("user.home")+"/files";

    public FileGenerator() throws IOException{


            new File(dirName).mkdir();
            FileWriter fw = new FileWriter(dirName+"/"+filename);

            String qty = JOptionPane.showInputDialog("Quantity of messages to generate");

            for (int i = 0 ; i < Integer.valueOf(qty) ; i++){
                fw.write("8=FIX.4.4|9="+getRandomChar()+"|35=D|34=1|49=TRADER|52=20190101-20:20:41:000|56=ERISX|11=123|38="+getRandomNumber(1,10)+"|40=2|44="+getRandomNumber(3500,4000)+"|54=1|55=BTCUSD|59=1|60="+ LocalDateTime.now()+"|10=111|");
                fw.write("\n");

            }
            fw.close();
            StringBuilder st = new StringBuilder();
            st.append("The file is located in");
            st.append(dirName);
            st.append("/");
            st.append(filename);
            JOptionPane.showMessageDialog(null,st.toString());
        }

        public String getFileLocation(){
           return dirName;
        }



    private static String getRandomNumber(int min ,int max){

        return String.valueOf((Math.random() * ((max-min)+1)+min));


    }


        private static char getRandomChar(){
            Random r = new Random();
            return (char)(r.nextInt(26) + 'a');
        }


    }





