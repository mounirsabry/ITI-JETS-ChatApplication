package jets.projects;

import io.tasks.classes.ServerCommand;

public class ServerManager {
    private volatile ServerCommand nextCommand;
    
    ServerManager() {
        nextCommand = ServerCommand.WAIT;
    }
    
    public synchronized ServerCommand getNextCommand() {
        return nextCommand;
    }
    
    public synchronized void setNextCommand(ServerCommand command) {
        nextCommand = command;
    }
    
    public synchronized ServerCommand getAndSetNextCommand(
            ServerCommand command) {
        ServerCommand currentCommand = nextCommand;
        nextCommand = command;
        return currentCommand;
    }
}

