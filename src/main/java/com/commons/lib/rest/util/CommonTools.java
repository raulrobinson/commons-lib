package com.commons.lib.rest.util;

import lombok.experimental.UtilityClass;
import org.springframework.http.server.reactive.ServerHttpRequest;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

import java.util.Optional;

@UtilityClass
public class CommonTools {
    private static final String CONTEXTUAL = "CONTEXTUAL_TRANSACTION";
    private static final String MESSAGE_ID = "message-id";
    private static final String NO_MESSAGE = "NOT_TRANSACTION";

    public static Mono<AppContext> context() {
        return Mono.deferContextual(view -> Mono.just(view.get(CONTEXTUAL)));
    }

    public static Context appContext(Context context, ServerHttpRequest request) {
        return context.put(CONTEXTUAL, new AppContext(
                Optional.ofNullable(request.getHeaders().getFirst(MESSAGE_ID)).orElse(NO_MESSAGE),
                request.getPath().value()));
    }
}
