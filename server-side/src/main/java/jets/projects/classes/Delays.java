package jets.projects.classes;

public class Delays {
    public static final long SERVER_CHECK_NEXT_COMMAND_DELAY = 500; // In millis.
    public static final long EXECUTOR_AWAIT_TERMINATION_TIMEOUT = 2; // In millis.
    public static final long TIMEOUT_USER_CHECK_DELAY = 10; // In seconds.
    public static final long USER_TIMEOUT_DELAY = 1000 * 60; // In millis.
    
    private Delays() {
        throw new UnsupportedOperationException("Do not create object.");
    }
}
