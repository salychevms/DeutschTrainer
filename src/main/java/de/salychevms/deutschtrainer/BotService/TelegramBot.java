package de.salychevms.deutschtrainer.BotService;

import de.salychevms.deutschtrainer.BotConfig.BotConfig;
import de.salychevms.deutschtrainer.Controllers.*;
import de.salychevms.deutschtrainer.Models.Language;
import de.salychevms.deutschtrainer.Models.Users;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Component
public class TelegramBot extends TelegramLongPollingBot {
    final BotConfig config;
    final UsersController usersController;
    final LanguageController languageController;
    final UserLanguageController userLanguageController;
    final MessageHistoryController messageHistoryController;
    final DeutschController deutschController;
    final RussianController russianController;
    final DeRuController deRuController;

    public TelegramBot(BotConfig config, UsersController usersController, LanguageController languageController, UserLanguageController userLanguageController, MessageHistoryController messageHistoryController,
                       DeutschController deutschController, RussianController russianController, DeRuController deRuController) {
        this.config = config;
        this.usersController = usersController;
        this.languageController = languageController;
        this.userLanguageController = userLanguageController;
        this.messageHistoryController = messageHistoryController;
        this.deutschController = deutschController;
        this.russianController = russianController;
        this.deRuController = deRuController;
    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }

    List<String> queue = new ArrayList<>();

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
                    sendKeyboard(globalMenu(adminStatus), chatId, "I'm here!!! Did someone call me???\n\nMain menu");
                    //global menu
                } else if (queue!=null) {
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
                //reg keyboard
                if (callBackData.equals("/training")) {
                    editKeyboard(update.getCallbackQuery(), trainingMenu(), "Training");
                } else if (callBackData.equals("/statistic")) {
                    editKeyboard(update.getCallbackQuery(), statisticMenu(), "Statistic");
                } else if (callBackData.equals("/settings")) {
                    editKeyboard(update.getCallbackQuery(), settingsMenu(), "Settings");
                } else if (callBackData.equals("/info")) {
                    editKeyboard(update.getCallbackQuery(), infoMenu(), "Info");
                } else if (callBackData.equals("/adminMenu")) {
                    editKeyboard(update.getCallbackQuery(), adminMenu(), "Amin Menu");
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
                    editKeyboard(update.getCallbackQuery(), globalMenu(adminStatus), "I'm here!!! Did someone call me???\n\nMain menu");
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
                    }else editMessage(update.getCallbackQuery(), "You're haven't registered!\nPress /start and get a sign up, please!");
                }
            } else sendMessage(chatId, "Oooopsie! Sorry, something wrong happened..." +
                    "\nWrite \"/start\" and try again!!");
        }

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

    private InlineKeyboardMarkup trainingMenu() {
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        //button "Daily"
        InlineKeyboardButton dailyTrainingButton = new InlineKeyboardButton("Daily Training");
        dailyTrainingButton.setCallbackData("/dailyTraining");
        //button "main menu
        InlineKeyboardButton goToMainMenuButton = new InlineKeyboardButton("<< main menu");
        goToMainMenuButton.setCallbackData("/mainMenu");
        ////position from left to right
        List<InlineKeyboardButton> row1 = new ArrayList<>();
        List<InlineKeyboardButton> row2 = new ArrayList<>();
        //
        row1.add(dailyTrainingButton);
        row2.add(goToMainMenuButton);
        //position from up to down
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        rows.add(row1);
        rows.add(row2);
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
