package de.salychevms.deutschtrainer.Models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
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

    @Override
    public String toString() {
        return "Deutsch{" +
                "id=" + id + '\'' +
                ", word='" + deWord + '\'' +
                '}';
    }
}
