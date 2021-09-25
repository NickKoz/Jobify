package com.webdev.jobify.services;


import com.webdev.jobify.model.Connection;
import com.webdev.jobify.repos.ConnectionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConnectionService {

    public final ConnectionRepo connectionRepo;

    @Autowired
    public ConnectionService(ConnectionRepo connectionRepo) {
        this.connectionRepo = connectionRepo;
    }

    public Connection findConnectionById(Long id) {
        return connectionRepo.findConnectionById(id);
    }

    public List<Connection> findAllConnections() {
        return connectionRepo.findAll();
    }

    public Connection addConnection(Connection connection) {
        return connectionRepo.save(connection);
    }

    public boolean existsEmployee1Employee2Connection(Long id1, Long id2) {
        return connectionRepo.findConnectionEmployee1Employee2(id1, id2) != null;
    }

    public List<Connection> findConnectionsOfEmployee(Long employee_id) {
        return connectionRepo.findConnectionsOfEmployee(employee_id);
    }

    public List<Connection> findPendingConnectionsOfEmployee(Long employee_id) {
        return connectionRepo.findPendingConnectionsOfEmployee(employee_id);
    }

}
