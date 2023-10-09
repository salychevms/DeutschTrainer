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
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_dictionary_generator")
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
    private Deutsch word;
    @Column(name = "date_added")
    private Date dateAdded;

    public UserDictionary(){}
    public UserDictionary(UserLanguage userLanguage, Deutsch word, Date dateAdded){
        this.userLanguage=userLanguage;
        this.word=word;
        this.dateAdded = dateAdded;
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

    public Deutsch getWord() {
        return word;
    }

    public void setWord(Deutsch word) {
        this.word = word;
    }

    public Date getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(Date dateAdded) {
        this.dateAdded = dateAdded;
    }

    @Override
    public String toString() {
        return "UserDictionary{" +
                "id=" + id +'\'' +
                ", userLanguage=" + userLanguage +'\'' +
                ", word=" + word +'\'' +
                ", additionDate=" + dateAdded +'\'' +
                '}';
    }
}
