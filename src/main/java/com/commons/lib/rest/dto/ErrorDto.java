package com.commons.lib.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErrorDto {
    private String message;
    private String parameter;

    public static ErrorDto of(String message, String parameter) {
        return ErrorDto.builder()
                .message(message)
                .parameter(parameter)
                .build();
    }
}
