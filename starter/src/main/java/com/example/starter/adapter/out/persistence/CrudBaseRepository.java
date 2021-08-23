package com.example.starter.adapter.out.persistence;

import io.vertx.core.json.JsonObject;

public interface CrudBaseRepository<T> {
  T wrapJsonToObject(JsonObject json);
}
