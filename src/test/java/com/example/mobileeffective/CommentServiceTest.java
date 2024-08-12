package com.example.mobileeffective;

import com.example.mobileeffective.entity.Comment;
import com.example.mobileeffective.exception.InvalidInputException;
import com.example.mobileeffective.repository.CommentRepository;
import com.example.mobileeffective.service.CommentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class CommentServiceTest {
    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private CommentService commentService;
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    public void testGetCommentsByTaskId_Success() {

        Long taskId = 1L;
        Pageable pageable = PageRequest.of(0, 10);
        Comment comment1 = new Comment();
        Comment comment2 = new Comment();
        Page<Comment> page = new PageImpl<>(Arrays.asList(comment1, comment2), pageable, 2);

        when(commentRepository.findByTask_Id(taskId, pageable)).thenReturn(page);


        Page<Comment> result = commentService.getCommentsByTaskId(taskId, pageable);


        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
    }

    @Test
    public void testGetCommentsByTaskId_InvalidTaskId() {

        Long invalidTaskId = -1L;
        Pageable pageable = PageRequest.of(0, 10);


        InvalidInputException thrown = assertThrows(
                InvalidInputException.class,
                () -> commentService.getCommentsByTaskId(invalidTaskId, pageable)
        );
        assertEquals("Недопустимый идентификатор задачи: -1", thrown.getMessage());
    }
}
