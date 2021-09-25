package com.webdev.jobify.repos;

import com.webdev.jobify.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepo extends JpaRepository<Post, Long> {

    Post findPostById(Long id);

    List<Post> findAllByCreatorId(Long id);

}
