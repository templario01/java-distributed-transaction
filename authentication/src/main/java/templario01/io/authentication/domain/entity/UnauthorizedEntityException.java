package templario01.io.authentication.domain.entity;

public class UnauthorizedEntityException extends RuntimeException {
    public UnauthorizedEntityException() {
        super("Usuario o Contraseña incorrectos");
    }
}
