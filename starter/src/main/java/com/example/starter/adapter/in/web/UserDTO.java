package com.example.starter.adapter.in.web;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

@DataObject(generateConverter = true)
public class UserDTO {
  private Long id;
  private String name;
  private String nickName;
  private String password;

  public UserDTO(Long id, String name, String nickName, String password) {
    this.id = id;
    this.name = name;
    this.nickName = nickName;
    this.password = password;
  }

  public UserDTO(String name, String nickName, String password) {
    this.name = name;
    this.nickName = nickName;
    this.password = password;
  }

  public UserDTO(JsonObject json) {
    UserDTOConverter.fromJson(json, this);
  }

  public JsonObject toJson() {
    JsonObject json = new JsonObject();
    UserDTOConverter.toJson(this, json);
    return json;
  }

  @Override
  public String toString() {
    return this.toJson().encode();
  }

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getNickName() {
    return nickName;
  }

  public String getPassword() {
    return password;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setNickName(String nickName) {
    this.nickName = nickName;
  }

  public void setPassword(String password) {
    this.password = password;
  }
}
