package com.recommendationservice.service.util;

import com.recommendationservice.domain.CryptoResponseDto;
import com.recommendationservice.entity.CryptoEntity;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author edgarayvazyan
 * @created 22.11.22
 * @project recommendation-service
 */
@Component
public class CryptoHelperImpl {
    private CryptoHelperImpl(){}

    public static CryptoResponseDto cryptoValueToCryptoResponseDto(CryptoEntity entity) {
        return new CryptoResponseDto(entity.getPriceDate(), entity.getSymbol(), entity.getPrice(), entity.getUploadedFileEntity().getId());
    }

    public static List<CryptoResponseDto> cryptoValuesToCryptoResponseDto(List<CryptoEntity> list) {
        return list.stream().map(CryptoHelperImpl::cryptoValueToCryptoResponseDto).toList();
    }
}
