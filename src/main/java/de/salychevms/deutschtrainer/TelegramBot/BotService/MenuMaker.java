package de.salychevms.deutschtrainer.TelegramBot.BotService;

import de.salychevms.deutschtrainer.TrainerDataBase.Controllers.UserStatisticController;
import de.salychevms.deutschtrainer.TelegramBot.Emojies.EmojiGive;
import de.salychevms.deutschtrainer.TrainerDataBase.Models.UserLanguage;
import de.salychevms.deutschtrainer.TrainerDataBase.Models.UserStatistic;
import org.springframework.stereotype.Component;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

@Component
public class MenuMaker {
    private final UserStatisticController userStatisticController;

    public MenuMaker(UserStatisticController userStatisticController) {
        this.userStatisticController = userStatisticController;
    }

    InlineKeyboardMarkup adminMenu() {
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        //buttons
        InlineKeyboardButton addWordsButton = new InlineKeyboardButton("Добавить новое слово в общий словарь");
        addWordsButton.setCallbackData("/adminMenu/addWordsAdmin");
        InlineKeyboardButton addListButton = new InlineKeyboardButton("Добавить список слов в общий словарь");
        addListButton.setCallbackData("/adminMenu/addListAdmin");
        //button
        InlineKeyboardButton goToMainMenuButton = new InlineKeyboardButton("<<" + EmojiGive.joystick + " в главное меню");
        goToMainMenuButton.setCallbackData("/mainMenu");
        ////position from left to right
        List<InlineKeyboardButton> row1 = new ArrayList<>();
        List<InlineKeyboardButton> row2 = new ArrayList<>();
        List<InlineKeyboardButton> row3 = new ArrayList<>();
        //
        row1.add(addWordsButton);
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

    InlineKeyboardMarkup getSearchWordMenu(Map<Long, String> forTrimming, String translatableWord, String lang, String isTranslate) {
        final String YES = "yes";
        final String NO = "no";

        Map<Long, String> mapCollection = new HashMap<>();
        int count = 0;
        //first 20 words
        if (forTrimming.size() > 20) {
            for (Entry<Long, String> entry : forTrimming.entrySet()) {
                if (count > 20) {
                    break;
                }
                mapCollection.put(entry.getKey(), entry.getValue());
                count++;
            }
        } else {
            mapCollection = forTrimming;
        }

        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        //button
        InlineKeyboardButton goToTrainingMenuButton = new InlineKeyboardButton();
        for (Entry<Long, String> item : mapCollection.entrySet()) {
            InlineKeyboardButton button = new InlineKeyboardButton();
            List<InlineKeyboardButton> buttons = new ArrayList<>();
            button.setText(item.getValue());
            String substr1 = " <<еще варианты>>";
            String substr2 = " <<уже добавлено>>";
            if (item.getValue().contains(substr1)) {
                mapCollection.put(item.getKey(), "");
            }
            if (item.getValue().contains(substr2)) {
                mapCollection.put(item.getKey(), "");
            }
            /* CallbackData constructor:
            for search words (/so - searchOffer): "/so="+item  ==>>  /so=rennen
            for translate words (/to - translationsOffer): "/to="+item  ==>>  /to=rennen */
            if (isTranslate.equalsIgnoreCase(NO)) {
                //a user looks for a word and can choose from the word list, also we get a translatable word for the next step
                button.setCallbackData("/so=" + item.getKey().toString() + "=" + lang + "=" + translatableWord);// /so - searchOffer
                goToTrainingMenuButton = new InlineKeyboardButton("<" + EmojiGive.gameDie + " к меню тренировок");
                goToTrainingMenuButton.setCallbackData("/tr");// /tr - training
            } else if (isTranslate.equalsIgnoreCase(YES)) {
                //this step gets to a user a lot of translations for the search word;
                button.setCallbackData("/to=" + item.getKey().toString() + "=" + lang + "=" + translatableWord);// /to - translationsOffer
                goToTrainingMenuButton = new InlineKeyboardButton("<" + EmojiGive.backArrow + " назад");
                goToTrainingMenuButton.setCallbackData("/toSearchOffer");
            }
            buttons.add(button);
            rows.add(buttons);
        }
        //button
        InlineKeyboardButton goToMainMenuButton = new InlineKeyboardButton("<<" + EmojiGive.joystick + " в главное меню");
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

    InlineKeyboardMarkup statisticMenu() {
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        //button "main menu
        InlineKeyboardButton goToMainMenuButton = new InlineKeyboardButton("<<" + EmojiGive.joystick + " в главное меню");
        goToMainMenuButton.setCallbackData("/mainMenu");
        ////position from left to right
        List<InlineKeyboardButton> row1 = new ArrayList<>();
        //
        row1.add(goToMainMenuButton);
        ////position from up to down
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        rows.add(row1);
        //save buttons in the markup variable
        keyboardMarkup.setKeyboard(rows);
        return keyboardMarkup;
    }

    InlineKeyboardMarkup trainingMenu(Long telegramId, UserLanguage userLanguage, String languageIdentifier) {
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        //position from up to down
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        int countNewWords= userStatisticController.getCountPairsWithNewWordForUserAndLanguage(telegramId, languageIdentifier);
        int countWordsWithFailStatusTrue= userStatisticController.countWordsWithFailStatusForUserAndLanguage(telegramId, languageIdentifier);
        //new words training button
        List<UserStatistic> userStatisticListOrderByNewWord = userStatisticController.getAllStatisticWithNewWords(userLanguage);
        if (!userStatisticListOrderByNewWord.isEmpty()) {

            InlineKeyboardButton learningNewWordsTrainingButton = new InlineKeyboardButton(EmojiGive.newButton + " Учить новые слова ("+countNewWords+")");
            learningNewWordsTrainingButton.setCallbackData("/tr=/sLT");// /tr=/sLT - training/StartLearningTraining
            List<InlineKeyboardButton> row = new ArrayList<>();
            row.add(learningNewWordsTrainingButton);
            rows.add(row);
        }
        //fails training button
        List<UserStatistic> userStatisticListOrderByFails = userStatisticController.getAllStatisticWithFailsAllDesc(userLanguage);
        List<UserStatistic> freshFailsStatistic = new ArrayList<>();
        for (UserStatistic value : userStatisticListOrderByFails) {
            if (value.getFailsPerDay() != null || value.getFailsPerWeek() != null || value.getFailsPerMonth() != null) {
                freshFailsStatistic.add(value);
            }
        }
        if (!freshFailsStatistic.isEmpty()) {
            InlineKeyboardButton failsTrainingButton = new InlineKeyboardButton(EmojiGive.redQuestionMark + " Ошиблись - повторите ("+countWordsWithFailStatusTrue+")");
            failsTrainingButton.setCallbackData("/tr=/sFT");// /tr=/sFT - training/StartFailsTraining
            List<InlineKeyboardButton> row = new ArrayList<>();
            row.add(failsTrainingButton);
            rows.add(row);
        }

        List<UserStatistic> ifWordsExist = userStatisticController.getAllStatisticWithIterationsAllAsc(userLanguage);
        if (!ifWordsExist.isEmpty()) {
            InlineKeyboardButton repeatWordsTrainingButton = new InlineKeyboardButton(EmojiGive.repeatButton + " Ежедневная тренировка");
            repeatWordsTrainingButton.setCallbackData("/tr=/sRT");// /tr=/sRT - training/StartRepeatTraining
            List<InlineKeyboardButton> row = new ArrayList<>();
            row.add(repeatWordsTrainingButton);
            rows.add(row);
        }

        //button "Add new"
        InlineKeyboardButton addNewWordsButton = new InlineKeyboardButton(EmojiGive.inboxTray + " Добавить новые слова");
        addNewWordsButton.setCallbackData("/tr/addNewWords");// /tr=/addNewWords - training/addNewWords
        //button "main menu
        InlineKeyboardButton goToMainMenuButton = new InlineKeyboardButton("<<" + EmojiGive.joystick + " в главное меню");
        goToMainMenuButton.setCallbackData("/mainMenu");
        ////position from left to right
        List<InlineKeyboardButton> row1 = new ArrayList<>();
        List<InlineKeyboardButton> row2 = new ArrayList<>();
        //
        row1.add(addNewWordsButton);
        row2.add(goToMainMenuButton);
        //position from up to down
        rows.add(row1);
        rows.add(row2);
        //save buttons in the markup variable
        keyboardMarkup.setKeyboard(rows);
        return keyboardMarkup;
    }

    InlineKeyboardMarkup mainMenu(boolean ifAdmin) {
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        //button "Training"
        InlineKeyboardButton trainingButton = new InlineKeyboardButton(EmojiGive.gameDie + " Тренировки");
        trainingButton.setCallbackData("/tr");// /tr - training
        //button "Statistic"
        InlineKeyboardButton statisticButton = new InlineKeyboardButton(EmojiGive.barChart + " Статитстика");
        statisticButton.setCallbackData("/statistic");
        //button "Settings"
        InlineKeyboardButton settingsButton = new InlineKeyboardButton(EmojiGive.wrench + " Настройки");
        settingsButton.setCallbackData("/settings");
        //button "Info"
        InlineKeyboardButton infoButton = new InlineKeyboardButton(EmojiGive.clipboard + " Инфо");
        infoButton.setCallbackData("/info");
        //========for admin========
        InlineKeyboardButton adminMenuButton = new InlineKeyboardButton(EmojiGive.lockedWithKey + " Admin Menu");
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

    InlineKeyboardMarkup settingsMenu() {
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        //button "change Name"
        InlineKeyboardButton changeNameButton = new InlineKeyboardButton("Сменить имя");
        changeNameButton.setCallbackData("/settings/changeName");
        //button "change Surname"
        InlineKeyboardButton changeSurnameButton = new InlineKeyboardButton("Сменить фамилию");
        changeSurnameButton.setCallbackData("/settings/changeSurname");
        //button "settings menu go back"
        InlineKeyboardButton goToMainMenuButton = new InlineKeyboardButton("<<" + EmojiGive.joystick + " в главное меню");
        goToMainMenuButton.setCallbackData("/mainMenu");
        //position from left to right
        List<InlineKeyboardButton> row1 = new ArrayList<>();
        List<InlineKeyboardButton> row2 = new ArrayList<>();
        //List<InlineKeyboardButton> row3 = new ArrayList<>();
        List<InlineKeyboardButton> row4 = new ArrayList<>();
        //
        row1.add(changeNameButton);
        row2.add(changeSurnameButton);
        //row3.add(setNewLanguageButton);
        row4.add(goToMainMenuButton);
        //position from up to down
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        rows.add(row1);
        rows.add(row2);
        //rows.add(row3);
        rows.add(row4);
        //save buttons in the markup variable
        keyboardMarkup.setKeyboard(rows);
        return keyboardMarkup;
    }

    InlineKeyboardMarkup infoMenu() {
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        //button "User Info"
        InlineKeyboardButton userInfoButton = new InlineKeyboardButton(EmojiGive.alien + " Инфо о пользователе");
        userInfoButton.setCallbackData("/info/userInfo");
        //button "About This Bot"
        InlineKeyboardButton aboutBotButton = new InlineKeyboardButton(EmojiGive.robot + " Об этом боте");
        aboutBotButton.setCallbackData("/info/aboutThisBot");
        //button "About This Bot"
        InlineKeyboardButton goToMainMenuButton = new InlineKeyboardButton("<<" + EmojiGive.joystick + " в главное меню");
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

    InlineKeyboardMarkup registration() {
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
}
