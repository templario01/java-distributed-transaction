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
                .transaction_external_id(UUID.randomUUID())
                .created_at(java.time.LocalDateTime.now())
                .account_external_id_debit(transactionRequestDto.getAccountExternalIdDebit())
                .account_external_id_credit(transactionRequestDto.getAccountExternalIdCredit())
                .transaction_status(TransactionStatusEnum.PENDING)
                .transfer_type_id(transactionRequestDto.getTransferTypeId())
                .value(transactionRequestDto.getValue())
                .build();

        log.info("Creating transaction: {}", transaction);

        return this.transactionRepository.save(transaction)
                .doOnSuccess(saved -> log.info("Transaction saved: {}", saved.getTransaction_external_id()))
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
                .transactionExternalId(savedTransaction.getTransaction_external_id())
                .accountExternalIdDebit(savedTransaction.getAccount_external_id_debit())
                .accountExternalIdCredit(savedTransaction.getAccount_external_id_credit())
                .transactionStatus(savedTransaction.getTransaction_status())
                .transferTypeId(savedTransaction.getTransfer_type_id())
                .value(savedTransaction.getValue())
                .createdAt(savedTransaction.getCreated_at())
                .build();
    }
}