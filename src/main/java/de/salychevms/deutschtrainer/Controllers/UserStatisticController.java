package de.salychevms.deutschtrainer.Controllers;

import de.salychevms.deutschtrainer.Models.*;
import de.salychevms.deutschtrainer.Services.UserStatisticService;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class UserStatisticController {
    private final UserStatisticService userStatisticService;
    private final UserDictionaryController userDictionaryController;
    private final DeRuPairsController deRuPairsController;
    private final DeutschController deutschController;
    private final RussianController russianController;

    public UserStatisticController(UserStatisticService userStatisticService, UserDictionaryController userDictionaryController, DeRuPairsController deRuPairsController, DeutschController deutschController, RussianController russianController) {
        this.userStatisticService = userStatisticService;
        this.userDictionaryController = userDictionaryController;
        this.deRuPairsController = deRuPairsController;
        this.deutschController = deutschController;
        this.russianController = russianController;
    }

    public List<UserStatistic> getAllStatisticWithNewWords(UserLanguage userLanguage) {
        List<UserStatistic> userStatistics = new ArrayList<>();
        List<UserStatistic> statistics = userStatisticService.getUserStatisticNewWordIsTrue();
        for (UserStatistic value : statistics) {
            Optional<UserDictionary> word = userDictionaryController.getById(value.getWord().getId());
            if (word.isPresent() && userLanguage.getId().equals(word.get().getUserLanguage().getId())) {
                userStatistics.add(value);
            }
        }
        return userStatistics;
    }

    public List<UserStatistic> getAllStatisticWithFailsAllDesc(UserLanguage userLanguage) {
        List<UserStatistic> userStatistics = new ArrayList<>();
        List<UserStatistic> statistics = userStatisticService.getUserStatisticSortByFailsAllDesc();
        for (UserStatistic value : statistics) {
            Optional<UserDictionary> word = userDictionaryController.getById(value.getWord().getId());
            if (word.isPresent() && userLanguage.getId().equals(word.get().getUserLanguage().getId())) {
                userStatistics.add(value);
            }
        }
        return userStatistics;
    }

    public List<UserStatistic> getAllStatisticWithIterationsAllAsc(UserLanguage userLanguage) {
        List<UserStatistic> userStatistics = new ArrayList<>();
        List<UserStatistic> statistics = userStatisticService.getUserStatisticSortByIterationAllAsc();
        for (UserStatistic value : statistics) {
            Optional<UserDictionary> word = userDictionaryController.getById(value.getWord().getId());
            if (word.isPresent() && userLanguage.getId().equals(word.get().getUserLanguage().getId())) {
                userStatistics.add(value);
            }
        }
        return userStatistics;
    }

    public Optional<UserStatistic> getUserStatisticByUserDictionary(UserDictionary userDictionary, UserLanguage userLanguage) {
        Optional<UserStatistic> statistic = userStatisticService.getUserStatisticByUserDictionary(userDictionary);
        if (statistic.isPresent()) {
            if (userDictionary.getUserLanguage().getId().equals(userLanguage.getId())) {
                return statistic;
            }
        }
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

    public String getBasicStatistic(Long telegramId) {
        String dateString = null;
        List<DeRuPairs> pairs = new ArrayList<>();
        int newWordsCounter = 0;
        int pairCounter = 0;
        int errorMax = 0;
        String maximumErrors = null;
        UserStatistic lastTrainingStatistic = new UserStatistic();
        Date currentDate = new Date();
        long closesDifference = Long.MAX_VALUE;

        List<UserDictionary> userDictionaries = userDictionaryController.getAllByTelegramId(telegramId);
        if (!userDictionaries.isEmpty()) {
            for (UserDictionary value : userDictionaries) {
                Optional<DeRuPairs> pair = deRuPairsController.getDeRuById(value.getPair().getId());
                pair.ifPresent(pairs::add);
                Optional<UserStatistic> userStatistic = userStatisticService.getUserStatisticByUserDictionary(value);
                if (userStatistic.isPresent()) {
                    pairCounter++;
                    if (userStatistic.get().isNewWord()) {
                        newWordsCounter++;
                    }
                    Long allFails = userStatistic.get().getFailsAll();
                    if (allFails != null && allFails >= errorMax) {
                        Optional<DeRuPairs> failPair = deRuPairsController.getDeRuById(value.getPair().getId());
                        if (failPair.isPresent()) {
                            Deutsch deutsch = deutschController.findById(failPair.get().getDeutsch().getId());
                            Russian russian = russianController.findById(failPair.get().getRussian().getId());
                            maximumErrors = deutsch.getDeWord() + " - " + russian.getRuWord();
                        }
                        errorMax = Math.toIntExact(userStatistic.get().getFailsAll());
                    }
                    Date tempDate = userStatistic.get().getLastTraining();
                    if (tempDate.before(currentDate)) {
                        long difference = Math.abs(TimeUnit.DAYS.convert(
                                currentDate.getTime() - tempDate.getTime(), TimeUnit.MILLISECONDS));

                        if (difference < closesDifference) {
                            closesDifference = difference;
                            lastTrainingStatistic = userStatistic.get();
                        }
                    }
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
                    if (dateFormat.format(lastTrainingStatistic.getLastTraining()).equals(dateFormat.format(new Date()))) {
                        dateString = "сегодня";
                    } else if (dateFormat.format(lastTrainingStatistic.getLastTraining())
                            .equals(dateFormat.format(new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000)))) {
                        dateString = "вчера";
                    } else {
                        dateString = dateFormat.format(lastTrainingStatistic.getLastTraining());
                    }
                }
            }
        }
        List<DeRuPairs> uniquePairs = pairs.stream()
                .collect(Collectors.toMap(DeRuPairs::getDeutsch, Function.identity(), (existing, replacement) -> existing))
                .values().stream().toList();
        int wordCounter = uniquePairs.size();
        return "В Вашем словаре " + wordCounter + " немецких уникальных слов.\n" +
                "С этими словами у Вас " + pairCounter + " пар.\n" +
                "Из них " + newWordsCounter + " новых пар, которые вам еще предстоит учить.\n" +
                "Пара слов, в которой вы чаще всего ошибаетесь:\n" + maximumErrors +
                "\nКоличество ошибок с этой парой = " + errorMax +
                "\nВаша последняя тренировка была: " + dateString + "\n\n";
    }

    public void setFailStatusTrue(UserDictionary userDictionary){
        userStatisticService.setFailStatusTrue(userDictionary);
    }

    public void setFailStatusFalse(UserDictionary userDictionary){
        userStatisticService.setFailStatusFalse(userDictionary);
    }

    public void decreaseFailTraining(UserDictionary userDictionary){
        userStatisticService.decreaseFailTraining(userDictionary);
    }
}
