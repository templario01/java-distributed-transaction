package templario01.io.antifraud.application;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import templario01.io.antifraud.adapter.input.event.dto.TransactionEventInbound;
import templario01.io.antifraud.adapter.output.event.dto.TransactionEventOutbound;
import templario01.io.antifraud.adapter.output.event.dto.TransactionStatusEnum;
import templario01.io.antifraud.adapter.output.event.KafkaProducerAdapter;
import templario01.io.antifraud.domain.repository.EventBrokerProducerRepository;

@RequiredArgsConstructor
@Service
public class ValidateSuspiciousTransactionUseCase {
    private static final double MAX_AMOUNT_PER_TRANSACTION = 1000;
    private static final Logger log = LoggerFactory.getLogger(ValidateSuspiciousTransactionUseCase.class);
    private final EventBrokerProducerRepository eventBrokerProducerRepository;

    public Mono<Void> execute(TransactionEventInbound transactionEvent) {
        TransactionEventOutbound transactionEventOutbound = TransactionEventOutbound.builder()
                .transactionExternalId(transactionEvent.getTransactionExternalId())
                .transactionStatus(transactionEvent.getValue() > MAX_AMOUNT_PER_TRANSACTION
                        ? TransactionStatusEnum.REJECTED
                        : TransactionStatusEnum.APPROVED)
                .build();

        return eventBrokerProducerRepository.sendMessage(transactionEventOutbound, "payment.transaction.status")
                .doOnError(e -> log.error("Error al enviar: {}", e.getMessage()))
                .then();
    }
}