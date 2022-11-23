package com.recommendationservice.service;

import com.recommendationservice.domain.CryptoRateDto;
import com.recommendationservice.repository.CryptoRepository;
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

import java.util.List;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * @author edgarayvazyan
 * @created 22.11.22
 * @project recomentation-service
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class CryptoServiceImplTest {
    @Mock
    private CryptoRepository cryptoRepository;
    @Mock
    private ModelMapper modelMapper;
    @InjectMocks
    private CryptoService cryptoService;

    @Test
    public void getCryptoValuesBySymbolTest() {
        List<CryptoRateDto> methodCall = cryptoRepository.findCryptoEntityBySymbol(any())
                .stream()
                .map(crypto -> modelMapper.map(crypto, CryptoRateDto.class)).toList();
        when(methodCall);
        assertNotNull(methodCall);
    }
}
