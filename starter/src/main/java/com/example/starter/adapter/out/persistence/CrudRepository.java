package com.example.starter.adapter.out.persistence;

import io.reactivex.Observable;
import io.vertx.reactivex.ext.jdbc.JDBCClient;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.sql.ResultSet;
import io.vertx.reactivex.core.Vertx;


public abstract class CrudRepository<T,ID> implements CrudBaseRepository<T>{
  protected final JDBCClient jdbcClient;

  public CrudRepository(Vertx vertx, JsonObject config) {
    this.jdbcClient = JDBCClient.create(vertx, config);
  }

  protected Observable<T> executeQueryResult(String query, JsonArray params) {
    return jdbcClient.rxGetConnection()
      .flatMapObservable(conn -> conn
        .rxQueryWithParams(query, params)
        .map(ResultSet::getRows)
        .flatMapObservable(Observable::fromIterable)
        .map(this::wrapJsonToObject)
        .doAfterTerminate(conn::close)
      );
  }
}
