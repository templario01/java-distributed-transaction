package templario01.io.authentication.application;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import templario01.io.authentication.domain.entity.UnauthorizedEntityException;
import templario01.io.authentication.domain.repository.TokenProvider;
import templario01.io.authentication.domain.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class LoginUseCase {

    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;

    public Mono<String> execute(String username, String password) {
        return userRepository.findByUsername(username)
                .filter(user -> passwordEncoder.matches(password, user.getPassword()))
                .map(tokenProvider::generateToken)
                .switchIfEmpty(Mono.error(new UnauthorizedEntityException()));
    }
}