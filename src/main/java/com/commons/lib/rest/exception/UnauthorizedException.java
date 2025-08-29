package com.commons.lib.rest.exception;

import com.commons.lib.rest.enums.TechnicalMessage;

public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(TechnicalMessage message) {
        super(message.getMessage());
    }
}
