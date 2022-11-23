package com.recommendationservice.exception.exception;

import java.io.Serial;

/**
 * @author edgarayvazyan
 * @created 22.11.22
 * @project recommendation-service
 */
public class CryptoValueNotFoundException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1279560298094330394L;

    private final String symbol;

    public CryptoValueNotFoundException(String message, String symbol) {
        super(message);
        this.symbol = symbol;
    }

    @Override
    public String getMessage() {
        return String.format("%s: %s", super.getMessage(), this.symbol);
    }
}
