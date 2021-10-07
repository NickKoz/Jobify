package com.webdev.jobify.services;


import com.webdev.jobify.model.Post;
import com.webdev.jobify.repos.PostRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {

    public final PostRepo postRepo;

    @Autowired
    public PostService(PostRepo postRepo) {this.postRepo = postRepo;}

    public Post findPostById(Long id) {
        return postRepo.findPostById(id);
    }

    public List<Post> findAllPosts() {
        return postRepo.findAll();
    }

    public Post savePost(Post post) {return postRepo.save(post);}

    public List<Post> findPostsByCreatorId(Long id) {
        return postRepo.findAllByCreatorId(id);
    }
}
