package de.salychevms.deutschtrainer.Models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "users")
public class Users {
    @Id
    @Column(name = "telegram_id", nullable = false, unique = true)
    private Long telegramId;
    @Column(name = "name")
    private String name;
    @Column(name = "surname")
    private String surname;
    @Column(name = "user_name", nullable = false, unique = true)
    private String userName;
    @Column(name = "phone_number", unique = true)
    private String phoneNumber;
    @Column(name = "registration_date", nullable = false)
    private Date registrationDate;
    @Column(name = "admin")
    private boolean admin;

    public Users() {
    }

    public Users(Long telegramId, String userName, Date registrationDate) {
        this.userName = userName;
        this.registrationDate = registrationDate;
        this.telegramId = telegramId;
        this.admin = false;
    }

    @Override
    public String toString() {
        return "Users{" +
                ", telegramId=" + telegramId + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", userName='" + userName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", registrationDate=" + registrationDate +
                ", admin=" + admin +
                '}';
    }
}
