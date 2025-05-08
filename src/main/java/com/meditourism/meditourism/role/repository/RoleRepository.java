package com.meditourism.meditourism.role.repository;

import com.meditourism.meditourism.role.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
}
