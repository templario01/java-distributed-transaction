package templario01.io.authentication.domain.repository;

import reactor.core.publisher.Mono;
import templario01.io.authentication.domain.entity.UserEntity;

public interface UserRepository {
    Mono<UserEntity> findByUsername(String username);
    Mono<UserEntity> save(UserEntity user);
}