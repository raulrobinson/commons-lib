package com.commons.lib.rest.exception;

import com.commons.lib.rest.enums.TechnicalCode;
import lombok.Getter;

@Getter
public class TechnicalException extends RuntimeException {
    private final TechnicalCode technicalCode;
    private final Object additionalInfo;

    public TechnicalException(TechnicalCode technicalCode, Object additionalInfo) {
        super(technicalCode.getMessage());
        this.technicalCode = technicalCode;
        this.additionalInfo = additionalInfo;
    }
}