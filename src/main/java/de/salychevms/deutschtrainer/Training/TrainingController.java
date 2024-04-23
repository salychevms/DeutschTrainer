package de.salychevms.deutschtrainer.Training;

import de.salychevms.deutschtrainer.Controllers.*;
import de.salychevms.deutschtrainer.Emojies.EmojiGive;
import de.salychevms.deutschtrainer.Models.*;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.*;

@Component
public class TrainingController {
    private final UserStatisticController userStatisticController;
    private final DeRuPairsController deRuPairsController;
    private final UserDictionaryController userDictionaryController;
    private final DeutschController deutschController;
    private final RussianController russianController;
    private final UserLanguageController userLanguageController;
    private final UsersController usersController;
    private final LanguageController languageController;

    public TrainingController(UserStatisticController userStatisticController, DeRuPairsController deRuPairsController, UserDictionaryController userDictionaryController, DeutschController deutschController, RussianController russianController, UserLanguageController userLanguageController, UsersController usersController, LanguageController languageController) {
        this.userStatisticController = userStatisticController;
        this.deRuPairsController = deRuPairsController;
        this.userDictionaryController = userDictionaryController;
        this.deutschController = deutschController;
        this.russianController = russianController;
        this.userLanguageController = userLanguageController;
        this.usersController = usersController;
        this.languageController = languageController;
    }

    private TrainingPair createNewPair(UserStatistic statisticInfo, UserDictionary userPair, DeRuPairs compareWith, Deutsch german, Russian russian) {
        return new TrainingPair(userPair, compareWith, statisticInfo, german, russian);
    }

    public List<TrainingPair> createTrainingRepeatList(Long telegramId) {
        List<TrainingPair> trainingPairList = new ArrayList<>();
        Optional<Users> user = usersController.findUserByTelegramId(telegramId);
        Optional<Language> language = languageController.getLanguageByIdentifier("DE");
        Optional<UserLanguage> userLanguage;
        Long userLanguageId;
        if (user.isPresent() && language.isPresent()) {
            userLanguage = userLanguageController.getByUserIdAndLanguageId(user.get().getTelegramId(), language.get().getId());
            if (userLanguage.isPresent()) {
                userLanguageId = userLanguage.get().getId();
                List<UserStatistic> statisticList = userStatisticController.getAllStatisticWithIterationsAllAsc(userLanguage.get());
                for (UserStatistic statisticItem : statisticList) {
                    Long statisticLanguageId = getUserLangByUserStatistic(statisticItem).getId();
                    if (statisticLanguageId.equals(userLanguageId)) {
                        Optional<UserDictionary> userPair = userDictionaryController.getById(statisticItem.getWord().getId());
                        if (userPair.isPresent()) {
                            Optional<DeRuPairs> compareWith = deRuPairsController.getDeRuById(userPair.get().getPair().getId());
                            if (compareWith.isPresent()) {
                                Deutsch german = deutschController.findById(compareWith.get().getDeutsch().getId());
                                Russian russian = russianController.findById(compareWith.get().getRussian().getId());
                                trainingPairList.add(createNewPair(statisticItem, userPair.get(), compareWith.get(), german, russian));
                            }
                        }
                    }
                }
            }
        }
        Collections.shuffle(trainingPairList);
        if (trainingPairList.size() >= 50) {
            trainingPairList = trainingPairList.subList(0, 49);
        }
        return trainingPairList;
    }

    public List<TrainingPair> createTrainingFailList(Long telegramId) {
        List<TrainingPair> trainingFailList = new ArrayList<>();
        Optional<Users> user = usersController.findUserByTelegramId(telegramId);
        Optional<Language> language = languageController.getLanguageByIdentifier("DE");
        Optional<UserLanguage> userLanguage;
        Long userLanguageId;
        if (user.isPresent() && language.isPresent()) {
            userLanguage = userLanguageController.getByUserIdAndLanguageId(user.get().getTelegramId(), language.get().getId());
            if (userLanguage.isPresent()) {
                userLanguageId = userLanguage.get().getId();
                List<UserStatistic> statisticList = userStatisticController.getAllStatisticWithFailsAllDesc(userLanguage.get());
                for (UserStatistic statisticItem : statisticList) {
                    if (statisticItem.getFailsPerDay() != null || statisticItem.getFailsPerWeek() != null || statisticItem.getFailsPerMonth() != null) {
                        Long statisticLanguageId = getUserLangByUserStatistic(statisticItem).getId();
                        if (statisticLanguageId.equals(userLanguageId)) {
                            Optional<UserDictionary> userPair = userDictionaryController.getById(statisticItem.getWord().getId());
                            if (userPair.isPresent()) {
                                Optional<DeRuPairs> compareWith = deRuPairsController.getDeRuById(userPair.get().getPair().getId());
                                if (compareWith.isPresent()) {
                                    Deutsch german = deutschController.findById(compareWith.get().getDeutsch().getId());
                                    Russian russian = russianController.findById(compareWith.get().getRussian().getId());
                                    trainingFailList.add(createNewPair(statisticItem, userPair.get(), compareWith.get(), german, russian));
                                }
                            }
                        }
                    }
                }
            }
        }
        Collections.shuffle(trainingFailList);
        if (trainingFailList.size() >= 50) {
            trainingFailList = trainingFailList.subList(0, 49);
        }
        return trainingFailList;
    }

