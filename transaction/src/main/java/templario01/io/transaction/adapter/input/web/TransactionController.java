package templario01.io.transaction.adapter.input.web;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import templario01.io.transaction.adapter.input.web.dto.TransactionRequestDto;
import templario01.io.transaction.adapter.input.web.dto.TransactionResponseDto;
import templario01.io.transaction.application.CreateTransactionUseCase;
import templario01.io.transaction.application.FindTransactionUseCase;

@RestController
@RequestMapping("/transaction")
public class TransactionController {
    private static final Logger log = LoggerFactory.getLogger(TransactionController.class);
    private final CreateTransactionUseCase createTransactionUseCase;
    private final FindTransactionUseCase findTransactionUseCase;

    public TransactionController(CreateTransactionUseCase createTransactionUseCase, FindTransactionUseCase findTransactionUseCase) {
        this.createTransactionUseCase = createTransactionUseCase;
        this.findTransactionUseCase = findTransactionUseCase;
    }

    @GetMapping("/{transactionExternalId}")
    public Mono<ResponseEntity<TransactionResponseDto>> getTransaction(@PathVariable() String transactionExternalId) {
        return this.findTransactionUseCase.execute(java.util.UUID.fromString(transactionExternalId))
                .map(response -> ResponseEntity.status(HttpStatus.OK).body(response));
    }

    @PostMapping
    public Mono<ResponseEntity<TransactionResponseDto>> createTransfer(@Valid @RequestBody TransactionRequestDto request) {
        return this.createTransactionUseCase.execute(request)
                .map(response -> ResponseEntity.status(HttpStatus.CREATED).body(response));
    }

}
