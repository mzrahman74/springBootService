package com.mohammadrahman.springBootService.controller;

public class AddResponse {
    private String msg;
    private String id;

  public String getMsg() {
    return msg;
  }

  public String getId() {
    return id;
  }

  public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setId(String id) {
        this.id = id;
    }
}
