package de.salychevms.deutschtrainer.Models;

import jakarta.persistence.*;

@Entity
@Table(name = "russian")
public class Russian {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "russian_generator")
    private Long id;
    @Column(name = "ru_word")
    private String ruWord;

    public Russian() {
    }

    public Russian(String word) {
        this.ruWord = word;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getWord() {
        return ruWord;
    }

    public void setWord(String word) {
        this.ruWord = word;
    }

    @Override
    public String toString() {
        return "Russian{" +
                "id=" + id + '\'' +
                ", word='" + ruWord + '\'' +
                '}';
    }
}
