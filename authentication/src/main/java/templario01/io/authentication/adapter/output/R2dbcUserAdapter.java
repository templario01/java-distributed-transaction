package templario01.io.authentication.adapter.output;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import templario01.io.authentication.adapter.output.r2dbc.Rd2bcUserRepository;
import templario01.io.authentication.domain.entity.UserEntity;
import templario01.io.authentication.domain.repository.UserRepository;

@RequiredArgsConstructor
@Repository
public class R2dbcUserAdapter implements UserRepository {

    private final Rd2bcUserRepository repository;

    @Override
    public Mono<UserEntity> findByUsername(String username) {
        return repository.findByUsername(username);
    }

    @Override
    public Mono<UserEntity> save(UserEntity user) {
        return repository.save(user);
    }
}