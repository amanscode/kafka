package com.example.amanscode.kafka_load_simulator.main;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.example.amanscode.kafka_load_simulator.pojo.MyRequestPojo;
import com.google.common.util.concurrent.RateLimiter;
import com.google.gson.Gson;

public class KafkaSimulatorProducer {

	private static final Logger logger = LogManager.getLogger(KafkaSimulatorProducer.class);
	private static int rateLimit;
	public static int myRequestSent = 0;
	public static String topic;

	public static void startProducer() {

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

		//		Load kafkaSimulator.properties
		try {
			props.load(new FileInputStream("./kafkaSimulator.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		//		String brokers = "10.32.141.8:9092";
		String brokers = props.getProperty("brokers");

		//		String topic = "quickstart-events";
		topic = props.getProperty("topic");
		
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, brokers);
		props.put(ProducerConfig.ACKS_CONFIG, "all");
		props.put(ProducerConfig.RETRIES_CONFIG, 0);
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
				"org.apache.kafka.common.serialization.StringSerializer");
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");

		KafkaProducer<String, String> producer = new KafkaProducer<>(props);
		TestCallback callback = new TestCallback();

		rateLimit = Integer.parseInt(props.getProperty("rateLimit"));
		RateLimiter rateLimiter = RateLimiter.create(rateLimit);

		System.out.println("!!! Kafka Producer Connected !!!");
		logger.info("!!! Kafka Producer Connected !!!");

		for (int i = 1; i <= rateLimit; i++) {
			rateLimiter.acquire();
		}
		logger.info("!!!!! Load started : "+ (new Date()).toString());

		for (int i = 1; i <= Integer.parseInt(props.getProperty("totalNoOfMsg")); i++) {
			rateLimiter.acquire(); 
			MyRequestPojo myRequestPojo = new MyRequestPojo(); myRequestPojo.setId(myRequestSent + "my_imsi");
			Gson myGson = new Gson();
			String myRequestPojoString = myGson.toJson(myRequestPojo);
			new Thread(new MyLoadSender(producer, myRequestPojoString, callback)).start();
		}


		logger.info("All messages sent !!! Closing Producer !!!");
		producer.flush();
		producer.close();

		logger.info("!!!!! Load ended : " + (new Date()).toString() + " :: No Of Succesful Requests Produced by Producer : " + myRequestSent);

	}

	private static class TestCallback implements Callback {
		@Override
		public void onCompletion(RecordMetadata recordMetadata, Exception e) {
			if (e != null) {
				System.out.println("Error while producing message to topic :" + recordMetadata);
				e.printStackTrace();
			} else {
				String message = String.format("sent message to topic:%s partition:%s  offset:%s",
						recordMetadata.topic(), recordMetadata.partition(), recordMetadata.offset());
//				System.out.println(message);
			}
		}
	}
	
	public static class MyLoadSender implements Runnable{
    	
    	String myRequestPojoString;
    	KafkaProducer<String, String> producer;
    	TestCallback callback;
    	
    	public MyLoadSender(KafkaProducer<String, String> producer, String myRequestPojoString, TestCallback callback) {
    		this.producer = producer;
    		this.myRequestPojoString = myRequestPojoString;
    		this.callback = callback;
		}

		@Override
		public void run() {
			ProducerRecord<String, String> data = new ProducerRecord<String, String>(topic, myRequestPojoString);
			producer.send(data, callback);
			incrementRequestSent();
		}
    	
    }
    
    public static synchronized void incrementRequestSent()
    {
    	myRequestSent++;
    }

}
