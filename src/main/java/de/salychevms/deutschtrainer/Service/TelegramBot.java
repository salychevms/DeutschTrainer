package de.salychevms.deutschtrainer.Service;

import de.salychevms.deutschtrainer.Config.BotConfig;
import de.salychevms.deutschtrainer.Controllers.UsersController;
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
import java.util.List;

@Component
public class TelegramBot extends TelegramLongPollingBot {
    final BotConfig config;
    final UsersController usersController;
    private long chatId;
    private String userName;
    private long telegramId;

    public TelegramBot(BotConfig config, UsersController usersController) {
        this.config = config;
        this.usersController = usersController;
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
        //check message
        //check global commands
        if (update.hasMessage()) {
            if (message.hasText()) {
                String messageText = message.getText();
                chatId = message.getChatId();
                userName = message.getChat().getUserName() != null ? message.getChat().getUserName() : "NONAME";
                telegramId = (message.getFrom().getId());

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
                    if (messageText.equals("/start")) {
                        //sendMessage(chatId, "I'm here!!! Did someone call me???\n");
                        //give global menu
                        sendKeyboard(globalMenu(), chatId, "I'm here!!! Did someone call me???\n\nMain menu");
                        //global menu
                    }
                }
            }
        } else if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            String callBackData = callbackQuery.getData();
            //if user exists
            if (usersController.registeredOr(telegramId)) {
                //reg keyboard
                if (callBackData.equals("/training")) {
                    editKeyboard(update.getCallbackQuery(), trainingMenu(), "Training");
                } else if (callBackData.equals("/statistic")) {
                    editKeyboard(update.getCallbackQuery(), statisticMenu(), "Statistic");
                } else if (callBackData.equals("/settings")) {
                    editKeyboard(update.getCallbackQuery(), settingsMenu(), "Settings");
                } else if (callBackData.equals("/info")) {
                    editKeyboard(update.getCallbackQuery(), infoMenu(), "Info");
                } else if (callBackData.equals("/userInfo")) {
                    editKeyboard(update.getCallbackQuery(), infoMenu(), sendUserInfo(telegramId) + "\n\nInfo");
                } else if (callBackData.equals("/aboutThisBot")) {
                    editKeyboard(update.getCallbackQuery(), infoMenu(), "I hope this bot will help me and people to study german words very fast!"
                            + "\nThis bot is free and also it's my opportunity to get new experience as a program developer."
                            + "\nAll questions you can send me on my email:\nsalychevms@gmail.com."
                            + "\nGood luck and have fun! =)" + "\n\nInfo");
                } else if (callBackData.equals("/changeName")) {
                    editMessage(callbackQuery, "Change Name");
                    sendMessage(chatId, "enter new name");
                } else if (callBackData.equals("/changeSurname")) {

                } else if (callBackData.equals("/setNewLanguage")) {

                } else if (callBackData.equals("/mainMenu")) {
                    editKeyboard(update.getCallbackQuery(), globalMenu(), "I'm here!!! Did someone call me???\n\nMain menu");
                }
                //if user doesn't exist
            } else if (!usersController.registeredOr(telegramId)) {
                if (callBackData.equals("/registration")) {
                    usersController.createNewUser(telegramId, userName);
                    if (usersController.registeredOr(telegramId)) {
                        editKeyboard(update.getCallbackQuery(), globalMenu(), "Successfully! You're registered!" + "\n\nMain menu");
                    } else sendMessage(chatId, "Oooopsie! Sorry, something wrong happened..." +
                            "\nWrite \"/start\" and try again!!");
                } else if (callBackData.equals("/aboutRegistration")) {
                    editKeyboard(update.getCallbackQuery(), registration(), "You have to register!\n\nPlease, push *Registration* in this menu.");
                }
            } else sendMessage(chatId, "Oooopsie! Sorry, something wrong happened..." +
                    "\nWrite \"/start\" and try again!!");
        }
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

    private InlineKeyboardMarkup globalMenu() {
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
        //position from left to right
        List<InlineKeyboardButton> row1 = new ArrayList<>();
        List<InlineKeyboardButton> row2 = new ArrayList<>();
        List<InlineKeyboardButton> row3 = new ArrayList<>();
        List<InlineKeyboardButton> row4 = new ArrayList<>();
        //
        row1.add(trainingButton);
        row2.add(statisticButton);
        row3.add(settingsButton);
        row4.add(infoButton);
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
        setNewLanguageButton.setCallbackData("/SetNewLanguage");
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
        return "User: " + userName
                + "\nUser id: " + usersController.findUserByTelegramId(telegramId).get().getId()
                + "\nTelegram id: " + usersController.findUserByTelegramId(telegramId).get().getTelegramId()
                + "\nFirstname: " + usersController.findUserByTelegramId(telegramId).get().getName()
                + "\nLastname: " + usersController.findUserByTelegramId(telegramId).get().getSurname()
                + "\nPhone: " + usersController.findUserByTelegramId(telegramId).get().getPhoneNumber()
                + "\nRegistration date: " + usersController.findUserByTelegramId(telegramId).get().getRegistrationDate();
    }
}
