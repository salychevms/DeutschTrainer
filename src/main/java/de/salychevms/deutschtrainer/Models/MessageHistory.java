package de.salychevms.deutschtrainer.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Date;

@Entity
@Table(name = "message_history")
@Getter
@Setter
public class MessageHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "message_history_generator")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Users user;

    @Column(name = "addition_date", nullable = false)
    private Date additonDate;

    @Column(name = "chat_id", nullable = false)
    private Long chatId;

    @Column(name="content")
    private String content;

    @Column(name = "is_callback_data")
    private boolean isCallbackData;

    @Column(name = "is_message")
    private boolean isMessage;

    public MessageHistory() {

    }

    public MessageHistory(Users user, Long chatId, String content, boolean isMessage, boolean isCallbackData) {
        this.user = user;
        this.chatId = chatId;
        this.content=content;
        this.isMessage = isMessage;
        this.isCallbackData = isCallbackData;
    }

    @Override
    public String toString() {
        return "MessageHistory{" +
                "id=" + id + "\n" +
                ", user=" + user + "\n" +
                ", additonDate=" + additonDate + "\n" +
                ", chatId=" + chatId + "\n" +
                ", content=" + content + "\n" +
                ", isCallbackData=" + isCallbackData + "\n" +
                ", isMessage=" + isMessage + "\n" +
                '}';
    }
}
