package com.example.starter.application.ports.out;


import com.example.starter.domain.User;

import java.util.List;

public interface LoadUserListPort {
  List<User> loadUserList();
}
