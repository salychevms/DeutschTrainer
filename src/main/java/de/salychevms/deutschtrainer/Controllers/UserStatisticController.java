package de.salychevms.deutschtrainer.Controllers;

import de.salychevms.deutschtrainer.Models.*;
import de.salychevms.deutschtrainer.Services.UserStatisticService;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;
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
        String dateString;
        int failsCount = 0;
        Date tempDate = userStatisticService.findLastTrainingForUserAndLanguage(telegramId, "DE");
        int countUniqueDEWords = userDictionaryController.getCountUniqueGermanWordsForTelegramIdAndLanguageIdentifier(telegramId, "DE");
        int countUserPairs = userDictionaryController.getCountPairsForUserAndLanguageIdentifier(telegramId, "DE");
        int countWordsToLearn = userStatisticService.getCountPairsWithNewWordForUserAndLanguage(telegramId, "DE");
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        if (dateFormat.format(tempDate).equals(dateFormat.format(new Date()))) {
            dateString = "сегодня";
        } else if (dateFormat.format(tempDate)
                .equals(dateFormat.format(new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000)))) {
            dateString = "вчера";
        } else {
            dateString = dateFormat.format(tempDate);
        }
        StringBuilder pairsString = new StringBuilder();
        List<UserStatistic> failedUserStatistic = userStatisticService.findWordsWithMaxFailsAllForUserAndLanguageIdentifier(telegramId, "DE");
        if (!failedUserStatistic.isEmpty()) {
            if (failedUserStatistic.size() >= 5) {
                failedUserStatistic = failedUserStatistic.subList(0, 5);
            }
            failsCount = Math.toIntExact(failedUserStatistic.get(0).getFailsAll());
            for (UserStatistic statistic : failedUserStatistic) {
                Optional<UserDictionary> userDictionary = userDictionaryController.getById(statistic.getWord().getId());
                if (userDictionary.isPresent()) {
                    Optional<DeRuPairs> pair = deRuPairsController.getDeRuById(userDictionary.get().getPair().getId());
                    if (pair.isPresent()) {
                        Deutsch deutsch = deutschController.findById(pair.get().getDeutsch().getId());
                        Russian russian = russianController.findById(pair.get().getRussian().getId());
                        String ruWord = russian.getRuWord();
                        String deWord = deutsch.getDeWord();
                        String concat = deWord + " - " + ruWord + "\n";
                        pairsString.append("\n").append(concat);
                    }
                }
            }
        } else {
            pairsString = new StringBuilder(("\n*** у вас пока нет статистики по тренировкам ***\n"));
        }
        return "В Вашем словаре " + countUniqueDEWords + " немецких уникальных слов.\n" +
                "\nС этими словами у Вас " + countUserPairs + " пар.\n" +
                "\nИз них " + countWordsToLearn + " новых пар, которые вам еще предстоит учить.\n" +
                "\nПара(ы) слов, в которой(ых) вы чаще всего ошибаетесь:\n" + pairsString +
                "\nКоличество ошибок с этой парой = " + failsCount +
                "\n\nВаша последняя тренировка была: " + dateString + "\n\n";
    }

    public void setFailStatusTrue(UserDictionary userDictionary) {
        userStatisticService.updateDayFails(userDictionary);
    }

    public void setFailStatusFalse(UserDictionary userDictionary) {
        userStatisticService.setFailStatusFalse(userDictionary);
    }

    public void decreaseFailTraining(UserDictionary userDictionary) {
        userStatisticService.decreaseFailTraining(userDictionary);
    }

    public int getCountPairsWithNewWordForUserAndLanguage(Long telegramId, String languageIdentifier) {
        return userStatisticService.getCountPairsWithNewWordForUserAndLanguage(telegramId, languageIdentifier);
    }

    public int countWordsWithFailStatusForUserAndLanguage(Long telegramId, String languageIdentifier){
        return userStatisticService.countWordsWithFailStatusForUserAndLanguage(telegramId, languageIdentifier);
    }
}
