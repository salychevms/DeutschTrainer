package de.salychevms.deutschtrainer.BotService;

import de.salychevms.deutschtrainer.BotConfig.BotConfig;
import de.salychevms.deutschtrainer.Controllers.*;
import de.salychevms.deutschtrainer.Emojies.EmojiGive;
import de.salychevms.deutschtrainer.Models.*;
import de.salychevms.deutschtrainer.Training.TrainingController;
import de.salychevms.deutschtrainer.Training.TrainingPair;
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
    private final MenuMaker mm;
    private List<TrainingPair> learningList = new ArrayList<>();
    private List<TrainingPair> failList = new ArrayList<>();
    private List<TrainingPair> repeatList = new ArrayList<>();
    private List<String> queue = new ArrayList<>();

    public TelegramBot(BotConfig config, UsersController usersController, LanguageController languageController,
                       UserLanguageController userLanguageController, DeutschController deutschController,
                       RussianController russianController, DeRuPairsController deRuPairsController,
                       UserStatisticController userStatisticController, UserDictionaryController userDictionaryController,
                       TrainingController trainingController, MenuMaker mm) {
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
        this.mm = mm;
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
        String addWordsIdentifier = null;
        if (update.hasMessage() && message.hasText()) {
            String messageText = message.getText();
            long chatId = message.getChatId();
            long telegramId = (message.getFrom().getId());
            if (!usersController.registeredOr(telegramId)) {
                //if user sent /start
                if (messageText.equals("/start")) {
                    //give a start menu
                    sendKeyboard(mm.registration(), chatId, "Вы активировали бота-тренера." + "\nВы должны зарегистрироваться.");
                    //if something unusual happened
                } else {
                    sendMessage(chatId, "Упс! Так не пойдет!\nВведите снова команду \"/start\" пожалуйста!");
                }
                //if user already exists give global menu
            } else if (usersController.registeredOr(telegramId)) {
                boolean adminStatus = usersController.findUserByTelegramId(telegramId).get().isAdmin();
                if (messageText.equals("/start")) {
                    //give global menu
                    sendKeyboard(mm.mainMenu(adminStatus), chatId,
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
                                sendKeyboard(mm.settingsMenu(), chatId, "Ваше имя изменено! \""
                                        + messageText + "\""
                                        + "\n\n" + EmojiGive.wrench + " Настройки:");
                            } else if (userText.equals("/settings/changeSurname")) {
                                usersController.updateSurnameByTelegramId(telegramId, messageText);
                                iterator.remove();
                                sendKeyboard(mm.settingsMenu(), chatId, "Ваша фамилия изменена! \""
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
                                sendKeyboard(mm.adminMenu(), chatId, result);
                            } else if (userText.equals("/training/addNewWords")) {
                                iterator.remove();
                                if (!messageText.isEmpty()) {
                                    if (deRuPairsController.isItGerman(messageText)) {
                                        List<String> searchList = deRuPairsController.getWordsWhichUserLooksFor(telegramId, messageText, "DE");
                                        if (!searchList.isEmpty()) {
                                            sendKeyboard(mm.getSearchWordMenu(searchList, messageText, "no"), chatId,
                                                    "Вы ввели слово: " + messageText + "\nВыберите одно из предложенных слов (первые 20 совпадений)" +
                                                            "\nи получите доступные варианты перевода." +
                                                            "\n\nВажно!!! Выводятся только первые 20 совпадений по запросу пользователя. " +
                                                            "Возможно нужное слово вы найдете, если напишете новый более подробный запрос.");
                                        } else {
                                            UserLanguage userLanguage = trainingController.getUserLangByTgIdAndLangIdentifier(telegramId, "DE");
                                            sendKeyboard(mm.trainingMenu(userLanguage), chatId,
                                                    "Вы ввели слово: " + messageText +
                                                            "\nК сожалению ни чего подходящегок этому не нашлось! Попробуйте что-то другое.\n\n" +
                                                            EmojiGive.gameDie + " Тренировки:");
                                        }
                                    } else if (deRuPairsController.isItRussian(messageText)) {
                                        List<String> searchList = deRuPairsController.getWordsWhichUserLooksFor(telegramId, messageText, "RU");
                                        if (!searchList.isEmpty()) {
                                            sendKeyboard(mm.getSearchWordMenu(searchList, messageText, "no"), chatId,
                                                    "Вы ввели слово: " + messageText + "\nВыберите одно из преложенных слов (первые 20 совпадений)" +
                                                            "\nи получите доступные варианты перевода." +
                                                            "\n\nВажно!!! Выводятся только первые 20 совпадений по запросу пользователя. " +
                                                            "Возможно нужное слово вы найдете, если напишете новый более подробный запрос.");
                                        } else {
                                            UserLanguage userLanguage = trainingController.getUserLangByTgIdAndLangIdentifier(telegramId, "DE");
                                            sendKeyboard(mm.trainingMenu(userLanguage), chatId,
                                                    "Вы ввели слово: " + messageText +
                                                            "\nК сожалению ни чего подходящего к этому не нашлось! Попробуйте что-то другое.\n\n" +
                                                            EmojiGive.gameDie + " Тренировки:");
                                        }
                                    }
                                } else {
                                    UserLanguage userLanguage = trainingController.getUserLangByTgIdAndLangIdentifier(telegramId, "DE");
                                    sendKeyboard(mm.trainingMenu(userLanguage), chatId,
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
                if (callBackData.equals("/training")) {
                    editKeyboard(update.getCallbackQuery(), mm.trainingMenu(userLanguage), EmojiGive.gameDie + " Тренировки:");
                } else if (callBackData.equals("/training/addNewWords")) {
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
                } else if (callBackData.startsWith("/SearchOffer=")) {
                    String[] selectedWord = callBackData.split("=");
                    List<String> translationList;
                    if (deRuPairsController.isItGerman(selectedWord[1])) {
                        translationList = deRuPairsController.getTranslations(telegramId, selectedWord[1], "DE");
                        editKeyboard(update.getCallbackQuery(), mm.getSearchWordMenu(translationList, selectedWord[1], "yes"),
                                "\nВы можете выбрать одно слово и получить все доступные переводы:\n" + selectedWord[1]);
                    } else if (deRuPairsController.isItRussian(selectedWord[1])) {
                        translationList = deRuPairsController.getTranslations(telegramId, selectedWord[1], "RU");
                        editKeyboard(update.getCallbackQuery(), mm.getSearchWordMenu(translationList, selectedWord[1], "yes"),
                                "\nВы можете выбрать одно слово и получить все доступные переводы:\n" + selectedWord[1]);
                    }
                } else if (callBackData.startsWith("/TranslationsOffer=")) {
                    String[] selectedWord = callBackData.split("=");
                    Optional<Deutsch> deutsch = Optional.of(new Deutsch());
                    Optional<Russian> russian = Optional.of(new Russian());
                    if (deRuPairsController.isItGerman(selectedWord[1]) && deRuPairsController.isItRussian(selectedWord[2])) {
                        deutsch = deutschController.findByWord(selectedWord[1]);
                        russian = russianController.findByWord(selectedWord[2]);
                    } else if (deRuPairsController.isItGerman(selectedWord[2]) && deRuPairsController.isItRussian(selectedWord[1])) {
                        deutsch = deutschController.findByWord(selectedWord[2]);
                        russian = russianController.findByWord(selectedWord[1]);
                    }
                    if (deutsch.isPresent() && russian.isPresent()) {
                        Optional<DeRuPairs> pair = deRuPairsController.getPairByGermanIdAndRussianId(deutsch.get().getId(), russian.get().getId());
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
                                        mm.trainingMenu(trainingController.getUserLangByTgIdAndLangIdentifier(
                                                telegramId, "DE")),
                                        "Вы добавили пару слов:\n" +
                                                deutsch.get().getDeWord() +
                                                " = " + russian.get().getRuWord() +
                                                "\n\n" + EmojiGive.gameDie + " Тренировки:");
                            } else {
                                editKeyboard(update.getCallbackQuery(),
                                        mm.trainingMenu(trainingController.getUserLangByTgIdAndLangIdentifier(
                                                telegramId, "DE")),
                                        "Вы у вас уже есть эта пара слов:\n" +
                                                deutsch.get().getDeWord() +
                                                " = " + russian.get().getRuWord() +
                                                "\n\n" + EmojiGive.gameDie + " Тренировки:");
                            }
                        }
                    }
                } else if (callBackData.startsWith("/training")) {
                    if (callBackData.equals("/training=/StartLearningTraining")) {
                        learningList = trainingController.createLearningList(telegramId);
                        TrainingPair pair = learningList.get(0);
                        learningList.remove(pair);
                        editKeyboard(callbackQuery,
                                trainingController.getTestKeyboard(pair, "/training=/LearningTraining=/DeRuId=:"),
                                getHeaderForTraining(pair.getGerman().getDeWord()));
                    } else if (callBackData.equals("/training=/StartRepeatTraining")) {
                        repeatList = trainingController.createTrainingRepeatList(telegramId);
                        TrainingPair pair = repeatList.get(0);
                        repeatList.remove(pair);
                        editKeyboard(callbackQuery,
                                trainingController.getTestKeyboard(pair, "/training=/RepeatTraining=/DeRuId=:"),
                                getHeaderForTraining(pair.getGerman().getDeWord()));
                    } else if (callBackData.equals("/training=/StartFailsTraining")) {
                        failList = trainingController.createTrainingFailList(telegramId);
                        TrainingPair pair = failList.get(0);
                        failList.remove(pair);
                        editKeyboard(callbackQuery,
                                trainingController.getTestKeyboard(pair, "/training=/FailsTraining=/DeRuId=:"),
                                getHeaderForTraining(pair.getGerman().getDeWord()));
                    } else if (callBackData.startsWith("/training=/LearningTraining=/DeRuId=:")) {
                        String result = trainingController.getAnswerFromUser(callBackData);
                        if (learningList.isEmpty()) {
                            editKeyboard(callbackQuery, null, result + "\n\nОтлично! Вы завершили обучение!");
                            sendMessage(chatId, EmojiGive.oK);
                            sendKeyboard(mm.trainingMenu(trainingController.getUserLangByTgIdAndLangIdentifier(telegramId, "DE")),
                                    chatId, EmojiGive.gameDie + " Тренировки:");
                        } else {
                            TrainingPair pair = learningList.get(0);
                            learningList.remove(pair);
                            editKeyboard(callbackQuery, null, result);
                            sendKeyboard(trainingController.getTestKeyboard(pair, "/training=/LearningTraining=/DeRuId=:"),
                                    chatId, getHeaderForTraining(pair.getGerman().getDeWord()));
                        }
                    } else if (callBackData.startsWith("/training=/RepeatTraining=/DeRuId=:")) {
                        String result = trainingController.getAnswerFromUser(callBackData);
                        if (repeatList.isEmpty()) {
                            editKeyboard(callbackQuery, null, result + "\n\nОтлично! Вы завершили тренировку!");
                            sendMessage(chatId, EmojiGive.oK);
                            sendKeyboard(mm.trainingMenu(trainingController.getUserLangByTgIdAndLangIdentifier(telegramId, "DE")),
                                    chatId, EmojiGive.gameDie + " Тренировки:");
                        } else {
                            TrainingPair pair = repeatList.get(0);
                            repeatList.remove(pair);
                            editKeyboard(callbackQuery, null, result);
                            sendKeyboard(trainingController.getTestKeyboard(pair, "/training=/RepeatTraining=/DeRuId=:"),
                                    chatId, getHeaderForTraining(pair.getGerman().getDeWord()));
                        }
                    } else if (callBackData.startsWith("/training=/FailsTraining=/DeRuId=:")) {
                        String result = trainingController.getAnswerFromUser(callBackData);
                        if (failList.isEmpty()) {
                            editKeyboard(callbackQuery, null, result + "\n\nОтлично! Вы повторили слова с ошибками!");
                            sendMessage(chatId, EmojiGive.oK);
                            sendKeyboard(mm.trainingMenu(trainingController.getUserLangByTgIdAndLangIdentifier(telegramId, "DE")),
                                    chatId, EmojiGive.gameDie + " Тренировки:");
                        } else {
                            TrainingPair pair = failList.get(0);
                            failList.remove(pair);
                            editKeyboard(callbackQuery, null, result);
                            sendKeyboard(trainingController.getTestKeyboard(pair, "/training=/FailsTraining=/DeRuId=:"),
                                    chatId, getHeaderForTraining(pair.getGerman().getDeWord()));
                        }
                    } else if (callBackData.startsWith("/training=/FinishAllTrainings")) {
                        editKeyboard(callbackQuery, null, "Вы прервали занятие! Жаль!" +
                                "\nНадеюсь Вы вернетесь к обучению как можно скорее!");
                        sendMessage(chatId, EmojiGive.thinkingFace);
                        sendKeyboard(mm.trainingMenu(trainingController.getUserLangByTgIdAndLangIdentifier(telegramId, "DE")),
                                chatId, EmojiGive.gameDie + " Тренировки:");
                        learningList.clear();
                        repeatList.clear();
                        failList.clear();
                    }
                } else if (callBackData.equals("/statistic")) {
                    String basicStatistic = userStatisticController.getBasicStatistic(telegramId);
                    editKeyboard(update.getCallbackQuery(), mm.statisticMenu(), EmojiGive.barChart + " Статистика: \n\n" + basicStatistic);
                } else if (callBackData.equals("/settings")) {
                    editKeyboard(update.getCallbackQuery(), mm.settingsMenu(), EmojiGive.wrench + " Настройки:");
                } else if (callBackData.equals("/info")) {
                    editKeyboard(update.getCallbackQuery(), mm.infoMenu(), EmojiGive.clipboard + " Инфо:");
                } else if (callBackData.equals("/adminMenu")) {
                    editKeyboard(update.getCallbackQuery(), mm.adminMenu(), EmojiGive.lockedWithKey + " Admin Menu:");
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
                        int i=0;
                        while (scanner.hasNextLine()) {
                            String string = scanner.nextLine();
                            wordList.add(string);
                            i++;
                        }
                        System.out.println("ВСЕ СЛОВА ДОБАВЛЕНЫ!\nВСЕГО СТРОК ПРОЧИТАНО ИЗ ФАЙЛА: "+i);
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
                        sendKeyboard(mm.mainMenu(adminStatus), chatId, EmojiGive.germanFlag + "\n\nЯ тут! Меня кто-то звал???\n\n"
                                + EmojiGive.joystick + " Главное меню:");
                    } else {
                        sendMessage(chatId, "Нет новых слов или Ваш список пуст!");
                        sendKeyboard(mm.mainMenu(adminStatus), chatId, EmojiGive.germanFlag + "\n\nЯ тут! Меня кто-то звал???\n\n"
                                + EmojiGive.joystick + " Главное меню:");
                    }
                } else if (callBackData.equals("/info/userInfo")) {
                    editKeyboard(update.getCallbackQuery(), mm.mainMenu(adminStatus), sendUserInfo(telegramId) + "\n\n" + EmojiGive.joystick + " Главное меню:");
                } else if (callBackData.equals("/info/aboutThisBot")) {
                    editKeyboard(update.getCallbackQuery(), mm.mainMenu(adminStatus),
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
                    editKeyboard(update.getCallbackQuery(), mm.mainMenu(adminStatus),
                            EmojiGive.germanFlag + "\n\nЯ тут! Меня кто-то звал???\n\n"
                                    + EmojiGive.joystick + " Главное меню:");
                }
                //if user doesn't exist
            } else if (!usersController.registeredOr(telegramId)) {
                if (callBackData.equals("/registration")) {
                    usersController.createNewUser(telegramId, userName);
                    userLanguageController.createUserLanguage(telegramId, languageController.getLanguageByIdentifier("DE").orElseThrow().getId());
                    if (usersController.registeredOr(telegramId)) {
                        editKeyboard(update.getCallbackQuery(), mm.mainMenu(false),
                                "Успех! Вы зарегистрированы!\n\n" +
                                        EmojiGive.joystick + " Главное меню:");
                    } else sendMessage(chatId, "Оооой! Что-то пошло не так..." +
                            "\nНажмите \"/start\" и попробуйте снова!!!");
                } else if (callBackData.equals("/aboutRegistration")) {
                    if (!callbackQuery.getMessage().getText().equals("You have to register!\n\nPlease, push *Registration* in this menu.")) {
                        editKeyboard(update.getCallbackQuery(), mm.registration(), "You have to register!\n\nPlease, push *Registration* in this menu.");
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
        for (int i = 0; i < count; i++) {
            stripe.append("=");
        }
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
            System.out.println(message);
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
