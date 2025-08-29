package com.commons.lib.redis.adapter;

import com.commons.lib.rest.enums.TechnicalCode;
import com.commons.lib.rest.exception.TechnicalException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.RedisSystemException;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisAdapter {
    private final ReactiveRedisTemplate<String, String> redisTemplate;

    /**
     * Saves a value in Redis with the specified key and duration.
     *
     * @param key      the key to store the value
     * @param value    the value to store
     * @param duration the duration for which the key should be stored
     * @return a Mono signaling completion or error
     */
    public Mono<Void> save(String key, String value, Duration duration) {
        return validateSaveInputs(key, value, duration)
                .then(Mono.defer(() -> redisTemplate.opsForValue().set(key, value, duration).then()))
                .doOnSuccess(v -> log.info("Redis: Successfully saved value for key: {}", key))
                .doOnError(e -> log.error("Redis: Failed to save value for key: {}", key))
                .onErrorMap(this::mapToTechnicalException);
    }

    /**
     * Retrieves a value from Redis by key.
     *
     * @param key the key to retrieve the value
     * @return a Mono containing the value or empty if not found
     */
    public Mono<String> get(String key) {
        return validateKey(key)
                .then(Mono.defer(() -> redisTemplate.opsForValue().get(key)))
                .doOnSuccess(result -> log.info("Redis: {} for key: {}", result != null ? "Retrieved value" : "Key not found", key))
                .doOnError(e -> log.error("Redis: Failed to retrieve value for key: {}", key))
                .onErrorMap(this::mapToTechnicalException);
    }

    /**
     * Maps exceptions to a TechnicalException with appropriate error codes.
     *
     * @param throwable the exception to map
     * @return a TechnicalException with a specific error code and message
     */
    private Throwable mapToTechnicalException(Throwable throwable) {
        TechnicalCode errorCode = TechnicalCode.INTERNAL_SERVER_ERROR;
        String errorMessage;

        switch (throwable) {
            case TechnicalException technicalException -> {
                errorCode = technicalException.getTechnicalCode();
                errorMessage = (String) technicalException.getAdditionalInfo();
            }
            case RedisConnectionFailureException redisConnectionFailureException -> errorMessage = "Failed to connect to Redis";
            case RedisSystemException redisSystemException -> errorMessage = "Redis system error";
            default -> {
                errorMessage = "Unexpected error in Redis operation";
                log.error("Unexpected error during Redis operation: {}", errorMessage, throwable);
            }
        }

        return new TechnicalException(errorCode, errorMessage);
    }

    /**
     * Validates the key for Redis operations.
     *
     * @param key the key to validate
     * @return a Mono<Void> signaling success or error
     */
    private Mono<Void> validateKey(String key) {
        if (!StringUtils.hasText(key)) {
            return Mono.error(new TechnicalException(TechnicalCode.INTERNAL_SERVER_ERROR, "Key cannot be null or empty"));
        }
        return Mono.empty();
    }

    /**
     * Validates input parameters for the save operation.
     *
     * @param key      the key to validate
     * @param value    the value to validate
     * @param duration the duration to validate
     * @return a Mono<Void> signaling success or error
     */
    private Mono<Void> validateSaveInputs(String key, String value, Duration duration) {
        if (!StringUtils.hasText(key)) {
            return Mono.error(new TechnicalException(TechnicalCode.INTERNAL_SERVER_ERROR, "Key cannot be null or empty"));
        }
        if (!StringUtils.hasText(value)) {
            return Mono.error(new TechnicalException(TechnicalCode.INTERNAL_SERVER_ERROR, "Value cannot be null or empty"));
        }
        Objects.requireNonNull(duration, "Duration cannot be null");
        if (duration.isNegative() || duration.isZero()) {
            return Mono.error(new TechnicalException(TechnicalCode.INTERNAL_SERVER_ERROR, "Duration must be positive"));
        }
        return Mono.empty();
    }
}
