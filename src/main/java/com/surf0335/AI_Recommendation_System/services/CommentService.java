package com.surf0335.AI_Recommendation_System.services;

import com.surf0335.AI_Recommendation_System.model.Comment;
import com.surf0335.AI_Recommendation_System.repository.CommentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CommentService {

    @Autowired
    private CommentRepo commentRepository;

    public Comment saveComment(Comment comment) {
        return commentRepository.save(comment);
    }

    public List<Comment> getCommentsByTrainerId(Long trainerId) {
        // Assuming a method to find comments by trainerId is defined in the repository
        return commentRepository.findByTrainerId(trainerId);
    }
}
