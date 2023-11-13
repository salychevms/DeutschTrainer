package de.salychevms.deutschtrainer.Training;

import de.salychevms.deutschtrainer.Models.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TrainingPair {
    private DeRu compareWith;
    private UserDictionary userPair;
    private UserStatistic userStatistic;
    private Deutsch german;
    private Russian russian;

    public TrainingPair() {
    }

    public TrainingPair(UserDictionary userPair, DeRu compareWith, UserStatistic userStatistic, Deutsch german, Russian russian) {
        this.compareWith = compareWith;
        this.userPair = userPair;
        this.userStatistic = userStatistic;
        this.german = german;
        this.russian = russian;
    }
}
