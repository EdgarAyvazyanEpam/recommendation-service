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

    public static final String TYPE = "text/csv";
    private final CryptoImportService cryptoImportService;
    private final UploadedFileRepository uploadedFileRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public UploadFileServiceImpl(CryptoImportService cryptoImportService,
                                 UploadedFileRepository uploadedFileRepository) {
        this.cryptoImportService = cryptoImportService;
        this.uploadedFileRepository = uploadedFileRepository;
    }

    @Transactional
    @Override
    public void processUploadedFile(MultipartFile file) {
        if (file != null && validateFile(file)) {
            UploadedFileEntity entity = uploadedFileProcess(file);
            cryptoImportService.cryptoProcess(file, entity);
        }
    }

    @Override
    public UploadedFileEntity saveUploadedFile(MultipartFile file) {
        UploadedFileEntity entity = UploadedFileEntity.builder()
                .fileName(file.getOriginalFilename())
                .fileStatus(UploadedFIleStatusEnum.STORED)
                .creationDate(LocalDateTime.now())
                .build();
        return uploadedFileRepository.saveAndFlush(entity);
    }

    private UploadedFileEntity updateUploadedFile(UploadedFileEntity entity) {
        entity.setFileStatus(UploadedFIleStatusEnum.UPDATED);
        return entityManager.merge(entity);
    }

    @Transactional
    @Override
    public void processUpdateUploadedFile(MultipartFile file) {
        if (file != null && validateFile(file)) {
            UploadedFileEntity entity = updateUploadedFileDB(file);
            updateCrypto(file, entity);
        }
    }

    @Transactional
    public List<CryptoEntity> updateCrypto(MultipartFile file, UploadedFileEntity entity) {
        return cryptoImportService.updateCrypto(file, entity);
    }

    /**
     * Chek if file has corresponding .csv format
     *
     * @param file
     * @return boolean
     */
    private boolean validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            final String errorMessage = "The CSV file is empty or not provided. file:" + file;
            log.error(errorMessage);
            throw new InvalidUploadedFileException(errorMessage);
        } else if (!TYPE.equals(file.getContentType())) {
            final String errorMessage = "The CSV file format not supported:" + file;
            log.error(errorMessage);
            throw new InvalidFileFormatException(errorMessage);
        }
        return true;
    }

    private UploadedFileEntity uploadedFileProcess(MultipartFile file) {
        Optional<UploadedFileEntity> uploadedFileEntity = uploadedFileRepository
                .findFirstByFileNameAndFileStatus(file.getOriginalFilename(),
                        UploadedFIleStatusEnum.STORED);
        if (uploadedFileEntity.isEmpty()) {
            return saveUploadedFile(file);
        } else {
            throw new FileProcessingException(
                    String.format("The file already has been processed successfully in: %s",
                            uploadedFileEntity.get().getCreationDate()), file.getOriginalFilename());
        }
    }

    private UploadedFileEntity updateUploadedFileDB(MultipartFile file) {
        Optional<UploadedFileEntity> uploadedFileEntity = uploadedFileRepository
                .findUploadedFileEntityByFileName(file.getOriginalFilename());
        if (uploadedFileEntity.isPresent()) {
            return updateUploadedFile(uploadedFileEntity.get());
        } else {
            throw new FileProcessingException(
                    String.format("Cannot fined corresponding file in DB by : %s",
                            uploadedFileEntity.get().getCreationDate()), file.getOriginalFilename());
        }
    }
}
