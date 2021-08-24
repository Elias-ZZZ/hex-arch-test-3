package com.example.starter;

import com.example.starter.adapter.in.web.Controller;
import com.example.starter.adapter.out.persistence.UserPersistenceAdapter;
import com.example.starter.adapter.out.persistence.UserRepository;
import com.example.starter.application.ports.in.RegisterUserUseCase;
import com.example.starter.application.ports.out.LoadUserListPort;
import com.example.starter.application.ports.out.RegisterNewUserPort;
import com.example.starter.application.service.RegisterUserService;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.json.JsonObject;

import io.reactivex.Single;
public class MainVerticle extends MicroserviceVerticle {

  private RegisterUserUseCase registerUserUseCase;

  @Override
  public void start() throws Exception {
    super.start();
    retrieveConfigServer()
      .flatMap(config -> {
        UserRepository userRepository = new UserRepository(vertx,config);
        LoadUserListPort loadUserListPort = new UserPersistenceAdapter(userRepository);
        RegisterNewUserPort registerNewUserPort = new UserPersistenceAdapter(userRepository);
        registerUserUseCase = new RegisterUserService(loadUserListPort, registerNewUserPort);
        return deployRestVerticle(config);
      })
      .doOnError(Throwable::printStackTrace)
      .subscribe();
  }

  private Single<String> deployRestVerticle(JsonObject config) {

    return vertx.rxDeployVerticle(new Controller(registerUserUseCase),
      new DeploymentOptions().setConfig(config));
  }
}
