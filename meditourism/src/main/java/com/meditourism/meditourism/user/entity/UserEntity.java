package com.meditourism.meditourism.user.entity;
import com.meditourism.meditourism.blockedUser.entity.BlockedUserEntity;
import com.meditourism.meditourism.comment.entity.CommentEntity;
import com.meditourism.meditourism.contactForm.entity.ContactFormEntity;
import com.meditourism.meditourism.report.entity.ReportEntity;
import com.meditourism.meditourism.review.entity.ReviewEntity;
import com.meditourism.meditourism.role.entity.RoleEntity;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
public class UserEntity implements Serializable, UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String password;

    @Column(name="is_verified")
    private boolean isVerified;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private RoleEntity roleEntity;

    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public RoleEntity getRoleEntity() {
        return roleEntity;
    }

    public void setRoleEntity(RoleEntity roleEntity) {
        this.roleEntity = roleEntity;
    }


    /**
     * @return 
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (roleEntity != null && roleEntity.getName() != null) {
            return List.of(new SimpleGrantedAuthority(roleEntity.getName()));
        }
        return List.of(); // Return empty list if no role assigned
    }

    /**
     * @return
     */
    @Override
    public String getUsername() {
        return email;
    }

    /**
     * @return 
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * @return 
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * @return 
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * @return 
     */
    @Override
    public boolean isEnabled() {
        return isVerified;
    }

    @OneToMany(mappedBy = "userEntity", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<CommentEntity> comments;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<ContactFormEntity> contactForms;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<ReviewEntity> reviews;

    @OneToOne(mappedBy = "blockedUser", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private BlockedUserEntity blockedUser;

    @OneToMany(mappedBy = "reporterUser", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<ReportEntity> reports;

}

