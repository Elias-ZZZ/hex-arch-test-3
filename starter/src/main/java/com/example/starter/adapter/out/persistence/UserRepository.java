package com.example.starter.adapter.out.persistence;

import io.reactivex.Observable;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.core.Vertx;

public class UserRepository extends CrudRepository<UserDBM, Long> {

  public UserRepository(Vertx vertx, JsonObject config) {
    super(vertx, config);
  }

  public Observable<UserDBM> retrieveUserList() {
    String query = "SELECT * FROM `user`";
    return executeQueryResult(query, new JsonArray());
  }

  @Override
  public UserDBM wrapJsonToObject(JsonObject json) {
    return new UserDBM(json);
  }
}
