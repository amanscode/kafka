# Kafka Demo


## Installation

Download Kafka:

Download URL:

https://www.apache.org/dyn/closer.cgi?path=/kafka/2.8.0/kafka_2.13-2.8.0.tgz

Run below command after downloading

```bash
tar -xvf kafka_2.13-2.8.0.tgz
```

## Start Zookeeper and Kafka Broker

To run Zookeeper, hit the below command :

```bash
bin/zookeeper-server-start.sh config/zookeeper.properties
```

To run the Kafka Broker, hit the below command :

```bash
bin/kafka-server-start.sh config/server.properties
```

## Commands to create Topics

```bash
bin/kafka-topics.sh --create --topic quickstart-events --bootstrap-server localhost:9092
bin/kafka-topics.sh --create --topic word-count-input-topic --bootstrap-server localhost:9092
bin/kafka-topics.sh --create --topic word-count-output-topic --bootstrap-server localhost:9092
```

## Commands to Describe Topics

```bash
bin/kafka-topics.sh --describe --topic quickstart-events --bootstrap-server localhost:9092
bin/kafka-topics.sh --describe --topic word-count-input-topic --bootstrap-server localhost:9092
bin/kafka-topics.sh --describe --topic word-count-output-topic --bootstrap-server localhost:9092
```

## Commands to change retention time of a Topic

Try any one of them :

```bash
bin/kafka-topics.sh --zookeeper localhost:2181 --alter --topic quickstart-events --config retention.ms=10000
```

```bash
bin/kafka-configs.sh --zookeeper localhost:2181 --alter --entity-name quickstart-events --entity-type topics  --add-config retention.ms=1000
```

```bash
bin/kafka-configs.sh --bootstrap-server localhost:2181 --alter --entity-name quickstart-events --entity-type topics  --add-config retention.ms=1000
```


## Commands to Delete Topics

```bash
bin/kafka-topics.sh --zookeeper localhost:2181 --delete --topic quickstart-events
bin/kafka-topics.sh --zookeeper localhost:2181 --delete --topic word-count-input-topic
bin/kafka-topics.sh --zookeeper localhost:2181 --delete --topic word-count-output-topic
```

## Commannd to Start Kafka Producer

```bash
bin/kafka-console-producer.sh --topic quickstart-events --bootstrap-server localhost:9092
bin/kafka-console-producer.sh --topic word-count-input-topic --bootstrap-server localhost:9092
```

## Commands to Start Kafka Consumer

```bash
bin/kafka-console-consumer.sh --topic quickstart-events --from-beginning --bootstrap-server localhost:9092
bin/kafka-console-consumer.sh --topic word-count-input-topic --from-beginning --bootstrap-server localhost:9092
bin/kafka-console-consumer.sh --topic word-count-output-topic --from-beginning --bootstrap-server localhost:9092
```

## Important Links

Uses of Kafka:
https://kafka.apache.org/uses

For Kafka Monitoring:
https://kafka.apache.org/documentation/#monitoring

For Monitoring, you can use Prometheus agent with Grafana, see below link:
https://medium.com/@mousavi310/monitor-apache-kafka-using-grafana-and-prometheus-873c7a0005e2

How Consumer and Consumer Groups work?
https://www.javatpoint.com/apache-kafka-consumer-and-consumer-groups

Overcome Data Order Issue in Kafka:
https://www.dataversity.net/how-to-overcome-data-order-issues-in-apache-kafka/#
