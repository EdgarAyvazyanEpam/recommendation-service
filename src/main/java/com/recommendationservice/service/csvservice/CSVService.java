package com.recommendationservice.service.csvservice;

import com.recommendationservice.domain.CryptoRateDto;
import com.recommendationservice.entity.CryptoEntity;
import com.recommendationservice.entity.UploadedFileEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
/**
 * @author Edgar_Ayvazyan
 * @created 18/11/2022
 * @project recommendation-service
 */
public interface CSVService {
    List<CryptoRateDto> readCryptoRateDtos(MultipartFile file, UploadedFileEntity uploadedFileEntity);
}
