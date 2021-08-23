package com.example.starter.adapter.out.persistence;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

@DataObject(generateConverter = true)
public class UserDBM {
  private Long id;
  private String name;
  private String nickName;
  private String password;

  public UserDBM(Long id, String name, String nickName, String password) {
    this.id = id;
    this.name = name;
    this.nickName = nickName;
    this.password = password;
  }

  public UserDBM(JsonObject json) {
    UserDBMConverter.fromJson(json,this);
  }

  public JsonObject toJson() {
    JsonObject json = new JsonObject();
    UserDBMConverter.toJson(this,json);
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
}
