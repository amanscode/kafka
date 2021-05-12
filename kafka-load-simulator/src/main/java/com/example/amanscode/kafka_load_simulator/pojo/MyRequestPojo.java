package com.example.amanscode.kafka_load_simulator.pojo;

public class MyRequestPojo {
	
	private byte[] message;
	private String id;

	public byte[] getMessage() {
		return message;
	}

	public void setMessage(byte[] message) {
		this.message = message;
	}

	public String getImsi() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
