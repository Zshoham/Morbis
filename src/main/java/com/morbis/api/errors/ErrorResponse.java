package com.morbis.api.errors;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@Schema(name = "Error Response")
public class ErrorResponse {

    @Schema(example = "the id provided does not exist")
    private final String message;
    @Schema(example = "IllegalArgumentException")
    private final String type;
    @Schema(example = "BAD_REQUEST")
    private final HttpStatus status;
    private final LocalDateTime timestamp;

    public ErrorResponse(String message, String type, HttpStatus status) {
        this.message = message;
        this.type = type;
        this.status = status;
        timestamp = LocalDateTime.now();
    }
}
