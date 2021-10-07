package com.webdev.jobify.model;


import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import javax.persistence.*;


@Entity
public class Employee implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String surname;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    private String jobTitle;
    private String phone;
    private String photo;

    @ElementCollection
    @CollectionTable(name = "skills")
    @Column(name = "skill")
    private List<String> skills = new LinkedList<String>();

    private int jobAdViews = 0;

    public Employee() {}

    public Employee(String name, String surname, String password, String email, String phone, String photo, List<String> skills
                    , int jobAdViews){
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
        this.jobTitle = null;
        this.phone = phone;
        this.photo = photo;
        this.skills = skills;
        this.jobAdViews = jobAdViews;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getSkills() {
        return skills;
    }

    public void setSkills(List<String> skills) {
        this.skills = skills;
    }

    public int getJobAdViews() {
        return jobAdViews;
    }

    public void setJobAdViews(int jobAdViews) {
        this.jobAdViews = jobAdViews;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", jobTitle='" + jobTitle + '\'' +
                ", phone='" + phone + '\'' +
                ", photo='" + photo + '\'' +
                '}';
    }

    public void updateProfilePicture(MultipartFile picture) throws IOException {

        if(picture == null) {
            this.setPhoto(null);
            return;
        }

        if(!picture.isEmpty()){
            byte[] bytes = picture.getBytes();
            Path currRelativePath = Paths.get("");

            String uploadPath = currRelativePath.toAbsolutePath().toString() + File.separator + "src" + File.separator +
                    "main" + File.separator + "resources" + File.separator + "uploaded" + File.separator + "profile" + File.separator +
                    "pictures" + File.separator;

            File uploadDir = new File(uploadPath);
            if(!uploadDir.exists()){
                uploadDir.mkdirs();
            }

            // Generate a new random name for profile picture.
            String newRandomPictureName = UUID.randomUUID().toString();

            // Get its extension.
            String pictureName = picture.getOriginalFilename();
            String pictureExtension = pictureName.substring(pictureName.lastIndexOf(".") + 1);

            String picturePath = uploadPath + newRandomPictureName + "." + pictureExtension;
            Path destPath = Paths.get(picturePath);

            // Save it to uploaded folder.
            Files.write(destPath, bytes);

            // Update employee's data.
            this.setPhoto(picturePath);
        }
    }
}