    public List<TrainingPair> createLearningList(Long telegramId) {
        List<TrainingPair> learningPairList = new ArrayList<>();
        Optional<Users> user = usersController.findUserByTelegramId(telegramId);
        Optional<Language> language = languageController.getLanguageByIdentifier("DE");
        Optional<UserLanguage> userLanguage;
        Long userLanguageId;
        if (user.isPresent() && language.isPresent()) {
            userLanguage = userLanguageController.getByUserIdAndLanguageId(user.get().getTelegramId(), language.get().getId());
            if (userLanguage.isPresent()) {
                userLanguageId = userLanguage.get().getId();
                List<UserStatistic> statisticList = userStatisticController.getAllStatisticWithNewWords(userLanguage.get());
                if (!statisticList.isEmpty()) {
                    for (UserStatistic statisticItem : statisticList) {
                        Long statisticLanguageId = getUserLangByUserStatistic(statisticItem).getId();
                        if (statisticLanguageId.equals(userLanguageId)) {
                            Optional<UserDictionary> userPair = userDictionaryController.getById(statisticItem.getWord().getId());
                            if (userPair.isPresent()) {
                                Optional<DeRuPairs> compareWith = deRuPairsController.getDeRuById(userPair.get().getPair().getId());
                                if (compareWith.isPresent()) {
                                    Deutsch german = deutschController.findById(compareWith.get().getDeutsch().getId());
                                    Russian russian = russianController.findById(compareWith.get().getRussian().getId());
                                    learningPairList.add(createNewPair(statisticItem, userPair.get(), compareWith.get(), german, russian));
                                }
                            }
                        }
                    }
                }
            }
        }
        Collections.shuffle(learningPairList);
        if (learningPairList.size() >= 25) {
            learningPairList = learningPairList.subList(0, 24);
        }
        return learningPairList;
    }

    public InlineKeyboardMarkup getTestKeyboard(TrainingPair pair, String answer) {
        List<Russian> wrongAnswers;
        wrongAnswers = random3(pair);
        Random random = new Random();
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        //position from up to down
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        while (!wrongAnswers.isEmpty()) {
            int randomIndex = random.nextInt(wrongAnswers.size());
            Russian ru = wrongAnswers.get(randomIndex);
            InlineKeyboardButton testButton = new InlineKeyboardButton(ru.getRuWord());
            testButton.setCallbackData(answer + ru.getId() + "%" + pair.getCompareWith().getId());
            List<InlineKeyboardButton> row = new ArrayList<>();
            row.add(testButton);
            rows.add(row);
            wrongAnswers.remove(randomIndex);
        }
        //button
        InlineKeyboardButton goToMainMenuButton = new InlineKeyboardButton("=Прервать тренировку=");
        goToMainMenuButton.setCallbackData("/tr=/FAT");// /tr/FAT - training/FinishAllTrainings
        //position from left to right
        List<InlineKeyboardButton> row = new ArrayList<>();
        //
        row.add(goToMainMenuButton);
        rows.add(row);
        //save buttons in the markup variable
        keyboardMarkup.setKeyboard(rows);
        return keyboardMarkup;
    }

