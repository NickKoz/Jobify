package com.webdev.jobify._aux;

import com.webdev.jobify.model.Employee;

import javax.persistence.*;

@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    Long id;

    String text;

    @ManyToOne
    Employee creator;

    public Comment(Long id, String text, Employee creator) {
        this.id = id;
        this.text = text;
        this.creator = creator;
    }

    public Comment() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Employee getCreator() {
        return creator;
    }

    public void setCreator(Employee creator) {
        this.creator = creator;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", creator=" + creator.getId() +
                '}';
    }
}
