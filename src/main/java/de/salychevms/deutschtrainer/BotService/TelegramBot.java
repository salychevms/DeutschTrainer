package de.salychevms.deutschtrainer.BotService;

import de.salychevms.deutschtrainer.BotConfig.BotConfig;
import de.salychevms.deutschtrainer.Controllers.*;
import de.salychevms.deutschtrainer.DataExchange.Classes.BasicPairStatisticInfoClass;
import de.salychevms.deutschtrainer.DataExchange.Classes.UserPairStatisticInfoClass;
import de.salychevms.deutschtrainer.DataExchange.Controlles.DataExchangeInOutController;
import de.salychevms.deutschtrainer.DataExchange.Controlles.VocabularyController;
import de.salychevms.deutschtrainer.Emojies.EmojiGive;
import de.salychevms.deutschtrainer.Models.*;
import de.salychevms.deutschtrainer.Training.TrainingController;
import de.salychevms.deutschtrainer.Training.TrainingPair;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

@EnableScheduling //ResetStatisticTask.java activate
@Component
public class TelegramBot extends TelegramLongPollingBot {
    private final BotConfig config;
    private final UsersController usersController;
    private final LanguageController languageController;
    private final UserLanguageController userLanguageController;
    private final DeutschController deutschController;
    private final RussianController russianController;
    private final DeRuPairsController deRuPairsController;
    private final UserStatisticController userStatisticController;
    private final UserDictionaryController userDictionaryController;
    private final TrainingController trainingController;
    private final MenuMaker menuMaker;
    private final VocabularyController vocabularyController;
    private final DataExchangeInOutController dataExchangeInOutController;
    private List<TrainingPair> learningList = new ArrayList<>();
    private List<TrainingPair> failList = new ArrayList<>();
    private List<TrainingPair> repeatList = new ArrayList<>();
    private List<String> queue = new ArrayList<>();

