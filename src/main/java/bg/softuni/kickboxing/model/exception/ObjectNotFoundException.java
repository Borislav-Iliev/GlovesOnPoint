package bg.softuni.kickboxing.model.exception;

public class ObjectNotFoundException extends RuntimeException {
    private final Long objectId;

    public ObjectNotFoundException(Long objectId) {
        this.objectId = objectId;
    }

    public Long getObjectId() {
        return objectId;
    }
}
