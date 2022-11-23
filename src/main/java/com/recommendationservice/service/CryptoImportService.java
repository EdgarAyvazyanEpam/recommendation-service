package com.recommendationservice.service;

import com.recommendationservice.entity.CryptoEntity;
import com.recommendationservice.entity.UploadedFileEntity;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author Edgar_Ayvazyan
 * @created 21/11/2022
 * @project recommendation-service
 */
public interface CryptoImportService {
    List<CryptoEntity> cryptoProcess(MultipartFile file, UploadedFileEntity uploadedFileEntity);

    List<CryptoEntity> updateCrypto(MultipartFile file, UploadedFileEntity uploadedFileEntity);

}
