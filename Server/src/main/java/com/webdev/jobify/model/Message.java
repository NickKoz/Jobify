package com.webdev.jobify.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Table(name = "message")
public class Message implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Long id;

    @ManyToOne
    private Employee sender;

    @ManyToOne
    private Employee receiver;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private Timestamp timeSent;

    @Column(nullable = false)
    private String text;

    public Message() {}

    public Message(Employee sender, Employee receiver, String text, Timestamp timeSent) {
        this.sender = sender;
        this.receiver = receiver;
        this.text = text;
        this.timeSent = timeSent;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Employee getSender() {
        return sender;
    }

    public void setSender(Employee sender) {
        this.sender = sender;
    }

    public Employee getReceiver() {
        return receiver;
    }

    public void setReceiver(Employee receiver) {
        this.receiver = receiver;
    }

    public Timestamp getTimeSent() {
        return timeSent;
    }

    public void setTimeSent(Timestamp timeSent) {
        this.timeSent = timeSent;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }


}
