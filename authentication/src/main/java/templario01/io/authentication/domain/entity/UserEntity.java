package templario01.io.authentication.domain.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("users")
public class UserEntity {

    @Id
    @Column("id")
    private Long id;

    @Column("account_id")
    private UUID accountId;

    @Column("username")
    private String username;

    @Column("password")
    private String password;

    @Column("roles")
    private String roles;
}