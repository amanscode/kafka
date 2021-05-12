package com.example.amanscode.kafka_load_simulator.main;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.example.amanscode.kafka_load_simulator.counters.KafkaSimulatorCounters;

public class KafkaSimulatorConsumer {

	private static final Logger logger = LogManager.getLogger(KafkaSimulatorConsumer.class);

	public static void startConsumer() {

		Properties props = new Properties();

		//		Initialize Logger
		String log4JPropertyFile = "./log4j.properties";
		try {
			props.load(new FileInputStream(log4JPropertyFile));
			PropertyConfigurator.configure(props);
			logger.info("Logger configured !");
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Load kafkaSimulator.properties
		try {
			props.load(new FileInputStream("./kafkaSimulator.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		//		String brokers = "10.32.141.8:9092";
		String brokers = props.getProperty("brokers");

		//		String topic = "quickstart-events";
		String topic = props.getProperty("topic");

		//		String consumerGroupId = "aman-group-1";
		String consumerGroupId = props.getProperty("consumerGroupId");

		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, brokers);
		props.put(ConsumerConfig.GROUP_ID_CONFIG, consumerGroupId);
		props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
				"org.apache.kafka.common.serialization.StringDeserializer");
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
				"org.apache.kafka.common.serialization.StringDeserializer");

		System.out.println("!!! Kafka Producer Connected !!!");
		logger.info("!!! Kafka Producer Connected !!!");

		KafkaConsumer<byte[], byte[]> consumer = new KafkaConsumer<>(props);

		TestConsumerRebalanceListener rebalanceListener = new TestConsumerRebalanceListener();
		consumer.subscribe(Collections.singletonList(topic), rebalanceListener);

		new Thread(new MyLoadReceiver(consumer)).start();

		int printCountersTimeForConsumer = Integer.parseInt(props.getProperty("printCountersTimeForConsumer"));
		while(true) {
			try {
				Thread.sleep(printCountersTimeForConsumer*1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			logger.info("Number of Requests Received from Kafka Producer(s) : " + KafkaSimulatorCounters.noOfRequestReceived);
		}
	}

	private static class TestConsumerRebalanceListener implements ConsumerRebalanceListener {
		@Override
		public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
//			System.out.println("Called onPartitionsRevoked with partitions:" + partitions);
		}

		@Override
		public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
//			System.out.println("Called onPartitionsAssigned with partitions:" + partitions);
		}
	}

	public static class MyLoadReceiver implements Runnable{

		KafkaConsumer<byte[], byte[]> consumer;

		public MyLoadReceiver(KafkaConsumer<byte[], byte[]> consumer) {
			this.consumer = consumer;
		}

		@Override
		public void run() {
			while (true) {
				final ConsumerRecords<byte[], byte[]> consumerRecords = consumer.poll(Duration.ofMillis(1000));
				if (consumerRecords.count() == 0)
					continue;
				KafkaSimulatorCounters.incrementRequestReceived(consumerRecords.count());
				consumer.commitAsync();
				
			}
		}

	}

}
