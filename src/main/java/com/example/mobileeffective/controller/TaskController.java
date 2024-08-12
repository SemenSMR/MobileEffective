package com.example.mobileeffective.controller;


import com.example.mobileeffective.mappers.TaskMapper;
import com.example.mobileeffective.config.UserDetailsImpl;
import com.example.mobileeffective.dto.TaskListResponse;
import com.example.mobileeffective.entity.Task;
import com.example.mobileeffective.entity.User;
import com.example.mobileeffective.exception.InvalidInputException;
import com.example.mobileeffective.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.Authentication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
@RequestMapping("/api/tasks")
public class TaskController {


    TaskService taskService;
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Показывает задачи", description = "Позволяет Показать  все созданые задачи")
    @GetMapping
    public ResponseEntity <List<TaskListResponse>> getAllTasks() {
        List<TaskListResponse> taskResponseDTO = taskService.getAllTasks();
        return ResponseEntity.ok(taskResponseDTO);
    }


    @PostMapping
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Добавление задачи", description = "Позволяет Добавить новую задачу")
    public ResponseEntity<TaskListResponse> createTask(@RequestBody TaskListResponse taskListResponse) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User currentUser = userDetails.getUser();
        TaskListResponse createdTask = taskService.createTask(taskListResponse, currentUser);
        return ResponseEntity.ok(createdTask);
    }
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Обновление задачи", description = "Позволяет Обновить текущую задачу")
    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody TaskListResponse response) {
        Task update = taskService.updateTask(id,response);
        return ResponseEntity.ok(update);
    }
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Удаление задачи", description = "Позволяет Удалить текущую задачу")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.ok().build();
    }
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Получение задачи", description = "Позволяет Получить задачу определенным автором")
    @GetMapping("/author/{authorId}")
    public ResponseEntity<Page<TaskListResponse>> getTasksByAuthor(
            @PathVariable Long authorId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        try {
            Page<Task> tasks = taskService.getTasksByAuthor(authorId, pageable);
            Page<TaskListResponse> taskResponses = tasks.map(TaskMapper::toDTO);
            return ResponseEntity.ok(taskResponses);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Получение задачи", description = "Позволяет Получить задачу определенному исполнителю ")
    @GetMapping("/assignee/{assigneeId}")
    public ResponseEntity<Page<Task>> getTasksByAssignee
        (
            @PathVariable Long assigneeId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        if (assigneeId == null || assigneeId <= 0) {
            throw new InvalidInputException("Недействительный преемник ID: " + assigneeId);
        }
        Pageable pageable = PageRequest.of(page, size);
        Page<Task> tasks = taskService.getTasksByAssignee(assigneeId, pageable);
        return ResponseEntity.ok(tasks);
    }
}
