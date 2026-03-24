package com.inditex.priceservice.infrastructure.config;

import com.inditex.priceservice.application.usecase.GetPriceUseCase;
import com.inditex.priceservice.application.usecase.GetPriceUseCaseImpl;
import com.inditex.priceservice.domain.port.PriceRepositoryPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DomainConfig {

    @Bean
    public GetPriceUseCase getPriceUseCase(PriceRepositoryPort priceRepositoryPort) {
        return new GetPriceUseCaseImpl(priceRepositoryPort);
    }
}
