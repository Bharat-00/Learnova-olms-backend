package com.learnova.certificate.service;

import java.io.File;
import java.io.FileOutputStream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.learnova.certificate.entity.Certificate;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;

@Service
public class PdfGeneratorService {

    @Value("${app.certificate.storage-path}")
    private String storagePath;

    public String generateCertificatePdf(Certificate certificate) {
        try {
            File directory = new File(storagePath);

            if (!directory.exists()) {
                directory.mkdirs();
            }

            String fileName = certificate.getCertificateNumber() + ".pdf";
            String filePath = storagePath + File.separator + fileName;

            Document document = new Document(PageSize.A4.rotate());
            PdfWriter.getInstance(document, new FileOutputStream(filePath));

            document.open();

            Font titleFont = new Font(Font.HELVETICA, 34, Font.BOLD);
            Font subTitleFont = new Font(Font.HELVETICA, 18, Font.NORMAL);
            Font nameFont = new Font(Font.HELVETICA, 28, Font.BOLD);
            Font normalFont = new Font(Font.HELVETICA, 14, Font.NORMAL);

            Paragraph title = new Paragraph("CERTIFICATE OF COMPLETION", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(35);
            document.add(title);

            Paragraph line1 = new Paragraph("This certificate is proudly presented to", subTitleFont);
            line1.setAlignment(Element.ALIGN_CENTER);
            line1.setSpacingAfter(25);
            document.add(line1);

            Paragraph studentName = new Paragraph(certificate.getStudentName(), nameFont);
            studentName.setAlignment(Element.ALIGN_CENTER);
            studentName.setSpacingAfter(25);
            document.add(studentName);

            Paragraph line2 = new Paragraph("for successfully completing the course", subTitleFont);
            line2.setAlignment(Element.ALIGN_CENTER);
            line2.setSpacingAfter(20);
            document.add(line2);

            Paragraph courseTitle = new Paragraph(certificate.getCourseTitle(), nameFont);
            courseTitle.setAlignment(Element.ALIGN_CENTER);
            courseTitle.setSpacingAfter(40);
            document.add(courseTitle);

            Paragraph certificateNo = new Paragraph(
                    "Certificate Number: " + certificate.getCertificateNumber(),
                    normalFont
            );
            certificateNo.setAlignment(Element.ALIGN_CENTER);
            certificateNo.setSpacingAfter(15);
            document.add(certificateNo);

            Paragraph issuedAt = new Paragraph(
                    "Issued At: " + certificate.getIssuedAt(),
                    normalFont
            );
            issuedAt.setAlignment(Element.ALIGN_CENTER);
            document.add(issuedAt);

            document.close();

            return filePath;

        } catch (Exception e) {
            throw new RuntimeException("Failed to generate certificate PDF");
        }
    }
}