package com.example.mobileeffective.repository;

import com.example.mobileeffective.entity.Comment;
import com.example.mobileeffective.entity.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    Page<Task> findByAuthor_Id(Long authorId, Pageable pageable);
    Page<Task> findByAssignee_Id(Long assigneeId, Pageable pageable);
}
