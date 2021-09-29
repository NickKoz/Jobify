package com.webdev.jobify.repos;

import com.webdev.jobify.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageRepo extends JpaRepository<Message, Long> {

    Message findMessageById(Long id);

//    @Query(value = "select * from message m where m.sender_id = :id")
//    List<Message> findMessagesOfEmployee(@Param("id") Long id);

    @Query(value = "select * from message m where m.sender_id = :id or m.receiver_id = :id",nativeQuery = true)
    List<Message> findMessagesOfEmployee(@Param("id") Long id);

}
