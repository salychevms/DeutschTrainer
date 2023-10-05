package de.salychevms.deutschtrainer.Models;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "users")
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_generator")
    private Long id;
    @Column(name = "username", nullable = false, unique = true)
    private String userName;
    @Column(name = "telegram_id", unique = true)
    private String telegramId;
    @Column(name = "phone_number", unique = true)
    private String phoneNumber;
    @Column(name = "registration_date", nullable = false)
    private Date registrationDate;

    public Users() {
    }

    public Users(String userName, Date registrationDate) {
        this.userName = userName;
        this.registrationDate = registrationDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTelegramId() {
        return telegramId;
    }

    public void setTelegramId(String telegramId) {
        this.telegramId = telegramId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }

    @Override
    public String toString() {
        return "Users{" +
                "id=" + id +
                ", telegramId='" + telegramId + '\'' +
                ", userName='" + userName + '\'' +
                ", registrationDate=" + registrationDate +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }
}
