package templario01.io.transaction.application;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import templario01.io.transaction.adapter.input.web.dto.TransactionRequestDto;
import templario01.io.transaction.adapter.input.web.dto.TransactionResponseDto;
import templario01.io.transaction.application.services.KafkaProducer;
import templario01.io.transaction.domain.entity.TransactionEntity;
import templario01.io.transaction.domain.entity.TransactionStatusEnum;
import templario01.io.transaction.domain.port.TransactionRepository;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class CreateTransactionUseCase {
    private static final Logger log = LoggerFactory.getLogger(CreateTransactionUseCase.class);

    private final TransactionRepository transactionRepository;
    private final KafkaProducer kafkaProducer; // Inyectamos el adapter de infraestructura

    @Transactional
    public Mono<TransactionResponseDto> execute(TransactionRequestDto transactionRequestDto) {
        TransactionEntity transaction = TransactionEntity.builder()
                .transactionExternalId(UUID.randomUUID())
                .createdAt(java.time.LocalDateTime.now())
                .accountExternalIdDebit(transactionRequestDto.getAccountExternalIdDebit())
                .accountExternalIdCredit(transactionRequestDto.getAccountExternalIdCredit())
                .transactionStatus(TransactionStatusEnum.PENDING)
                .transferTypeId(transactionRequestDto.getTransferTypeId())
                .value(transactionRequestDto.getValue())
                .build();

        log.info("Creating transaction: {}", transaction);

        return this.transactionRepository.save(transaction)
                .doOnSuccess(saved -> log.info("Transaction saved: {}", saved.getTransactionExternalId()))
                .flatMap(savedTransaction ->
                        kafkaProducer.sendMessage(savedTransaction, "payment.transaction")
                                .thenReturn(savedTransaction)
                )
                .map(this::mapToResponseDto)
                .doOnError(error -> log.error("Error in transaction flow", error));
    }

    private TransactionResponseDto mapToResponseDto(TransactionEntity savedTransaction) {
        return TransactionResponseDto.builder()
                .id(savedTransaction.getId())
                .transactionExternalId(savedTransaction.getTransactionExternalId())
                .accountExternalIdDebit(savedTransaction.getAccountExternalIdDebit())
                .accountExternalIdCredit(savedTransaction.getAccountExternalIdCredit())
                .transactionStatus(savedTransaction.getTransactionStatus())
                .transferTypeId(savedTransaction.getTransferTypeId())
                .value(savedTransaction.getValue())
                .createdAt(savedTransaction.getCreatedAt())
                .build();
    }
}