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
bin/kafka-topics.sh --create --topic topic_name --bootstrap-server localhost:9092
bin/kafka-topics.sh --create --topic topic_name --bootstrap-server localhost:9092
bin/kafka-topics.sh --create --topic topic_name --bootstrap-server localhost:9092
```

## Commands to Describe Topics

```bash
bin/kafka-topics.sh --describe --topic topic_name --bootstrap-server localhost:9092
bin/kafka-topics.sh --describe --topic topic_name --bootstrap-server localhost:9092
bin/kafka-topics.sh --describe --topic topic_name --bootstrap-server localhost:9092
```

## Commands to change retention time of a Topic

Try any one of them :

```bash
bin/kafka-topics.sh --zookeeper localhost:2181 --alter --topic topic_name --config retention.ms=10000
```

```bash
bin/kafka-configs.sh --zookeeper localhost:2181 --alter --entity-name topic_name --entity-type topics  --add-config retention.ms=1000
```

```bash
bin/kafka-configs.sh --bootstrap-server localhost:2181 --alter --entity-name topic_name --entity-type topics  --add-config retention.ms=1000
```


## Commands to Delete Topics

```bash
bin/kafka-topics.sh --zookeeper localhost:2181 --delete --topic topic_name
bin/kafka-topics.sh --zookeeper localhost:2181 --delete --topic topic_name
bin/kafka-topics.sh --zookeeper localhost:2181 --delete --topic topic_name
```

## Commannd to Start Kafka Producer

```bash
bin/kafka-console-producer.sh --topic topic_name --bootstrap-server localhost:9092
bin/kafka-console-producer.sh --topic topic_name --bootstrap-server localhost:9092
```

## Commands to Start Kafka Consumer

```bash
bin/kafka-console-consumer.sh --topic topic_name --from-beginning --bootstrap-server localhost:9092
bin/kafka-console-consumer.sh --topic topic_name --from-beginning --bootstrap-server localhost:9092
bin/kafka-console-consumer.sh --topic topic_name --from-beginning --bootstrap-server localhost:9092
bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic topic_name --property print.key=true --property key.separator="-" --from-beginning
```

## Command to list the consumer groups in the Kafka

```bash
./bin/kafka-consumer-groups.sh --list --bootstrap-server localhost:9092
```

## Command to list all the consumers present in the consumer groups

```bash
kafka-consumer-groups.sh --describe --group consumer_group_name --members --bootstrap-server localhost:9092
```

## Command to make broker accessible from other servers

```bash
kafka-server-start.sh config/server.properties --override  advertised.listeners=PLAINTEXT://<broker-hostname>:9092
```

## Important Links

Uses of Kafka:
https://kafka.apache.org/uses

For Kafka Monitoring:
https://kafka.apache.org/documentation/#monitoring

For Monitoring, you can use Prometheus agent with Grafana, see below link:
https://medium.com/@mousavi310/monitor-apache-kafka-using-grafana-and-prometheus-873c7a0005e2

Delivery Semantics For Producer:
https://dzone.com/articles/kafka-producer-delivery-semantics

Delivery Semantics For Consumer:
https://www.javatpoint.com/apache-kafka-consumer-and-consumer-groups

Overcome Data Order Issue in Kafka:
https://www.dataversity.net/how-to-overcome-data-order-issues-in-apache-kafka/#


## Kafka Monitoring

1)	Prometheus JMX exporter: Prometheus JMX exporter is a collector, designed for scraping (getting metrics from the services). Java Management Extensions (JMX) is a Java technology that supplies tools for managing and monitoring application. It runs as a Java agent as well as an independent HTTP server. The JMX exporter can export from various applications and can work with the matrix.

2)	Prometheus: Prometheus is an open-source system’s monitoring and alerting toolkit. Metrics collection with Prometheus relies on the pull model. Prometheus is responsible for getting metrics (scraping) from the services that it monitors. There other tools like Graphite, which waits for clients to push their metrics to a known server.

3)	Download jmx_prometheus_javaagent-0.6.jar from internet. Sample is being attached below:
 

4)	Download kafka-0-8-2.yml from internet. Sample is being attached below:
 

5)	Add below to kafka-server-start.sh and start the broker:
KAFKA_OPTS="$KAFKA_OPTS -javaagent:$PWD/jmx_prometheus_javaagent-0.6.jar=7071:$PWD/kafka-0-8-2.yml" \

6)	Visit http://localhost:7071/ to see the metrics.

7)	Download Prometheus application from below path:
https://github.com/prometheus/prometheus/releases/download/v1.2.1/prometheus-1.2.1.linux-amd64.tar.gz

8)	In prometheus.yml file, provide localhost and port on which Prometheus Java agent is running like this:
- targets:
-localhost:7071

9)	Start the Prometheus using ./prometheus

10)	Visit http://localhost:9090/graph. This is Prometheus platform that monitors all data from the Kafka index. (9090 is the default port on which the Prometheus will be running)

11)	Grafana: Grafana is a tool that helps to visualize and understand matrices. Visualizations in Grafana are called panels. Users can create a dashboard containing panels for different data sources. Here, we can also make use of a large ecosystem of ready-made dashboards for different data types and sources.

12)	Download Grafana below path and untar it:
https://grafanarel.s3.amazonaws.com/builds/grafana-2.5.0.linux-x64.tar.gz

13)	Go to grafana-2.50 directory and start Grafana using below command:
./bin/grafana-server web

14)	By default, Grafana will be listening on http://localhost:3000 (visit here). The default login is “admin” / “admin”.

15)	To create Prometheus data source:
-	Click on the Grafana logo to open the sidebar menu.
-	Look for “Data Source” in the sidebar.
-	Click on “Add New”.
-	Select “Prometheus” as a type.
-	Select the Prometheus server URL(http://localhost:9090/).
-	Click Add to save the new DataSource.

16)	On Grafana, click on the Dashboard, then on Home and lastly click on Import and import the below JSON file. This file contains the dashboard configuration available for Kafka visualizations on Grafana:
 

17)	Dashboard will be visible now.
Configure the Prometheus as a DataSource. Click on each of the dashboard column, select the Edit menu, select Metrics and then select the Data Source, the one you created as “Prometheus data source”.

18)	Now, we are able to view the Kafka Overview Dashboard with appropriate Kafka monitored data.


## Kafka Benchmarking

-	Keep 6 brokers and keep 12 partitions for the topic.
-	Each server is equipped with 500G RAM.
-	Keep ProducerConfig.ACKS_CONFIG as ‘0’.
-	Record size is 5 KB per record.
-	Use snappy compression for faster results. Keep ProducerConfig.COMPRESSION_TYPE_CONFIG as ‘snappy’. This compresses the 5KB message to almost 30% of its size.
-	Using above configuration, 1 million per second (1M TPS) was achieved.
