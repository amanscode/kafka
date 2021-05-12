package com.example.amanscode.kafka_demo;

import java.util.Collection;
import java.util.Collections;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;

public class Consumer {
	public static void main(String[] args) {
Properties props = new Properties();
		
//		String brokers = "broker1:9093,broker2:9093,broker3:9093";
		String brokers = "localhost:9092";
		String topic = "my-sample-topic";
		String groupId = "my-sample-group-1";
		
		//configure the following three settings for SSL Encryption
//        props.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SSL");
//        props.put(SslConfigs.SSL_KEYSTORE_LOCATION_CONFIG, "/etc/pki/tls/keystore.jks");
//        props.put(SslConfigs.SSL_KEYSTORE_PASSWORD_CONFIG, "password");
//        props.put(SslConfigs.SSL_KEY_PASSWORD_CONFIG, "password");
        
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, brokers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        
        KafkaConsumer<byte[], byte[]> consumer = new KafkaConsumer<>(props);
        
        TestConsumerRebalanceListener rebalanceListener = new TestConsumerRebalanceListener();
        consumer.subscribe(Collections.singletonList(topic), rebalanceListener);
        
        while(true) {
        	final ConsumerRecords < byte[], byte[] > consumerRecords = consumer.poll(1000);
        	
        	if(consumerRecords.count() == 0) continue;
        	
        	consumerRecords.forEach(record -> {
                System.out.printf("Consumed Record: (%d, %s, %d, %d)\n",
                    record.key(), record.value(),
                    record.partition(), record.offset());
            });

            consumer.commitAsync();
        }
        
	}
	
	private static class TestConsumerRebalanceListener implements ConsumerRebalanceListener {
        @Override
        public void onPartitionsRevoked(Collection < TopicPartition > partitions) {
            System.out.println("Called onPartitionsRevoked with partitions:" + partitions);
        }

        @Override
        public void onPartitionsAssigned(Collection < TopicPartition > partitions) {
            System.out.println("Called onPartitionsAssigned with partitions:" + partitions);
        }
    }

}
