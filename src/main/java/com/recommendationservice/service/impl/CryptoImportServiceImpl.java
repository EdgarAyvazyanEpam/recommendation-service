package com.recommendationservice.service.impl;

import com.recommendationservice.entity.CryptoEntity;
import com.recommendationservice.entity.UploadedFileEntity;
import com.recommendationservice.enums.UploadedFIleStatusEnum;
import com.recommendationservice.exception.exception.FileProcessingException;
import com.recommendationservice.exception.exception.InvalidFileFormatException;
import com.recommendationservice.exception.exception.InvalidUploadedFileException;
import com.recommendationservice.repository.CryptoRepository;
import com.recommendationservice.repository.UploadedFileRepository;
import com.recommendationservice.service.CryptoImportService;
import com.recommendationservice.service.csvservice.CSVService;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Edgar_Ayvazyan
 * @created 21/11/2022
 * @project recommendation-service
 */
@Slf4j
@Service
@AllArgsConstructor
public class CryptoImportServiceImpl implements CryptoImportService {

  private final CSVService csvService;
  private final CryptoRepository cryptoRepository;
  private final ModelMapper modelMapper;
  private final UploadedFileRepository uploadedFileRepository;
  public static final String TYPE = "text/csv";

  @Override
  public List<CryptoEntity> cryptoProcess(MultipartFile file,
      UploadedFileEntity uploadedFileEntity) {
    return cryptoRepository.saveAll(
        csvService.readCryptoRateDtos(file, uploadedFileEntity).stream()
            .map(cryptoRateDto -> modelMapper.map(cryptoRateDto, CryptoEntity.class)).toList());
  }

  public UploadedFileEntity processUploadedFile(
      MultipartFile file) {
    if (file != null && validateFile(file)) {
      UploadedFileEntity entity = uploadedFileProcess(file);
      cryptoProcess(file, entity);
      return entity;
    }else {
        throw new FileProcessingException("File processing fail for:", file.getOriginalFilename());
    }
  }

  @Override
  public void updateCrypto(MultipartFile file, UploadedFileEntity uploadedFileEntity) {

    if (!deleteCryptoValuesByFileId(uploadedFileEntity.getId())) {
      csvService.readCryptoRateDtos(file, uploadedFileEntity)
          .stream()
          .map(cryptoRateDto -> modelMapper.map(cryptoRateDto, CryptoEntity.class))
          .forEach(cryptoRepository::save);
    }
  }

  private boolean deleteCryptoValuesByFileId(Long id) {
    Optional<List<CryptoEntity>> cryptoEntities = cryptoRepository.deleteAllByUploadedFileEntityId(
        id);
    return cryptoEntities.isEmpty();
  }

  public UploadedFileEntity uploadedFileProcess(MultipartFile file) {
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

  @Override
  public UploadedFileEntity saveUploadedFile(MultipartFile file) {
    UploadedFileEntity entity = UploadedFileEntity.builder()
        .fileName(file.getOriginalFilename())
        .fileStatus(UploadedFIleStatusEnum.STORED)
        .creationDate(LocalDateTime.now())
        .build();
    return uploadedFileRepository.saveAndFlush(entity);
  }

  @Transactional
  @Override
  public void processUpdateUploadedFile(MultipartFile file) {
    if (file != null && validateFile(file)) {
      UploadedFileEntity entity = updateUploadedFileDB(file);
      updateCrypto(file, entity);
    }
  }

  /**
   * Check if file has corresponding .csv format
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

  private UploadedFileEntity updateUploadedFileDB(MultipartFile file) {
    Optional<UploadedFileEntity> uploadedFileEntity = uploadedFileRepository
        .findUploadedFileEntityByFileName(file.getOriginalFilename());
    if (uploadedFileEntity.isPresent()) {
      uploadedFileEntity.get().setFileStatus(UploadedFIleStatusEnum.UPDATED);
      return uploadedFileEntity.get();
    } else {
      throw new FileProcessingException(
          String.format("Cannot fined corresponding file in DB by : %s",
              uploadedFileEntity.get().getCreationDate()), file.getOriginalFilename());
    }
  }
}
