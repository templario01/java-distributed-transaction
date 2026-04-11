package templario01.io.transaction.domain.repository;

import reactor.core.publisher.Mono;

public interface EventBrokerProducerRepository {
    public <T> Mono<Void> sendMessage(T data, String topic);
}
