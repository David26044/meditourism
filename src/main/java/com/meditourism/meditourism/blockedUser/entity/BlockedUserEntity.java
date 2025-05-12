package com.meditourism.meditourism.blockedUser.entity;

import com.meditourism.meditourism.user.entity.UserEntity;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
public class BlockedUserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "blocked_user_id", nullable = false)
    private UserEntity blockedUser;

    @Column(nullable = false)
    private String reason;

    @Column
    @CreationTimestamp
    private LocalDateTime date = LocalDateTime.now();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserEntity getBlockedUser() {
        return blockedUser;
    }

    public void setBlockedUser(UserEntity blockedUser) {
        this.blockedUser = blockedUser;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}
