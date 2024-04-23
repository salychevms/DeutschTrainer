package de.salychevms.deutschtrainer.DataExchange.Classes;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class UserPairStatisticInfoClass extends BasicPairStatisticInfoClass {
    private Long telegramId;
    private Long userLanguageId;
    private Long userDictionaryId;
    private Long pairId;
    private Long deId;
    private Long ruId;
    private Date dateAdded;
    private String deWord;
    private String ruWord;

    public UserPairStatisticInfoClass() {
    }

    public UserPairStatisticInfoClass(Long telegramId, Long userLanguageId, Long userDictionaryId, Long pairId, Long deId, Long ruId, Date dateAdded, String deWord, String ruWord) {
        this.telegramId = telegramId;
        this.userLanguageId = userLanguageId;
        this.userDictionaryId = userDictionaryId;
        this.pairId = pairId;
        this.deId = deId;
        this.ruId = ruId;
        this.dateAdded = dateAdded;
        this.deWord = deWord;
        this.ruWord = ruWord;
    }

    public UserPairStatisticInfoClass(Long word, Long statisticId, boolean isNewWord, Date lastTraining, Long failsAll, Long failsPerDay, Long failsPerWeek, Long failsPerMonth, Long iterationsAll, Long iterationsPerDay, Long iterationsPerWeek, Long iterationsPerMonth, Long telegramId, Long userLanguageId, Long userDictionaryId, Long pairId, Long deId, Long ruId, Date dateAdded, String deWord, String ruWord) {
        super(word, statisticId, isNewWord, lastTraining, failsAll, failsPerDay, failsPerWeek, failsPerMonth, iterationsAll, iterationsPerDay, iterationsPerWeek, iterationsPerMonth);
        this.telegramId = telegramId;
        this.userLanguageId = userLanguageId;
        this.userDictionaryId = userDictionaryId;
        this.pairId = pairId;
        this.deId = deId;
        this.ruId = ruId;
        this.dateAdded = dateAdded;
        this.deWord = deWord;
        this.ruWord = ruWord;
    }

    public UserPairStatisticInfoClass(BasicPairStatisticInfoClass pairStatisticInfo) {
        super.setStatisticId(pairStatisticInfo.getWord());
        super.setStatisticId(pairStatisticInfo.getStatisticId());
        super.setNewWord(pairStatisticInfo.isNewWord());
        super.setLastTraining(pairStatisticInfo.getLastTraining());
        super.setFailsAll(pairStatisticInfo.getFailsAll());
        super.setFailsPerDay(pairStatisticInfo.getFailsPerDay());
        super.setFailsPerWeek(pairStatisticInfo.getFailsPerWeek());
        super.setFailsPerMonth(pairStatisticInfo.getFailsPerMonth());
        super.setIterationsAll(pairStatisticInfo.getIterationsAll());
        super.setIterationsPerDay(pairStatisticInfo.getIterationsPerDay());
        super.setIterationsPerWeek(pairStatisticInfo.getIterationsPerWeek());
        super.setIterationsPerMonth(pairStatisticInfo.getIterationsPerMonth());
    }
}
