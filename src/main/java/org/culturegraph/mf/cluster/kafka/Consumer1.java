package org.culturegraph.mf.cluster.kafka;

import org.apache.hadoop.hbase.client.Put;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.culturegraph.mf.cluster.sink.ComplexPutWriter;
import org.culturegraph.mf.morph.Metamorph;
import org.culturegraph.mf.stream.reader.MultiFormatReader;
import org.culturegraph.mf.stream.reader.Reader;

import java.util.*;

/**
 * Created by swissbib on 02.09.16.
 */
public class Consumer1 {

    public static void main(String[] args) {



        Reader reader = new MultiFormatReader("marcxml");
        ComplexPutWriter collector = new ComplexPutWriter();
        final Metamorph metamorph = new Metamorph("mapping/ingest.marc21.xml");

        //metamorph.setErrorHandler(this);
        reader.setReceiver(metamorph);
        metamorph.setReceiver(collector);

        collector.setCollection(new ArrayList<Put>());

        collector.reset();




        Properties p = new Properties();
        p.put("bootstrap.servers", "localhost:9092");
        p.put("group.id", "swissbib.oai");
        p.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        p.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        p.put("zookeeper.connect", "localhost:2181");

        KafkaConsumer<String, String> c = new KafkaConsumer<String, String>(p);
        //c.subscribe(Collections.singletonList("OAI")); //doesn't work ??
        //c.subscribe(Arrays.asList("OAI"));




        String topic = "OAI";
        TopicPartition partition0 = new TopicPartition(topic, 0);
        //TopicPartition partition1 = new TopicPartition(topic, 1);
        List<TopicPartition> partitionList = Arrays.asList(partition0);
        c.assign(partitionList);



        c.seekToBeginning(partitionList);

        Collection<Put> putCollection = null;
        try {
            while (true) {
                ConsumerRecords<String, String> rec = c.poll(100);
                System.out.println("We got record count " + rec.count());
                putCollection = collector.getCollection();
                for (ConsumerRecord<String, String> r : rec) {
                    //System.out.println(r.value());
                    //System.out.printf("offset = %d, key = %s, value= %s  \n", r.offset(), r.key(),r.value());
                    reader.read(r.value());
                    //Collection<Put> putCollection =  collector.getCollection();

                    //System.out.println();

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            c.close();
        }
    }



}
