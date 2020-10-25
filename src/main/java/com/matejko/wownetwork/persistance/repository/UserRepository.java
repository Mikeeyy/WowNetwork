package com.matejko.wownetwork.persistance.repository;

import com.matejko.wownetwork.persistance.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByNickname(final String nickname);
}
