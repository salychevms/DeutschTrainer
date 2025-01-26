package de.salychevms.deutschtrainer.TelegramBot.DataExchange.Classes;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class BasicPairStatisticInfoClass {
    private Long word;
    private Long statisticId;
    private boolean isNewWord;
    private Date lastTraining;
    private Long failsAll;
    private Long failsPerDay;
    private Long failsPerWeek;
    private Long failsPerMonth;
    private Long iterationsAll;
    private Long iterationsPerDay;
    private Long iterationsPerWeek;
    private Long iterationsPerMonth;

    public BasicPairStatisticInfoClass() {
    }

    public BasicPairStatisticInfoClass(Long word, Long statisticId, boolean isNewWord, Date lastTraining, Long failsAll, Long failsPerDay, Long failsPerWeek, Long failsPerMonth, Long iterationsAll, Long iterationsPerDay, Long iterationsPerWeek, Long iterationsPerMonth) {
        this.word = word;
        this.statisticId = statisticId;
        this.isNewWord = isNewWord;
        this.lastTraining = lastTraining;
        this.failsAll = failsAll;
        this.failsPerDay = failsPerDay;
        this.failsPerWeek = failsPerWeek;
        this.failsPerMonth = failsPerMonth;
        this.iterationsAll = iterationsAll;
        this.iterationsPerDay = iterationsPerDay;
        this.iterationsPerWeek = iterationsPerWeek;
        this.iterationsPerMonth = iterationsPerMonth;
    }
}
