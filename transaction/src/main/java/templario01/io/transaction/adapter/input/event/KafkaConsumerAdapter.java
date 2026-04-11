package templario01.io.transaction.adapter.input.event;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate;
import org.springframework.stereotype.Service;
import templario01.io.transaction.adapter.input.event.dto.ValidatedTransactionEventInbound;
import templario01.io.transaction.application.UpdateValidatedTransactionUseCase;
import templario01.io.transaction.domain.repository.EventBrokerConsumerRepository;

@Service
public class KafkaConsumerAdapter implements EventBrokerConsumerRepository {
    private static final Logger log = LoggerFactory.getLogger(KafkaConsumerAdapter.class);

    private final ReactiveKafkaConsumerTemplate<String, Object> kafkaTemplate;
    private final UpdateValidatedTransactionUseCase updateValidatedTransactionUseCase;

    public KafkaConsumerAdapter(ReactiveKafkaConsumerTemplate<String, Object> kafkaTemplate, UpdateValidatedTransactionUseCase updateValidatedTransactionUseCase) {
        this.kafkaTemplate = kafkaTemplate;
        this.updateValidatedTransactionUseCase = updateValidatedTransactionUseCase;
    }

    @PostConstruct
    public void listen() {
        kafkaTemplate
                .receiveAutoAck()
                .doOnNext(record -> log.info("Mensaje recibido del tópico: {} | Partición: {} | Offset: {}",
                        record.topic(), record.partition(), record.offset()))
                .flatMap(record -> {
                    ValidatedTransactionEventInbound data = (ValidatedTransactionEventInbound) record.value();
                    return updateValidatedTransactionUseCase.execute(data);
                })
                .doOnError(e -> log.error("Error receiving data from Kafka", e))
                .retry()
                .subscribe();
    }
}