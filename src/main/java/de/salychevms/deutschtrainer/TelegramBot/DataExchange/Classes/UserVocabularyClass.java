package de.salychevms.deutschtrainer.TelegramBot.DataExchange.Classes;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class UserVocabularyClass extends BasicVocabularyClass {
    private Long telegramId;
    private Long userDictionaryId;
    private Date dateAdded;
    private Long userLanguageId;
    private Long languageId;
    private String languageIdentifier;

    public UserVocabularyClass(Long telegramId, Long userDictionaryId, Date dateAdded, Long userLanguageId, Long languageId, String languageIdentifier) {
        this.telegramId = telegramId;
        this.userDictionaryId = userDictionaryId;
        this.dateAdded = dateAdded;
        this.userLanguageId = userLanguageId;
        this.languageId = languageId;
        this.languageIdentifier = languageIdentifier;
    }

    public UserVocabularyClass(Long pairId, Long deId, Long ruId, String deWord, String ruWord) {
        super(pairId, deId, ruId, deWord, ruWord);
    }

    public UserVocabularyClass() {
    }
}
