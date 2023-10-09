package de.salychevms.deutschtrainer.Service;

import de.salychevms.deutschtrainer.Config.BotConfig;
import de.salychevms.deutschtrainer.Controllers.UsersController;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
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
        //check message
        if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            String callBackData = callbackQuery.getData();

            if (callBackData.equals("/registration")) {
                if (!usersController.registeredOr(userName)) {
                    usersController.createNewUser(userName);
                    if (usersController.registeredOr(userName)) {
                        sendMessage(chatId, "You're already registered!");
                        try {
                            execute(globalMenu(chatId, userName));
                        } catch (TelegramApiException e) {
                            throw new RuntimeException(e);
                        }
                    } else {
                        sendMessage(chatId, "Sorry! Something bad happened! Write \"/start\" and try again!");
                        try {
                            execute((registration(chatId)));
                        } catch (TelegramApiException e) {
                            throw new RuntimeException(e);
                        }
                    }
                } else {
                    sendMessage(chatId, "You're already registered!");
                    try {
                        execute(globalMenu(chatId, userName));
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                }
            } else if (callBackData.equals("/aboutRegistration")) {
                if (!usersController.registeredOr(userName)) {
                    sendMessage(chatId, "You have to registered before using this bot!\nYou have to just click \"Registration\"!");
                    try {
                        execute((registration(chatId)));
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    sendMessage(chatId, "You're already registered!");
                    try {
                        execute(globalMenu(chatId, userName));
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        } else if (update.hasMessage()) {
            Message message = update.getMessage();
            if (message.hasText()) {
                String messageText = message.getText();
                chatId = message.getChatId();
                userName = message.getChat().getUserName() != null ? message.getChat().getUserName() : "NONAME";
                if (messageText.equals("/start")) {
                    sendMessage(chatId, "Hi, " + userName + "! You activated this bot.");
                    if (!usersController.registeredOr(userName)) {
                        try {
                            execute(registration(chatId));
                        } catch (TelegramApiException e) {
                            throw new RuntimeException(e);
                        }
                    } else {
                        try {
                            execute(globalMenu(chatId, userName));
                        } catch (TelegramApiException e) {
                            throw new RuntimeException(e);
                        }
                    }
                } else {
                    sendMessage(chatId, "Sorry! It does not works.");
                }
            }
        }
    }

    private SendMessage globalMenu(long chatId, String userName) {
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
        //prepare to send
        String msg = "Hi" + userName + "! Why don't we practice our words?\n";
        SendMessage toSend = new SendMessage();
        toSend.setChatId(chatId);
        toSend.setText(msg);
        toSend.setReplyMarkup(keyboardMarkup);
        //send
        return toSend;
    }

    private SendMessage registration(long chatId) {
        String msg = "You're not registered before!\nPlease, register to use";
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        //button "Registration"
        InlineKeyboardButton button = new InlineKeyboardButton("Registration");
        button.setCallbackData("/registration");
        //button "About registration"
        InlineKeyboardButton button1 = new InlineKeyboardButton("About registration");
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

        SendMessage toSend = new SendMessage();
        toSend.setChatId(chatId);
        toSend.setText(msg);
        toSend.setReplyMarkup(keyboardMarkup);
        return toSend;
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
}
