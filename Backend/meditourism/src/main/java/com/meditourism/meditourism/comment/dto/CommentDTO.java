package com.meditourism.meditourism.comment.dto;

import com.meditourism.meditourism.comment.entity.CommentEntity;
import com.meditourism.meditourism.review.entity.ReviewEntity;
import com.meditourism.meditourism.user.dto.UserDTO;
import com.meditourism.meditourism.user.entity.UserEntity;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

public class CommentDTO {
    private Long id;
    private String content;
    private LocalDateTime date;
    private UserDTO userDTO;
}
