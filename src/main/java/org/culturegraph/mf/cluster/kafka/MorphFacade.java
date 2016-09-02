package org.culturegraph.mf.cluster.kafka;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Table;
import org.culturegraph.mf.cluster.sink.ComplexPutWriter;
import org.culturegraph.mf.morph.Metamorph;
import org.culturegraph.mf.stream.reader.MultiFormatReader;
import org.culturegraph.mf.stream.reader.Reader;
import util.HBaseHelper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by swissbib on 02.09.16.
 */
public class MorphFacade {


    private Reader reader = new MultiFormatReader("marcxml");
    private ComplexPutWriter collector = null;
    //private Connection connection = null;
    private Table table = null;
    private HBaseHelper helper = null;

    MorphFacade() throws IOException{
        Configuration conf = HBaseConfiguration.create();


        helper = HBaseHelper.getHelper(conf);
        if (!helper.existsTable("swissbib")) {
            helper.createTable("swissbib", "prop");
        }


        collector = new ComplexPutWriter();
        final Metamorph metamorph = new Metamorph("mapping/ingest.marc21.xml");

        //metamorph.setErrorHandler(this);
        reader.setReceiver(metamorph);
        metamorph.setReceiver(collector);
        collector.setCollection(new ArrayList<Put>());
        collector.reset();

        table = helper.getConnection().getTable(TableName.valueOf("swissbib")); // co GetExample-2-NewTable Instantiate a new table reference.



    }

    void transformRecord(String record) {

        reader.read(record);
    }

    void writeRecords() {

        if (collector.getCollection().size() > 0) {

            try {
                table.put((List<Put>) collector.getCollection());
            } catch (IOException ex) {
                ex.printStackTrace();

            } finally {
                collector.reset();
            }

        }
    }


    void closeResources() {
        try {
            System.out.println("table and Hbase connection is going to be closed ...");

            table.close();
            //connection.close();
            helper.close();

            System.out.println("Hbase clean up done !...");
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }



}
