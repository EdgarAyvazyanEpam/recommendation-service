package com.recommendationservice.service;

import com.recommendationservice.entity.CryptoEntity;
import com.recommendationservice.entity.UploadedFileEntity;

import java.util.List;

import java.util.Map;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Edgar_Ayvazyan
 * @created 21/11/2022
 * @project recommendation-service
 */
public interface CryptoImportService {
    List<CryptoEntity> cryptoProcess(MultipartFile file, UploadedFileEntity uploadedFileEntity);

    void updateCrypto(MultipartFile file, UploadedFileEntity uploadedFileEntity);

    UploadedFileEntity processUploadedFile(MultipartFile file);

    UploadedFileEntity saveUploadedFile(MultipartFile file);

    void processUpdateUploadedFile(MultipartFile file);
}
