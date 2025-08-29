package com.commons.lib.rest.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TechnicalCode {

    REQUEST_INVALID(400, "INVALID_DATA", "Datos erroneos en la entrada de la solicitud."),
    UNAUTHORIZED(401, "CODE-401", "Usuario no autenticado."),
    INTERNAL_SERVER_ERROR(500, "CODE-500", "Error interno del servidor.");

    private final Integer httpStatus;
    private final String errorCode;
    private final String message;
}
