package com.example.starter.application.ports.in;

import static java.util.Objects.requireNonNull;

public class RegisterUserCommand {
  private Long id;
  private String name;
  private String nickName;
  private String password;

  public RegisterUserCommand(Long id, String name, String nickName, String password) {
    this.id = id;
    this.name = name;
    this.nickName = nickName;
    this.password = password;

    requireNonNull(id);
    requireNonNull(name);
    requireNonNull(nickName);
    requireNonNull(password);
  }

  public RegisterUserCommand(String name, String nickName, String password) {
    this.name = name;
    this.nickName = nickName;
    this.password = password;

    requireNonNull(name);
    requireNonNull(nickName);
    requireNonNull(password);
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
