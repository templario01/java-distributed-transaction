package templario01.io.authentication.application;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import templario01.io.authentication.adapter.input.web.dto.UserRequestDto;
import templario01.io.authentication.domain.entity.DuplicatedEntityException;
import templario01.io.authentication.domain.entity.RoleEnum;
import templario01.io.authentication.domain.entity.UserEntity;
import templario01.io.authentication.domain.repository.UserRepository;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RegisterUseCase {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public Mono<Void> execute(UserRequestDto user) {
        UserEntity userEntity = new UserEntity();
        userEntity.setAccountId(UUID.randomUUID());
        userEntity.setUsername(user.getUsername());
        userEntity.setPassword(passwordEncoder.encode(user.getPassword()));
        userEntity.setRoles((RoleEnum.USER.name()));

        return userRepository.findByUsername(user.getUsername())
                .flatMap(existingUser -> Mono.error(new DuplicatedEntityException("El nombre de usuario ya está registrado")))
                .switchIfEmpty(userRepository.save(userEntity))
                .then();
    }
}