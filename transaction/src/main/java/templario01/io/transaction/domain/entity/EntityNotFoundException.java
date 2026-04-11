package templario01.io.transaction.domain.entity;

public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException() {
        super("Resource not found");
    }

}