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

    @Query(value = "select * from message m where (m.sender_id = :id1 and m.receiver_id = :id2) or "
    + "((m.sender_id = :id2 and m.receiver_id = :id1))", nativeQuery = true)
    List<Message> findMessagesOfEmployee1WithEmployee2(@Param("id1") Long id1, @Param("id2") Long id2);

}
