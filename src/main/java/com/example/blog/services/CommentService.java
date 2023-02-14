package com.example.blog.services;

import com.example.blog.entites.Comment;
import com.example.blog.entites.Post;
import com.example.blog.exceptions.ResourceNotFoundException;
import com.example.blog.payloads.ApiResponse;
import com.example.blog.payloads.CommentDto;
import com.example.blog.repositories.CommentRepository;
import com.example.blog.repositories.PostRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private ModelMapper modelMapper;

    public CommentDto createComment(CommentDto commentDto, Integer postId) {
        Post post = this.postRepository.findById(postId).orElseThrow(()->new ResourceNotFoundException("Post","posId",postId));
        Comment comment = this.modelMapper.map(commentDto,Comment.class);
        comment.setPost(post);
        Comment savedComment = this.commentRepository.save(comment);
        return this.modelMapper.map(savedComment,CommentDto.class);
    }

    public void deleteComment(Integer commentId) {
        Comment deleteComment = this.commentRepository.findById(commentId).orElseThrow(()-> new ResourceNotFoundException("Comment","CommentId",commentId));
        this.commentRepository.delete(deleteComment);
    }
}
