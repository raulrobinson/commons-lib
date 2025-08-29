package com.commons.lib.rest.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@ToString
public class ApiResponse {
    private int code;
    private String message;
    private String identifier;
    private String timestamp;
    private List<ErrorDto> errors;
}
