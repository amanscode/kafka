package com.example.amanscode.kafka_load_simulator.main;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class KafkaSimulatorMain {
	public static final Properties props = new Properties();
	public static void main(String[] args) {
		try {
			props.load(new FileInputStream("./kafkaSimulator.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
		String producerOrConsumer = props.getProperty("producerOrConsumer");
		
		if("producer".equalsIgnoreCase(producerOrConsumer)) {
			KafkaSimulatorProducer.startProducer();
		}
		else if("consumer".equalsIgnoreCase(producerOrConsumer)) {
			KafkaSimulatorConsumer.startConsumer();
		}
		else {
			System.out.println("Please enter a valid value in producerOrConsumer !");
		}
	}

}
