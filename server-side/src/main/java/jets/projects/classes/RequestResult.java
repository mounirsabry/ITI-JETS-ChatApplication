package jets.projects.classes;

public class RequestResult<T> {
    private Boolean isValid;
    private T requiredData;

    public RequestResult(Boolean isValid, T requiredData) {
        this.isValid = isValid;
        this.requiredData = requiredData;
    }

    public Boolean getIsValid() {
        return isValid;
    }

    public T getRequiredData() {
        return requiredData;
    }
}
