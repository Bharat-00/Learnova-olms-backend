package com.learnova.certificate.service;

import com.learnova.certificate.dto.CertificateRequest;
import com.learnova.certificate.entity.Certificate;

import java.util.List;

public interface CertificateService {

    Certificate generateCertificate(CertificateRequest request);

    List<Certificate> getCertificatesByUserId(Long userId);

    List<Certificate> getAllCertificates();

    Certificate getCertificateById(Long certificateId);
}
