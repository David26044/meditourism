package com.meditourism.meditourism.blockedUser.entity;

import com.meditourism.meditourism.user.entity.UserEntity;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "blocked_users")
public class BlockedUserEntity {

    @Id
    @Column(name = "user_id")
    private Long id;

    @OneToOne
    @MapsId  // Esto hace que el id de esta entidad sea el mismo que el id del usuario
    @JoinColumn(name = "user_id")
    private UserEntity blockedUser;

    @Column(nullable = false)
    private String reason;

    @CreationTimestamp
    @Column
    private LocalDateTime date;

    public Long getId() {
        return id;
    }

    // No seteamos el id directamente, se obtiene del blockedUser

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
