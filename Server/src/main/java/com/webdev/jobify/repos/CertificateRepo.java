package com.webdev.jobify.repos;

import com.webdev.jobify.model.Certificate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CertificateRepo extends JpaRepository<Certificate, Long> {

    Certificate findCertificateById(Long id);

    @Query(value = "select * from certificates c where c.employee_id = :employee_id", nativeQuery = true)
    List<Certificate> findCertificatesByEmployeeId(@Param("employee_id") Long employee_id);

}
