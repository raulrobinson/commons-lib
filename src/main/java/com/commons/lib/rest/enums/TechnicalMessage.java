package com.commons.lib.rest.enums;

import lombok.Getter;

@Getter
public enum TechnicalMessage {
    INTERNAL_SERVER_ERROR("500", "Internal server error", ""),
    INTERNAL_ERROR_IN_ADAPTERS("503", "Internal error in adapters", ""),
    MINIMUM_OR_MAXIMUM_CAPACITY("400", "A capability must have 3 to 20 unique technologies.", ""),
    BAD_REQUEST("400", "Bad request", ""),
    NOT_FOUND("404", "Not found", ""),
    NO_CONTENT("204", "No content", ""),
    INVALID_REQUEST("400", "Request null or incomplete", ""),
    ALREADY_EXISTS("409", "Already exists", ""),
    NOT_ONLY_NUMBERS("400", "The field must contain only numbers", ""),
    NAME_CHARACTER_LIMIT("400", "Name must be between 3 and 50 characters", ""),
    UNAUTHORIZED("401", "Unauthorized access", ""),
    X_MESSAGE_ID("X-Message-ID", "Unique identifier for the message", ""),
    DESCRIPTION_CHARACTER_LIMIT("400", "Description must be between 3 and 100 characters", "");

    private final String code;
    private final String message;
    private final String parameter;

    TechnicalMessage(String code, String message, String parameter) {
        this.code = code;
        this.message = message;
        this.parameter = parameter;
    }
}
