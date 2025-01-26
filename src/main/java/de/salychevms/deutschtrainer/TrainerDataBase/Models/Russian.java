package de.salychevms.deutschtrainer.TrainerDataBase.Models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
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

    @Override
    public String toString() {
        return "Russian{" +
                "id=" + id + '\'' +
                ", word='" + ruWord + '\'' +
                '}';
    }
}
