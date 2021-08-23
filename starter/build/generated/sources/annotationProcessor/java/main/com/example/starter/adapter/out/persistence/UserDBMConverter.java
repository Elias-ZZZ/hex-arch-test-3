package com.example.starter.adapter.out.persistence;

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.impl.JsonUtil;
import java.time.Instant;
import java.time.format.DateTimeFormatter;

/**
 * Converter and mapper for {@link com.example.starter.adapter.out.persistence.UserDBM}.
 * NOTE: This class has been automatically generated from the {@link com.example.starter.adapter.out.persistence.UserDBM} original class using Vert.x codegen.
 */
public class UserDBMConverter {


  public static void fromJson(Iterable<java.util.Map.Entry<String, Object>> json, UserDBM obj) {
    for (java.util.Map.Entry<String, Object> member : json) {
      switch (member.getKey()) {
        case "id":
          break;
        case "name":
          break;
        case "nickName":
          break;
        case "password":
          break;
      }
    }
  }

  public static void toJson(UserDBM obj, JsonObject json) {
    toJson(obj, json.getMap());
  }

  public static void toJson(UserDBM obj, java.util.Map<String, Object> json) {
    if (obj.getId() != null) {
      json.put("id", obj.getId());
    }
    if (obj.getName() != null) {
      json.put("name", obj.getName());
    }
    if (obj.getNickName() != null) {
      json.put("nickName", obj.getNickName());
    }
    if (obj.getPassword() != null) {
      json.put("password", obj.getPassword());
    }
  }
}
