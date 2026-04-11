package templario01.io.transaction.adapter.output.db;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import templario01.io.transaction.adapter.output.db.r2dbc.R2dbcTransactionRepository;
import templario01.io.transaction.domain.entity.TransactionEntity;
import templario01.io.transaction.domain.repository.TransactionRepository;

import java.util.UUID;

@RequiredArgsConstructor
@Repository
public class TransactionR2dbcAdapter implements TransactionRepository {
    private final R2dbcTransactionRepository transactionRepository;

    @Transactional
    public Mono<TransactionEntity> save(TransactionEntity transaction) {
        return transactionRepository.save(transaction);
    }

    public Mono<TransactionEntity> findByTransactionExternalId(UUID transactionExternalId) {
        return transactionRepository.findByTransactionExternalId(transactionExternalId);
    }
}
