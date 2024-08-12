package com.example.mobileeffective.service;

import com.example.mobileeffective.dto.TaskListResponse;
import com.example.mobileeffective.entity.Task;
import com.example.mobileeffective.entity.User;
import com.example.mobileeffective.exception.InvalidInputException;
import com.example.mobileeffective.exception.ResourceNotFoundException;
import com.example.mobileeffective.mappers.TaskMapper;
import com.example.mobileeffective.repository.TaskRepository;
import com.example.mobileeffective.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Service
public class TaskService {


    private TaskRepository taskRepository;

    private UserRepository userRepository;


    public List<TaskListResponse> getAllTasks() {
        List<Task> tasks = taskRepository.findAll();
        return tasks.stream()
                .map(TaskMapper::toDTO)
                .collect(Collectors.toList());
    }

    public TaskListResponse createTask(TaskListResponse taskListResponse, User currentUser) {

        User assignee = userRepository.findById(taskListResponse.getAssignee())
                .orElseThrow(() -> new InvalidInputException("Исполнитель с ID " + taskListResponse.getAssignee() + " не найден"));

        Task task = TaskMapper.toEntity(taskListResponse, currentUser, assignee);

        if (task == null || !StringUtils.hasText(task.getTitle()) || !StringUtils.hasText(task.getDescription())) {
            throw new InvalidInputException("Недопустимая задача Заголовок и описание задачи не должны быть пустыми");
        }
        Task savedTask = taskRepository.save(task);
        return TaskMapper.toDTO(savedTask);
    }

    public Task updateTask(Long id, TaskListResponse task) {
        Task existingTask = taskRepository.findById(id).orElseThrow(() -> new RuntimeException("Задача не найдена"));
        existingTask.setTitle(task.getTitle());
        existingTask.setDescription(task.getDescription());
        existingTask.setStatus(task.getStatus());
        existingTask.setPriority(task.getPriority());

        if (task.getAssignee() != null) {
            User assignee = userRepository.findById(task.getAssignee())
                    .orElseThrow(() -> new RuntimeException("Исполнитель не найден"));
            existingTask.setAssignee(assignee);
        } else {
            existingTask.setAssignee(null);
        }
        return taskRepository.save(existingTask);
    }

    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }

    public Task getTaskById(Long id) {
        if (id == null || id <= 0) {
            throw new InvalidInputException("Неверный идентификатор задачи: " + id);
        }


        return taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Задача не найдена"));
    }

    public Page<Task> getTasksByAuthor(Long authorId, Pageable pageable) {
        if (authorId == null || authorId <= 0) {
            throw new InvalidInputException("Недействительный автор ID: " + authorId);
        }
        return taskRepository.findByAuthor_Id(authorId, pageable);
    }

    public Page<Task> getTasksByAssignee(Long assigneeId, Pageable pageable) {
        if (assigneeId == null || assigneeId <= 0) {
            throw new InvalidInputException("Недействительный преемник ID: " + assigneeId);
        }
        return taskRepository.findByAssignee_Id(assigneeId, pageable);
    }
}
