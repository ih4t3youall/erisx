package ar.com.erisx;

import java.util.LinkedList;
import java.util.List;

public class CheckMessage {


    private int cont = 0;
    private List<String> messages = new LinkedList<String>();
    private StringBuilder stringBuilder = new StringBuilder();

    public List<String> check(String message){


        messages.clear();
        for(char c : message.toCharArray()){

            stringBuilder.append(c);
            if(c == '|'){
                cont ++;
            }
            if(cont == 16){
                messages.add(stringBuilder.toString());
                stringBuilder = new StringBuilder();
                cont =0;

            }


        }
        return messages;


    }



}
