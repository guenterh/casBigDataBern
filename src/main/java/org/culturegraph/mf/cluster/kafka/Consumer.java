package org.culturegraph.mf.cluster.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

/**
 * Created by swissbib on 02.09.16.
 */
public class Consumer {

    public static void main(String[] args) {


        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        //props.put("group.id", "test");
        props.put("enable.auto.commit", "true");
        props.put("auto.commit.interval.ms", "1000");
        props.put("session.timeout.ms", "30000");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        //consumer.subscribe(Arrays.asList("OAI"));

        String topic = "OAI";
        TopicPartition partition0 = new TopicPartition(topic, 0);
        //TopicPartition partition1 = new TopicPartition(topic, 1);
        List<TopicPartition> partitionList = Arrays.asList(partition0);
        consumer.assign(partitionList);


        consumer.seekToBeginning(partitionList);
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(100);
            System.out.println("records fetched: " + records.count());
            for (ConsumerRecord<String, String> record : records)
                System.out.printf("offset = %d, key = %s  \n", record.offset(), record.key());
        }



    }


}
