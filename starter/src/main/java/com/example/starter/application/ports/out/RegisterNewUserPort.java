package com.example.starter.application.ports.out;


import com.example.starter.domain.User;

public interface RegisterNewUserPort {
  void registerNewUser(User user);
}
