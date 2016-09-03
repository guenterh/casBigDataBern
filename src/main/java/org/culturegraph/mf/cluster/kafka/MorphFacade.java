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
 *
 * Copyright 2016 Günter Hipler
 *
 * Licensed under the Apache License, Version 2.0 the "License";
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * [...description of the type ...]
 *
 *
 * @author Guenter Hipler  <guenter.hipler@unibas.ch>
 * @link http://www.swissbib.org
 *
 */

public class MorphFacade {


    private Reader reader = null;
    private ComplexPutWriter collector = null;
    private Table table = null;
    private HBaseHelper helper = null;

    MorphFacade(String morpdefinition,
                String usedFormat,
                String tablename) throws IOException{
        Configuration conf = HBaseConfiguration.create();

        //marcxml should be default
        reader = new MultiFormatReader(usedFormat);

        helper = HBaseHelper.getHelper(conf);
        if (!helper.existsTable(tablename)) {
            helper.createTable(tablename, "prop");
        }


        collector = new ComplexPutWriter();
        //"mapping/ingest.marc21.xml" should be the default, not implemented by now
        final Metamorph metamorph = new Metamorph(morpdefinition);

        //metamorph.setErrorHandler(this);
        reader.setReceiver(metamorph);
        metamorph.setReceiver(collector);
        collector.setCollection(new ArrayList<Put>());
        collector.reset();

        table = helper.getConnection().getTable(TableName.valueOf(tablename)); // co GetExample-2-NewTable Instantiate a new table reference.



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
