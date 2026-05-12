package com.learnova.certificate.service;

import java.util.List;

import com.learnova.certificate.dto.CertificateRequest;
import com.learnova.certificate.entity.Certificate;

public interface CertificateService {

    Certificate generateCertificate(CertificateRequest request);

    List<Certificate> getCertificatesByUserId(Long userId);

    Certificate getCertificateById(Long certificateId);
}