package com.example.mobileeffective.controller;

import com.example.mobileeffective.entity.Comment;
import com.example.mobileeffective.entity.User;
import com.example.mobileeffective.exception.ErrorResponse;
import com.example.mobileeffective.exception.InvalidInputException;
import com.example.mobileeffective.service.CommentService;
import com.example.mobileeffective.service.TaskService;
import com.example.mobileeffective.service.UserService;
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.media.Content;
//import io.swagger.v3.oas.annotations.media.Schema;
//import io.swagger.v3.oas.annotations.responses.ApiResponse;
//import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    TaskService taskService;

    CommentService commentService;

    UserService userService;


    @PostMapping()
    public ResponseEntity<String> addComment(@PathVariable Long taskId, @RequestBody Comment comment, @RequestHeader("Authorization") String token) {
        if (taskId == null || taskId <= 0) {
            throw new InvalidInputException("Неверный идентификатор задачи: " + taskId);
        }
        if (comment == null || comment.getText() == null || comment.getText().isEmpty()) {
            throw new InvalidInputException("Текст комментария не должен быть пустым");
        }
        User currentUser = userService.getCurrentUserFromToken(token);
        comment.setTask(taskService.getTaskById(taskId));
        comment.setAuthor(currentUser);
        return ResponseEntity.ok("Комментарий создан успешно");
    }
//    @Operation(
//            summary = "Получить комментарий",
//            description = "Получить комментарий для задачи",
//            tags = {"task, get"}
//    )
//    @ApiResponses({
//            @ApiResponse(responseCode = "200", description = "Книга успешно получена",
//                    content = @Content(schema = @Schema(implementation = ResponseEntity.class), mediaType = "application/json")),
//            @ApiResponse(responseCode = "404", description = "Недопустимая задача ID",
//                    content = @Content(schema = @Schema(implementation = InvalidInputException.class), mediaType = "application/json")),
//            @ApiResponse(responseCode = "400", description = "ID не прошло валидацию",
//                    content = @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json"))
//    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public ResponseEntity<Page<Comment>> getCommentsForTask(
            @PathVariable Long taskId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        if (taskId == null || taskId <= 0) {
            throw new InvalidInputException("Недопустимая задача ID: " + taskId);
        }
        Pageable pageable = PageRequest.of(page, size);
        Page<Comment> comments = commentService.getCommentsByTaskId(taskId, pageable);
        return ResponseEntity.ok(comments);
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<Comment> updateComment(
            @PathVariable Long commentId,
            @RequestBody Comment updatedComment) {

        Comment comment = commentService.updateComment(commentId, updatedComment);
        return ResponseEntity.ok(comment);
    }

    @DeleteMapping("{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.ok("Комментарий с ID " + commentId + " был успешно удален.");
    }
//    @Operation(
//            summary = "Получить книгу",
//            description = "Получить книгу по ID",
//            tags = {"book, get"}
//    )
//    @ApiResponses({
//            @ApiResponse(responseCode = "200", description = "Книга успешно получена",
//                    content = @Content(schema = @Schema(implementation = ResponseEntity.class), mediaType = "application/json")),
//            @ApiResponse(responseCode = "404", description = "Книга с таким ID не найдена",
//                    content = @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")),
//            @ApiResponse(responseCode = "400", description = "ID не прошло валидацию",
//                    content = @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json"))
//    })

//    @GetMapping("/{id}")
//    public GetBookViewDTO getBookViewById(@PathVariable @Parameter(description = "ID книги") @Positive Integer id) throws BookNotFoundException {
//        log.info("Begin get bookView: id={}", id);
//        return service.getBookViewById(id);
//    }
}
