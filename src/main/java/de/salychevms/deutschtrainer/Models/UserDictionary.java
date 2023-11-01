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
    @JoinColumn(name = "user_language")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private UserLanguage userLanguage;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "pair")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private DeRu pair;
    @Column(name = "date_added")
    private Date dateAdded;

    public UserDictionary(){}
    public UserDictionary(UserLanguage userLanguage, DeRu pair, Date dateAdded){
        this.userLanguage=userLanguage;
        this.pair=pair;
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

    public DeRu getPair() {
        return pair;
    }

    public void setPair(DeRu pair) {
        this.pair = pair;
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
                ", pair=" + pair +'\'' +
                ", additionDate=" + dateAdded +'\'' +
                '}';
    }
}
