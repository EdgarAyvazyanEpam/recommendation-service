package com.recommendationservice.domain;

import java.time.LocalDateTime;

import com.recommendationservice.entity.UploadedFileEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author Edgar_Ayvazyan
 * @created 18/11/2022
 * @project recommendation-service
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CryptoRateDto {
    private LocalDateTime priceDate;
    private String symbol;
    private BigDecimal price;
    private UploadedFileEntity uploadedFileEntity;
}
