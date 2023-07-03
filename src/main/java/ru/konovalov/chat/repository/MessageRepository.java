package ru.konovalov.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.konovalov.chat.model.Message;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findByTag(String tag);
}
