package templario01.io.authentication.adapter.output.r2dbc;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;
import templario01.io.authentication.domain.entity.UserEntity;

public interface Rd2bcUserRepository extends R2dbcRepository<UserEntity, Long> {
    Mono<UserEntity> findByUsername(String username);
}
