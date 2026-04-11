package templario01.io.transaction.domain.repository;

import reactor.core.publisher.Mono;
import templario01.io.transaction.domain.entity.TransactionEntity;

import java.util.UUID;

public interface TransactionRepository {
    Mono<TransactionEntity> save(TransactionEntity transaction);
    Mono<TransactionEntity> findByTransactionExternalId(UUID transactionExternalId);
}
