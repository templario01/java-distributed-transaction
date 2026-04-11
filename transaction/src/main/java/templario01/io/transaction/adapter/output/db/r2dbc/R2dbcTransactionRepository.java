package templario01.io.transaction.adapter.output.db.r2dbc;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;
import templario01.io.transaction.domain.entity.TransactionEntity;

import java.util.UUID;

public interface R2dbcTransactionRepository extends R2dbcRepository<TransactionEntity, Long> {
    Mono<TransactionEntity> findByTransactionExternalId(UUID transactionExternalId);
}

