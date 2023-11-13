package de.salychevms.deutschtrainer.Services;

import de.salychevms.deutschtrainer.Models.UserStatistic;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ResetStatisticsTask {
    private final UserStatisticService userStatisticService;

    public ResetStatisticsTask(UserStatisticService userStatisticService) {
        this.userStatisticService = userStatisticService;
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void dailyReset (){
        List<UserStatistic> allStatistic=userStatisticService.findAll();
        if(!allStatistic.isEmpty()){
            for(UserStatistic item:allStatistic){
                userStatisticService.deleteDayIteration(item.getWord());
            }
        }
    }

    @Scheduled(cron = "0 0 0 * * MON")
    public void weeklyReset (){
        List<UserStatistic> allStatistic=userStatisticService.findAll();
        if(!allStatistic.isEmpty()){
            for(UserStatistic item:allStatistic){
                userStatisticService.deleteWeekIteration(item.getWord());
            }
        }
    }

    @Scheduled(cron = "0 0 0 1 * *")
    public void monthlyReset (){
        List<UserStatistic> allStatistic=userStatisticService.findAll();
        if(!allStatistic.isEmpty()){
            for(UserStatistic item:allStatistic){
                userStatisticService.deleteMonthIteration(item.getWord());
            }
        }
    }
}
