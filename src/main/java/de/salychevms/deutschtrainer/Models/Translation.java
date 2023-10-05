package de.salychevms.deutschtrainer.Models;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "translation")
public class Translation {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "translation_generator")
    private Long id;
    @Column(name = "word")
    private String word;
    @ManyToMany
    private Set<MainDictionary> mainDictionaryWord;

    public Translation() {
    }

    public Translation(String word, Set<MainDictionary> mainDictionaryWord) {
        this.word = word;
        this.mainDictionaryWord = mainDictionaryWord;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public Set<MainDictionary> getMainDictionaryWord() {
        return mainDictionaryWord;
    }

    public void setMainDictionaryWord(Set<MainDictionary> mainDictionaryWord) {
        this.mainDictionaryWord = mainDictionaryWord;
    }

    @Override
    public String toString() {
        return "Translation{" +
                "id=" + id + '\'' +
                ", word='" + word + '\'' +
                ", mainDictionaryWord=" + mainDictionaryWord + '\'' +
                '}';
    }
}
