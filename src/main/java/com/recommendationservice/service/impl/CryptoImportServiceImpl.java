package com.recommendationservice.service.impl;

import com.recommendationservice.entity.CryptoEntity;
import com.recommendationservice.entity.UploadedFileEntity;
import com.recommendationservice.repository.CryptoRepository;
import com.recommendationservice.service.CryptoImportService;
import com.recommendationservice.service.csvservice.CSVService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Edgar_Ayvazyan
 * @created 21/11/2022
 * @project recommendation-service
 */
@Slf4j
@Service
public class CryptoImportServiceImpl implements CryptoImportService {

    private final CSVService csvService;
    private final CryptoRepository cryptoRepository;
    private final ModelMapper modelMapper;

    public CryptoImportServiceImpl(CSVService csvService, CryptoRepository cryptoRepository,
                                   ModelMapper modelMapper) {
        this.csvService = csvService;
        this.cryptoRepository = cryptoRepository;
        this.modelMapper = modelMapper;
    }


    @Override
    public List<CryptoEntity> cryptoProcess(MultipartFile file, UploadedFileEntity uploadedFileEntity) {
        return cryptoRepository.saveAll(
                csvService.readCryptoRateDtos(file, uploadedFileEntity).stream()
                        .map(cryptoRateDto -> modelMapper.map(cryptoRateDto, CryptoEntity.class)).toList());
    }

    @Override
    public List<CryptoEntity> updateCrypto(MultipartFile file, UploadedFileEntity uploadedFileEntity) {

        if (!deleteCryptoValuesByFileId(uploadedFileEntity.getId())) {
            csvService.readCryptoRateDtos(file, uploadedFileEntity)
                    .stream()
                    .map(cryptoRateDto -> modelMapper.map(cryptoRateDto, CryptoEntity.class))
                    .forEach(cryptoRepository::save);
        }

        return Collections.emptyList();
    }

    private boolean deleteCryptoValuesByFileId(Long id) {
        Optional<List<CryptoEntity>> cryptoEntities = cryptoRepository.deleteAllByUploadedFileEntityId(id);
        return cryptoEntities.isEmpty();
    }
}
