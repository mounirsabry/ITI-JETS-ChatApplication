module jets.projects {
    requires java.sql;
    requires java.naming;
    requires java.rmi;
    
    opens jets.projects.api to java.rmi;
    exports jets.projects.api;
}