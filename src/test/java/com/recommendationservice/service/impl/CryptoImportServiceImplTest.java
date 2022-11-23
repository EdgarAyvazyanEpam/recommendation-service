package com.recommendationservice.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertLinesMatch;
import static org.mockito.Mockito.when;

import com.recommendationservice.entity.CryptoEntity;
import com.recommendationservice.entity.UploadedFileEntity;
import com.recommendationservice.enums.UploadedFIleStatusEnum;
import com.recommendationservice.repository.CryptoRepository;
import com.recommendationservice.repository.UploadedFileRepository;
import com.recommendationservice.service.csvservice.CSVService;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.modelmapper.ModelMapper;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Edgar_Ayvazyan
 * @created 23/11/2022
 * @project recommendation-service
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class CryptoImportServiceImplTest {

  @Mock
  private CSVService csvService;
  @Mock
  private CryptoRepository cryptoRepository;
  @Mock
  private ModelMapper modelMapper;
  @Mock
  private UploadedFileRepository uploadedFileRepository;
  private final String testFilePath = "src/main/resources/files/BTC_values.csv";
  private MultipartFile file;
  private UploadedFileEntity uploadedFileEntity;
  private List<CryptoEntity> cryptoEntities = new ArrayList<>();

  @InjectMocks
  private CryptoImportServiceImpl cryptoImportService;

  @SneakyThrows
  @BeforeEach
  void setUp() {
    uploadedFileEntity = new UploadedFileEntity();
    Set<CryptoEntity> cryptoEntity = new HashSet<>();
    cryptoEntity.add(new CryptoEntity(1L, LocalDateTime.now(), "BTC", new BigDecimal("123.321"),
        uploadedFileEntity));
    uploadedFileEntity.setId(1L);
    uploadedFileEntity.setFileStatus(UploadedFIleStatusEnum.STORED);
    uploadedFileEntity.setCreationDate(LocalDateTime.now());
    uploadedFileEntity.setFileName("BTC_values.csv");
    uploadedFileEntity.setCryptoEntity(cryptoEntity);
    cryptoEntities = cryptoEntity.stream().toList();
    Path path = Paths.get(testFilePath);
    String name = "BTC_values.csv";
    String originalFileName = "BTC_values.csv";
    String contentType = "text/csv";
    byte[] content = Files.readAllBytes(path);
    file = new MockMultipartFile(name,
        originalFileName, contentType, content);
  }

  @Test
  void cryptoProcessTest() {
    when(cryptoImportService.uploadedFileProcess(file)).thenReturn(uploadedFileEntity);
    UploadedFileEntity entity = cryptoImportService.processUploadedFile(file);
    Assertions.assertEquals(uploadedFileEntity, entity);
  }

  @Test
  void updateCryptoTest() {
    when(cryptoImportService.uploadedFileProcess(file)).thenReturn(uploadedFileEntity);
    List<CryptoEntity> cryptoEntities1 = cryptoImportService.cryptoProcess(file,
        uploadedFileEntity);
    cryptoImportService.updateCrypto(file, uploadedFileEntity);
    assertEquals(cryptoEntities, cryptoEntities1);
    assertEquals(UploadedFIleStatusEnum.UPDATED,
        cryptoEntities.get(0).getUploadedFileEntity().getFileStatus());
  }

}
