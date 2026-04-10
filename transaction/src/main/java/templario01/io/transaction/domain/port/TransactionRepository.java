package templario01.io.transaction.domain.port;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import templario01.io.transaction.domain.entity.TransactionEntity;


public interface TransactionRepository extends R2dbcRepository<TransactionEntity, Long> { }

