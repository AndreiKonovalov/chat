package ru.konovalov.chat.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;
import org.hibernate.validator.constraints.Length;

@Entity
@Getter
@Setter
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotBlank(message = "Вы не ввели сообщение!")
    @Length(max = 2048, message = "Слишком большой текст(более 2 кБ)")
    private String text;
    @Length(max = 255, message = "Слишком большой текст тэга")
    private String tag;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User author;

    private String filename;

    public Message() {
    }

    public Message(String text, String tag, User user) {
        this.author = user;
        this.text = text;
        this.tag = tag;
    }

    public String getAuthorName() {

        return author != null ? author.getUsername() : "<none>";
    }
}
