package com.webdev.jobify.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.webdev.jobify._aux.Comment;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;
import java.util.LinkedList;
import java.util.List;

@Entity
public class Post implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Long id;

    private String description;

    private String file;

    @JsonFormat(pattern = "dd-MM-yyyy")
    private Date date;

    @ManyToOne
    private Employee creator;

    @OneToMany
    private List<Comment> comments = new LinkedList<Comment>();

    @OneToMany
    private List<Employee> employeeLikes = new LinkedList<Employee>();

    public Post(Long id, String description, String file, Employee creator, List<Employee> employeeLikes) {
        this.id = id;
        this.description = description;
        this.file = file;
        this.creator = creator;
        this.employeeLikes = employeeLikes;
    }

    public Post() {}


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Employee getCreator() {
        return creator;
    }

    public void setCreator(Employee creator) {
        this.creator = creator;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public List<Employee> getEmployeeLikes() {
        return employeeLikes;
    }

    public void setEmployeeLikes(List<Employee> employeeLikes) {
        this.employeeLikes = employeeLikes;
    }
}
