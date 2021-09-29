package com.webdev.jobify.repos;

import com.webdev.jobify._aux.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepo extends JpaRepository<Comment, Long> {
}
