package de.salychevms.deutschtrainer.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;


@Entity
@Getter
@Setter
@Table(name = "de_ru")
public class DeRuPairs {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "de_ru_generator")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "deutsch")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Deutsch deutsch;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "russian")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Russian russian;

    public DeRuPairs() {
    }

    public DeRuPairs(Deutsch deutsch, Russian russian) {
        this.deutsch = deutsch;
        this.russian = russian;
    }

    @Override
    public String toString() {
        return "De-Ru pair{" +
                "id=" + id + '\'' +
                ", deutsch=" + deutsch.getId() + '\'' +
                ", russian=" + russian.getId() + '\'' +
                '}';
    }
}