    public TelegramBot(BotConfig config, UsersController usersController, LanguageController languageController,
                       UserLanguageController userLanguageController, DeutschController deutschController,
                       RussianController russianController, DeRuPairsController deRuPairsController,
                       UserStatisticController userStatisticController, UserDictionaryController userDictionaryController,
                       TrainingController trainingController, MenuMaker menuMaker, VocabularyController vocabularyController, DataExchangeInOutController dataExchangeInOutController) {
        this.config = config;
        this.usersController = usersController;
        this.languageController = languageController;
        this.userLanguageController = userLanguageController;
        this.deutschController = deutschController;
        this.russianController = russianController;
        this.deRuPairsController = deRuPairsController;
        this.userStatisticController = userStatisticController;
        this.userDictionaryController = userDictionaryController;
        this.trainingController = trainingController;
        this.menuMaker = menuMaker;
        this.vocabularyController = vocabularyController;
        this.dataExchangeInOutController = dataExchangeInOutController;
    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        if (update.hasMessage() && message.hasText()) {
            String messageText = message.getText();
            long chatId = message.getChatId();
            long telegramId = (message.getFrom().getId());
            if (!usersController.registeredOr(telegramId)) {
                //if user sent /start
                if (messageText.equals("/start")) {
                    //give a start menu
                    sendKeyboard(menuMaker.registration(), chatId, "Вы активировали бота-тренера." + "\nВы должны зарегистрироваться.");
                    //if something unusual happened
                } else {
                    sendMessage(chatId, "Упс! Так не пойдет!\nВведите снова команду \"/start\" пожалуйста!");
                }
                //if user already exists give global menu
            } else if (usersController.registeredOr(telegramId)) {
                boolean adminStatus = usersController.isAdminChecker(telegramId);
                if (messageText.equals("/start")) {
                    //give global menu
                    sendKeyboard(menuMaker.mainMenu(adminStatus), chatId,
                            EmojiGive.germanFlag + "\n\nЯ тут! Меня кто-то звал???\n\n"
                                    + EmojiGive.joystick + " Главное меню:");
                    //global menu
                } else if (queue != null) {
                    Iterator<String> iterator = queue.iterator();
                    while (iterator.hasNext()) {
                        String item = iterator.next();
                        if (item.startsWith(String.valueOf(telegramId))) {
                            String userText = item.substring(String.valueOf(telegramId).length());
                            if (userText.equals("/settings/changeName")) {
                                usersController.updateNameByTelegramID(telegramId, messageText);
                                iterator.remove();
                                sendKeyboard(menuMaker.settingsMenu(), chatId, "Ваше имя изменено! \""
                                        + messageText + "\""
                                        + "\n\n" + EmojiGive.wrench + " Настройки:");
                            } else if (userText.equals("/settings/changeSurname")) {
                                usersController.updateSurnameByTelegramId(telegramId, messageText);
                                iterator.remove();
                                sendKeyboard(menuMaker.settingsMenu(), chatId, "Ваша фамилия изменена! \""
                                        + messageText + "\""
                                        + "\n\n" + EmojiGive.wrench + " Настройки:");
                            } else if (userText.equals("/adminMenu/addWordsAdmin")) {
                                iterator.remove();
                                //create one german word
                                Deutsch germanWord = deutschController.createNewWord(messageText);
                                //create a lot of russian words
                                List<Russian> russianTranslate = russianController.createNewWords(messageText);
                                //create pairs of words
                                List<Long> pairs = deRuPairsController.createPairs(germanWord, russianTranslate);
                                String result = deRuPairsController.getAllWordPairsByPairId(germanWord.getId(), pairs);
                                sendKeyboard(menuMaker.adminMenu(), chatId, result);
                            } else if (userText.equals("/tr/addNewWords")) {
                                iterator.remove();
                                if (!messageText.isEmpty()) {
                                    if (deRuPairsController.isItGerman(messageText)) {
                                        //look for a word
                                        //List<String> searchList = deRuPairsController.getWordsWhichUserLooksFor(telegramId, messageText, "DE");
                                        Map<Long, String> searchMap = deRuPairsController.getWordMapWhichUserLooksFor(telegramId, messageText, "DE");
                                        if (!searchMap.isEmpty()) {
                                            sendKeyboard(menuMaker.getSearchWordMenu(searchMap, messageText, "DE", "no"), chatId,
                                                    "Вы ввели слово: " + messageText + "\nВыберите одно из предложенных слов (первые 20 совпадений)" +
                                                            "\nи получите доступные варианты перевода." +
                                                            "\n\nВажно!!! Выводятся только первые 20 совпадений по запросу пользователя. " +
                                                            "Возможно нужное слово вы найдете, если напишете новый более подробный запрос.");
                                        } else {
                                            UserLanguage userLanguage = trainingController.getUserLangByTgIdAndLangIdentifier(telegramId, "DE");
                                            sendKeyboard(menuMaker.trainingMenu(userLanguage), chatId,
                                                    "Вы ввели слово: " + messageText +
                                                            "\nК сожалению ни чего подходящегок этому не нашлось! Попробуйте что-то другое.\n\n" +
                                                            EmojiGive.gameDie + " Тренировки:");
                                        }
                                    } else if (deRuPairsController.isItRussian(messageText)) {
                                        //look for a word
                                        //List<String> searchList = deRuPairsController.getWordsWhichUserLooksFor(telegramId, messageText, "RU");
                                        Map<Long, String> searchMap = deRuPairsController.getWordMapWhichUserLooksFor(telegramId, messageText, "RU");
                                        if (!searchMap.isEmpty()) {
                                            sendKeyboard(menuMaker.getSearchWordMenu(searchMap, messageText, "RU", "no"), chatId,
                                                    "Вы ввели слово: " + messageText + "\nВыберите одно из преложенных слов (первые 20 совпадений)" +
                                                            "\nи получите доступные варианты перевода." +
                                                            "\n\nВажно!!! Выводятся только первые 20 совпадений по запросу пользователя. " +
                                                            "Возможно нужное слово вы найдете, если напишете новый более подробный запрос.");
                                        } else {
                                            UserLanguage userLanguage = trainingController.getUserLangByTgIdAndLangIdentifier(telegramId, "DE");
                                            sendKeyboard(menuMaker.trainingMenu(userLanguage), chatId,
                                                    "Вы ввели слово: " + messageText +
                                                            "\nК сожалению ни чего подходящего к этому не нашлось! Попробуйте что-то другое.\n\n" +
                                                            EmojiGive.gameDie + " Тренировки:");
                                        }
                                    }
                                } else {
                                    UserLanguage userLanguage = trainingController.getUserLangByTgIdAndLangIdentifier(telegramId, "DE");
                                    sendKeyboard(menuMaker.trainingMenu(userLanguage), chatId,
                                            "Либо вы ни чего не ввели, либо что-то пошло не так...\nИзвините, найдем - починим!\n\n" +
                                                    EmojiGive.gameDie + " Тренировки:");
                                }
                            }
                        }
                    }
                }
            }
        } else if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            String callBackData = callbackQuery.getData();
            long chatId = callbackQuery.getMessage().getChatId();
            long telegramId = callbackQuery.getFrom().getId();
            String userName = callbackQuery.getMessage().getChat().getUserName();
            //if user exists
            if (usersController.registeredOr(telegramId)) {
                boolean adminStatus = usersController.getAdminStatus(telegramId);
                UserLanguage userLanguage = trainingController.getUserLangByTgIdAndLangIdentifier(telegramId, "DE");
                //reg keyboard
                if (callBackData.equals("/tr")) {
                    editKeyboard(update.getCallbackQuery(), menuMaker.trainingMenu(userLanguage), EmojiGive.gameDie + " Тренировки:");
                } else if (callBackData.equals("/tr/addNewWords")) {
                    editMessage(callbackQuery, """
                            Вы можете ввести слово (к примеру существительное с/без артикля)
                            и Вы получите все доступные варианты слов.
                            Так же Вы получите все доступные варианты перевода.
                            К примеру: *Haus* вернет *das Haus // дом*,
                            и другие доступные слова и переводы.
                            Например: *der Hausmeister // управдом*.
                            """);
                    sendMessage(chatId, "Введите немецкое или русское слово: ");
                    queue.add(telegramId + callBackData);
                } else if (callBackData.startsWith("/so=")) {
                    String[] selectedWord = callBackData.split("=");
                    Map<Long, String> translationList;
                    if (selectedWord[2].equals("DE")) {
                        translationList = deRuPairsController.getTranslations(telegramId, selectedWord[1], "DE");
                        Deutsch deutsch = deutschController.findById(Long.valueOf(selectedWord[1]));
                        editKeyboard(update.getCallbackQuery(), menuMaker.getSearchWordMenu(translationList, selectedWord[1], "DE", "yes"),
                                "\nВы можете выбрать одно слово и получить все доступные переводы:\n" + deutsch.getDeWord());
                    } else if (selectedWord[2].equals("RU")) {
                        translationList = deRuPairsController.getTranslations(telegramId, selectedWord[1], "RU");
                        Russian russian = russianController.findById(Long.valueOf(selectedWord[1]));
                        editKeyboard(update.getCallbackQuery(), menuMaker.getSearchWordMenu(translationList, selectedWord[1], "RU", "yes"),
                                "\nВы можете выбрать одно слово и получить все доступные переводы:\n" + russian.getRuWord());
                    }
                } else if (callBackData.startsWith("/to=")) {
                    String[] selectedWord = callBackData.split("=");
                    Deutsch deutsch = new Deutsch();
                    Russian russian = new Russian();
                    //while a translation is a german: /to , 123(de), RU, 321(ru)
                    if (selectedWord[2].equals("DE")) {
                        deutsch = deutschController.findById(Long.valueOf(selectedWord[3]));
                        russian = russianController.findById(Long.valueOf(selectedWord[1]));
                        //while a translation is a russian: /to , 123(ru), DE, 321(de)
                    } else if (selectedWord[2].equals("RU")) {
                        deutsch = deutschController.findById(Long.valueOf(selectedWord[1]));
                        russian = russianController.findById(Long.valueOf(selectedWord[3]));
                    }
                    Optional<DeRuPairs> pair = deRuPairsController.getPairByGermanIdAndRussianId(deutsch.getId(), russian.getId());
                    if (pair.isPresent()) {
                        List<UserDictionary> dictionaries = userDictionaryController.getAllByTelegramId(telegramId);
                        boolean bool = false;
                        for (UserDictionary dict : dictionaries) {
                            if (dict.getPair().getId().equals(pair.get().getId())) {
                                bool = true;
                            }
                        }
                        if (!bool) {
                            userStatisticController.saveNewPairInStatistic(userDictionaryController.saveNewPair(telegramId, "DE", pair.get()));
                            editKeyboard(update.getCallbackQuery(),
                                    menuMaker.trainingMenu(trainingController.getUserLangByTgIdAndLangIdentifier(
                                            telegramId, "DE")),
                                    "Вы добавили пару слов:\n" +
                                            deutsch.getDeWord() +
                                            " = " + russian.getRuWord() +
                                            "\n\n" + EmojiGive.gameDie + " Тренировки:");
                        } else {
                            editKeyboard(update.getCallbackQuery(),
                                    menuMaker.trainingMenu(trainingController.getUserLangByTgIdAndLangIdentifier(
                                            telegramId, "DE")),
                                    "Вы у вас уже есть эта пара слов:\n" +
                                            deutsch.getDeWord() +
                                            " = " + russian.getRuWord() +
                                            "\n\n" + EmojiGive.gameDie + " Тренировки:");
                        }
                    }
                } else if (callBackData.startsWith("/tr")) {// /tr= - training
                    if (callBackData.equals("/tr=/sLT")) {// /tr=/sLT - training/StartLearningTraining
                        learningList = trainingController.createLearningList(telegramId);
                        TrainingPair pair = learningList.get(0);
                        learningList.remove(pair);
                        editKeyboard(callbackQuery,
                                trainingController.getTestKeyboard(pair, "/tr=/LT=/DRI=:"),// /tr=/LT=/DRI=: - training/LearningTraining/DeRuId
                                getHeaderForTraining(pair.getGerman().getDeWord()));
                    } else if (callBackData.equals("/tr=/sRT")) {// /tr=/sRT - training/StartRepeatTraining
                        repeatList = trainingController.createTrainingRepeatList(telegramId);
                        TrainingPair pair = repeatList.get(0);
                        repeatList.remove(pair);
                        editKeyboard(callbackQuery,
                                trainingController.getTestKeyboard(pair, "/tr=/RT=/DRI=:"),// /tr=/RT=/DRI=: - training/RepeatTraining/DeRuId
                                getHeaderForTraining(pair.getGerman().getDeWord()));
                    } else if (callBackData.equals("/tr=/sFT")) {// /tr=/sFT - training/StartFailsTraining
                        failList = trainingController.createTrainingFailList(telegramId);
                        TrainingPair pair = failList.get(0);
                        failList.remove(pair);
                        editKeyboard(callbackQuery,
                                trainingController.getTestKeyboard(pair, "/tr=/FT=/DRI=:"),// /tr=/FT=/DRI=: - training/FailsTraining/DeRuId
                                getHeaderForTraining(pair.getGerman().getDeWord()));
                    } else if (callBackData.startsWith("/tr=/LT=/DRI=:")) {// /tr=/LT=/DRI=: - training/LearningTraining/DeRuId
                        String result = trainingController.getAnswerFromUser(callBackData);
                        if (learningList.isEmpty()) {
                            editKeyboard(callbackQuery, null, result + "\n\nОтлично! Вы завершили обучение!");
                            sendMessage(chatId, EmojiGive.oK);
                            sendKeyboard(menuMaker.trainingMenu(trainingController.getUserLangByTgIdAndLangIdentifier(telegramId, "DE")),
                                    chatId, EmojiGive.gameDie + " Тренировки:");
                        } else {
                            TrainingPair pair = learningList.get(0);
                            learningList.remove(pair);
                            editKeyboard(callbackQuery, null, result);
                            sendKeyboard(trainingController.getTestKeyboard(pair, "/tr=/LT=/DRI=:"),// /tr=/LT=/DRI=: - training/LearningTraining/DeRuId
                                    chatId, getHeaderForTraining(pair.getGerman().getDeWord()));
                        }
                    } else if (callBackData.startsWith("/tr=/RT=/DRI=:")) {// /tr=/RT=/DRI=: - training/RepeatTraining/DeRuId
                        String result = trainingController.getAnswerFromUser(callBackData);
                        if (repeatList.isEmpty()) {
                            editKeyboard(callbackQuery, null, result + "\n\nОтлично! Вы завершили тренировку!");
                            sendMessage(chatId, EmojiGive.oK);
                            sendKeyboard(menuMaker.trainingMenu(trainingController.getUserLangByTgIdAndLangIdentifier(telegramId, "DE")),
                                    chatId, EmojiGive.gameDie + " Тренировки:");
                        } else {
                            TrainingPair pair = repeatList.get(0);
                            repeatList.remove(pair);
                            editKeyboard(callbackQuery, null, result);
                            sendKeyboard(trainingController.getTestKeyboard(pair, "/tr=/RT=/DRI=:"),// /tr=/RT=/DRI=: - training/RepeatTraining/DeRuId
                                    chatId, getHeaderForTraining(pair.getGerman().getDeWord()));
                        }
                    } else if (callBackData.startsWith("/tr=/FT=/DRI=:")) {// /tr=/FT=/DRI=: - training/FailsTraining/DeRuId
                        String result = trainingController.getAnswerFromUser(callBackData);
                        if (failList.isEmpty()) {
                            editKeyboard(callbackQuery, null, result + "\n\nОтлично! Вы повторили слова с ошибками!");
                            sendMessage(chatId, EmojiGive.oK);
                            sendKeyboard(menuMaker.trainingMenu(trainingController.getUserLangByTgIdAndLangIdentifier(telegramId, "DE")),
                                    chatId, EmojiGive.gameDie + " Тренировки:");
                        } else {
                            TrainingPair pair = failList.get(0);
                            failList.remove(pair);
                            editKeyboard(callbackQuery, null, result);
                            sendKeyboard(trainingController.getTestKeyboard(pair, "/tr=/FT=/DRI=:"),// /tr=/FT=/DRI=: - training/FailsTraining/DeRuId
                                    chatId, getHeaderForTraining(pair.getGerman().getDeWord()));
                        }
                    } else if (callBackData.startsWith("/tr=/FAT")) { // /tr=/FAT - training/FinishAllTrainings
                        editKeyboard(callbackQuery, null, "Вы прервали занятие! Жаль!" +
                                "\nНадеюсь Вы вернетесь к обучению как можно скорее!");
                        sendMessage(chatId, EmojiGive.thinkingFace);
                        sendKeyboard(menuMaker.trainingMenu(trainingController.getUserLangByTgIdAndLangIdentifier(telegramId, "DE")),
                                chatId, EmojiGive.gameDie + " Тренировки:");
                        learningList.clear();
                        repeatList.clear();
                        failList.clear();
                    }
                } else if (callBackData.equals("/statistic")) {
                    //////////////////////////////
                    /*List<BasicPairStatisticInfoClass> basicPairStatistic=vocabularyController.getUserPairStatisticInfo(telegramId, 2L);
                    System.out.println(basicPairStatistic);*/
                    List<UserPairStatisticInfoClass> userStatisticList=vocabularyController.getAllUserPairStatisticInfo(telegramId, "DE");
                    dataExchangeInOutController.writeUserStatisticToExcel(userStatisticList);
                    //////////////////////////////
                    String basicStatistic = userStatisticController.getBasicStatistic(telegramId);
                    editKeyboard(update.getCallbackQuery(), menuMaker.statisticMenu(), EmojiGive.barChart + " Статистика: \n\n" + basicStatistic);
                } else if (callBackData.equals("/settings")) {
                    editKeyboard(update.getCallbackQuery(), menuMaker.settingsMenu(), EmojiGive.wrench + " Настройки:");
                } else if (callBackData.equals("/info")) {
                    editKeyboard(update.getCallbackQuery(), menuMaker.infoMenu(), EmojiGive.clipboard + " Инфо:");
                } else if (callBackData.equals("/adminMenu")) {
                    editKeyboard(update.getCallbackQuery(), menuMaker.adminMenu(), EmojiGive.lockedWithKey + " Admin Menu:");
                } else if (callBackData.equals("/adminMenu/addWordsAdmin")) {
                    editMessage(callbackQuery, """
                            A Very Important Description:
                            New record should looks like **der Hund // собака**.
                            If a word has several meanings write please with **/**.
                            For example: **laufen // идти/бежать**.
                            Please, enter new german nouns only with article.
                            For example: **das Haus**.
                            Write an article from small letter: **der/die/das**.
                            A word from big letter: **Haus/Hund/Ampel.**""");
                    sendMessage(chatId, "Введите пару слов: ");
                    queue.add(telegramId + callBackData);
                } else if (callBackData.equals("/adminMenu/addListAdmin")) {
                    List<String> wordList = new ArrayList<>();
                    File file = new File("C:\\Develop\\Learning\\DeutschTrainer\\src\\main\\resources\\contentList.txt");
                    try (Scanner scanner = new Scanner(file)) {
                        int i = 0;
                        while (scanner.hasNextLine()) {
                            String string = scanner.nextLine();
                            wordList.add(string);
                            i++;
                        }
                        System.out.println("ВСЕ СЛОВА ДОБАВЛЕНЫ!\nВСЕГО СТРОК ПРОЧИТАНО ИЗ ФАЙЛА: " + i);
                    } catch (FileNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                    boolean create = false;
                    editKeyboard(update.getCallbackQuery(), null, "Слова были сохранены: ");
                    if (!wordList.isEmpty()) {
                        create = true;
                        for (String str : wordList) {
                            if (!str.isEmpty()) {
                                //create one german word
                                Deutsch germanWord = deutschController.createNewWord(str);
                                //create a lot of russian words
                                List<Russian> russianTranslate = russianController.createNewWords(str);
                                //create pairs of words
                                deRuPairsController.createPairs(germanWord, russianTranslate);
                            }
                        }
                    }
                    if (create) {
                        sendMessage(chatId, "Успешно!");
                        sendKeyboard(menuMaker.mainMenu(adminStatus), chatId, EmojiGive.germanFlag + "\n\nЯ тут! Меня кто-то звал???\n\n"
                                + EmojiGive.joystick + " Главное меню:");
                    } else {
                        sendMessage(chatId, "Нет новых слов или Ваш список пуст!");
                        sendKeyboard(menuMaker.mainMenu(adminStatus), chatId, EmojiGive.germanFlag + "\n\nЯ тут! Меня кто-то звал???\n\n"
                                + EmojiGive.joystick + " Главное меню:");
                    }
                } else if (callBackData.equals("/info/userInfo")) {
                    editKeyboard(update.getCallbackQuery(), menuMaker.mainMenu(adminStatus), sendUserInfo(telegramId) + "\n\n" + EmojiGive.joystick + " Главное меню:");
                } else if (callBackData.equals("/info/aboutThisBot")) {
                    editKeyboard(update.getCallbackQuery(), menuMaker.mainMenu(adminStatus),
                            "Я надеюсь что этот бот поможет людям учить немецкие слова. " +
                                    "Бот бесплатный. Это моя практика как программиста. Да и слова мне самому учить надо!=) " +
                                    "Словарь как таковой пока я набиваю в ручную, если не сильно лень, " +
                                    "так что буду признателен за помощь в составлении списка слов, которые нужны вам для изучения. " +
                                    "Любые вопросы, пожелания, проблемы, а также списки слов, которые вам нужны - всё шлём и пишем сюда: " +
                                    "mishanya_k-city@mail.ru." +
                                    "\nСпасибо, что уделили время этому проекту! Надеюсь он вам поможет!" +
                                    "\nУдачи в учебе и хорошего настроения! =)\n\n" +
                                    EmojiGive.joystick + " Главное меню:");
                } else if (callBackData.equals("/settings/changeName")) {
                    editMessage(callbackQuery, "Сменить имя:");
                    sendMessage(chatId, "введите новое имя:");
                    queue.add(telegramId + callBackData);
                } else if (callBackData.equals("/settings/changeSurname")) {
                    editMessage(callbackQuery, "Сменить фамилию:");
                    sendMessage(chatId, "введите новую фамилию:");
                    queue.add(telegramId + callBackData);
                } else if (callBackData.equals("/mainMenu")) {
                    editKeyboard(update.getCallbackQuery(), menuMaker.mainMenu(adminStatus),
                            EmojiGive.germanFlag + "\n\nЯ тут! Меня кто-то звал???\n\n"
                                    + EmojiGive.joystick + " Главное меню:");
                }
                //if user doesn't exist
            } else if (!usersController.registeredOr(telegramId)) {
                if (callBackData.equals("/registration")) {
                    usersController.createNewUser(telegramId, userName);
                    userLanguageController.createUserLanguage(telegramId, languageController.getLanguageByIdentifier("DE").orElseThrow().getId());
                    if (usersController.registeredOr(telegramId)) {
                        editKeyboard(update.getCallbackQuery(), menuMaker.mainMenu(false),
                                "Успех! Вы зарегистрированы!\n\n" +
                                        EmojiGive.joystick + " Главное меню:");
                    } else sendMessage(chatId, "Оооой! Что-то пошло не так..." +
                            "\nНажмите \"/start\" и попробуйте снова!!!");
                } else if (callBackData.equals("/aboutRegistration")) {
                    if (!callbackQuery.getMessage().getText().equals("You have to register!\n\nPlease, push *Registration* in this menu.")) {
                        editKeyboard(update.getCallbackQuery(), menuMaker.registration(), "You have to register!\n\nPlease, push *Registration* in this menu.");
                    } else
                        editMessage(update.getCallbackQuery(), "You're haven't registered!\nPress /start and get a sign up, please!");
                }
            } else sendMessage(chatId, "Oooopsie! Sorry, something wrong happened..." +
                    "\nWrite \"/start\" and try again!!");
        }
    }

