package templario01.io.authentication.domain.repository;

import templario01.io.authentication.domain.entity.UserEntity;

public interface TokenProvider {
    String generateToken(UserEntity user);
}
