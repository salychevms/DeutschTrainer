package de.salychevms.deutschtrainer.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Date;

@Entity
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
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public UserDictionary getWord() {
        return word;
    }

    public void setWord(UserDictionary word) {
        this.word = word;
    }

    public boolean isNewWord() {
        return newWord;
    }

    public void setNewWord(boolean newWord) {
        this.newWord = newWord;
    }

    public Long getIterationsAll() {
        return iterationsAll;
    }

    public void setIterationsAll(Long iterationsAll) {
        this.iterationsAll = iterationsAll;
    }

    public Long getIterationsPerMonth() {
        return iterationsPerMonth;
    }

    public void setIterationsPerMonth(Long iterationsPerMonth) {
        this.iterationsPerMonth = iterationsPerMonth;
    }

    public Long getIterationsPerWeek() {
        return iterationsPerWeek;
    }

    public void setIterationsPerWeek(Long iterationsPerWeek) {
        this.iterationsPerWeek = iterationsPerWeek;
    }

    public Long getIterationsPerDay() {
        return iterationsPerDay;
    }

    public void setIterationsPerDay(Long iterationsPerDay) {
        this.iterationsPerDay = iterationsPerDay;
    }

    public Long getFailsAll() {
        return failsAll;
    }

    public void setFailsAll(Long failsAll) {
        this.failsAll = failsAll;
    }

    public Long getFailsPerMonth() {
        return failsPerMonth;
    }

    public void setFailsPerMonth(Long failsPerMonth) {
        this.failsPerMonth = failsPerMonth;
    }

    public Long getFailsPerWeek() {
        return failsPerWeek;
    }

    public void setFailsPerWeek(Long failsPerWeek) {
        this.failsPerWeek = failsPerWeek;
    }

    public Long getFailsPerDay() {
        return failsPerDay;
    }

    public void setFailsPerDay(Long failsPerDay) {
        this.failsPerDay = failsPerDay;
    }

    public Date getLastTraining() {
        return lastTraining;
    }

    public void setLastTraining(Date lastTraining) {
        this.lastTraining = lastTraining;
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
                ", failsAll=" + failsAll +'\'' +
                ", failsPerMonth=" + failsPerMonth +'\'' +
                ", failsPerWeek=" + failsPerWeek +'\'' +
                ", failsPerDay=" + failsPerDay +'\'' +
                ", lastTraining=" + lastTraining +'\'' +
                '}';
    }
}
