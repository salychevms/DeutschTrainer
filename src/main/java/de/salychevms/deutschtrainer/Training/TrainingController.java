package de.salychevms.deutschtrainer.Training;

import de.salychevms.deutschtrainer.Controllers.*;
import de.salychevms.deutschtrainer.Emojies.EmojiGive;
import de.salychevms.deutschtrainer.Models.*;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.*;

@RestController
public class TrainingController {
    private final UserStatisticController userStatisticController;
    private final DeRuController deRuController;
    private final UserDictionaryController userDictionaryController;
    private final DeutschController deutschController;
    private final RussianController russianController;
    private final UserLanguageController userLanguageController;
    private final UsersController usersController;
    private final LanguageController languageController;

    public TrainingController(UserStatisticController userStatisticController, DeRuController deRuController, UserDictionaryController userDictionaryController, DeutschController deutschController, RussianController russianController, UserLanguageController userLanguageController, UsersController usersController, LanguageController languageController) {
        this.userStatisticController = userStatisticController;
        this.deRuController = deRuController;
        this.userDictionaryController = userDictionaryController;
        this.deutschController = deutschController;
        this.russianController = russianController;
        this.userLanguageController = userLanguageController;
        this.usersController = usersController;
        this.languageController = languageController;
    }

    private TrainingPair createNewPair(UserStatistic statisticInfo, UserDictionary userPair, DeRu compareWith, Deutsch german, Russian russian) {
        return new TrainingPair(userPair, compareWith, statisticInfo, german, russian);
    }

