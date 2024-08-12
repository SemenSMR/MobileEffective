package com.example.mobileeffective.controller;

import com.example.mobileeffective.dto.CommentRequest;
import com.example.mobileeffective.entity.Comment;
import com.example.mobileeffective.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tasks/{taskId}/comments")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class CommentController {
    CommentService commentService;

    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Добавление коментария", description = "Позволяет добавить коментарий к задаче")
    @PostMapping()
    public ResponseEntity<String> addComment(@PathVariable Long taskId, @RequestBody CommentRequest commentRequest, @RequestHeader("Authorization") String token) {

        commentService.saveComment(taskId, commentRequest, token);
        return ResponseEntity.ok("Comment successfully added ");
    }
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Получить комментарии", description = "Получаем комментарии по id задачи ")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public ResponseEntity<Page<Comment>> getCommentsForTask(
            @PathVariable Long taskId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Comment> comments = commentService.getCommentsByTaskId(taskId, pageable);
        return ResponseEntity.ok(comments);
    }
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Изменение комментария", description = "Позволяет изменить комментарий по id коментарию")
    @PutMapping("/{commentId}")
    public ResponseEntity<Comment> updateComment(
            @PathVariable Long commentId,
            @RequestBody CommentRequest commentRequest) {

        Comment comment = commentService.updateComment(commentId, commentRequest);
        return ResponseEntity.ok(comment);
    }
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Удаление коментария", description = "Позволяет удалить комментарий по id комментария")
    @DeleteMapping("{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.ok("Комментарий с ID " + commentId + " successfully deleted");
    }

}
