package com.example.mobileeffective.service;

import com.example.mobileeffective.entity.Comment;
import com.example.mobileeffective.exception.InvalidInputException;
import com.example.mobileeffective.exception.ResourceNotFoundException;
import com.example.mobileeffective.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;

    public Comment saveComment(Comment comment) {
        if (comment == null || comment.getTask() == null || comment.getAuthor() == null) {
            throw new InvalidInputException("Комментарий, задача и автор не должны быть пустыми");
        }
        return commentRepository.save(comment);
    }

    public Page<Comment> getCommentsByTaskId(Long taskId, Pageable pageable) {
        if (taskId == null || taskId <= 0) {
            throw new InvalidInputException("Недопустимая задача ID: " + taskId);
        }
        return commentRepository.findByTaskId(taskId, pageable);
    }

    public Comment getCommentById(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Комментарий не найден"));
    }

    public Comment updateComment(Long commentId, Comment updatedComment) {
        Comment existingComment = getCommentById(commentId);
        existingComment.setText(updatedComment.getText());
        return commentRepository.save(existingComment);
    }

    public void deleteComment(Long commentId) {
        if (!commentRepository.existsById(commentId)) {
            throw new ResourceNotFoundException("Комментарий с идентификатором не найден: " + commentId);
        }
        commentRepository.deleteById(commentId);
    }
}
