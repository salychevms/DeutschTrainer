package de.salychevms.deutschtrainer.Controllers;

import de.salychevms.deutschtrainer.Models.*;
import de.salychevms.deutschtrainer.Services.UserStatisticService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class UserStatisticController {
    private final UserStatisticService userStatisticService;

    public UserStatisticController(UserStatisticService userStatisticService) {
        this.userStatisticService = userStatisticService;

    }

    public List<UserStatistic> getAllStatisticWithNewWords() {
        return userStatisticService.getUserStatisticNewWordIsTrue();
    }

    public List<UserStatistic> getAllStatisticWithFailsAllDesc() {
        return userStatisticService.getUserStatisticSortByFailsAllDesc();
    }

    public List<UserStatistic> getAllStatisticWithIterationsAllAsc() {
        return userStatisticService.getUserStatisticSortByIterationAllAsc();
    }

    public Optional<UserStatistic> getUserStatisticByUserDictionary(UserDictionary userDictionary, UserLanguage userLanguage) {
        Optional<UserStatistic> statistic = userStatisticService.getUserStatisticByWord(userDictionary);
        if (statistic.isPresent() && userDictionary.getUserLanguage().equals(userLanguage))
            return statistic;
        return Optional.empty();
    }

    public void saveNewPairInStatistic(UserDictionary userDictionary) {
        userStatisticService.saveNewWordInStatistic(userDictionary);
    }

    public void updateAllIterationsAndNewWordStatus(UserDictionary userDictionary) {
        userStatisticService.updateLastTraining(userDictionary);
        userStatisticService.updateAllIteration(userDictionary);
        userStatisticService.updateStatusNewWordByUserDictionary(userDictionary);
        userStatisticService.updateDayIteration(userDictionary);
        userStatisticService.updateMonthIteration(userDictionary);
        userStatisticService.updateWeekIteration(userDictionary);
    }

    public void updateAllFails(UserDictionary userDictionary) {
        userStatisticService.updateAllFails(userDictionary);
        userStatisticService.updateDayFails(userDictionary);
        userStatisticService.updateWeekFails(userDictionary);
        userStatisticService.updateMonthFails(userDictionary);
    }
}
