package de.salychevms.deutschtrainer.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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
}
