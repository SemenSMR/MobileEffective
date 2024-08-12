package com.example.mobileeffective.service;

import com.example.mobileeffective.dto.CommentRequest;
import com.example.mobileeffective.entity.Comment;
import com.example.mobileeffective.entity.Task;
import com.example.mobileeffective.entity.User;
import com.example.mobileeffective.exception.InvalidInputException;
import com.example.mobileeffective.exception.ResourceNotFoundException;
import com.example.mobileeffective.repository.CommentRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class CommentService {

    private CommentRepository commentRepository;
    private TaskService taskService;
    private UserService userService;

    public void saveComment(Long taskId, CommentRequest commentRequest, String token) {

        User currentUser = userService.getCurrentUserFromToken(token);
        Task task = taskService.getTaskById(taskId);


        Comment comment = new Comment();
        comment.setText(commentRequest.getText());
        comment.setTask(task);
        comment.setAuthor(currentUser);


        if (comment.getText() == null || comment.getText().isEmpty()) {
            throw new InvalidInputException("Текст комментария не должен быть пустым");
        }

        commentRepository.save(comment);

    }

    public Page<Comment> getCommentsByTaskId(Long taskId, Pageable pageable) {
        if (taskId == null || taskId <= 0) {
            throw new InvalidInputException("Недопустимый идентификатор задачи: " + taskId);
        }

        return commentRepository.findByTask_Id(taskId, pageable);
    }

    public Comment getCommentById(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Комментарий не найден"));
    }

    public Comment updateComment(Long commentId, CommentRequest commentRequest) {
        Comment existingComment = getCommentById(commentId);
        existingComment.setText(commentRequest.getText());
        return commentRepository.save(existingComment);
    }

    public void deleteComment(Long commentId) {
        if (commentId == null || commentId <= 0) {
            throw new InvalidInputException("Недопустимый комментарий ID: " + commentId);
        }
        if (!commentRepository.existsById(commentId)) {
            throw new ResourceNotFoundException("Комментарий с идентификатором не найден: " + commentId);
        }
        commentRepository.deleteById(commentId);
    }
}
