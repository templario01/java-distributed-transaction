package templario01.io.authentication.adapter.input.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import templario01.io.authentication.adapter.input.web.dto.AuthResponseDto;
import templario01.io.authentication.adapter.input.web.dto.LoginRequestDto;
import templario01.io.authentication.adapter.input.web.dto.UserRequestDto;
import templario01.io.authentication.application.LoginUseCase;
import templario01.io.authentication.application.RegisterUseCase;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final LoginUseCase loginUseCase;
    private final RegisterUseCase registerUseCase;

    @PostMapping("/login")
    public Mono<ResponseEntity<AuthResponseDto>> login(@Valid @RequestBody LoginRequestDto request) {
        return loginUseCase.execute(request.getUsername(), request.getPassword())
                .map(token -> ResponseEntity.ok(new AuthResponseDto(token)));
    }

    @PostMapping("/register")
    public Mono<ResponseEntity<Void>> register(@Valid @RequestBody UserRequestDto user) {
        return registerUseCase.execute(user)
                .map(savedUser -> ResponseEntity.status(HttpStatus.CREATED).build());
    }
}