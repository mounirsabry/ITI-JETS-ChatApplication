package jets.projects.classes;

public class RequestResult<T> {
    private final T responseData;
    private final String errorMessage;
    
    public RequestResult(T responseData, String errorMessage) {
        this.responseData = responseData;
        this.errorMessage = errorMessage;
    }

    public T getResponseData() {
        return responseData;
    }
    
    public String getErrorMessage() {
        return errorMessage;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        
        builder.append(RequestResult.class.getName());
        builder.append('{');
        
        builder.append("responseData=");
        builder.append(responseData);
        
        builder.append(", errorMessage=");
        builder.append(errorMessage);
        
        builder.append('}');
        return builder.toString();
    }
}
