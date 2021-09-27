package com.webdev.jobify.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;
import java.util.LinkedList;
import java.util.List;

@Entity
@Table(name = "jobad")
public class JobAd implements Serializable { // Job Advertisement

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Long id;

    @Column(nullable = false)
    private String position;

    @Column(nullable = false)
    private String company;

    private String location;

    private int type;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date startDate;

    @ManyToOne
    private Employee creator;

    @OneToMany
    private List<Employee> applicants = new LinkedList<>();

    public JobAd(Long id, String position, String company, String location, int type,
                 Date startDate, Employee creator, List<Employee> applicants) {
        this.id = id;
        this.position = position;
        this.company = company;
        this.location = location;
        this.type = type;
        this.startDate = startDate;
        this.creator = creator;
        this.applicants = applicants;
    }

    public JobAd() {}


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Employee getCreator() {
        return creator;
    }

    public void setCreator(Employee creator) {
        this.creator = creator;
    }

    public List<Employee> getApplicants() {
        return applicants;
    }

    public void setApplicants(List<Employee> appliers) {
        this.applicants = appliers;
    }
}
