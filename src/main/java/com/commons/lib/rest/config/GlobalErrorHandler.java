package com.commons.lib.rest.config;

import com.commons.lib.rest.dto.ApiResponse;
import com.commons.lib.rest.dto.ErrorDto;
import com.commons.lib.rest.enums.TechnicalMessage;
import com.commons.lib.rest.exception.BusinessException;
import com.commons.lib.rest.exception.UnauthorizedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.List;

@Component
@Slf4j
@Order(-2)
public class GlobalErrorHandler {

    public Mono<ServerResponse> handle(Throwable throwable, String messageId) {
        log.error("Exception captured globally: {}", throwable.toString());


        return switch (throwable) {
            case UnauthorizedException ex -> buildErrorResponse(
                    HttpStatus.UNAUTHORIZED,
                    messageId,
                    TechnicalMessage.UNAUTHORIZED,
                    List.of(ErrorDto.of(
                            ex.getMessage(),
                            TechnicalMessage.UNAUTHORIZED.getParameter()
                    ))
            );

            case BusinessException ex -> buildErrorResponse(
                    HttpStatus.BAD_REQUEST,
                    messageId,
                    TechnicalMessage.BAD_REQUEST,
                    List.of(ErrorDto.of(
                            ex.getMessage(),
                            ex.getParameter()
                    ))
            );

            default -> buildErrorResponse(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    messageId,
                    TechnicalMessage.INTERNAL_SERVER_ERROR,
                    List.of(ErrorDto.of(
                            TechnicalMessage.INTERNAL_SERVER_ERROR.getMessage(),
                            TechnicalMessage.INTERNAL_SERVER_ERROR.getParameter()
                    ))
            );
        };

    }

    private Mono<ServerResponse> buildErrorResponse(HttpStatus httpStatus,
                                                    String messageId,
                                                    TechnicalMessage technicalMessage,
                                                    List<ErrorDto> errors) {
        return ServerResponse.status(httpStatus)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(ApiResponse.builder()
                        .code(httpStatus.value())
                        .message(technicalMessage.getMessage())
                        .timestamp(Instant.now().toString())
                        .identifier(messageId)
                        .errors(errors)
                        .build());
    }

}
