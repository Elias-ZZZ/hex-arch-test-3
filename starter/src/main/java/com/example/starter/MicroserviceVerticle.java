package com.example.starter;


import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;
import io.vertx.circuitbreaker.CircuitBreakerOptions;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.impl.ConcurrentHashSet;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.reactivex.circuitbreaker.CircuitBreaker;
import io.vertx.reactivex.config.ConfigRetriever;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.ServiceDiscoveryOptions;
import io.vertx.servicediscovery.types.EventBusService;
import io.vertx.servicediscovery.types.HttpEndpoint;

import java.util.Set;

public class MicroserviceVerticle extends AbstractVerticle {

  protected CircuitBreaker circuitBreaker;
  protected ServiceDiscovery serviceDiscovery;
  protected Set<Record> registeredRecords = new ConcurrentHashSet<>();
  protected Set<String> registeredServices = new ConcurrentHashSet<>();
  private static final Logger logger = LoggerFactory.getLogger(MicroserviceVerticle.class);
  private static final String LOCAL_PROFILE = "local";



  @Override
  public void start() throws Exception {
    System.out.println("Start en MicroserviceVerticle");
    // init service discovery instance
    serviceDiscovery = ServiceDiscovery.create(vertx, new ServiceDiscoveryOptions().setBackendConfiguration(config()));
    // init circuit breaker instance
    JsonObject cbOptions = config().getJsonObject("circuit-breaker") != null ?
      config().getJsonObject("circuit-breaker") : new JsonObject();
    circuitBreaker = CircuitBreaker.create(cbOptions.getString("name", "circuit-breaker"), vertx,
      new CircuitBreakerOptions()
        .setMaxFailures(cbOptions.getInteger("max-failures", 5))
        .setTimeout(cbOptions.getLong("timeout", 10000L))
        .setFallbackOnFailure(true)
        .setResetTimeout(cbOptions.getLong("reset-timeout", 30000L))
    );
  }

  protected Single<Record> publishEventBusService(String name, String address, Class serviceClass) {
    Record record = EventBusService.createRecord(name, address, serviceClass);
    return publish(record);
  }

  private Single<Record> publish(Record record) {
    if (serviceDiscovery == null) {
      try {
        start();
      } catch (Exception e) {
        throw new IllegalStateException("Cannot create serviceDiscovery service");
      }
    }
    // publish the service
    return serviceDiscovery
      .rxPublish(record)
      .doOnSuccess(rec -> {
        serviceDiscovery.close();
        registeredRecords.add(record);
        logger.info("Service <" + rec.getName() + "> published");
      });
  }

  protected Single<JsonObject> retrieveConfigServer() {
    if (!config().containsKey("app.profile") || (config().containsKey("app.profile") && config().getString("app.profile").equals(LOCAL_PROFILE)))
      return Single.just(config());
    JsonObject vault_config = new JsonObject()
      .put("host", config().getString("config.host"))
      .put("port", config().getInteger("config.port"))
      .put("path", config().getString("config.path"))
      .put("token", config().getString("config.token"))
      .put("ssl", config().getBoolean("config.ssl"));
    ConfigStoreOptions store = new ConfigStoreOptions()
      .setType("vault")
      .setConfig(vault_config);
    ConfigRetriever retriever = ConfigRetriever.create(vertx,
      new ConfigRetrieverOptions().addStore(store));
    return retriever
      .rxGetConfig()
      .flatMap(config -> {
        if (config.isEmpty()) {
          return rxStop().toSingleDefault(config);
        }
        return Single.just(config().mergeIn(config));
      });
  }

  public Completable rxStop() {
    // In current design, the publisher is responsible for removing the service
    logger.info("Anulando registro de Records en Service Discovery.");
    return Flowable.fromArray(registeredRecords.toArray())
      .map(record -> serviceDiscovery.rxUnpublish(((Record) record).getRegistration()))
      .flatMap(completable -> Flowable.fromArray(registeredServices.toArray()))
      .map(record -> serviceDiscovery.rxUnpublish(((Record) record).getRegistration()))
      .flatMapCompletable(completable -> {
        serviceDiscovery.close();
        return completable;
      });
  }

  protected Single<Record> publishHttpEndpoint(String name, String host, int port) {
    Record record = HttpEndpoint.createRecord(name, host, port, "/",
      new JsonObject().put("api.name", config().getString("api.name", ""))
    );
    return publish(record);
  }
}
