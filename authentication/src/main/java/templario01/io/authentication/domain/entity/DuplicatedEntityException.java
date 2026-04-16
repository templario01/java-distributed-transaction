package templario01.io.authentication.domain.entity;

public class DuplicatedEntityException extends RuntimeException {

    public DuplicatedEntityException(String message) {
        super(message);
    }

}