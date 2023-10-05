package de.salychevms.deutschtrainer.Models;

import jakarta.persistence.*;

import java.util.Set;
@Entity
@Table(name = "main_dictionary")
public class MainDictionary {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "mainDictionary_generator")
    private Long id;
    @Column(name = "word")
    private String word;
    @ManyToMany
    private Set<Translation> translation;

    public MainDictionary(){}
    public MainDictionary(String word, Set<Translation> translation){
        this.word=word;
        this.translation=translation;
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

    public Set<Translation> getTranslation() {
        return translation;
    }

    public void setTranslation(Set<Translation> translation) {
        this.translation = translation;
    }

    @Override
    public String toString() {
        return "MainDictionary{" +
                "id=" + id +'\'' +
                ", word='" + word + '\'' +
                ", translation=" + translation +'\'' +
                '}';
    }
}
