package com.example.starter.adapter.out.persistence;


import com.example.starter.application.ports.out.LoadUserListPort;
import com.example.starter.application.ports.out.RegisterNewUserPort;
import com.example.starter.domain.User;
import io.vertx.core.Future;

import java.util.List;

public class UserPersistenceAdapter implements LoadUserListPort, RegisterNewUserPort {

  private final UserRepository userRepository;

  public UserPersistenceAdapter(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public List<User> loadUserList() {
    return userRepository
      .retrieveUserList()
      .map(userDBM ->
        new User(userDBM.getId(),userDBM.getName(),userDBM.getNickName(),userDBM.getPassword())
      )
      .toList()
      .blockingGet();
      //.subscribe(future::onComplete);
  }

  @Override
  public void registerNewUser(User user) {

  }
}
