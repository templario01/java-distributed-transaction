package templario01.io.antifraud.adapter.input.event.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionEventInbound {

    private UUID transaction_external_id;

    private String account_external_id_debit;

    private String account_external_id_credit;

    private Integer transfer_type_id;

    private Double value;

    private LocalDateTime created_at;

}
