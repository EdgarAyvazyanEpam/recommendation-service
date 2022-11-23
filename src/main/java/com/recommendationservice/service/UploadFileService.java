package com.recommendationservice.service;

import com.recommendationservice.entity.UploadedFileEntity;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Edgar_Ayvazyan
 * @created 18/11/2022
 * @project recommendation-service
 */
public interface UploadFileService {
    void processUploadedFile(MultipartFile file);

    UploadedFileEntity saveUploadedFile(MultipartFile file);

    void processUpdateUploadedFile(MultipartFile file);
}
