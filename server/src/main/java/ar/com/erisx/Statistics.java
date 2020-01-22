package ar.com.erisx;

import java.util.concurrent.TimeUnit;

public class Statistics {


    private long startTime;
    private long endTime;
    private long finalTime;
    private int messagesProcessed;

    public  Statistics(){
        this.startTime = System.currentTimeMillis();
        this.messagesProcessed =0;
        System.out.println("statistics created");
    }

    public void end(){
        this.finalTime =  System.currentTimeMillis() -this.startTime   ;
        this.printStatistics();
    }

    public long getFinalTime(){
        return this.finalTime;
    }

    public void messagesProcessed(int cant){
        this.messagesProcessed =this.messagesProcessed+ cant;
    }

    public void printStatistics(){
        float sec = (finalTime) / 1000F;
        System.out.println("Proccessed "+this.messagesProcessed +" in "+ sec +" seconds");
    }

    public int getMessagesProcessed(){
        return this.messagesProcessed;
    }

}
