package templario01.io.transaction.application;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import templario01.io.transaction.adapter.input.web.dto.TransactionResponseDto;
import templario01.io.transaction.domain.entity.EntityNotFoundException;
import templario01.io.transaction.domain.entity.TransactionEntity;
import templario01.io.transaction.domain.repository.TransactionRepository;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class FindTransactionUseCase {
    private static final Logger log = LoggerFactory.getLogger(FindTransactionUseCase.class);
    private final TransactionRepository transactionRepository;

    public Mono<TransactionResponseDto> execute(UUID transactionExternalId) {
        return transactionRepository.findByTransactionExternalId(transactionExternalId)
                .switchIfEmpty(Mono.error(new EntityNotFoundException()))
                .map(this::mapToResponseDto)
                .doOnError(error -> log.error("Error on get transaction", error));

    }

    private TransactionResponseDto mapToResponseDto(TransactionEntity savedTransaction) {
        return TransactionResponseDto.builder()
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
