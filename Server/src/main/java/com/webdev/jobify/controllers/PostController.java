package com.webdev.jobify.controllers;


import com.webdev.jobify._aux.Comment;
import com.webdev.jobify.assemblers.EmployeeModelAssembler;
import com.webdev.jobify.assemblers.PostModelAssembler;
import com.webdev.jobify.model.Employee;
import com.webdev.jobify.model.Post;
import com.webdev.jobify.services.CommentService;
import com.webdev.jobify.services.EmployeeService;
import com.webdev.jobify.services.PostService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@RestController
@CrossOrigin(origins = "https://localhost:4200")
@RequestMapping("/post")
public class PostController {

    private final PostService postService;
    private final PostModelAssembler assembler;
    private final EmployeeService employeeService;
    private final EmployeeModelAssembler employeeAssembler;
    private final CommentService commentService;

    public PostController(PostService postService, PostModelAssembler assembler, EmployeeService employeeService, EmployeeModelAssembler employeeAssembler, CommentService commentService) {
        this.postService = postService;
        this.assembler = assembler;
        this.employeeService = employeeService;
        this.employeeAssembler = employeeAssembler;
        this.commentService = commentService;
    }

    @GetMapping("/{id}")
    public EntityModel<Post> getPostById(@PathVariable("id") Long id) {
        Post post = postService.findPostById(id);
        return assembler.toModel(post);
    }

    @GetMapping("/{id}/comments")
    public List<Comment> getCommentsOfPost(@PathVariable("id") Long id) {
        Post post = postService.findPostById(id);
        return post.getComments();
    }

    @GetMapping("/{id}/likes")
    public CollectionModel<EntityModel<Employee>> getLikesOfPost(@PathVariable("id") Long id) {
        Post post = postService.findPostById(id);
        List<EntityModel<Employee>> likes = post.getLikes().stream().map(employeeAssembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(likes, linkTo(methodOn(EmployeeController.class).getAllEmployees()).withSelfRel());
    }


    @GetMapping("/all")
    public CollectionModel<EntityModel<Post>> getAllPosts() {
        List<EntityModel<Post>> posts = postService.findAllPosts().stream().map(assembler::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(posts, linkTo(methodOn(PostController.class).getAllPosts()).withSelfRel());
    }

    @PostMapping("/add")
    public ResponseEntity<?> addPostToEmployee(@RequestPart(value = "post") Post newPost,
                                               @RequestPart(value = "file", required = false) MultipartFile file,
                                               @RequestParam("id")Long employee_id) throws IOException {

        newPost.storeFile(file);

        EntityModel<Post> entityModel;

        try {
            Employee employee = employeeService.findEmployeeById(employee_id);

            newPost.setCreator(employee);

            entityModel = assembler.toModel(postService.savePost(newPost));
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cannot add post!");
        }

        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);

    }

    @PostMapping("/like")
    public ResponseEntity<?> addLikeToPost(@RequestParam("post_id") Long post_id, @RequestParam("employee_id")Long employee_id) {

        try {
            Post post = postService.findPostById(post_id);

            Employee employee = employeeService.findEmployeeById(employee_id);

            // Adding employee's like to post.
            LinkedList<Employee> employeeLikes = new LinkedList<>(post.getLikes());

            employeeLikes.push(employee);

            post.setLikes(employeeLikes);

            postService.savePost(post);
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cannot add like to post!");
        }

        return ResponseEntity.status(HttpStatus.OK).body("Like added to post!");
    }

    @PostMapping("/comment")
    @ResponseBody
    public ResponseEntity<?> addCommentToPost(@RequestParam("post_id") Long post_id, @RequestBody Comment comment) {

        System.out.println(comment.toString());

        try {
            Post post = postService.findPostById(post_id);

            comment = commentService.saveComment(comment);

            LinkedList<Comment> postComments = new LinkedList<>(post.getComments());

            postComments.push(comment);

            post.setComments(postComments);

            postService.savePost(post);
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cannot add comment to post!");
        }

        return ResponseEntity.status(HttpStatus.OK).body("Comment added to post!");
    }









}
