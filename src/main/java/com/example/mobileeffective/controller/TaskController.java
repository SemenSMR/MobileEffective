package com.example.mobileeffective.controller;


import com.example.mobileeffective.entity.Task;
import com.example.mobileeffective.exception.InvalidInputException;
import com.example.mobileeffective.service.TaskService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
@RequestMapping("/api/tasks")
public class TaskController {


    TaskService taskService;

    @GetMapping
    public List<Task> getAllTasks() {
        return taskService.getAllTasks();
    }

    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody Task task) {
        if (task == null || !StringUtils.hasText(task.getTitle()) || !StringUtils.hasText(task.getDescription())) {
            throw new InvalidInputException("Недопустимая задача Заголовок и описание задачи не должны быть пустыми");
        }
        return ResponseEntity.ok(taskService.createTask(task));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody Task task) {
        return ResponseEntity.ok(taskService.updateTask(id, task));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/author/{authorId}")
    public ResponseEntity<Page<Task>> getTasksByAuthor(
            @PathVariable Long authorId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        if (authorId == null || authorId <= 0) {
            throw new InvalidInputException("Недействительный автор ID: " + authorId);
        }
        Pageable pageable = PageRequest.of(page, size);
        Page<Task> tasks = taskService.getTasksByAuthor(authorId, pageable);
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/assignee/{assigneeId}")
    public ResponseEntity<Page<Task>> getTasksByAssignee(
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
