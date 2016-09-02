package org.culturegraph.mf.cluster.kafka;

import org.apache.commons.collections.buffer.CircularFifoBuffer;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.WakeupException;

import java.io.IOException;
import java.util.Collections;
import java.util.Properties;
import org.apache.hadoop.conf.Configuration;
import util.HBaseHelper;


public class ConsumerDriverWithWakeUp {

    private Properties kafkaProps = new Properties();
    private String waitTime;
    private KafkaConsumer<String, String> consumer;
    private MorphFacade morphFacade = null;

    public static void main(String[] args) throws IOException{
        if (args.length == 0) {
            System.out.println("ConsumerDriverWithWakeUp {brokers} {group.id} {topic}");
            return;
        }

        final ConsumerDriverWithWakeUp consumerDriver = new ConsumerDriverWithWakeUp();
        String brokers = args[0];
        String groupId = args[1];
        String topic = args[2];



        consumerDriver.morphFacade = new MorphFacade();




        consumerDriver.configure(brokers, groupId);


        final Thread mainThread = Thread.currentThread();

        // Registering a shutdown hook so we can exit cleanly
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                System.out.println("Starting exit...");
                // Note that shutdownhook runs in a separate thread, so the only thing we can safely do to a consumer is wake it up
                consumerDriver.consumer.wakeup();
                try {
                    mainThread.join();
                } catch (InterruptedException e) {
                    //cleanup the HBase resources

                    e.printStackTrace();
                }
            }
        });


        try {
            consumerDriver.consumer.subscribe(Collections.singletonList(topic));

            // looping until ctrl-c, the shutdown hook will cleanup on exit
            while (true) {
                ConsumerRecords<String, String> records = consumerDriver.consumer.poll(1000);
                System.out.println(System.currentTimeMillis() + "  --  waiting for data...");
                consumerDriver.morphFacade.writeRecords();
                for (ConsumerRecord<String, String> record : records) {


                    consumerDriver.morphFacade.transformRecord(record.value());


                }
                for (TopicPartition tp: consumerDriver.consumer.assignment())
                    System.out.println("Committing offset at position:" + consumerDriver.consumer.position(tp));
                consumerDriver.consumer.commitSync();
            }
        } catch (WakeupException e) {
            // ignore for shutdown
        } finally {
            System.out.println("now we are going to close the consumer");
            consumerDriver.consumer.close();
            consumerDriver.morphFacade.closeResources();
            System.out.println("ready with all and exit ...");
        }
    }

    private void configure(String servers, String groupId) {
        kafkaProps.put("group.id",groupId);
        kafkaProps.put("bootstrap.servers",servers);
        kafkaProps.put("auto.offset.reset","earliest");         // when in doubt, read everything
        kafkaProps.put("key.deserializer","org.apache.kafka.common.serialization.StringDeserializer");
        kafkaProps.put("value.deserializer","org.apache.kafka.common.serialization.StringDeserializer");
        consumer = new KafkaConsumer<String, String>(kafkaProps);
    }

}