    public List<TrainingPair> createTrainingRepeatList(Long telegramId) {
        List<TrainingPair> trainingPairList = new ArrayList<>();
        Optional<Users> user = usersController.findUserByTelegramId(telegramId);
        Optional<Language> language = languageController.getLanguageByIdentifier("DE");
        Optional<UserLanguage> userLanguage;
        Long userLanguageId;
        if (user.isPresent() && language.isPresent()) {
            userLanguage = userLanguageController.getByUserIdAndLanguageId(user.get().getId(), language.get().getId());
            if (userLanguage.isPresent()) {
                userLanguageId = userLanguage.get().getId();
                List<UserStatistic> statisticList = userStatisticController.getAllStatisticWithIterationsAllAsc();
                for (UserStatistic statisticItem : statisticList) {
                    Long statisticLanguageId = getUserLangByUserStatistic(statisticItem).getId();
                    if (statisticLanguageId.equals(userLanguageId)) {
                        Optional<UserDictionary> userPair = userDictionaryController.getById(statisticItem.getWord().getId());
                        if (userPair.isPresent()) {
                            Optional<DeRu> compareWith = deRuController.getDeRuById(userPair.get().getPair().getId());
                            if (compareWith.isPresent()) {
                                Deutsch german = deutschController.findById(compareWith.get().getDeutschId());
                                Russian russian = russianController.findById(compareWith.get().getRussianId());
                                trainingPairList.add(createNewPair(statisticItem, userPair.get(), compareWith.get(), german, russian));
                            }
                        }
                    }
                }
            }
        }
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
            userLanguage = userLanguageController.getByUserIdAndLanguageId(user.get().getId(), language.get().getId());
            if (userLanguage.isPresent()) {
                userLanguageId = userLanguage.get().getId();
                List<UserStatistic> statisticList = userStatisticController.getAllStatisticWithFailsAllDesc();
                for (UserStatistic statisticItem : statisticList) {
                    if (statisticItem.getFailsAll() != null) {
                        Long statisticLanguageId = getUserLangByUserStatistic(statisticItem).getId();
                        if (statisticLanguageId.equals(userLanguageId)) {
                            Optional<UserDictionary> userPair = userDictionaryController.getById(statisticItem.getWord().getId());
                            if (userPair.isPresent()) {
                                Optional<DeRu> compareWith = deRuController.getDeRuById(userPair.get().getPair().getId());
                                if (compareWith.isPresent()) {
                                    Deutsch german = deutschController.findById(compareWith.get().getDeutschId());
                                    Russian russian = russianController.findById(compareWith.get().getRussianId());
                                    trainingFailList.add(createNewPair(statisticItem, userPair.get(), compareWith.get(), german, russian));
                                }
                            }
                        }
                    }
                }
            }
        }
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
            userLanguage = userLanguageController.getByUserIdAndLanguageId(user.get().getId(), language.get().getId());
            if (userLanguage.isPresent()) {
                userLanguageId = userLanguage.get().getId();
                List<UserStatistic> statisticList = userStatisticController.getAllStatisticWithNewWords();
                for (UserStatistic statisticItem : statisticList) {
                    Long statisticLanguageId = getUserLangByUserStatistic(statisticItem).getId();
                    if (statisticLanguageId.equals(userLanguageId)) {
                        Optional<UserDictionary> userPair = userDictionaryController.getById(statisticItem.getWord().getId());
                        if (userPair.isPresent()) {
                            Optional<DeRu> compareWith = deRuController.getDeRuById(userPair.get().getPair().getId());
                            if (compareWith.isPresent()) {
                                Deutsch german = deutschController.findById(compareWith.get().getDeutschId());
                                Russian russian = russianController.findById(compareWith.get().getRussianId());
                                learningPairList.add(createNewPair(statisticItem, userPair.get(), compareWith.get(), german, russian));
                            }
                        }
                    }
                }
            }
        }
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
            InlineKeyboardButton testButton = new InlineKeyboardButton(ru.getWord());
            testButton.setCallbackData(answer + ru.getId() + "%" + pair.getCompareWith().getId());
            List<InlineKeyboardButton> row = new ArrayList<>();
            row.add(testButton);
            rows.add(row);
            wrongAnswers.remove(randomIndex);
        }
        //button
        InlineKeyboardButton goToMainMenuButton = new InlineKeyboardButton("=abort training=");
        goToMainMenuButton.setCallbackData("/Trainings=/FinishAllTrainings");
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
        int startIndex = callBackData.indexOf("DeRuId=:");
        Long userAnswerId = null;
        Long correctPairId = null;
        if (startIndex != -1) {
            String answer = callBackData.substring(startIndex + "DeRuId=:".length());
            String[] parts = answer.split("%");
            userAnswerId = Long.parseLong(parts[0]);
            correctPairId = Long.parseLong(parts[1]);
        }
        if (userAnswerId != null && correctPairId != null) {
            Optional<Russian> ruCorrect = deRuController
                    .getDeRuById(correctPairId).flatMap(deRu -> Optional.ofNullable(russianController.findById(deRu.getRussianId())));
            Russian ruFromUser = russianController.findById(userAnswerId);
            Optional<Deutsch> german = deRuController
                    .getDeRuById(correctPairId).flatMap(deRu -> Optional.ofNullable(deutschController.findById(deRu.getDeutschId())));
            System.out.println("answerId: " + userAnswerId + "\ncorrectPairID: " + correctPairId);//////////////////////////////////////////////////////////////////////////
            System.out.println("ruCorrectWord: " + ruCorrect.get().getWord() + " id: " + ruCorrect.get().getId());/////////////////////////////////////////////////////////
            System.out.println("ruFromUser: " + ruFromUser.getWord() + " id: " + ruFromUser.getId());//////////////////////////////////////////////////////////////////////
            System.out.println("germanWord: " + german.get().getDeWord() + " id: " + german.get().getId());///////////////////////////////////////////////////////////////
            if (ruCorrect.isPresent() && german.isPresent()) {
                if (userAnswerId.equals(ruCorrect.get().getId())) {
                    Optional<UserDictionary> forStatistic = userDictionaryController.getUserDictionaryByPairId(correctPairId);
                    forStatistic.ifPresent(userStatisticController::updateAllIterationsAndNewWordStatus);
                    answerToUser = "***** GOOD!!! *****\n\n"
                            + german.get().getDeWord() + " --> " + ruCorrect.get().getWord()
                            + "\n\n*******************\n"
                            + EmojiGive.greyCheck;
                } else {
                    Optional<UserDictionary> forStatistic = userDictionaryController.getUserDictionaryByPairId(correctPairId);
                    forStatistic.ifPresent(userStatisticController::updateAllIterationsAndNewWordStatus);
                    forStatistic.ifPresent(userStatisticController::updateAllFails);
                    answerToUser = "***** WRONG!!! *****"
                            + "\nYou choosed: " + ruFromUser.getWord()
                            + "\n--- Correct pair ---\n\n"
                            + german.get().getDeWord() + " --> " + ruCorrect.get().getWord()
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
        Optional<DeRu> deRu;
        Optional<Russian> russian = Optional.empty();
        Optional<Deutsch> german = Optional.empty();
        if (userDictionary.isPresent()) {
            deRu = deRuController.getDeRuById(userDictionary.get().getPair().getId());
            if (deRu.isPresent()) {
                russian = Optional.ofNullable(russianController.findById(deRu.get().getRussianId()));
                german = Optional.ofNullable(deutschController.findById(deRu.get().getDeutschId()));
            }
        }
        while (wrongAnswers.size() < 4) {
            Optional<Russian> randomRu = russianController.get1RandomRussian();
            if (randomRu.isPresent() && russian.isPresent() && german.isPresent()) {
                if (!randomRu.get().getId().equals(russian.get().getId())) {
                    Optional<DeRu> check = deRuController.getPairByGermanIdAndRussianId(german.get().getId(), randomRu.get().getId());
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
            userLanguage = userLanguageController.getByUserIdAndLanguageId(user.get().getId(), language.get().getId());
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
