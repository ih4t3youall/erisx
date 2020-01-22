package ar.com.erisx;

import java.util.Map;
import java.util.TimerTask;
import java.util.stream.Collectors;

public class CronStatistics extends TimerTask {

    private Map<String,Statistics> statisticsMap;
    private int totalMessages = 0;
    private long totalTime  = 0;

    public CronStatistics(Map<String,Statistics> statisticsMap){
        this.statisticsMap = statisticsMap;
    }

    @Override
    public void run() {
        Map<String,Statistics> filteredMap = statisticsMap.entrySet().stream()
                .filter(x -> x.getValue().getFinalTime() != 0)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        filteredMap.forEach((k,v)->{
            this.totalMessages = this.totalMessages + v.getMessagesProcessed();
            this.totalTime = this.totalTime  + v.getFinalTime();
        });

        System.out.println("received "+ this.totalMessages+ " in "+this.totalTime  + " milliseconds");

        filteredMap.entrySet().forEach(x->{
           statisticsMap.remove(x.getKey());
        });

    }
}
