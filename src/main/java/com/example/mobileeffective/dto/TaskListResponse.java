package com.example.mobileeffective.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskListResponse {

    private String title;

    private String description;

    private String status;

    private String priority;

    private Long assignee;

}
