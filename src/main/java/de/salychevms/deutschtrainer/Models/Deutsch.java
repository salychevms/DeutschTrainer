package de.salychevms.deutschtrainer.Models;

import jakarta.persistence.*;


@Entity
@Table(name = "deutsch")
public class Deutsch {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "deutsch_generator")
    private Long id;
    @Column(name = "de_word")
    private String deWord;

    public Deutsch() {
    }

    public Deutsch(String deWord) {
        this.deWord = deWord;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDeWord() {
        return deWord;
    }

    public void setDeWord(String deWord) {
        this.deWord = deWord;
    }

    @Override
    public String toString() {
        return "Deutsch{" +
                "id=" + id + '\'' +
                ", word='" + deWord + '\'' +
                '}';
    }
}
