package com.example.starter.domain;

import java.util.List;

public class User {
  private Long id;
  private String name;
  private String nickName;
  private String password;

  public User(Long id, String name, String nickName, String password) {
    this.id = id;
    this.name = name;
    this.nickName = nickName;
    this.password = password;
  }

  public User(String name, String nickName, String password) {
    this.name = name;
    this.nickName = nickName;
    this.password = password;
  }

  public boolean isNicknameAvailable(List<User> userList){
    long coincidences = userList
      .stream()
      .filter(user -> user.nickName.equals(this.nickName))
      .count();
    if(coincidences > 0) {
      return false;
    }
    return true;
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
