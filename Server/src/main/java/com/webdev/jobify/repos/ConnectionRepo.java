package com.webdev.jobify.repos;

import com.webdev.jobify.model.Connection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ConnectionRepo extends JpaRepository<Connection, Long> {

    Connection findConnectionById(Long id);

    @Query(value = "select * from connections c where (c.receiver_id = :employee_id" +
            " or c.requester_id = :employee_id) and c.pending = false", nativeQuery = true)
    List<Connection> findConnectionsOfEmployee(@Param("employee_id")Long employee_id);

}
