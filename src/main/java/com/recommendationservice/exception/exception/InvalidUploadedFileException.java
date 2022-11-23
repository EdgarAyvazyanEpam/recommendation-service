package com.recommendationservice.exception.exception;

import java.io.Serial;

/**
 * @author Edgar_Ayvazyan
 * @created 20/11/2022
 * @project recommendation-service
 */
public class InvalidUploadedFileException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -5460185325774700620L;

    public InvalidUploadedFileException(String message) {
        super(message);
    }
}

