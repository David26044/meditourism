package com.meditourism.meditourism.user.repository;

import com.meditourism.meditourism.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    //Si lo agrego acá, JPA lo va a crear en tiempo de ejecucion. Entonces, hay que definir el metodo con la convencion
    //correcta y jpa se encarga de crearlo en tiempo de ejecucion
    boolean existsByEmail(String email);

    Optional<UserEntity> findByEmail(String email);
}

