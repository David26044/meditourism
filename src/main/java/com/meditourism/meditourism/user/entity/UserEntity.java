package com.meditourism.meditourism.user.entity;
import com.meditourism.meditourism.accountState.entity.AccountStateEntity;
import com.meditourism.meditourism.role.entity.RoleEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String password;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private RoleEntity roleEntity;

    @ManyToOne
    @JoinColumn(name = "account_state_id")
    private AccountStateEntity accountStateEntity;



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

    public AccountStateEntity getAccountStateEntity() {
        return accountStateEntity;
    }

    public void setAccountStateEntity(AccountStateEntity accountStateEntity) {
        this.accountStateEntity = accountStateEntity;
    }

    public RoleEntity getRoleEntity() {
        return roleEntity;
    }

    public void setRoleEntity(RoleEntity roleEntity) {
        this.roleEntity = roleEntity;
    }

}

