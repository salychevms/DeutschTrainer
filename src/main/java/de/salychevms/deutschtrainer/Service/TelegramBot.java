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

            //if user exists
            if (usersController.registeredOr(userName)) {
                if (callBackData.equals("/registration")) {
                    sendMessage(chatId, "You're already registered!");
                    sendGlobalKeyboard(chatId, userName);
                } else if (callBackData.equals("/aboutRegistration")) {
                    sendMessage(chatId, "You're already registered!");
                    sendGlobalKeyboard(chatId, userName);
                }

                //if user doesn't exist
            } else if (!usersController.registeredOr(userName)) {
                if (callBackData.equals("/registration")) {
                    usersController.createNewUser(userName);
                    if (usersController.registeredOr(userName)) {
                        sendMessage(chatId, "You're already registered!");
                        sendGlobalKeyboard(chatId, userName);
                    } else sendMessage(chatId, "Oooopsie! Sorry, something wrong happened..." +
                            "\nWrite \"/start\" and try again!!");
                } else if (callBackData.equals("/aboutRegistration")) {
                    sendRegistrationKeyboard(chatId, "You have to register!");
                }
            } else sendMessage(chatId, "Oooopsie! Sorry, something wrong happened..." +
                    "\nWrite \"/start\" and try again!!");

            //check global commands
        } else if (update.hasMessage()) {
            Message message = update.getMessage();
            if (message.hasText()) {
                String messageText = message.getText();
                chatId = message.getChatId();
                userName = message.getChat().getUserName() != null ? message.getChat().getUserName() : "NONAME";

                if (!usersController.registeredOr(userName)) {
                    //if user sent /start
                    if (messageText.equals("/start")) {
                        sendMessage(chatId, "Hi, " + userName + "! You activated this bot.");
                        //give start menu
                        sendRegistrationKeyboard(chatId, "You have to register.");
                        //if something unusual happened
                    } else {
                        sendMessage(chatId, "Sorry! It does not works.\nSend command: \"/start\" again, please!");
                    }
                    //if user already exists give global menu
                } else if (usersController.registeredOr(userName)) {
                    if (messageText.equals("/start")) {
                        sendMessage(chatId, "I'm here!!! Did someone call me???\n");
                    }
                    //give global menu
                    sendGlobalKeyboard(chatId, userName);
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
        String msg = "Hi, " + userName + "! Why don't we practice new words?\n";
        SendMessage toSend = new SendMessage();
        toSend.setChatId(chatId);
        toSend.setText(msg);
        toSend.setReplyMarkup(keyboardMarkup);
        //send
        return toSend;
    }

    private SendMessage registration(long chatId, String msg) {
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
        toSend.setText(msg);
        toSend.setChatId(chatId);
        toSend.setReplyMarkup(keyboardMarkup);

        return toSend;
    }

    private void sendGlobalKeyboard(long chatId, String userName) {
        try {
            execute(globalMenu(chatId, userName));
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendRegistrationKeyboard(long chatId, String msg) {
        try {
            execute(registration(chatId, msg));
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
}
