package de.salychevms.deutschtrainer.TrainerDataBase.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Date;

@Entity
@Getter
@Setter
@Table(name="user_statistic")
public class UserStatistic {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_statistic_generator")
    private Long Id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "word")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private UserDictionary word;
    @Column(name="new_word")
    private boolean newWord;
    @Column(name = "iterations_all")
    private Long iterationsAll;
    @Column(name = "iterations_per_month")
    private Long iterationsPerMonth;
    @Column(name = "iterations_per_week")
    private Long iterationsPerWeek;
    @Column(name = "iterations_per_day")
    private Long iterationsPerDay;
    @Column(name="fail_training")
    private Integer failTraining;
    @Column(name="fail_status")
    private boolean failStatus;
    @Column(name = "fails_all")
    private Long failsAll;
    @Column(name="fails_per_month")
    private Long failsPerMonth;
    @Column(name = "fails_per_week")
    private Long failsPerWeek;
    @Column(name="fails_per_day")
    private Long failsPerDay;
    @Column(name="last_traininig")
    private Date lastTraining;

    public UserStatistic(){}

    public UserStatistic(UserDictionary word){
        this.word=word;
        this.newWord=true;
        this.failStatus=false;
    }

    @Override
    public String toString() {
        return "UserStatistic{" +
                "Id=" + Id +'\'' +
                ", word=" + word.getId() +'\'' +
                ", newWord=" + newWord +'\'' +
                ", iterationsAll=" + iterationsAll +'\'' +
                ", iterationsPerMonth=" + iterationsPerMonth +'\'' +
                ", iterationsPerWeek=" + iterationsPerWeek +'\'' +
                ", iterationsPerDay=" + iterationsPerDay +'\'' +
                ", failTraining=" + failTraining +'\'' +
                ", failStatus=" + failStatus +'\'' +
                ", failsAll=" + failsAll +'\'' +
                ", failsPerMonth=" + failsPerMonth +'\'' +
                ", failsPerWeek=" + failsPerWeek +'\'' +
                ", failsPerDay=" + failsPerDay +'\'' +
                ", lastTraining=" + lastTraining +'\'' +
                '}';
    }
}
