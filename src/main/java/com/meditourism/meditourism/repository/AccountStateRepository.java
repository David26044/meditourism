package com.meditourism.meditourism.repository;

import com.meditourism.meditourism.entity.AccountStateEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountStateRepository extends JpaRepository<AccountStateEntity, Long> {
}
