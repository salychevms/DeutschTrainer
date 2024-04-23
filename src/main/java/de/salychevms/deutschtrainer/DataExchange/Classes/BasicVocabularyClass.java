package de.salychevms.deutschtrainer.DataExchange.Classes;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BasicVocabularyClass {
    private Long pairId;
    private Long deId;
    private Long ruId;
    private String deWord;
    private String ruWord;

    public BasicVocabularyClass(){}

    public BasicVocabularyClass(Long pairId, Long deId, Long ruId, String deWord, String ruWord) {
        this.pairId = pairId;
        this.deId = deId;
        this.ruId = ruId;
        this.deWord = deWord;
        this.ruWord = ruWord;
    }
}
