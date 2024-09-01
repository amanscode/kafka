package com.example.amanscode.kafka_demo;

import java.util.Properties;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

public class Producer {
	
	public static void main(String[] args) {
		
		Properties props = new Properties();
		
//		String brokers = "broker1:9093,broker2:9093,broker3:9093";
		String brokers = "localhost:9092";
		String topic = "my-sample-topic";
		
		//configure the following three settings for SSL Encryption
//        props.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SSL");
//        props.put(SslConfigs.SSL_KEYSTORE_LOCATION_CONFIG, "/etc/pki/tls/keystore.jks");
//        props.put(SslConfigs.SSL_KEYSTORE_PASSWORD_CONFIG, "password");
//        props.put(SslConfigs.SSL_KEY_PASSWORD_CONFIG, "password");
        
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, brokers);
        props.put(ProducerConfig.ACKS_CONFIG, "all");
        props.put(ProducerConfig.RETRIES_CONFIG, 0);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        
        KafkaProducer < String, String > producer = new KafkaProducer < > (props);
        TestCallback callback = new TestCallback();
        
        for (int i = 0; i < 10; i++) {
        	String message = "message #" + i + " from Producer.";
        	ProducerRecord <String, String> data = new ProducerRecord <String, String> (topic, message);
        	producer.send(data, callback);
        	System.out.println("Message #" + i + " sent from Producer");
		}
        System.out.println("All messages sent !!! Closing Producer !!!");
        producer.flush();
        producer.close();
        
	}
	
	private static class TestCallback implements Callback {
        @Override
        public void onCompletion(RecordMetadata recordMetadata, Exception e) {
            if (e != null) {
                System.out.println("Error while producing message to topic :" + recordMetadata);
                e.printStackTrace();
            } else {
                String message = String.format("sent message to topic:%s partition:%s  offset:%s", recordMetadata.topic(), recordMetadata.partition(), recordMetadata.offset());
                System.out.println(message);
            }
        }
    }

}
