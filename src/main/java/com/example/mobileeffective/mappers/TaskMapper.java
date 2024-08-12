package com.example.mobileeffective.mappers;

import com.example.mobileeffective.dto.TaskListResponse;
import com.example.mobileeffective.entity.Task;
import com.example.mobileeffective.entity.User;

public class TaskMapper {

    public static Task toEntity(TaskListResponse dto, User author, User assignee) {
        return new Task(null, dto.getTitle(), dto.getDescription(), dto.getStatus(), dto.getPriority(), author, assignee, null);
    }

    public static TaskListResponse toDTO(Task task) {
        return new TaskListResponse(task.getTitle(), task.getDescription(), task.getStatus(), task.getPriority(), task.getAssigneeId());
    }

    }



