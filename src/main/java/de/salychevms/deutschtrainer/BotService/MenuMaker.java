package de.salychevms.deutschtrainer.BotService;

import de.salychevms.deutschtrainer.Controllers.UserStatisticController;
import de.salychevms.deutschtrainer.Emojies.EmojiGive;
import de.salychevms.deutschtrainer.Models.UserLanguage;
import de.salychevms.deutschtrainer.Models.UserStatistic;
import de.salychevms.deutschtrainer.Training.TrainingController;
import org.springframework.stereotype.Component;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Component
public class MenuMaker {
    private final UserStatisticController userStatisticController;
    private final TrainingController trainingController;

    public MenuMaker(UserStatisticController userStatisticController, TrainingController trainingController) {
        this.userStatisticController = userStatisticController;
        this.trainingController = trainingController;
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

    InlineKeyboardMarkup getSearchWordMenu(List<String> wordCollection, String translatableWord, String isTranslate) {
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
                goToTrainingMenuButton = new InlineKeyboardButton("<" + EmojiGive.gameDie + " к меню тренировок");
                goToTrainingMenuButton.setCallbackData("/training");
            } else if (isTranslate.equalsIgnoreCase(YES)) {
                //this step get to a user a lot of translations for the search word
                button.setCallbackData("/TranslationsOffer=" + item + "=" + translatableWord);
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
        //position from up to down
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        rows.add(row1);
        //save buttons in the markup variable
        keyboardMarkup.setKeyboard(rows);
        return keyboardMarkup;
    }

    InlineKeyboardMarkup trainingMenu(UserLanguage userLanguage) {
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        //position from up to down
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<UserStatistic> userStatisticList = userStatisticController.getAllStatisticWithNewWords(userLanguage);
        System.out.println(userStatisticList);/////////////////////////////////////////////////////////////////////////////////////
        int count = 0;
        if (!userStatisticList.isEmpty()) {
            for (UserStatistic item : userStatisticList) {
                Long userLangIdFromStat = trainingController.getUserLangByUserStatistic(item).getId();
                Long userLangIdFromIncomingParam = userLanguage.getId();
                if (userLangIdFromIncomingParam.equals(userLangIdFromStat))
                    count++;
            }
        }
        if (count != 0) {
            //button "Daily"
            InlineKeyboardButton learningNewWordsTrainingButton = new InlineKeyboardButton(EmojiGive.newButton + " Учить новые слова");
            learningNewWordsTrainingButton.setCallbackData("/training=/StartLearningTraining");
            List<InlineKeyboardButton> row = new ArrayList<>();
            row.add(learningNewWordsTrainingButton);
            rows.add(row);
        }
        //button "Daily"
        InlineKeyboardButton repeatWordsTrainingButton = new InlineKeyboardButton(EmojiGive.repeatButton + " Ежедневная тренировка");
        repeatWordsTrainingButton.setCallbackData("/training=/StartRepeatTraining");
        //button "Daily"
        InlineKeyboardButton failsTrainingButton = new InlineKeyboardButton(EmojiGive.redQuestionMark + " Ошиблись - повторите");
        failsTrainingButton.setCallbackData("/training=/StartFailsTraining");
        //button "Add new"
        InlineKeyboardButton addNewWordsButton = new InlineKeyboardButton(EmojiGive.inboxTray + " Добавить новые слова");
        addNewWordsButton.setCallbackData("/training/addNewWords");
        //button "main menu
        InlineKeyboardButton goToMainMenuButton = new InlineKeyboardButton("<<" + EmojiGive.joystick + " в главное меню");
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

    InlineKeyboardMarkup mainMenu(boolean ifAdmin) {
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        //button "Training"
        InlineKeyboardButton trainingButton = new InlineKeyboardButton(EmojiGive.gameDie + " Тренировки");
        trainingButton.setCallbackData("/training");
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
        //button "set new language"
        /*InlineKeyboardButton setNewLanguageButton = new InlineKeyboardButton("Установить новый язык");
        setNewLanguageButton.setCallbackData("/setNewLanguage");*/
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
