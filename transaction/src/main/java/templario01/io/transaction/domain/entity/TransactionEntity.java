package templario01.io.transaction.domain.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("transaction")
public class TransactionEntity {

    @Id
    @Column("id")
    private Long id;

    @Column("transaction_external_id")
    private UUID transaction_external_id;

    @Column("account_external_id_debit")
    private String account_external_id_debit;

    @Column("account_external_id_credit")
    private String account_external_id_credit;

    @Column("transaction_status")
    private TransactionStatusEnum transaction_status;

    @Column("transfer_type_id")
    private Integer transfer_type_id;

    @Column("value")
    private Double value;

    @Column("created_at")
    private LocalDateTime created_at;

}
