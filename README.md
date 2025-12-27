# Splitr: Distributed Synchronous Query Bus

**Splitr** is a lightweight, Spring Boot-based library designed to implement the **Request-Response pattern over Kafka**. It allows microservices to execute synchronous, typed queries across distributed boundaries while maintaining idempotency and high performance.

## 🚀 Features

* **Synchronous over Asynchronous:** Execute queries over Kafka topics while blocking the local thread for a response, mimicking a REST call over a message broker.
* **Automatic Dispatching:** Just implement `QueryHandler<T>`, and the library routes the messages automatically.
* **Built-in Idempotency:** Prevents redundant business logic execution using a configurable LRU (Least Recently Used) cache.
* **Type Safety:** Fully supports polymorphic queries through Java Generics and Jackson JSON serialization.
* **Configurable Timeout:** Global or per-query timeout management to prevent thread hanging.

---

## 📦 Installation

To use Splitr in your Spring Boot project, ensure the package `tr.kontas.splitr` is within your component scan path or use the Auto-Configuration.

### 1. Publisher Configuration (Service A)

Enable the publisher side to send queries.

```yaml
splitr:
  publisher:
    enabled: true
  callback-url: http://localhost:8080/internal/query/callback
  bus:
    kafka:
      topic: tr.kontas.splitr.query.topic
    default-timeout: 5000

```

### 2. Consumer Configuration (Service B)

Enable the consumer side to process queries.

```yaml
splitr:
  consumer:
    enabled: true
  idempotency:
    max-size: 1000

```

---

## 🛠 Usage

### Defining a Query

Create a DTO that represents your query. It is recommended to extend a base class or implement a marker interface.

```java
public record OrderQuery(String orderId) { }

```

### Executing a Query (Publisher)

Inject the `QueryBus` and call `querySync`.

```java
@RestController
@RequiredArgsConstructor
public class OrderController {

    private final QueryBus queryBus;

    @GetMapping("/orders/{id}")
    public OrderResponse getOrder(@PathVariable String id) {
        return queryBus.querySync(new OrderQuery(id), OrderResponse.class);
    }
}

```

### Handling a Query (Consumer)

Implement `QueryHandler` for the specific query type.

```java
@Component
public class OrderQueryHandler implements QueryHandler<OrderQuery> {

    @Override
    public Class<OrderQuery> type() { 
        return OrderQuery.class; 
    }

    @Override
    public Object handle(OrderQuery q) {
        // Business logic here
        return new OrderResponse(q.orderId(), "COMPLETED");
    }
}

```

---

## 🧩 Architecture

1. **Publisher** generates a unique `Correlation ID` and registers it in the `SyncRegistry`.
2. **Publisher** sends a `QueryRequest` to Kafka and blocks.
3. **Consumer** receives the message, checks the `IdempotencyStore`, and executes the `QueryHandler`.
4. **Consumer** sends the result back to the Publisher's `callback-url` via HTTP POST.
5. **Publisher's Controller** receives the callback, completes the `SyncRegistry`, and the blocked thread returns the data.

---

## ⚙️ Configuration Properties

| Property | Default | Description |
| --- | --- | --- |
| `splitr.publisher.enabled` | `false` | Enables the QueryBus and Callback controller. |
| `splitr.consumer.enabled` | `false` | Enables Kafka listeners for query processing. |
| `splitr.callback-url` | - | The HTTP endpoint for the publisher's callback. |
| `splitr.bus.default-timeout` | `10` | Default wait time in ms for sync queries. |
| `splitr.idempotency.max-size` | `100` | Size of the LRU cache for idempotent processing. |

---

## 🤝 Contributing

Developed and maintained by **BurakKontas**. Feel free to submit issues or pull requests to improve the bus performance or add new transport layers (like RabbitMQ).

## 📑 Roadmap & TODO's

### 🚀 High Priority (Core Engine)

* [x] **Query Bus:** Distributed request-response pattern implementation.
* [ ] **Command Bus:** Asynchronous command dispatching for state-changing operations.
* [ ] **Event Bus:** Pub/Sub pattern for broadcast notifications across services.
* [ ] **DLQ (Dead Letter Queue):** Automatic routing and recovery mechanism for failed messages.
* [ ] **Saga Pattern Support:** Orchestration logic for long-running distributed transactions.

### 🛡 Resilience & Security

* [ ] **Circuit Breaker:** Resilience4j integration to prevent cascading failures during callback timeouts.
* [ ] **Payload Encryption:** Support for AES encryption of sensitive data within Kafka records.
* [ ] **Rate Limiting:** Protect consumers from being overwhelmed by too many simultaneous queries.
* [ ] **Validation:** JSR-303 (Hibernate Validator) support for incoming Query/Command DTOs.

### 📊 Observability & Ops

* [ ] **Distributed Tracing:** Micrometer/Zipkin integration for end-to-end correlation-ID tracking.
* [ ] **Metrics:** Prometheus/Grafana dashboard support for latency and throughput monitoring.
* [ ] **Logging Interceptors:** Pre/Post processing hooks for auditing every message on the bus.
* [ ] **Admin UI:** A simple dashboard to monitor the `SyncRegistry` and `IdempotencyStore` status.

### 💾 Storage & Transports

* [x] **Kafka Support:** Default high-throughput message broker integration.
* [ ] **Redis Idempotency:** Distributed LRU store implementation to support horizontal scaling.
* [ ] **RabbitMQ Support:** Alternative transport layer for AMQP-based infrastructures.
* [ ] **In-Memory Transport:** Zero-latency transport for local (monolithic) testing/deployment.