    private String getHeaderForTraining(String word) {
        int count = word.length();
        StringBuilder stripe = new StringBuilder();
        stripe.append("=".repeat(count));
        stripe.append("=");
        return stripe + "\n\n" + word + "\n\n" + stripe;
    }

    private void sendKeyboard(InlineKeyboardMarkup keyboardMarkup, long chatId, String msg) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(msg);
        message.setReplyMarkup(keyboardMarkup);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void editKeyboard(CallbackQuery callbackQuery, InlineKeyboardMarkup keyboardMarkup, String msg) {
        long messageId = callbackQuery.getMessage().getMessageId();
        long chatId = callbackQuery.getMessage().getChatId();

        EditMessageText message = new EditMessageText();

        message.setChatId(chatId);
        message.setMessageId((int) messageId);
        message.setText(msg);
        message.setReplyMarkup(keyboardMarkup);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void editMessage(CallbackQuery callBackQuery, String msg) {
        long messageId = callBackQuery.getMessage().getMessageId();
        long chatId = callBackQuery.getMessage().getChatId();

        EditMessageText message = new EditMessageText();

        message.setText(msg);
        message.setChatId(chatId);
        message.setMessageId((int) messageId);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendMessage(long chatId, String toSend) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(toSend);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private String sendUserInfo(long telegramId) {
        Optional<Users> user = usersController.findUserByTelegramId(telegramId);
        return user.map(users -> "Пользователь: " + users.getUserName()
                + "\nTelegram id пользователя: " + users.getTelegramId()
                + "\nИмя: " + users.getName()
                + "\nФамилия: " + users.getSurname()
                + "\nНомер телефона: " + users.getPhoneNumber()
                + "\nДата регистрации: " + users.getRegistrationDate()).orElse(null);
    }
}
