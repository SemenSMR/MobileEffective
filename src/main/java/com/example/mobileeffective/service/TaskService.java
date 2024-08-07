package com.example.mobileeffective.service;

import com.example.mobileeffective.entity.Task;
import com.example.mobileeffective.exception.InvalidInputException;
import com.example.mobileeffective.exception.ResourceNotFoundException;
import com.example.mobileeffective.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Task createTask(Task task) {
        return taskRepository.save(task);
    }

    public Task updateTask(Long id, Task task) {
        Task existingTask = taskRepository.findById(id).orElseThrow(() -> new RuntimeException("Задача не найдена"));
        existingTask.setTitle(task.getTitle());
        existingTask.setDescription(task.getDescription());
        existingTask.setStatus(task.getStatus());
        existingTask.setPriority(task.getPriority());
        existingTask.setAssignee(task.getAssignee());
        return taskRepository.save(existingTask);
    }

    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }

    public Task getTaskById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Задача не найдена"));
    }

    public Page<Task> getTasksByAuthor(Long authorId, Pageable pageable) {
        if (authorId == null || authorId <= 0) {
            throw new InvalidInputException("Недействительный автор ID: " + authorId);
        }
        return taskRepository.findByAuthorId(authorId, pageable);
    }

    public Page<Task> getTasksByAssignee(Long assigneeId, Pageable pageable) {
        if (assigneeId == null || assigneeId <= 0) {
            throw new InvalidInputException("Недействительный преемник ID: " + assigneeId);
        }
        return taskRepository.findByAssigneeId(assigneeId, pageable);
    }
}
