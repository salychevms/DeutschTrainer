package de.salychevms.deutschtrainer.Models;

import jakarta.persistence.*;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDeutschId() {
        return deutschId;
    }

    public void setDeutschId(Long deutschId) {
        this.deutschId = deutschId;
    }

    public Long getRussianId() {
        return russianId;
    }

    public void setRussianId(Long russianId) {
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
