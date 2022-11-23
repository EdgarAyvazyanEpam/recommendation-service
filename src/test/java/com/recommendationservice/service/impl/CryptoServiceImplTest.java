package com.recommendationservice.service.impl;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.recommendationservice.domain.CryptoRateDto;
import com.recommendationservice.domain.CryptoResponseDto;
import com.recommendationservice.service.CryptoService;
import com.recommendationservice.service.UploadFileService;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author edgarayvazyan
 * @created 22.11.22
 * @project recomentation-service
 */
@SpringBootTest
class CryptoServiceImplTest {

  @Autowired
  private CryptoImportServiceImpl uploadFileService;
  @Autowired
  private CryptoService cryptoService;

  private final String symbol = "BTC";
  private final String testFilePath = "src/main/resources/files/BTC_values.csv";

  @SneakyThrows
  @BeforeEach
  void setUp() {
    if (cryptoService.getCryptoValuesBySymbol(symbol) == null) {
      Path path = Paths.get(testFilePath);
      String name = "BTC_values.csv";
      String originalFileName = "BTC_values.csv";
      String contentType = "text/csv";
      byte[] content = Files.readAllBytes(path);
      MultipartFile result = new MockMultipartFile(name,
          originalFileName, contentType, content);
      uploadFileService.uploadedFileProcess(result);
    }
  }

  @Test
  void getCryptoValuesBySymbolTest() {
    List<CryptoRateDto> btc = cryptoService.getCryptoValuesBySymbol(symbol);
    assertNotNull(btc);
    assertEquals(btc.get(0).getSymbol(), symbol);
  }

  @Test
  void getOldestCryptoValueBySymbolTest() {
    CryptoResponseDto btc = cryptoService.getOldestCryptoValueBySymbol(symbol);
    assertNotNull(btc);
    assertEquals(btc.getPriceDate(), LocalDateTime.parse("2022-01-01T08:00:00"));
  }

  @Test
  void getNewestCryptoValueBySymbolTest() {
    CryptoResponseDto btc = cryptoService.getNewestCryptoValueBySymbol(symbol);
    assertNotNull(btc);
    assertEquals(btc.getPriceDate(), LocalDateTime.parse("2022-02-01T00:00:00"));
  }

  @Test
  void getMinCryptoValueBySymbolTest() {
    CryptoResponseDto btc = cryptoService.getMinCryptoValueBySymbol(symbol);
    assertNotNull(btc);
    assertEquals(btc.getPrice(), new BigDecimal("33276.59"));
  }

  @Test
  void getMaxCryptoValueBySymbolTest() {
    CryptoResponseDto btc = cryptoService.getMaxCryptoValueBySymbol(symbol);
    assertNotNull(btc);
    assertEquals(btc.getPrice(), new BigDecimal("47722.66"));
  }

  @Test
  void getNormalizedByCryptoValueAndDateTest() {
    CryptoResponseDto btc = cryptoService.getNormalizedByCryptoValueAndDate(symbol,
        LocalDate.of(2022, 1, 1));
    assertNotNull(btc);
    assertEquals(btc.getPrice(), new BigDecimal("47143.98"));
  }

  @Test
  void getNormalizedCryptoValueTest() {
    List<CryptoResponseDto> btc = cryptoService.getNormalizedCryptoValue(symbol);
    assertNotNull(btc);
  }

}
