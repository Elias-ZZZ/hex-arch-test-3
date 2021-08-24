package com.example.starter.adapter.in.web;

import com.example.starter.MicroserviceVerticle;
import io.reactivex.Single;
import io.vertx.core.Future;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.reactivex.core.http.HttpServer;
import io.vertx.reactivex.core.http.HttpServerResponse;
import io.vertx.reactivex.ext.web.Router;
import io.vertx.reactivex.ext.web.RoutingContext;
import io.vertx.reactivex.ext.web.handler.BodyHandler;

public class Controller extends MicroserviceVerticle {
  private static final String NOMBRE_SERVICIO = "rest-api";



  private static final String API_POST_REGISTER_NEW_USER = "/new-user";

  private Single<HttpServer> createHttpServer(Router router, String host, int port) {
    HttpServerOptions options = new HttpServerOptions().setMaxHeaderSize(500000);
    return vertx.createHttpServer(options)
      .requestHandler(router)
      .rxListen(port, host);
  }


  @Override
  public void start(Promise<Void> future) throws Exception{
    super.start();
    final Router router = Router.router(vertx);
    router.route().handler(BodyHandler.create());
    System.out.println("TEST");

    router.post(API_POST_REGISTER_NEW_USER).handler(this::apiPostRegisterNewUser);

    String host = config().getString("api.http.address", "0.0.0.0");
    int port = config().getInteger("api.http.port", 8080);

    // create HTTP server and publish REST proxy
    createHttpServer(router, host, port)
      .flatMap(serverCreated -> publishHttpEndpoint(NOMBRE_SERVICIO, host, port))
      .subscribe((record) -> future.complete(), future::fail);
  }

  private void apiPostRegisterNewUser(RoutingContext context) {
    System.out.println("API_POST_NEW_USER");
    HttpServerResponse response = context.response();
    response.end();
  }

}
