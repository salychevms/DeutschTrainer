package de.salychevms.deutschtrainer.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@Entity
@Table(name = "user_languages")
public class UserLanguage {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_language_generator")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "users", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Users user;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "language", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Language language;

    public UserLanguage(Users user, Language language) {
        this.user = user;
        this.language = language;
    }

    public UserLanguage() {

    }

    @Override
    public String toString() {
        return "UserLanguage{" +
                "id=" + id + '\'' +
                ", users=" + user.getTelegramId() + '\'' +
                ", language=" + language.getId() + '\'' +
                '}';
    }
}
