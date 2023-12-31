package ru.konovalov.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.konovalov.chat.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);

    User findByActivationCode(String code);
}
