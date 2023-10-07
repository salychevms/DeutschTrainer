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
                    } else
                        sendMessage(chatId, "Sorry! Something bad happened! Write \"/start\" and try again!");
                } else sendMessage(chatId, "You're already registered!");
            } else if (callBackData.equals("/aboutRegistration")) {
                if (!usersController.registeredOr(userName)) {
                    sendMessage(chatId, "You have to registered before using this bot!\nYou have to just click \"Registration\"!");
                } else sendMessage(chatId, "You're already registered!");
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
                    }
                } else {
                    sendMessage(chatId, "Sorry! It does not works.");
                }
            }
        }
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
