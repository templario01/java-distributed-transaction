package templario01.io.antifraud.adapter.input.event;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate;
import org.springframework.stereotype.Service;
import templario01.io.antifraud.adapter.input.event.dto.TransactionEventInbound;
import templario01.io.antifraud.application.ValidateSuspiciousTransactionUseCase;


@Service
public class TransactionConsumer {
    private static final Logger log = LoggerFactory.getLogger(TransactionConsumer.class);

    private final ReactiveKafkaConsumerTemplate<String, Object> kafkaTemplate;
    private final ValidateSuspiciousTransactionUseCase validateSuspiciousTransactionUseCase;

    public TransactionConsumer(ReactiveKafkaConsumerTemplate<String, Object> kafkaTemplate, ValidateSuspiciousTransactionUseCase validateSuspiciousTransactionUseCase) {
        this.kafkaTemplate = kafkaTemplate;
        this.validateSuspiciousTransactionUseCase = validateSuspiciousTransactionUseCase;
    }

    @PostConstruct
    public void listen() {
        kafkaTemplate
                .receiveAutoAck()
                .doOnNext(record -> log.info("Mensaje recibido del tópico: {} | Partición: {} | Offset: {}",
                        record.topic(), record.partition(), record.offset()))
                .flatMap(record -> {
                    TransactionEventInbound data = (TransactionEventInbound) record.value();
                    return validateSuspiciousTransactionUseCase.execute(data);
                })
                .doOnError(e -> log.error("Error receiving data from Kafka", e))
                .retry()
                .subscribe();
    }
}