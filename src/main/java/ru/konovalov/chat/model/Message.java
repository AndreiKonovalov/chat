package ru.konovalov.chat.model;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;

@Entity
@Getter
@Setter
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String text;

    private String tag;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User author;

    private String filename;

    public Message(){
    }

    public Message(String text, String tag, User user){
        this.author = user;
        this.text = text;
        this.tag = tag;
    }
    public String getAuthorName(){

        return author != null ? author.getUsername() : "<none>";
    }
}
