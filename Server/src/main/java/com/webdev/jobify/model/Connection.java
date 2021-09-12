package com.webdev.jobify.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "connections")
public class Connection implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Long id;

    @ManyToOne
    private Employee requester;

    @ManyToOne
    private Employee receiver;

    private boolean pending = true;

    public Connection() {}


    public Connection(Long id, Employee requester, Employee receiver) {
        this.id = id;
        this.requester = requester;
        this.receiver = receiver;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Employee getRequester() {
        return requester;
    }

    public void setRequester(Employee requester) {
        this.requester = requester;
    }

    public Employee getReceiver() {
        return receiver;
    }

    public void setReceiver(Employee receiver) {
        this.receiver = receiver;
    }

    public boolean isPending() {
        return pending;
    }

    public void setPending(boolean pending) {
        this.pending = pending;
    }

}
