package templario01.io.transaction.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import templario01.io.transaction.adapter.input.event.dto.ValidatedTransactionEventInbound;
import templario01.io.transaction.domain.repository.TransactionRepository;

@RequiredArgsConstructor
@Service
public class UpdateValidatedTransactionUseCase {
    private final TransactionRepository transactionRepository;

    public Mono<Void> execute(ValidatedTransactionEventInbound data) {
        return transactionRepository.findByTransactionExternalId(data.getTransactionExternalId())
                .flatMap(transaction -> {
                    transaction.setTransactionStatus(data.getTransactionStatus());
                    return transactionRepository.save(transaction);
                })
                .then();
    }
}
