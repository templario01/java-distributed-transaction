package templario01.io.transaction.application;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import templario01.io.transaction.domain.repository.TransactionRepository;

@ExtendWith(MockitoExtension.class)
public class FindTransactionUseCaseTest {
    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private FindTransactionUseCase useCase;
}
