package com.example.amanscode.kafka_load_simulator.counters;

public class KafkaSimulatorCounters {

	public static int noOfRequestReceived = 0;

	public static synchronized void incrementRequestReceived(int count) {
		noOfRequestReceived = noOfRequestReceived + count;
	}

}
