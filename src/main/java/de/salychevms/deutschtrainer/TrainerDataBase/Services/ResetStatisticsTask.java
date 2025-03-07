package de.salychevms.deutschtrainer.TrainerDataBase.Services;

import de.salychevms.deutschtrainer.TrainerDataBase.Models.UserStatistic;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ResetStatisticsTask {
    private final UserStatisticService userStatisticService;

    public ResetStatisticsTask(UserStatisticService userStatisticService) {
        this.userStatisticService = userStatisticService;
    }

    @Scheduled(cron = "0 0 9 * * *")
    public void dailyReset (){
        List<UserStatistic> allStatistic=userStatisticService.findAll();
        if(!allStatistic.isEmpty()){
            for(UserStatistic item:allStatistic){
                userStatisticService.deleteDayIteration(item.getWord());
            }
        }
    }

    @Scheduled(cron = "0 0 9 * * MON")
    public void weeklyReset (){
        List<UserStatistic> allStatistic=userStatisticService.findAll();
        if(!allStatistic.isEmpty()){
            for(UserStatistic item:allStatistic){
                userStatisticService.deleteWeekIteration(item.getWord());
            }
        }
    }

    @Scheduled(cron = "0 0 9 1 * *")
    public void monthlyReset (){
        List<UserStatistic> allStatistic=userStatisticService.findAll();
        if(!allStatistic.isEmpty()){
            for(UserStatistic item:allStatistic){
                userStatisticService.deleteMonthIteration(item.getWord());
            }
        }
    }
}
