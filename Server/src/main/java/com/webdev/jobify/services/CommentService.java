package com.webdev.jobify.services;

import com.webdev.jobify._aux.Comment;
import com.webdev.jobify.repos.CommentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentService {

    public final CommentRepo commentRepo;

    @Autowired
    public CommentService(CommentRepo commentRepo) {
        this.commentRepo = commentRepo;
    }

    public Comment saveComment(Comment comment) {
        return commentRepo.save(comment);
    }

}
