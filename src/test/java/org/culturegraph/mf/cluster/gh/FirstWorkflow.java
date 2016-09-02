package org.culturegraph.mf.cluster.gh;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;
import org.culturegraph.mf.cluster.sink.ComplexPutWriter;
import org.culturegraph.mf.cluster.util.ConfigConst;
import org.culturegraph.mf.morph.Metamorph;
import org.culturegraph.mf.stream.reader.MultiFormatReader;
import org.culturegraph.mf.stream.reader.Reader;
import org.junit.Test;
import org.mockito.Mockito;
import util.HBaseHelper;

import java.io.IOException;


/**
 * Created by swissbib on 26.08.16.
 */
public class FirstWorkflow {

    @Test
    public void checkit() {

        //Configuration conf = HBaseConfiguration.create(); // co GetExample-1-CreateConf Create the configuration.

        Reader reader = new MultiFormatReader("marcxml");
        ComplexPutWriter collector = new ComplexPutWriter();
        final Metamorph metamorph = new Metamorph("mapping/ingest.marc21.xml");

        //metamorph.setErrorHandler(this);
        reader.setReceiver(metamorph);
        metamorph.setReceiver(collector);

        collector.reset();
        reader.read(FirstWorkflow.getSingleRecord());
        final Put put = collector.getCurrentPut();
        put.addColumn(Bytes.toBytes("prop"), Bytes.toBytes("raw"),Bytes.toBytes(FirstWorkflow.getSingleRecord()));

        Configuration conf = HBaseConfiguration.create();

        try {

            HBaseHelper helper = HBaseHelper.getHelper(conf);
            if (!helper.existsTable("swissbib")) {
                helper.createTable("swissbib", "prop");
            }



            // vv GetExample
            Connection connection = ConnectionFactory.createConnection(conf);
            Table table = connection.getTable(TableName.valueOf("swissbib")); // co GetExample-2-NewTable Instantiate a new table reference.

            table.put(put);

            table.close();
            connection.close();
            helper.close();

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        System.out.print("");




    }



    private static String getSingleRecord() {
        return String.join("",
                "<record type=\"Bibliographic\" xmlns=\"http://www.loc.gov/MARC21/slim\" ><leader>     cgm a22        4500</leader>",
                "<controlfield tag=\"001\">361554508</controlfield><controlfield tag=\"003\">CHVBK</controlfield>",
                "<controlfield tag=\"005\">20160613032046.0</controlfield><controlfield tag=\"006\">m        |        </controlfield>",
                "<controlfield tag=\"007\">vz ||  ||</controlfield><controlfield tag=\"008\">160519s2015    xx |||            z|fre d</controlfield>",
                "<datafield tag=\"035\" ind1=\" \" ind2=\" \"><subfield code=\"a\">(RERO)R008428854</subfield></datafield>",
                "<datafield tag=\"040\" ind1=\" \" ind2=\" \"><subfield code=\"a\">CH-MyRERO</subfield><subfield code=\"d\">CHVBK</subfield></datafield>",
                "<datafield tag=\"041\" ind1=\"1\" ind2=\" \"><subfield code=\"a\">fre</subfield><subfield code=\"h\">eng</subfield></datafield>",
                "<datafield tag=\"072\" ind1=\" \" ind2=\"7\"><subfield code=\"a\">s1so</subfield><subfield code=\"2\">rero</subfield></datafield><datafield tag=\"245\" ind1=\"0\" ind2=\"0\"><subfield code=\"a\">das Geschriebsel von Günter</subfield><subfield code=\"h\">Enregistrement vidéo</subfield><subfield code=\"c\">un film de Selma Schnabel</subfield></datafield><datafield tag=\"260\" ind1=\" \" ind2=\" \"><subfield code=\"a\">[S.l.]</subfield><subfield code=\"b\">ARTE [prod.]</subfield><subfield code=\"c\">2015</subfield></datafield><datafield tag=\"300\" ind1=\" \" ind2=\" \"><subfield code=\"a\">1 ressource en ligne (29 min.)</subfield></datafield><datafield tag=\"490\" ind1=\"0\" ind2=\" \"><subfield code=\"a\">Le doc</subfield></datafield><datafield tag=\"500\" ind1=\" \" ind2=\" \"><subfield code=\"a\">Emission diffusée sur RTSdeux le 09.05.2016</subfield></datafield><datafield tag=\"500\" ind1=\" \" ind2=\" \"><subfield code=\"a\">Ressource en ligne consultée le 19.05.2016</subfield></datafield><datafield tag=\"520\" ind1=\"8\" ind2=\" \"><subfield code=\"a\">Tiré de la web-série interactive du même nom, Do Not Track, explore les différentes manières dont la Toile enregistre et traque nos activités, nos publications et nos identités pour le plus grand bénéfice de l&#39;économie du Web et ses milliards de dollars. Quelle est la valeur cachée de chacun de nos clics? (RTS)</subfield></datafield><datafield tag=\"521\" ind1=\" \" ind2=\" \"><subfield code=\"a\">Harmos9</subfield></datafield><datafield tag=\"521\" ind1=\" \" ind2=\" \"><subfield code=\"a\">Harmos10</subfield></datafield><datafield tag=\"521\" ind1=\" \" ind2=\" \"><subfield code=\"a\">Harmos11</subfield></datafield><datafield tag=\"521\" ind1=\" \" ind2=\" \"><subfield code=\"a\">Degré secondaire 2</subfield></datafield><datafield tag=\"650\" ind1=\" \" ind2=\"7\"><subfield code=\"a\">Identité numérique</subfield><subfield code=\"2\">rero</subfield></datafield><datafield tag=\"650\" ind1=\" \" ind2=\"7\"><subfield code=\"a\">Données massives</subfield><subfield code=\"2\">rero</subfield></datafield><datafield tag=\"650\" ind1=\" \" ind2=\"7\"><subfield code=\"a\">Traces numériques</subfield><subfield code=\"2\">rero</subfield></datafield><datafield tag=\"650\" ind1=\" \" ind2=\"7\"><subfield code=\"a\">Économie numérique</subfield><subfield code=\"2\">rero</subfield></datafield><datafield tag=\"655\" ind1=\" \" ind2=\"7\"><subfield code=\"a\">Vidéos sur internet</subfield><subfield code=\"2\">rero</subfield></datafield><datafield tag=\"655\" ind1=\" \" ind2=\"7\"><subfield code=\"a\">Films documentaires</subfield><subfield code=\"2\">rero</subfield></datafield><datafield tag=\"655\" ind1=\" \" ind2=\"7\"><subfield code=\"a\">DVD</subfield><subfield code=\"2\">rero</subfield></datafield><datafield tag=\"700\" ind1=\"1\" ind2=\" \"><subfield code=\"a\">Schnabel</subfield><subfield code=\"D\">Selma</subfield></datafield><datafield tag=\"856\" ind1=\"4\" ind2=\" \"><subfield code=\"u\">https://laplattform.ch/node/2136</subfield><subfield code=\"z\">Emission en ligne</subfield><subfield code=\"z\">Accès réservé</subfield></datafield><datafield tag=\"950\" ind1=\" \" ind2=\" \"><subfield code=\"B\">RERO</subfield><subfield code=\"P\">490</subfield><subfield code=\"E\">0-</subfield><subfield code=\"a\">Le doc</subfield></datafield><datafield tag=\"950\" ind1=\" \" ind2=\" \"><subfield code=\"B\">RERO</subfield><subfield code=\"P\">700</subfield><subfield code=\"E\">1-</subfield><subfield code=\"a\">Schnabel</subfield><subfield code=\"D\">Selma</subfield></datafield><datafield tag=\"950\" ind1=\" \" ind2=\" \"><subfield code=\"B\">RERO</subfield><subfield code=\"P\">856</subfield><subfield code=\"E\">4-</subfield><subfield code=\"u\">https://laplattform.ch/node/2136</subfield><subfield code=\"z\">Emission en ligne</subfield><subfield code=\"z\">Accès réservé</subfield></datafield><datafield tag=\"898\" ind1=\" \" ind2=\" \"><subfield code=\"a\">VM010000</subfield><subfield code=\"b\">XM010000</subfield><subfield code=\"c\">XM010000</subfield></datafield><datafield tag=\"949\" ind1=\" \" ind2=\" \"><subfield code=\"B\">RERO</subfield><subfield code=\"E\">R008428854</subfield><subfield code=\"F\">RE01096</subfield><subfield code=\"b\">RE01096</subfield><subfield code=\"c\">RE010960040</subfield><subfield code=\"j\">- -</subfield><subfield code=\"p\">RERO-17553983</subfield></datafield><datafield tag=\"949\" ind1=\" \" ind2=\" \"><subfield code=\"B\">RERO</subfield><subfield code=\"E\">R008428854</subfield><subfield code=\"F\">RE11003</subfield><subfield code=\"b\">RE11003</subfield><subfield code=\"c\">RE110030059</subfield><subfield code=\"j\">004.6 DONO</subfield><subfield code=\"p\">1011867214</subfield><subfield code=\"4\">5200</subfield><subfield code=\"z\">DVD</subfield><subfield code=\"z\">Prêt limité à des fins pédagogiques</subfield></datafield><datafield tag=\"949\" ind1=\" \" ind2=\" \"><subfield code=\"B\">RERO</subfield><subfield code=\"E\">R008428854</subfield><subfield code=\"F\">RE31008</subfield><subfield code=\"b\">RE31008</subfield><subfield code=\"c\">RE310081020</subfield><subfield code=\"j\">- -</subfield><subfield code=\"p\">RERO-17562515</subfield><subfield code=\"4\">2102</subfield></datafield></record>");


        //return String.join("",
        //        "<leader>     cgm a22        4500</leader>",
        //        "<controlfield tag=\"001\">361554508</controlfield><controlfield tag=\"003\">CHVBK</controlfield>",
        //        "<controlfield tag=\"005\">20160613032046.0</controlfield><controlfield tag=\"006\">m        |        </controlfield>",
        //        "<controlfield tag=\"007\">vz ||  ||</controlfield><controlfield tag=\"008\">160519s2015    xx |||            z|fre d</controlfield>",
        //        "<datafield tag=\"035\" ind1=\" \" ind2=\" \"><subfield code=\"a\">(RERO)R008428854</subfield></datafield>",
        //        "<datafield tag=\"040\" ind1=\" \" ind2=\" \"><subfield code=\"a\">CH-MyRERO</subfield><subfield code=\"d\">CHVBK</subfield></datafield>",
        //        "<datafield tag=\"041\" ind1=\"1\" ind2=\" \"><subfield code=\"a\">fre</subfield><subfield code=\"h\">eng</subfield></datafield>",
        //        "<datafield tag=\"072\" ind1=\" \" ind2=\"7\"><subfield code=\"a\">s1so</subfield><subfield code=\"2\">rero</subfield></datafield><datafield tag=\"245\" ind1=\"0\" ind2=\"0\"><subfield code=\"a\">Do not track</subfield><subfield code=\"h\">Enregistrement vidéo</subfield><subfield code=\"c\">un film de Selma Schnabel</subfield></datafield><datafield tag=\"260\" ind1=\" \" ind2=\" \"><subfield code=\"a\">[S.l.]</subfield><subfield code=\"b\">ARTE [prod.]</subfield><subfield code=\"c\">2015</subfield></datafield><datafield tag=\"300\" ind1=\" \" ind2=\" \"><subfield code=\"a\">1 ressource en ligne (29 min.)</subfield></datafield><datafield tag=\"490\" ind1=\"0\" ind2=\" \"><subfield code=\"a\">Le doc</subfield></datafield><datafield tag=\"500\" ind1=\" \" ind2=\" \"><subfield code=\"a\">Emission diffusée sur RTSdeux le 09.05.2016</subfield></datafield><datafield tag=\"500\" ind1=\" \" ind2=\" \"><subfield code=\"a\">Ressource en ligne consultée le 19.05.2016</subfield></datafield><datafield tag=\"520\" ind1=\"8\" ind2=\" \"><subfield code=\"a\">Tiré de la web-série interactive du même nom, Do Not Track, explore les différentes manières dont la Toile enregistre et traque nos activités, nos publications et nos identités pour le plus grand bénéfice de l&#39;économie du Web et ses milliards de dollars. Quelle est la valeur cachée de chacun de nos clics? (RTS)</subfield></datafield><datafield tag=\"521\" ind1=\" \" ind2=\" \"><subfield code=\"a\">Harmos9</subfield></datafield><datafield tag=\"521\" ind1=\" \" ind2=\" \"><subfield code=\"a\">Harmos10</subfield></datafield><datafield tag=\"521\" ind1=\" \" ind2=\" \"><subfield code=\"a\">Harmos11</subfield></datafield><datafield tag=\"521\" ind1=\" \" ind2=\" \"><subfield code=\"a\">Degré secondaire 2</subfield></datafield><datafield tag=\"650\" ind1=\" \" ind2=\"7\"><subfield code=\"a\">Identité numérique</subfield><subfield code=\"2\">rero</subfield></datafield><datafield tag=\"650\" ind1=\" \" ind2=\"7\"><subfield code=\"a\">Données massives</subfield><subfield code=\"2\">rero</subfield></datafield><datafield tag=\"650\" ind1=\" \" ind2=\"7\"><subfield code=\"a\">Traces numériques</subfield><subfield code=\"2\">rero</subfield></datafield><datafield tag=\"650\" ind1=\" \" ind2=\"7\"><subfield code=\"a\">Économie numérique</subfield><subfield code=\"2\">rero</subfield></datafield><datafield tag=\"655\" ind1=\" \" ind2=\"7\"><subfield code=\"a\">Vidéos sur internet</subfield><subfield code=\"2\">rero</subfield></datafield><datafield tag=\"655\" ind1=\" \" ind2=\"7\"><subfield code=\"a\">Films documentaires</subfield><subfield code=\"2\">rero</subfield></datafield><datafield tag=\"655\" ind1=\" \" ind2=\"7\"><subfield code=\"a\">DVD</subfield><subfield code=\"2\">rero</subfield></datafield><datafield tag=\"700\" ind1=\"1\" ind2=\" \"><subfield code=\"a\">Schnabel</subfield><subfield code=\"D\">Selma</subfield></datafield><datafield tag=\"856\" ind1=\"4\" ind2=\" \"><subfield code=\"u\">https://laplattform.ch/node/2136</subfield><subfield code=\"z\">Emission en ligne</subfield><subfield code=\"z\">Accès réservé</subfield></datafield><datafield tag=\"950\" ind1=\" \" ind2=\" \"><subfield code=\"B\">RERO</subfield><subfield code=\"P\">490</subfield><subfield code=\"E\">0-</subfield><subfield code=\"a\">Le doc</subfield></datafield><datafield tag=\"950\" ind1=\" \" ind2=\" \"><subfield code=\"B\">RERO</subfield><subfield code=\"P\">700</subfield><subfield code=\"E\">1-</subfield><subfield code=\"a\">Schnabel</subfield><subfield code=\"D\">Selma</subfield></datafield><datafield tag=\"950\" ind1=\" \" ind2=\" \"><subfield code=\"B\">RERO</subfield><subfield code=\"P\">856</subfield><subfield code=\"E\">4-</subfield><subfield code=\"u\">https://laplattform.ch/node/2136</subfield><subfield code=\"z\">Emission en ligne</subfield><subfield code=\"z\">Accès réservé</subfield></datafield><datafield tag=\"898\" ind1=\" \" ind2=\" \"><subfield code=\"a\">VM010000</subfield><subfield code=\"b\">XM010000</subfield><subfield code=\"c\">XM010000</subfield></datafield><datafield tag=\"949\" ind1=\" \" ind2=\" \"><subfield code=\"B\">RERO</subfield><subfield code=\"E\">R008428854</subfield><subfield code=\"F\">RE01096</subfield><subfield code=\"b\">RE01096</subfield><subfield code=\"c\">RE010960040</subfield><subfield code=\"j\">- -</subfield><subfield code=\"p\">RERO-17553983</subfield></datafield><datafield tag=\"949\" ind1=\" \" ind2=\" \"><subfield code=\"B\">RERO</subfield><subfield code=\"E\">R008428854</subfield><subfield code=\"F\">RE11003</subfield><subfield code=\"b\">RE11003</subfield><subfield code=\"c\">RE110030059</subfield><subfield code=\"j\">004.6 DONO</subfield><subfield code=\"p\">1011867214</subfield><subfield code=\"4\">5200</subfield><subfield code=\"z\">DVD</subfield><subfield code=\"z\">Prêt limité à des fins pédagogiques</subfield></datafield><datafield tag=\"949\" ind1=\" \" ind2=\" \"><subfield code=\"B\">RERO</subfield><subfield code=\"E\">R008428854</subfield><subfield code=\"F\">RE31008</subfield><subfield code=\"b\">RE31008</subfield><subfield code=\"c\">RE310081020</subfield><subfield code=\"j\">- -</subfield><subfield code=\"p\">RERO-17562515</subfield><subfield code=\"4\">2102</subfield></datafield>");


    }


}
