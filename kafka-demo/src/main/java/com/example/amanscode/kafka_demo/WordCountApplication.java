package com.example.amanscode.kafka_demo;

import java.util.Arrays;
import java.util.Properties;
import java.util.regex.Pattern;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Produced;

public class WordCountApplication {

    public static void main(final String[] args) throws Exception {
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "wordcount-application");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

        String inputTopic = "word-count-input-topic";
        String outputTopic = "word-count-output-topic";
        
        StreamsBuilder builder = new StreamsBuilder();
        KStream<String, String> textLines = builder.stream(inputTopic);
        Pattern pattern = Pattern.compile("\\W+", Pattern.UNICODE_CHARACTER_CLASS);

//        KTable<String, Long> wordCounts = textLines
//          .flatMapValues(value -> Arrays.asList(pattern.split(value.toLowerCase())))
//          .groupBy((key, word) -> word)
//          .count();
        
        KTable<String, Long> wordCounts = textLines
                .flatMapValues(line -> Arrays.asList(line.toLowerCase().split(" ")))
                .groupBy((keyIgnored, word) -> word)
                .count();
        
        wordCounts.toStream().foreach((w, c) -> System.out.println("word: " + w + " -> " + c));
        
        wordCounts.toStream().to(outputTopic, Produced.with(Serdes.String(), Serdes.Long()));
        
//        Serde<String> stringSerde = Serdes.String();
//        Serde<Long> longSerde = Serdes.Long();
//        wordCounts.toStream().to(stringSerde, longSerde, outputTopic);

        KafkaStreams streams = new KafkaStreams(builder.build(), props);
        streams.start();
    }

}