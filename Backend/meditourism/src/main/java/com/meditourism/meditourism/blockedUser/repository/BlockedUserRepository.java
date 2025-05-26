package com.meditourism.meditourism.blockedUser.repository;

import com.meditourism.meditourism.blockedUser.entity.BlockedUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlockedUserRepository extends JpaRepository<BlockedUserEntity, Long> {
}
