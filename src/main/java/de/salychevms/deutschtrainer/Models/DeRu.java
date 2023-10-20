package de.salychevms.deutschtrainer.Models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "de_ru")
public class DeRu {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "de_ru_generator")
    private Long id;
    @Column(name = "deutsch_id")
    private Long deutschId;

    @Column(name = "russian_id")
    private Long russianId;

    public DeRu() {
    }

    public DeRu(Long deutschId, Long russianId) {
        this.deutschId = deutschId;
        this.russianId = russianId;
    }

    @Override
    public String toString() {
        return "DeRu{" +
                "id=" + id + '\'' +
                ", deutschId=" + deutschId + '\'' +
                ", russianId=" + russianId + '\'' +
                '}';
    }
}
