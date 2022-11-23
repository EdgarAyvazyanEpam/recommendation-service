package com.recommendationservice.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author edgarayvazyan
 * @created 22.11.22
 * @project recommendation-service
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CryptoResponseDto {
    private LocalDateTime priceDate;
    private String symbol;
    private BigDecimal price;
    private Long uploadedFileEntity;
}
