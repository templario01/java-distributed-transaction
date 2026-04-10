package templario01.io.antifraud.adapter.input.event.dto;

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
    private UUID transaction_external_id;

    private TransactionStatusEnum transaction_status;
}
