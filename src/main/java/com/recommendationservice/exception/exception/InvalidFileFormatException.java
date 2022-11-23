package com.recommendationservice.exception.exception;

import java.io.Serial;

/**
 * @author Edgar_Ayvazyan
 * @created 20/11/2022
 * @project recommendation-service
 */
public class InvalidFileFormatException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -4238166109105690160L;

    public InvalidFileFormatException(String message) {
        super(message);
    }
}
