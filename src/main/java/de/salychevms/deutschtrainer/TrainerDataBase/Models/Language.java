package de.salychevms.deutschtrainer.TrainerDataBase.Models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "language")
public class Language {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "language_generator")
    private Long id;
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "identifier", nullable = false, unique = true)
    private String identifier;

    public Language() {
    }

    public Language(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Language{" +
                "id=" + id + '\'' +
                ", name='" + name + '\'' +
                ", identifier='" + identifier + '\'' +
                '}';
    }
}
