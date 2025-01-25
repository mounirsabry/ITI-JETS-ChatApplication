package jets.projects.classes;

public class RequestResult<T> {
    private final Boolean isValid;
    private final T responseData;

    public RequestResult(Boolean isValid, T responseData) {
        this.isValid = isValid;
        this.responseData = responseData;
    }

    public Boolean getIsValid() {
        return isValid;
    }

    public T getResponseData() {
        return responseData;
    }
    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        
        builder.append(RequestResult.class.getName());
        builder.append('{');
        
        builder.append("isValid=");
        builder.append(isValid);
        
        builder.append(", responseData=");
        builder.append(responseData);
        
        builder.append('}');
        return builder.toString();
    }
}
