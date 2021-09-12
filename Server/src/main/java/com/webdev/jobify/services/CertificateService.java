package com.webdev.jobify.services;


import com.webdev.jobify.model.Certificate;
import com.webdev.jobify.repos.CertificateRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CertificateService {

    public final CertificateRepo certificateRepo;

    @Autowired
    public CertificateService(CertificateRepo cRepo) {
        this.certificateRepo = cRepo;
    }

    public Certificate findCertificateById(Long id) {
        return certificateRepo.findCertificateById(id);
    }

    public List<Certificate> findAllCertificates() {
        return certificateRepo.findAll();
    }

    public Certificate saveCertificate(Certificate certificate) {
        return certificateRepo.save(certificate);
    }

    public List<Certificate> findCertificatesByEmployeeId(Long employee_id) {
        return certificateRepo.findCertificatesByEmployeeId(employee_id);
    }

}
