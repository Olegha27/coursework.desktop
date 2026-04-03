package com.example.courseworkitfu.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@Entity
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(length = 1000, nullable = false)
    private String text;

    @ManyToOne
    private FoodOrder order;

    @ManyToOne
    private User sender;

    private LocalDateTime sentAt;

    private boolean edited;

    public Message(String text, FoodOrder order, User sender) {
        this.text = text;
        this.order = order;
        this.sender = sender;
        this.sentAt = LocalDateTime.now();
        this.edited = false;
    }

    @Override
    public String toString() {
        String senderName = sender != null ? sender.getUsername() : "Unknown";
        String time = sentAt != null ? sentAt.toString() : "";
        String editMark = edited ? " (edited)" : "";
        return "[" + time + "] " + senderName + ": " + text + editMark;
    }
}