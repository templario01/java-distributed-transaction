package templario01.io.transaction.domain.port;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;
import templario01.io.transaction.domain.entity.TransactionEntity;

import java.util.UUID;


public interface TransactionRepository extends R2dbcRepository<TransactionEntity, Long> {
    Mono<TransactionEntity> findByTransactionExternalId(UUID transactionExternalId);
}

