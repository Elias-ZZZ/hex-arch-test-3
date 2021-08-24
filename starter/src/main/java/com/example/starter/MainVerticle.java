package com.example.starter;

import com.example.starter.adapter.in.web.Controller;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;

import io.reactivex.Single;
public class MainVerticle extends MicroserviceVerticle {

  @Override
  public void start() throws Exception {
    super.start();
    retrieveConfigServer()
      .flatMap(config -> {
        return deployRestVerticle(config);
      })
      .doOnError(Throwable::printStackTrace)
      .subscribe();
    /*vertx.createHttpServer().requestHandler(req -> {
      req.response()
        .putHeader("content-type", "text/plain")
        .end("Hello from Vert.x!");
    }).listen(8888, http -> {
      if (http.succeeded()) {
        startPromise.complete();
        System.out.println("HTTP server started on port 8888");
      } else {
        startPromise.fail(http.cause());
      }
    });*/
  }

  private Single<String> deployRestVerticle(JsonObject config) {
    return vertx.rxDeployVerticle(new Controller(),
      new DeploymentOptions().setConfig(config));
  }
}
