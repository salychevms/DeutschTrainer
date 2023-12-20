package de.salychevms.deutschtrainer.Controllers;

import de.salychevms.deutschtrainer.Models.*;
import de.salychevms.deutschtrainer.Services.UserStatisticService;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class UserStatisticController {
    private final UserStatisticService userStatisticService;
    private final DeRuPairsController deRuPairsController;
    private final UserDictionaryController userDictionaryController;
    private final DeutschController deutschController;
    private final RussianController russianController;

    public UserStatisticController(UserStatisticService userStatisticService, DeRuPairsController deRuPairsController, UserDictionaryController userDictionaryController, DeutschController deutschController, RussianController russianController) {
        this.userStatisticService = userStatisticService;
        this.deRuPairsController = deRuPairsController;
        this.userDictionaryController = userDictionaryController;
        this.deutschController = deutschController;
        this.russianController = russianController;
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
        userStatisticService.updateAllIteration(userDictionary);
        userStatisticService.updateStatusNewWordByUserDictionary(userDictionary);
        userStatisticService.updateDayIteration(userDictionary);
        userStatisticService.updateMonthIteration(userDictionary);
        userStatisticService.updateWeekIteration(userDictionary);
    }

    public void updateAllFails(UserDictionary userDictionary){
        userStatisticService.updateAllFails(userDictionary);
        userStatisticService.updateDayFails(userDictionary);
        userStatisticService.updateWeekFails(userDictionary);
        userStatisticService.updateMonthFails(userDictionary);
    }
}
