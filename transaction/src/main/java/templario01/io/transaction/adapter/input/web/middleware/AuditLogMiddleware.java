package templario01.io.transaction.adapter.input.web.middleware;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Component
@Order(2)
public class AuditLogMiddleware implements WebFilter {
    private static final Logger log = LoggerFactory.getLogger(AuditLogMiddleware.class);

    @Override
    public @NotNull Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String path = exchange.getRequest().getPath().value();
        String method = exchange.getRequest().getMethod().name();

        String accountId = exchange.getAttributeOrDefault("accountId", "accountId");

        log.info("Inbound Request - AccountId: {} | Action: [{}] {}", accountId, method, path);

        return chain.filter(exchange)
                .timeout(Duration.ofSeconds(10))
                .onErrorResume(java.util.concurrent.TimeoutException.class, e -> {
                    log.error("Request Ending with timeout error - AccountId: {} | Action: [{}] {}", accountId, method, path);
                    exchange.getResponse().setStatusCode(HttpStatus.GATEWAY_TIMEOUT);
                    return exchange.getResponse().setComplete();
                });
    }
}