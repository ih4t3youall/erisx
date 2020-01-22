package ar.com.erisx;

import java.util.LinkedList;
import java.util.List;

public class CheckMessage {


    private int cont = 0;
    private List<String> messages = new LinkedList<String>();
    private StringBuilder stringBuilder = new StringBuilder();
    public List<String> check(String message){

        int divider = message.indexOf("|");
        while(divider  != -1) {
            String string = message.substring(0, divider);
            stringBuilder.append(string + "|");
            message = message.substring(divider+1);
            divider = message.indexOf("|");
            cont++;
            if(cont == 16){
                messages.add(stringBuilder.toString());
                cont=0;
                stringBuilder= new StringBuilder();
            }
        }
        return messages;


    }

}
