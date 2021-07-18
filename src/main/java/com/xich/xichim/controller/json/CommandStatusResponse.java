package com.xich.xichim.controller.json;

public class CommandStatusResponse {
    private boolean success;
    private String msg;

    public CommandStatusResponse( boolean success, String msg){
        this.success = success;
        this.msg = msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMsg() {
        return msg;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
