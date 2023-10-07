package de.salychevms.deutschtrainer.Service;

import de.salychevms.deutschtrainer.Config.BotConfig;
import de.salychevms.deutschtrainer.Controllers.UsersController;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;


@Component
public class TelegramBot extends TelegramLongPollingBot {

    final BotConfig config;
    final UsersController usersController;

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
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            //get userId
            long chatId = update.getMessage().getChatId();
            //get userName, if it hides we get default userName
            String userName = update.getMessage().getChat().getUserName() != null ? update.getMessage().getChat().getUserName() : "NONAME";

            switch (messageText) {
                case "/start":
                    sendMessage(chatId, "Hi, " + userName + "! You activated this bot.");
                    if (!usersController.registeredOr(userName)) {
                        String msg="You're not registered before!\nPlease, register to use";
                        ReplyKeyboardMarkup keyboardMarkup=new ReplyKeyboardMarkup();
                        List<KeyboardRow> rows=new ArrayList<>();
                        KeyboardRow row=new KeyboardRow();
                        row.add("Registration");
                        rows.add(row);
                        row=new KeyboardRow();
                        row.add("About registration");
                        rows.add(row);
                        keyboardMarkup.setKeyboard(rows);
                        sendRegButton(chatId,msg, keyboardMarkup);
                    }
                    break;
                case "Registration":
                    usersController.createNewUser(userName);
                    if(usersController.registeredOr(userName)){
                        sendMessage(chatId,"You're already registered!");
                    }else sendMessage(chatId, "Sorry! Something bad happened! Write \"/start\" and try again!");
                    break;
                case "About registration":
                    sendMessage(chatId,"You have to registered before using this bot!\nYou have to just click \"Registration\"!");
                    break;
                default:
                    sendMessage(chatId, "Sorry! It does not works.");
            }
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

    private void sendRegButton(long chatId, String msg, ReplyKeyboardMarkup markup) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(msg);
        message.setReplyMarkup(markup);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }

    }
}
