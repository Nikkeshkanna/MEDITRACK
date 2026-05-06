package com.meditrack.service;

import com.google.zxing.*;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.meditrack.entity.Patient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.*;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Service
@SuppressWarnings("null")
public class QrService {

    @Value("${meditrack.upload.dir}")
    private String uploadDir;

    public String generateQRCodeBase64(Patient patient) {
        try {
            String qrContent = buildQRContent(patient);
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            hints.put(EncodeHintType.MARGIN, 1);

            BitMatrix bitMatrix = qrCodeWriter.encode(
                    qrContent, BarcodeFormat.QR_CODE, 300, 300, hints);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);
            byte[] pngData = outputStream.toByteArray();
            return Base64.getEncoder().encodeToString(pngData);
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate QR code: " + e.getMessage());
        }
    }

    public String saveQRCode(Patient patient) {
        try {
            String qrContent = buildQRContent(patient);
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(
                    qrContent, BarcodeFormat.QR_CODE, 300, 300);

            Path qrPath = Paths.get(uploadDir, "qr-codes");
            Files.createDirectories(qrPath);

            String fileName = patient.getUhid() + "-qr.png";
            Path filePath = qrPath.resolve(fileName);
            MatrixToImageWriter.writeToPath(bitMatrix, "PNG", filePath);

            return "/uploads/qr-codes/" + fileName;
        } catch (Exception e) {
            throw new RuntimeException("Failed to save QR code: " + e.getMessage());
        }
    }

    private String buildQRContent(Patient patient) {
        return String.format(
                "{\"uhid\":\"%s\",\"name\":\"%s\",\"bloodGroup\":\"%s\",\"system\":\"MediTrack\"}",
                patient.getUhid(),
                patient.getName(),
                patient.getBloodGroup() != null ? patient.getBloodGroup() : "N/A"
        );
    }
}
