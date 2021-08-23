package com.example.starter.application.service;


import com.example.starter.application.ports.in.RegisterUserCommand;
import com.example.starter.application.ports.in.RegisterUserUseCase;
import com.example.starter.application.ports.out.LoadUserListPort;
import com.example.starter.application.ports.out.RegisterNewUserPort;
import com.example.starter.domain.User;

import java.util.List;

public class RegisterUserService implements RegisterUserUseCase {

  private final LoadUserListPort loadUserListPort;
  private final RegisterNewUserPort registerNewUserPort;

  public RegisterUserService(LoadUserListPort loadUserListPort, RegisterNewUserPort registerNewUserPort) {
    this.loadUserListPort = loadUserListPort;
    this.registerNewUserPort = registerNewUserPort;
  }

  @Override
  public void registerNewUser(RegisterUserCommand user) {
    User newUser = new User(user.getName(),user.getNickName(),user.getPassword());
    List<User> userList = loadUserListPort.loadUserList();
    if(!newUser.isNicknameAvailable(userList))
      throw new IllegalStateException("El nick " + newUser.getNickName() + "ya esta en uso");
    registerNewUserPort.registerNewUser(newUser);
  }
}
