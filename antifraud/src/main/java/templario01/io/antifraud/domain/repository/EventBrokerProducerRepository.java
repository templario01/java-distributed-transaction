package templario01.io.antifraud.domain.repository;

import reactor.core.publisher.Mono;

public interface EventBrokerProducerRepository {
    public <T> Mono<Void> sendMessage(T data, String topic);
}
