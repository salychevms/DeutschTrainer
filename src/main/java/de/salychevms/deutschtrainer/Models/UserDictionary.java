package de.salychevms.deutschtrainer.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Date;

@Entity
@Table(name = "user_dictionary")
public class UserDictionary {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_generator")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "user_language_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private UserLanguage userLanguage;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "main_dictionary_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private MainDictionary word;
    @Column(name = "addition_date")
    private Date additionDate;

    public UserDictionary(){}
    public UserDictionary(UserLanguage userLanguage, MainDictionary word, Date additionDate){
        this.userLanguage=userLanguage;
        this.word=word;
        this.additionDate = additionDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserLanguage getUserLanguage() {
        return userLanguage;
    }

    public void setUserLanguage(UserLanguage userLanguage) {
        this.userLanguage = userLanguage;
    }

    public MainDictionary getWord() {
        return word;
    }

    public void setWord(MainDictionary word) {
        this.word = word;
    }

    public Date getAdditionDate() {
        return additionDate;
    }

    public void setAdditionDate(Date additionDate) {
        this.additionDate = additionDate;
    }

    @Override
    public String toString() {
        return "UserDictionary{" +
                "id=" + id +'\'' +
                ", userLanguage=" + userLanguage +'\'' +
                ", word=" + word +'\'' +
                ", additionDate=" + additionDate +'\'' +
                '}';
    }
}
