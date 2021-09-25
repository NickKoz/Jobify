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

}
