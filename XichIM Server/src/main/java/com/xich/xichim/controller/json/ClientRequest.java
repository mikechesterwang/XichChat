package com.xich.xichim.controller.json;

public class ClientRequest {
    private String command;
    private String[] parameters;
    private String id;

    public String getCommand() {
        return command;
    }

    public String[] getParameters() {
        return parameters;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public void setParameters(String[] parameters) {
        this.parameters = parameters;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
