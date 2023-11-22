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
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

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
    private final DeRuController deRuController;
    private final UserStatisticController userStatisticController;
    private final UserDictionaryController userDictionaryController;
    private final TrainingController trainingController;

    private List<String> queue = new ArrayList<>();
    private List<TrainingPair> learningList = new ArrayList<>();
    private List<TrainingPair> failList = new ArrayList<>();
    private List<TrainingPair> repeatList = new ArrayList<>();

    public TelegramBot(BotConfig config, UsersController usersController, LanguageController languageController,
                       UserLanguageController userLanguageController, DeutschController deutschController,
                       RussianController russianController, DeRuController deRuController,
                       UserStatisticController userStatisticController, UserDictionaryController userDictionaryController,
                       TrainingController trainingController) {
        this.config = config;
        this.usersController = usersController;
        this.languageController = languageController;
        this.userLanguageController = userLanguageController;
        this.deutschController = deutschController;
        this.russianController = russianController;
        this.deRuController = deRuController;
        this.userStatisticController = userStatisticController;
        this.userDictionaryController = userDictionaryController;
        this.trainingController = trainingController;
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
                    //give start menu
                    sendKeyboard(registration(), chatId, "You activated this bot." + "\nYou have to register.");
                    //if something unusual happened
                } else {
                    sendMessage(chatId, "Sorry! It does not works.\nSend command: \"/start\" again, please!");
                }
                //if user already exists give global menu
            } else if (usersController.registeredOr(telegramId)) {
                boolean adminStatus = usersController.findUserByTelegramId(telegramId).get().isAdmin();
                if (messageText.equals("/start")) {
                    //give global menu
                    sendKeyboard(globalMenu(adminStatus), chatId, EmojiGive.germanFlag + "\n\nI'm here!!! Did someone call me???\n\nMain menu");
                    //global menu
                } else if (queue != null) {
                    Iterator<String> iterator = queue.iterator();
                    while (iterator.hasNext()) {
                        String item = iterator.next();
                        if (item.startsWith(String.valueOf(telegramId))) {
                            String userText = item.substring(String.valueOf(telegramId).length());
                            if (userText.equals("/changeName")) {
                                usersController.updateNameByTelegramID(telegramId, messageText);
                                iterator.remove();
                                sendKeyboard(settingsMenu(), chatId, "New name has been saved! \""
                                        + messageText
                                        + "\"" + "\n\nSetting");
                                break;
                            } else if (userText.equals("/changeSurname")) {
                                usersController.updateSurnameByTelegramId(telegramId, messageText);
                                iterator.remove();
                                sendKeyboard(settingsMenu(), chatId, "New surname has been saved! \""
                                        + messageText
                                        + "\"" + "\n\nSetting");
                                break;
                            } else if (userText.equals("/addWordAdmin")) {
                                iterator.remove();
                                //create one german word
                                Long germanWord = deutschController.createNewWord(messageText);
                                //create a lot of russian word
                                List<Long> russianTranslate = russianController.createNewWords(messageText);
                                //create pairs of words
                                List<Long> pairs = deRuController.createPairs(germanWord, russianTranslate);
                                String result = deRuController.getAllWordPairsByPairId(germanWord, pairs);
                                sendKeyboard(adminLanguagesMenu(), chatId, result);
                                break;
                            } else if (userText.equals("/addNewWords")) {
                                iterator.remove();
                                if (deRuController.isItGerman(messageText)) {
                                    List<String> searchList = deRuController.getWordsWhichUserLooksFor(messageText, "DE");
                                    sendKeyboard(getSearchWordMenu(searchList, messageText, "no"), chatId,
                                            "You entered a word: " + messageText + "\nYou can choose one word and get all existing translations.");
                                } else if (deRuController.isItRussian(messageText)) {
                                    List<String> searchList = deRuController.getWordsWhichUserLooksFor(messageText, "RU");
                                    sendKeyboard(getSearchWordMenu(searchList, messageText, "no"), chatId,
                                            "You entered a word: " + messageText + "\nYou can choose one word and get all existing translations.");
                                } else {
                                    UserLanguage userLanguage = trainingController.getUserLangByTgIdAndLangIdentifier(telegramId, "DE");
                                    sendKeyboard(trainingMenu(userLanguage), chatId, """
                                            This word doesn't exist! Sorry try another one word
                                            or send me email with your word list.

                                            TrainingController""");
                                    break;
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
                boolean adminStatus = usersController.findUserByTelegramId(telegramId).get().isAdmin();
                UserLanguage userLanguage = trainingController.getUserLangByTgIdAndLangIdentifier(telegramId, "DE");
                //reg keyboard
                if (callBackData.equals("/training")) {
                    editKeyboard(update.getCallbackQuery(), trainingMenu(userLanguage), "Training");
                } else if (callBackData.equals("/addNewWords")) {
                    editMessage(callbackQuery, """
                            You can enter a word (include nouns with/out an article)
                            and you get all existing translations for this word.
                            Also you will get all existing cognate words with translation.
                            E.g. *Haus* returns *das Haus // дом* and another word(s)
                            *der Hausmeister // управдом*
                            """);
                    sendMessage(chatId, "enter a german or russian word");
                    queue.add(telegramId + callBackData);
                } else if (callBackData.startsWith("/SearchOffer=")) {
                    String[] selectedWord = callBackData.split("=");
                    List<String> translationList;
                    if (deRuController.isItGerman(selectedWord[1])) {
                        translationList = deRuController.getTranslations(selectedWord[1], "DE");
                        sendKeyboard(getSearchWordMenu(translationList, selectedWord[1], "yes"), chatId,
                                "\nYou can choose one word and get all existing translations for the word:\n" + selectedWord[2]);
                    } else if (deRuController.isItRussian(selectedWord[1])) {
                        translationList = deRuController.getTranslations(selectedWord[1], "RU");
                        sendKeyboard(getSearchWordMenu(translationList, selectedWord[1], "yes"), chatId,
                                "\nYou can choose one word and get all existing translations for the word:\n" + selectedWord[2]);
                    }
                } else if (callBackData.startsWith("/TranslationsOffer=")) {
                    String[] selectedWord = callBackData.split("=");
                    Optional<Deutsch> deutsch = Optional.of(new Deutsch());
                    Optional<Russian> russian = Optional.of(new Russian());
                    if (deRuController.isItGerman(selectedWord[1]) && deRuController.isItRussian(selectedWord[2])) {
                        deutsch = deutschController.findByWord(selectedWord[1]);
                        russian = russianController.findByWord(selectedWord[2]);
                    } else if (deRuController.isItGerman(selectedWord[2]) && deRuController.isItRussian(selectedWord[1])) {
                        deutsch = deutschController.findByWord(selectedWord[2]);
                        russian = russianController.findByWord(selectedWord[1]);
                    }
                    if (deutsch.isPresent() && russian.isPresent()) {
                        Optional<DeRu> pair = deRuController.getPairByGermanIdAndRussianId(deutsch.get().getId(), russian.get().getId());
                        pair.ifPresent(deRu -> userStatisticController.saveNewPairInStatistic(userDictionaryController.saveNewPair(telegramId, "DE", deRu)));
                        sendKeyboard(trainingMenu(trainingController.getUserLangByTgIdAndLangIdentifier(telegramId, "DE")), chatId, "You added one pair:\n" + deutsch.get().getDeWord() + " = " + russian.get().getWord());
                    }
                } else if (callBackData.equals("/toSearchOffer")) {

                } else if (callBackData.startsWith("/Trainings=")) {
                    if (callBackData.equals("/Trainings=/StartLearningTraining")) {
                        learningList = trainingController.createLearningList(telegramId);
                        TrainingPair pair = learningList.get(0);
                        learningList.remove(pair);
                        editKeyboard(callbackQuery,
                                trainingController.getTestKeyboard(pair, "/Trainings=/LearningTraining=/DeRuId=:"),
                                getHeaderForTraining(pair.getGerman().getDeWord()));
                    } else if (callBackData.equals("/Trainings=/StartRepeatTraining")) {
                        repeatList = trainingController.createTrainingRepeatList(telegramId);
                        TrainingPair pair = repeatList.get(0);
                        repeatList.remove(pair);
                        editKeyboard(callbackQuery,
                                trainingController.getTestKeyboard(pair, "/Trainings=/RepeatTraining=/DeRuId=:"),
                                getHeaderForTraining(pair.getGerman().getDeWord()));
                    } else if (callBackData.equals("/Trainings=/StartFailsTraining")) {
                        failList = trainingController.createTrainingFailList(telegramId);
                        TrainingPair pair = failList.get(0);
                        failList.remove(pair);
                        editKeyboard(callbackQuery,
                                trainingController.getTestKeyboard(pair, "/Trainings=/FailsTraining=/DeRuId=:"),
                                getHeaderForTraining(pair.getGerman().getDeWord()));
                    } else if (callBackData.startsWith("/Trainings=/LearningTraining=/DeRuId=:")) {
                        String result = trainingController.getAnswerFromUser(callBackData);
                        if (learningList.isEmpty()) {
                            editKeyboard(callbackQuery, null, result + "\n\nYou're finished the learning!");
                            sendMessage(chatId, EmojiGive.oK);
                            sendKeyboard(trainingMenu(trainingController.getUserLangByTgIdAndLangIdentifier(telegramId, "DE")), chatId, "Training:");
                        } else {
                            TrainingPair pair = learningList.get(0);
                            learningList.remove(pair);
                            editKeyboard(callbackQuery, null, result);
                            sendKeyboard(trainingController.getTestKeyboard(pair, "/Trainings=/LearningTraining=/DeRuId=:"),
                                    chatId, getHeaderForTraining(pair.getGerman().getDeWord()));
                        }
                    } else if (callBackData.startsWith("/Trainings=/RepeatTraining=/DeRuId=:")) {
                        String result = trainingController.getAnswerFromUser(callBackData);
                        if (repeatList.isEmpty()) {
                            editKeyboard(callbackQuery, null, result + "\n\nYou're finished the training!");
                            sendMessage(chatId, EmojiGive.oK);
                            sendKeyboard(trainingMenu(trainingController.getUserLangByTgIdAndLangIdentifier(telegramId, "DE")), chatId, "Training:");
                        } else {
                            TrainingPair pair = repeatList.get(0);
                            repeatList.remove(pair);
                            editKeyboard(callbackQuery, null, result);
                            sendKeyboard(trainingController.getTestKeyboard(pair, "/Trainings=/RepeatTraining=/DeRuId=:"),
                                    chatId, getHeaderForTraining(pair.getGerman().getDeWord()));
                        }
                    } else if (callBackData.startsWith("/Trainings=/FailsTraining=/DeRuId=:")) {
                        String result = trainingController.getAnswerFromUser(callBackData);
                        if (failList.isEmpty()) {
                            editKeyboard(callbackQuery, null, result + "\n\nYou're finished the fails training!");
                            sendMessage(chatId, EmojiGive.oK);
                            sendKeyboard(trainingMenu(trainingController.getUserLangByTgIdAndLangIdentifier(telegramId, "DE")), chatId, "Training:");
                        } else {
                            TrainingPair pair = failList.get(0);
                            failList.remove(pair);
                            editKeyboard(callbackQuery, null, result);
                            sendKeyboard(trainingController.getTestKeyboard(pair, "/Trainings=/FailsTraining=/DeRuId=:"),
                                    chatId, getHeaderForTraining(pair.getGerman().getDeWord()));
                        }
                    } else if (callBackData.startsWith("/Trainings=/FinishAllTrainings")) {
                        editKeyboard(callbackQuery, null, "Training was interrupted!");
                        sendMessage(chatId, EmojiGive.thinkingFace);
                        sendKeyboard(trainingMenu(trainingController.getUserLangByTgIdAndLangIdentifier(telegramId, "DE")), chatId, "Training");
                        learningList.clear();
                        repeatList.clear();
                        failList.clear();
                    }
                } else if (callBackData.equals("/statistic")) {
                    editKeyboard(update.getCallbackQuery(), statisticMenu(), "Statistic");
                } else if (callBackData.equals("/settings")) {
                    editKeyboard(update.getCallbackQuery(), settingsMenu(), "Settings");
                } else if (callBackData.equals("/info")) {
                    editKeyboard(update.getCallbackQuery(), infoMenu(), "Info");
                } else if (callBackData.equals("/adminMenu")) {
                    editKeyboard(update.getCallbackQuery(), adminMenu(), "Admin Menu");
                } else if (callBackData.equals("/languagesAdmin")) {
                    editKeyboard(update.getCallbackQuery(), adminLanguagesMenu(), "Language Menu");
                } else if (callBackData.equals("/russianSettings")) {
                    editKeyboard(update.getCallbackQuery(), addWordsMenu(), "Add Words Menu\nRU - russian");
                } else if (callBackData.equals("/germanSettings")) {
                    editKeyboard(update.getCallbackQuery(), addWordsMenu(), "Add Words Menu\nDE - german");
                } else if (callBackData.equals("/addWordAdmin")) {
                    editMessage(callbackQuery, """
                            A Very Important Description:
                            New record should looks like **der Hund // собака**.
                            If a word has several meanings write please with **/**.
                            For example: **laufen // идти/бежать**.
                            Please, enter new german nouns only with article.
                            For example: **das Haus**.
                            Write an article from small letter: **der/die/das**.
                            A word from big letter: **Haus/Hund/Ampel.**""");
                    sendMessage(chatId, "enter a pair word");
                    queue.add(telegramId + callBackData);
                } else if (callBackData.equals("/addListAdmin")) {
                    List<String> wordList = new ArrayList<>();
                    File file = new File("C:\\Develop\\Learning\\DeutschTrainer\\src\\main\\resources\\contentList.txt");
                    try (Scanner scanner = new Scanner(file)) {
                        while (scanner.hasNextLine()) {
                            String string = scanner.nextLine();
                            wordList.add(string);
                        }
                    } catch (FileNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                    boolean create = false;
                    editKeyboard(update.getCallbackQuery(), null, "Words have been saved: ");
                    if (!wordList.isEmpty()) {
                        create = true;
                        for (String str : wordList) {
                            if (!str.isEmpty()) {
                                //create one german word
                                Long germanWord = deutschController.createNewWord(str);
                                //create a lot of russian word
                                List<Long> russianTranslate = russianController.createNewWords(str);
                                //create pairs of words
                                List<Long> pairs = deRuController.createPairs(germanWord, russianTranslate);
                                String result = deRuController.getAllWordPairsByPairId(germanWord, pairs);
                                sendMessage(chatId, result);
                            }
                        }
                    }
                    if (create) {
                        sendMessage(chatId, "Successfully!");
                        sendKeyboard(globalMenu(adminStatus), chatId, EmojiGive.germanFlag + "\n\nI'm here!!! Did someone call me???\n\nMain menu");
                    } else {
                        sendMessage(chatId, "You haven't new words, or the list is empty!");
                        sendKeyboard(globalMenu(adminStatus), chatId, EmojiGive.germanFlag + "\n\nI'm here!!! Did someone call me???\n\nMain menu");
                    }
                } else if (callBackData.equals("/userInfo")) {
                    editKeyboard(update.getCallbackQuery(), globalMenu(adminStatus), sendUserInfo(telegramId) + "\n\nMain menu");
                } else if (callBackData.equals("/aboutThisBot")) {
                    editKeyboard(update.getCallbackQuery(), globalMenu(adminStatus), """
                            I hope this bot will help me and people to study german words very fast!
                            This bot is free and also it's my opportunity to get new experience as a program developer.
                            All questions you can send me on my email:
                            salychevms@gmail.com.
                            Good luck and have fun! =)

                            Main menu""");
                } else if (callBackData.equals("/changeName")) {
                    editMessage(callbackQuery, "Change Name");
                    sendMessage(chatId, "enter new name");
                    queue.add(telegramId + callBackData);
                } else if (callBackData.equals("/changeSurname")) {
                    editMessage(callbackQuery, "Change Surname");
                    sendMessage(chatId, "enter new surname");
                    queue.add(telegramId + callBackData);
                    //if I pressed button "set new language"
                } else if (callBackData.equals("/setNewLanguage")) {
                    //I get all identifiers by my id
                    List<String> identifiers = userLanguageController.getAllLanguagesByTelegramId(telegramId);
                    //I set all identifiers in one string
                    String identifierString = String.join(" / ", identifiers);
                    //I send this string to my editMessage
                    editKeyboard(callbackQuery, setLanguageMenu(), "You already learn:\n"
                            + identifierString
                            + "\n\nSet Language");

                    //if I press "germanDE"
                } else if (callBackData.equals("/germanDE")) {
                    //I get language id by identifier
                    Optional<Language> language = languageController.getLanguageByIdentifier("DE");
                    //if this language is absent I create this language with this identifier
                    if (language.isEmpty()) {
                        languageController.createLanguage("russian", "RU");
                        languageController.createLanguage("german", "DE");
                        editKeyboard(callbackQuery, setLanguageMenu(), """
                                Language: german "DE" has been created.

                                Set Language""");
                    }
                    //when this language is exists I get all my languages by my id
                    List<String> identifiers = userLanguageController.getAllLanguagesByTelegramId(telegramId);
                    boolean found = false;
                    //and check this language in my language list
                    for (String str : identifiers) {
                        if (str.equals("DE")) {
                            found = true;
                            break;
                        }
                    }
                    //if this language doesn't exist
                    if (!found && language.isPresent()) {
                        //I save this language in my language list
                        userLanguageController.createUserLanguage(telegramId, language.get().getId());
                        editKeyboard(callbackQuery, setLanguageMenu(), """
                                Language: german "DE" has been added.

                                Set Language""");
                        //if it is existed I sent to my editMessage information I already learn
                    } else if (found && language.isPresent()) {
                        editKeyboard(callbackQuery, setLanguageMenu(), """
                                You have already learn "DE".

                                Set Language""");
                    }

                } else if (callBackData.equals("/mainMenu")) {
                    editKeyboard(update.getCallbackQuery(), globalMenu(adminStatus), EmojiGive.germanFlag + "\n\nI'm here!!! Did someone call me???\n\nMain menu");
                } else if (callBackData.startsWith("/Offer=")) {

                }
                //if user doesn't exist
            } else if (!usersController.registeredOr(telegramId)) {
                if (callBackData.equals("/registration")) {
                    usersController.createNewUser(telegramId, userName);
                    if (usersController.registeredOr(telegramId)) {
                        editKeyboard(update.getCallbackQuery(), globalMenu(false), """
                                Successfully! You're registered!

                                Main menu""");
                    } else sendMessage(chatId, "Oooopsie! Sorry, something wrong happened..." +
                            "\nWrite \"/start\" and try again!!");
                } else if (callBackData.equals("/aboutRegistration")) {
                    if (!callbackQuery.getMessage().getText().equals("You have to register!\n\nPlease, push *Registration* in this menu.")) {
                        editKeyboard(update.getCallbackQuery(), registration(), "You have to register!\n\nPlease, push *Registration* in this menu.");
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

    private InlineKeyboardMarkup addWordsMenu() {
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        //button "main menu
        InlineKeyboardButton addOneWordButton = new InlineKeyboardButton("Add Word");
        addOneWordButton.setCallbackData("/addWordAdmin");
        //button "main menu
        InlineKeyboardButton addListButton = new InlineKeyboardButton("Add Words From List");
        addListButton.setCallbackData("/addListAdmin");
        //button "main menu
        InlineKeyboardButton goToMainMenuButton = new InlineKeyboardButton("<< main menu");
        goToMainMenuButton.setCallbackData("/mainMenu");
        ////position from left to right
        List<InlineKeyboardButton> row1 = new ArrayList<>();
        List<InlineKeyboardButton> row2 = new ArrayList<>();
        List<InlineKeyboardButton> row3 = new ArrayList<>();
        //
        row1.add(addOneWordButton);
        row2.add(addListButton);
        row3.add(goToMainMenuButton);
        //position from up to down
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        rows.add(row1);
        rows.add(row2);
        rows.add(row3);
        //save buttons in the markup variable
        keyboardMarkup.setKeyboard(rows);
        return keyboardMarkup;
    }

    private InlineKeyboardMarkup adminMenu() {
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        //button "main menu
        InlineKeyboardButton languagesButton = new InlineKeyboardButton("Languages");
        languagesButton.setCallbackData("/languagesAdmin");

        //button "main menu
        InlineKeyboardButton goToMainMenuButton = new InlineKeyboardButton("<< main menu");
        goToMainMenuButton.setCallbackData("/mainMenu");
        ////position from left to right
        List<InlineKeyboardButton> row1 = new ArrayList<>();
        List<InlineKeyboardButton> row2 = new ArrayList<>();
        //
        row1.add(languagesButton);
        row2.add(goToMainMenuButton);
        //position from up to down
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        rows.add(row1);
        rows.add(row2);
        //save buttons in the markup variable
        keyboardMarkup.setKeyboard(rows);
        return keyboardMarkup;
    }

    private InlineKeyboardMarkup getSearchWordMenu(List<String> wordCollection, String translatableWord, String isTranslate) {
        final String YES = "yes";
        final String NO = "no";
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        //button
        InlineKeyboardButton goToTrainingMenuButton = new InlineKeyboardButton();
        for (String item : wordCollection) {
            InlineKeyboardButton button = new InlineKeyboardButton();
            List<InlineKeyboardButton> buttons = new ArrayList<>();
            button.setText(item);
            /* CallbackData constructor:
            for search words: "/SearchOffer="+item  ==>>  /SearchOffer=rennen
            for translate words: "/TranslationsOffer="+item  ==>>  /TranslationsOffer=rennen */
            if (isTranslate.equalsIgnoreCase(NO)) {
                //a user looks for a word and can choose from the word list, also we get a translatable word for the next step
                button.setCallbackData("/SearchOffer=" + item + "=" + translatableWord);
                goToTrainingMenuButton = new InlineKeyboardButton("< training menu");
                goToTrainingMenuButton.setCallbackData("/training");
            } else if (isTranslate.equalsIgnoreCase(YES)) {
                //this step get to a user a lot of translations for the search word
                button.setCallbackData("/TranslationsOffer=" + item + "=" + translatableWord);
                goToTrainingMenuButton = new InlineKeyboardButton("< back");
                goToTrainingMenuButton.setCallbackData("/toSearchOffer");
            }
            buttons.add(button);
            rows.add(buttons);
        }
        //button
        InlineKeyboardButton goToMainMenuButton = new InlineKeyboardButton("<< main menu");
        goToMainMenuButton.setCallbackData("/mainMenu");
        ////position from left to right
        List<InlineKeyboardButton> row1 = new ArrayList<>();
        List<InlineKeyboardButton> row2 = new ArrayList<>();
        //
        row1.add(goToTrainingMenuButton);
        row2.add(goToMainMenuButton);
        rows.add(row1);
        rows.add(row2);
        //save buttons in the markup variable
        keyboardMarkup.setKeyboard(rows);
        return keyboardMarkup;

    }

    private InlineKeyboardMarkup adminLanguagesMenu() {
        List<Language> languages = languageController.getAll();
        List<String> languageNames = new ArrayList<>();
        for (Language item : languages) {
            languageNames.add(item.getName());
        }
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        for (String item : languageNames) {
            InlineKeyboardButton button = new InlineKeyboardButton();
            List<InlineKeyboardButton> buttons = new ArrayList<>();
            button.setText(item);
            /* CallbackData constructor:  "/"+item+"Settings"  ==>>  /russianSettings*/
            button.setCallbackData("/" + item + "Settings");
            buttons.add(button);
            rows.add(buttons);
        }
        //button "main menu
        InlineKeyboardButton goToMainMenuButton = new InlineKeyboardButton("<< main menu");
        goToMainMenuButton.setCallbackData("/mainMenu");
        ////position from left to right
        List<InlineKeyboardButton> row = new ArrayList<>();
        //
        row.add(goToMainMenuButton);
        //position from up to down
        rows.add(row);
        //save buttons in the markup variable
        keyboardMarkup.setKeyboard(rows);
        return keyboardMarkup;
    }

    private InlineKeyboardMarkup statisticMenu() {
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        //button "main menu
        InlineKeyboardButton goToMainMenuButton = new InlineKeyboardButton("<< main menu");
        goToMainMenuButton.setCallbackData("/mainMenu");
        ////position from left to right
        List<InlineKeyboardButton> row1 = new ArrayList<>();
        //
        row1.add(goToMainMenuButton);
        //position from up to down
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        rows.add(row1);
        //save buttons in the markup variable
        keyboardMarkup.setKeyboard(rows);
        return keyboardMarkup;
    }

    private InlineKeyboardMarkup trainingMenu(UserLanguage userLanguage) {
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        //position from up to down
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<UserStatistic> userStatisticList=userStatisticController.getAllStatisticWithNewWords();
        int count=0;
        if (!userStatisticList.isEmpty()) {
            for (UserStatistic item: userStatisticList) {
                Long userLangIdFromStat= trainingController.getUserLangByUserStatistic(item).getId();
                Long userLangIdFromIncomingParam=userLanguage.getId();
                if(userLangIdFromIncomingParam.equals(userLangIdFromStat))
                    count++;
            }
        }
        if(count!=0) {
            //button "Daily"
            InlineKeyboardButton learningNewWordsTrainingButton = new InlineKeyboardButton("Learning New Words");
            learningNewWordsTrainingButton.setCallbackData("/Trainings=/StartLearningTraining");
            List<InlineKeyboardButton> row = new ArrayList<>();
            row.add(learningNewWordsTrainingButton);
            rows.add(row);
        }
        //button "Daily"
        InlineKeyboardButton repeatWordsTrainingButton = new InlineKeyboardButton("Repeat Training");
        repeatWordsTrainingButton.setCallbackData("/Trainings=/StartRepeatTraining");
        //button "Daily"
        InlineKeyboardButton failsTrainingButton = new InlineKeyboardButton("Fails Training");
        failsTrainingButton.setCallbackData("/Trainings=/StartFailsTraining");
        //button "Add new"
        InlineKeyboardButton addNewWordsButton = new InlineKeyboardButton("Add New Words");
        addNewWordsButton.setCallbackData("/addNewWords");
        //button "main menu
        InlineKeyboardButton goToMainMenuButton = new InlineKeyboardButton("<< main menu");
        goToMainMenuButton.setCallbackData("/mainMenu");
        ////position from left to right
        List<InlineKeyboardButton> row1 = new ArrayList<>();
        List<InlineKeyboardButton> row2 = new ArrayList<>();
        List<InlineKeyboardButton> row3 = new ArrayList<>();
        List<InlineKeyboardButton> row4 = new ArrayList<>();
        //
        row1.add(repeatWordsTrainingButton);
        row2.add(failsTrainingButton);
        row3.add(addNewWordsButton);
        row4.add(goToMainMenuButton);
        //position from up to down
        rows.add(row1);
        rows.add(row2);
        rows.add(row3);
        rows.add(row4);
        //save buttons in the markup variable
        keyboardMarkup.setKeyboard(rows);
        return keyboardMarkup;
    }

    private InlineKeyboardMarkup globalMenu(boolean ifAdmin) {
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        //button "Training"
        InlineKeyboardButton trainingButton = new InlineKeyboardButton("Training");
        trainingButton.setCallbackData("/training");
        //button "Statistic"
        InlineKeyboardButton statisticButton = new InlineKeyboardButton("Statistic");
        statisticButton.setCallbackData("/statistic");
        //button "Settings"
        InlineKeyboardButton settingsButton = new InlineKeyboardButton("Settings");
        settingsButton.setCallbackData("/settings");
        //button "Info"
        InlineKeyboardButton infoButton = new InlineKeyboardButton("Info");
        infoButton.setCallbackData("/info");
        //========for admin========
        InlineKeyboardButton adminMenuButton = new InlineKeyboardButton("Admin Menu");
        adminMenuButton.setCallbackData("/adminMenu");
        //position from left to right
        List<InlineKeyboardButton> row1 = new ArrayList<>();
        List<InlineKeyboardButton> row2 = new ArrayList<>();
        List<InlineKeyboardButton> row3 = new ArrayList<>();
        List<InlineKeyboardButton> row4 = new ArrayList<>();
        //========for admin========
        List<InlineKeyboardButton> adminRow = new ArrayList<>();
        //
        row1.add(trainingButton);
        row2.add(statisticButton);
        row3.add(settingsButton);
        row4.add(infoButton);
        //========for admin========
        adminRow.add(adminMenuButton);
        //position from up to down
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        rows.add(row1);
        rows.add(row2);
        rows.add(row3);
        rows.add(row4);
        //========for admin========
        if (ifAdmin) {
            rows.add(adminRow);
        }
        //save buttons in the markup variable
        keyboardMarkup.setKeyboard(rows);
        return keyboardMarkup;
    }

    private InlineKeyboardMarkup settingsMenu() {
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        //button "change Name"
        InlineKeyboardButton changeNameButton = new InlineKeyboardButton("Change Name");
        changeNameButton.setCallbackData("/changeName");
        //button "change Surname"
        InlineKeyboardButton changeSurnameButton = new InlineKeyboardButton("Change Surname");
        changeSurnameButton.setCallbackData("/changeSurname");
        //button "set new language"
        InlineKeyboardButton setNewLanguageButton = new InlineKeyboardButton("Set New Language");
        setNewLanguageButton.setCallbackData("/setNewLanguage");
        //button "settings menu go back"
        InlineKeyboardButton goToMainMenuButton = new InlineKeyboardButton("<< main menu");
        goToMainMenuButton.setCallbackData("/mainMenu");
        //position from left to right
        List<InlineKeyboardButton> row1 = new ArrayList<>();
        List<InlineKeyboardButton> row2 = new ArrayList<>();
        List<InlineKeyboardButton> row3 = new ArrayList<>();
        List<InlineKeyboardButton> row4 = new ArrayList<>();
        //
        row1.add(changeNameButton);
        row2.add(changeSurnameButton);
        row3.add(setNewLanguageButton);
        row4.add(goToMainMenuButton);
        //position from up to down
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        rows.add(row1);
        rows.add(row2);
        rows.add(row3);
        rows.add(row4);
        //save buttons in the markup variable
        keyboardMarkup.setKeyboard(rows);
        return keyboardMarkup;
    }

    private InlineKeyboardMarkup infoMenu() {
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        //button "User Info"
        InlineKeyboardButton userInfoButton = new InlineKeyboardButton("User Info");
        userInfoButton.setCallbackData("/userInfo");
        //button "About This Bot"
        InlineKeyboardButton aboutBotButton = new InlineKeyboardButton("About This Bot");
        aboutBotButton.setCallbackData("/aboutThisBot");
        //button "About This Bot"
        InlineKeyboardButton goToMainMenuButton = new InlineKeyboardButton("<< main menu");
        goToMainMenuButton.setCallbackData("/mainMenu");
        //position from left to right
        List<InlineKeyboardButton> row1 = new ArrayList<>();
        List<InlineKeyboardButton> row2 = new ArrayList<>();
        List<InlineKeyboardButton> row3 = new ArrayList<>();

        //
        row1.add(userInfoButton);
        row2.add(aboutBotButton);
        row3.add(goToMainMenuButton);
        //position from up to down
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        rows.add(row1);
        rows.add(row2);
        rows.add(row3);

        //save buttons in the markup variable
        keyboardMarkup.setKeyboard(rows);
        return keyboardMarkup;
    }

    private InlineKeyboardMarkup registration() {
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        //button "Registration"
        InlineKeyboardButton button = new InlineKeyboardButton("Registration");
        button.setCallbackData("/registration");
        //button "About registration"
        InlineKeyboardButton button1 = new InlineKeyboardButton("About Registration");
        button1.setCallbackData("/aboutRegistration");
        //positions
        List<InlineKeyboardButton> row = new ArrayList<>();
        List<InlineKeyboardButton> row1 = new ArrayList<>();
        //
        row.add(button);
        row1.add(button1);

        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        rows.add(row);
        rows.add(row1);

        keyboardMarkup.setKeyboard(rows);
        return keyboardMarkup;
    }

    private InlineKeyboardMarkup setLanguageMenu() {
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        //button
        InlineKeyboardButton userInfoButton = new InlineKeyboardButton("\"DE\" German");
        userInfoButton.setCallbackData("/germanDE");
        //button
        InlineKeyboardButton goToSettingsMenuButton = new InlineKeyboardButton("< settings menu");
        goToSettingsMenuButton.setCallbackData("/settings");
        //button
        InlineKeyboardButton goToMainMenuButton = new InlineKeyboardButton("<< main menu");
        goToMainMenuButton.setCallbackData("/mainMenu");
        //position from left to right
        List<InlineKeyboardButton> row1 = new ArrayList<>();
        List<InlineKeyboardButton> row2 = new ArrayList<>();
        List<InlineKeyboardButton> row3 = new ArrayList<>();
        //
        row1.add(userInfoButton);
        row2.add(goToSettingsMenuButton);
        row3.add(goToMainMenuButton);
        //position from up to down
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        rows.add(row1);
        rows.add(row2);
        rows.add(row3);

        //save buttons in the markup variable
        keyboardMarkup.setKeyboard(rows);
        return keyboardMarkup;
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
        return user.map(users -> "User: " + users.getUserName()
                + "\nUser id: " + users.getId()
                + "\nTelegram id: " + users.getTelegramId()
                + "\nFirstname: " + users.getName()
                + "\nLastname: " + users.getSurname()
                + "\nPhone: " + users.getPhoneNumber()
                + "\nRegistration date: " + users.getRegistrationDate()).orElse(null);
    }
}
