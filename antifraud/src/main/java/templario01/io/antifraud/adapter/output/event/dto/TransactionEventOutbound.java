package templario01.io.antifraud.adapter.output.event.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionEventOutbound {
    private UUID transactionExternalId;

    private TransactionStatusEnum transactionStatus;
}