    public String getAnswerFromUser(String callBackData) {
        String answerToUser = null;
        int startIndex = callBackData.indexOf("DRI=:");
        Long userAnswerId = null;
        Long correctPairId = null;
        if (startIndex != -1) {
            String answer = callBackData.substring(startIndex + "DRI=:".length());
            String[] parts = answer.split("%");
            userAnswerId = Long.parseLong(parts[0]);
            correctPairId = Long.parseLong(parts[1]);
        }
        if (userAnswerId != null) {
            Optional<Russian> ruCorrect = deRuPairsController
                    .getDeRuById(correctPairId).flatMap(deRu -> Optional.ofNullable(russianController.findById(deRu.getRussian().getId())));
            Russian ruFromUser = russianController.findById(userAnswerId);
            Optional<Deutsch> german = deRuPairsController
                    .getDeRuById(correctPairId).flatMap(deRu -> Optional.ofNullable(deutschController.findById(deRu.getDeutsch().getId())));
            if (ruCorrect.isPresent() && german.isPresent()) {
                if (userAnswerId.equals(ruCorrect.get().getId())) {
                    Optional<UserDictionary> forStatistic = userDictionaryController.getUserDictionaryByPairId(correctPairId);
                    forStatistic.ifPresent(userStatisticController::updateAllIterationsAndNewWordStatus);
                    answerToUser = "***** ОТЛИЧНО!!! *****\n\n"
                            + german.get().getDeWord() + " --> " + ruCorrect.get().getRuWord()
                            + "\n\n*******************\n"
                            + EmojiGive.greyCheck;
                } else {
                    Optional<UserDictionary> forStatistic = userDictionaryController.getUserDictionaryByPairId(correctPairId);
                    forStatistic.ifPresent(userStatisticController::updateAllIterationsAndNewWordStatus);
                    forStatistic.ifPresent(userStatisticController::updateAllFails);
                    answerToUser = "***** ОШИБКА!!! *****"
                            + "\n\nВы выбрали: " + ruFromUser.getRuWord()
                            + "\n\n--- Верный ответ ---\n\n"
                            + german.get().getDeWord() + " --> " + ruCorrect.get().getRuWord()
                            + "\n\n********************\n"
                            + EmojiGive.redCross;
                }
            }
        }
        return answerToUser;
    }

    private List<Russian> random3(TrainingPair pair) {
        List<Russian> wrongAnswers = new ArrayList<>();
        wrongAnswers.add(pair.getRussian());
        //max 3 wrong words
        Optional<UserDictionary> userDictionary = userDictionaryController.getById(pair.getUserPair().getId());
        Optional<DeRuPairs> deRu;
        Optional<Russian> russian = Optional.empty();
        Optional<Deutsch> german = Optional.empty();
        if (userDictionary.isPresent()) {
            deRu = deRuPairsController.getDeRuById(userDictionary.get().getPair().getId());
            if (deRu.isPresent()) {
                russian = Optional.ofNullable(russianController.findById(deRu.get().getRussian().getId()));
                german = Optional.ofNullable(deutschController.findById(deRu.get().getDeutsch().getId()));
            }
        }
        while (wrongAnswers.size() < 4) {
            Optional<Russian> randomRu = russianController.get1RandomRussian();
            if (randomRu.isPresent() && russian.isPresent() && german.isPresent()) {
                if (!randomRu.get().getId().equals(russian.get().getId())) {
                    Optional<DeRuPairs> check = deRuPairsController.getPairByGermanIdAndRussianId(german.get().getId(), randomRu.get().getId());
                    if (check.isEmpty()) {
                        if (!wrongAnswers.contains(randomRu.get())) {
                            wrongAnswers.add(randomRu.get());
                        }
                    }
                }
            }
        }
        return wrongAnswers;
    }

    public UserLanguage getUserLangByTgIdAndLangIdentifier(Long telegramId, String languageIdentifier) {
        Optional<Users> user = usersController.findUserByTelegramId(telegramId);
        Optional<Language> language = languageController.getLanguageByIdentifier(languageIdentifier);
        Optional<UserLanguage> userLanguage;
        if (user.isPresent() && language.isPresent()) {
            userLanguage = userLanguageController.getByUserIdAndLanguageId(user.get().getTelegramId(), language.get().getId());
            return userLanguage.orElse(null);
        } else return null;
    }

    public UserLanguage getUserLangByUserStatistic(UserStatistic userStatistic) {
        Optional<UserDictionary> userDictionary = userDictionaryController.getById(userStatistic.getWord().getId());
        if (userDictionary.isPresent()) {
            Optional<UserLanguage> userLanguage = userLanguageController.getById(userDictionary.get().getUserLanguage().getId());
            return userLanguage.orElse(null);
        }
        return null;
    }
}
