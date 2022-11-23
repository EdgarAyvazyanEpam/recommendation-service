package com.recommendationservice.service.impl;

import com.recommendationservice.domain.CryptoRateDto;
import com.recommendationservice.domain.CryptoResponseDto;
import com.recommendationservice.entity.CryptoEntity;
import com.recommendationservice.exception.exception.CryptoValueNotFoundException;
import com.recommendationservice.repository.CryptoRepository;
import com.recommendationservice.service.CryptoService;
import com.recommendationservice.service.util.CryptoHelperImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@Validated
@AllArgsConstructor
@Service
public class CryptoServiceImpl implements CryptoService {

    private final CryptoRepository cryptoRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<CryptoRateDto> getCryptoValuesBySymbol(@NotBlank @NotNull String symbol) {
        log.debug("Calling CryptoService.getCryptoValuesBySymbol method");
        List<CryptoEntity> cryptoEntityBySymbol = cryptoRepository.findCryptoEntityBySymbol(symbol);
        if (cryptoEntityBySymbol.isEmpty()) {
            throw new CryptoValueNotFoundException("The values could not be found by", symbol);
        }
        log.info("Found the following count for the requested crypto value:" + cryptoEntityBySymbol.size());

        return cryptoEntityBySymbol
                .stream()
                .map(cryptoEntity -> modelMapper.map(cryptoEntity, CryptoRateDto.class))
                .toList();
    }

    @Override
    public CryptoResponseDto getOldestCryptoValueBySymbol(String symbol) {
        log.debug("Calling CryptoValueService.getOldestCryptoValueBySymbol method");

        final CryptoEntity oldestCryptoValue = cryptoRepository.findFirstBySymbolOrderByPriceDateAsc(symbol)
                .orElseThrow(() -> new CryptoValueNotFoundException("The oldest value could not be found for the crypto value", symbol));

        log.info("Found the following the oldest value for the requested crypto value: " + symbol);

        return CryptoHelperImpl.cryptoValueToCryptoResponseDto(oldestCryptoValue);
    }

    @Override
    public CryptoResponseDto getNewestCryptoValueBySymbol(String symbol) {
        log.debug("Calling CryptoValueService.getNewestCryptoValueBySymbol method");

        final CryptoEntity newestCryptoValue = cryptoRepository.findFirstBySymbolOrderByPriceDateDesc(symbol)
                .orElseThrow(() -> new CryptoValueNotFoundException("The newest value could not be found for the crypto value", symbol));

        log.info("Found the following the newest value for the requested crypto value: " + symbol);

        return CryptoHelperImpl.cryptoValueToCryptoResponseDto(newestCryptoValue);
    }

    @Override
    public CryptoResponseDto getMinCryptoValueBySymbol(String symbol) {
        log.debug("Calling CryptoValueService.getMinCryptoValueBySymbol method");

        final CryptoEntity minCryptoValue = cryptoRepository.findFirstBySymbolOrderByPriceAsc(symbol)
                .orElseThrow(() -> new CryptoValueNotFoundException("The min value could not be found for the crypto value", symbol));

        log.info("Found the following the min value for the requested crypto value: " + symbol);

        return CryptoHelperImpl.cryptoValueToCryptoResponseDto(minCryptoValue);
    }

    @Override
    public CryptoResponseDto getMaxCryptoValueBySymbol(String symbol) {
        log.debug("Calling CryptoValueService.getMaxCryptoValueBySymbol method");
        log.info(String.format("Requested to find the max value for the crypto value: %s", symbol));

        final CryptoEntity maxCryptoValue = cryptoRepository.findFirstBySymbolOrderByPriceDesc(symbol)
                .orElseThrow(() -> new CryptoValueNotFoundException("The max value could not be found for the crypto value", symbol));

        log.info("Found the following the max value for the requested crypto value: " + symbol);

        return CryptoHelperImpl.cryptoValueToCryptoResponseDto(maxCryptoValue);
    }

    @Override
    public CryptoResponseDto getNormalizedByCryptoValueAndDate(String symbol, LocalDate date) {
        log.debug("Calling CryptoValueService.getNormalizedByCryptoValueAndDate method");
        final CryptoEntity normalizedCryptoValue = cryptoRepository.findNormalizedByCryptoValueAndDate(symbol, date).orElseThrow(
                () -> new CryptoValueNotFoundException("The normalized value could not be found for the crypto value for the date: " + date, symbol));

        return CryptoHelperImpl.cryptoValueToCryptoResponseDto(normalizedCryptoValue);
    }

    @Override
    public List<CryptoResponseDto> getNormalizedCryptoValue(String symbol) {
        log.debug("Calling CryptoValueService.getNormalizedCryptoValue method");
        List<CryptoEntity> normalizedCryptoValues = cryptoRepository.findNormalizedByCryptoValue(symbol);
        if (normalizedCryptoValues == null || normalizedCryptoValues.isEmpty()) {
            throw new CryptoValueNotFoundException("The normalized values could not be found for the crypto value", symbol);
        }

        return CryptoHelperImpl.cryptoValuesToCryptoResponseDto(normalizedCryptoValues);
    }
}
