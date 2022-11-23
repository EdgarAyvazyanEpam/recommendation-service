package com.recommendationservice.service.impl;

import com.recommendationservice.entity.CryptoEntity;
import com.recommendationservice.entity.UploadedFileEntity;
import com.recommendationservice.enums.UploadedFIleStatusEnum;
import com.recommendationservice.exception.exception.FileProcessingException;
import com.recommendationservice.exception.exception.InvalidFileFormatException;
import com.recommendationservice.exception.exception.InvalidUploadedFileException;
import com.recommendationservice.repository.UploadedFileRepository;
import com.recommendationservice.service.CryptoImportService;
import com.recommendationservice.service.UploadFileService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Edgar_Ayvazyan
 * @created 18/11/2022
 * @project recommendation-service
 */
@Slf4j
@Service
public class UploadFileServiceImpl implements UploadFileService {




}
