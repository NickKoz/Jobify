package com.webdev.jobify.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.webdev.jobify._aux.Comment;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@Entity
public class Post implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Long id;

    private String description;

    private String file;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date date;

    @ManyToOne
    private Employee creator;

    @ManyToMany
    private List<Comment> comments = new LinkedList<>();

    @ManyToMany
    private List<Employee> likes = new LinkedList<>();

    public Post(Long id, String description, String file, Employee creator, List<Employee> likes) {
        this.id = id;
        this.description = description;
        this.file = file;
        this.creator = creator;
        this.likes = likes;
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

    public List<Employee> getLikes() {
        return likes;
    }

    public void setLikes(List<Employee> likes) {
        this.likes = likes;
    }

    public void storeFile(MultipartFile file) throws IOException {

        if(file == null) {
            this.setFile(null);
            return;
        }

        if(!file.isEmpty()) {
            byte[] bytes = file.getBytes();

            Path currRelativePath = Paths.get("");

            String uploadPath = currRelativePath.toAbsolutePath().toString() + File.separator + "src" + File.separator +
                    "main" + File.separator + "resources" + File.separator + "uploaded" + File.separator + "post" + File.separator +
                    "files" + File.separator;

            File uploadDir = new File(uploadPath);
            if(!uploadDir.exists()){
                uploadDir.mkdirs();
            }

            // Generate a new random name for post file.
            String newRandomFileName = UUID.randomUUID().toString();

            // Get its extension.
            String fileName = file.getOriginalFilename();
            String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1);

            String filePath = uploadPath + newRandomFileName + "." + fileExtension;
            Path destPath = Paths.get(filePath);

            // Save it to uploaded folder.
            Files.write(destPath, bytes);

            // Update employee's data.
            this.setFile(filePath);
        }


    }






}
