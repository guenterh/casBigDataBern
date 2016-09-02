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

        /*
        return String.join("",
                "<record type=\"Bibliographic\" xmlns=\"http://www.loc.gov/MARC21/slim\" ><leader>     cgm a22        4500</leader>",
                "<controlfield tag=\"001\">361554508</controlfield><controlfield tag=\"003\">CHVBK</controlfield>",
                "<controlfield tag=\"005\">20160613032046.0</controlfield><controlfield tag=\"006\">m        |        </controlfield>",
                "<controlfield tag=\"007\">vz ||  ||</controlfield><controlfield tag=\"008\">160519s2015    xx |||            z|fre d</controlfield>",
                "<datafield tag=\"035\" ind1=\" \" ind2=\" \"><subfield code=\"a\">(RERO)R008428854</subfield></datafield>",
                "<datafield tag=\"040\" ind1=\" \" ind2=\" \"><subfield code=\"a\">CH-MyRERO</subfield><subfield code=\"d\">CHVBK</subfield></datafield>",
                "<datafield tag=\"041\" ind1=\"1\" ind2=\" \"><subfield code=\"a\">fre</subfield><subfield code=\"h\">eng</subfield></datafield>",
                "<datafield tag=\"072\" ind1=\" \" ind2=\"7\"><subfield code=\"a\">s1so</subfield><subfield code=\"2\">rero</subfield></datafield><datafield tag=\"245\" ind1=\"0\" ind2=\"0\"><subfield code=\"a\">das Geschriebsel von Günter</subfield><subfield code=\"h\">Enregistrement vidéo</subfield><subfield code=\"c\">un film de Selma Schnabel</subfield></datafield><datafield tag=\"260\" ind1=\" \" ind2=\" \"><subfield code=\"a\">[S.l.]</subfield><subfield code=\"b\">ARTE [prod.]</subfield><subfield code=\"c\">2015</subfield></datafield><datafield tag=\"300\" ind1=\" \" ind2=\" \"><subfield code=\"a\">1 ressource en ligne (29 min.)</subfield></datafield><datafield tag=\"490\" ind1=\"0\" ind2=\" \"><subfield code=\"a\">Le doc</subfield></datafield><datafield tag=\"500\" ind1=\" \" ind2=\" \"><subfield code=\"a\">Emission diffusée sur RTSdeux le 09.05.2016</subfield></datafield><datafield tag=\"500\" ind1=\" \" ind2=\" \"><subfield code=\"a\">Ressource en ligne consultée le 19.05.2016</subfield></datafield><datafield tag=\"520\" ind1=\"8\" ind2=\" \"><subfield code=\"a\">Tiré de la web-série interactive du même nom, Do Not Track, explore les différentes manières dont la Toile enregistre et traque nos activités, nos publications et nos identités pour le plus grand bénéfice de l&#39;économie du Web et ses milliards de dollars. Quelle est la valeur cachée de chacun de nos clics? (RTS)</subfield></datafield><datafield tag=\"521\" ind1=\" \" ind2=\" \"><subfield code=\"a\">Harmos9</subfield></datafield><datafield tag=\"521\" ind1=\" \" ind2=\" \"><subfield code=\"a\">Harmos10</subfield></datafield><datafield tag=\"521\" ind1=\" \" ind2=\" \"><subfield code=\"a\">Harmos11</subfield></datafield><datafield tag=\"521\" ind1=\" \" ind2=\" \"><subfield code=\"a\">Degré secondaire 2</subfield></datafield><datafield tag=\"650\" ind1=\" \" ind2=\"7\"><subfield code=\"a\">Identité numérique</subfield><subfield code=\"2\">rero</subfield></datafield><datafield tag=\"650\" ind1=\" \" ind2=\"7\"><subfield code=\"a\">Données massives</subfield><subfield code=\"2\">rero</subfield></datafield><datafield tag=\"650\" ind1=\" \" ind2=\"7\"><subfield code=\"a\">Traces numériques</subfield><subfield code=\"2\">rero</subfield></datafield><datafield tag=\"650\" ind1=\" \" ind2=\"7\"><subfield code=\"a\">Économie numérique</subfield><subfield code=\"2\">rero</subfield></datafield><datafield tag=\"655\" ind1=\" \" ind2=\"7\"><subfield code=\"a\">Vidéos sur internet</subfield><subfield code=\"2\">rero</subfield></datafield><datafield tag=\"655\" ind1=\" \" ind2=\"7\"><subfield code=\"a\">Films documentaires</subfield><subfield code=\"2\">rero</subfield></datafield><datafield tag=\"655\" ind1=\" \" ind2=\"7\"><subfield code=\"a\">DVD</subfield><subfield code=\"2\">rero</subfield></datafield><datafield tag=\"700\" ind1=\"1\" ind2=\" \"><subfield code=\"a\">Schnabel</subfield><subfield code=\"D\">Selma</subfield></datafield><datafield tag=\"856\" ind1=\"4\" ind2=\" \"><subfield code=\"u\">https://laplattform.ch/node/2136</subfield><subfield code=\"z\">Emission en ligne</subfield><subfield code=\"z\">Accès réservé</subfield></datafield><datafield tag=\"950\" ind1=\" \" ind2=\" \"><subfield code=\"B\">RERO</subfield><subfield code=\"P\">490</subfield><subfield code=\"E\">0-</subfield><subfield code=\"a\">Le doc</subfield></datafield><datafield tag=\"950\" ind1=\" \" ind2=\" \"><subfield code=\"B\">RERO</subfield><subfield code=\"P\">700</subfield><subfield code=\"E\">1-</subfield><subfield code=\"a\">Schnabel</subfield><subfield code=\"D\">Selma</subfield></datafield><datafield tag=\"950\" ind1=\" \" ind2=\" \"><subfield code=\"B\">RERO</subfield><subfield code=\"P\">856</subfield><subfield code=\"E\">4-</subfield><subfield code=\"u\">https://laplattform.ch/node/2136</subfield><subfield code=\"z\">Emission en ligne</subfield><subfield code=\"z\">Accès réservé</subfield></datafield><datafield tag=\"898\" ind1=\" \" ind2=\" \"><subfield code=\"a\">VM010000</subfield><subfield code=\"b\">XM010000</subfield><subfield code=\"c\">XM010000</subfield></datafield><datafield tag=\"949\" ind1=\" \" ind2=\" \"><subfield code=\"B\">RERO</subfield><subfield code=\"E\">R008428854</subfield><subfield code=\"F\">RE01096</subfield><subfield code=\"b\">RE01096</subfield><subfield code=\"c\">RE010960040</subfield><subfield code=\"j\">- -</subfield><subfield code=\"p\">RERO-17553983</subfield></datafield><datafield tag=\"949\" ind1=\" \" ind2=\" \"><subfield code=\"B\">RERO</subfield><subfield code=\"E\">R008428854</subfield><subfield code=\"F\">RE11003</subfield><subfield code=\"b\">RE11003</subfield><subfield code=\"c\">RE110030059</subfield><subfield code=\"j\">004.6 DONO</subfield><subfield code=\"p\">1011867214</subfield><subfield code=\"4\">5200</subfield><subfield code=\"z\">DVD</subfield><subfield code=\"z\">Prêt limité à des fins pédagogiques</subfield></datafield><datafield tag=\"949\" ind1=\" \" ind2=\" \"><subfield code=\"B\">RERO</subfield><subfield code=\"E\">R008428854</subfield><subfield code=\"F\">RE31008</subfield><subfield code=\"b\">RE31008</subfield><subfield code=\"c\">RE310081020</subfield><subfield code=\"j\">- -</subfield><subfield code=\"p\">RERO-17562515</subfield><subfield code=\"4\">2102</subfield></datafield></record>");
        */
        return "<marc:record type=\"Bibliographic\" xmlns:marc=\"http://www.loc.gov/MARC21/slim\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.loc.gov/standards/marcxml/schema/MARC21slim.xsd\"><marc:leader>     nam  22003854u 4500</marc:leader><marc:controlfield tag=\"FMT\">BK</marc:controlfield><marc:controlfield tag=\"LDR\">     nam  22003854u 4500</marc:controlfield><marc:controlfield tag=\"001\">000495412</marc:controlfield><marc:controlfield tag=\"008\">090707s2010    gw       l    00    ger d</marc:controlfield><marc:datafield tag=\"015\" ind1=\" \" ind2=\" \"><marc:subfield code=\"a\">GFR-DNB-09,N30,0313</marc:subfield></marc:datafield><marc:datafield tag=\"016\" ind1=\"7\" ind2=\" \"><marc:subfield code=\"a\">99515970X</marc:subfield><marc:subfield code=\"2\">GyFmDB</marc:subfield></marc:datafield><marc:datafield tag=\"020\" ind1=\" \" ind2=\" \"><marc:subfield code=\"a\">978-3-406-59378-9</marc:subfield><marc:subfield code=\"c\">GB.</marc:subfield></marc:datafield><marc:datafield tag=\"040\" ind1=\" \" ind2=\" \"><marc:subfield code=\"a\">DDB</marc:subfield><marc:subfield code=\"d\">SzZuIDS HSG</marc:subfield></marc:datafield><marc:datafield tag=\"091\" ind1=\" \" ind2=\" \"><marc:subfield code=\"a\">CH00173E5C5595DF7AAAFC1257700005AD43B</marc:subfield><marc:subfield code=\"b\">iCapture</marc:subfield></marc:datafield><marc:datafield tag=\"245\" ind1=\" \" ind2=\" \"><marc:subfield code=\"a\">Familienrecht</marc:subfield><marc:subfield code=\"b\">Scheidung, Unterhalt, Verfahren</marc:subfield><marc:subfield code=\"b\">Kommentar</marc:subfield><marc:subfield code=\"c\">mitbegründet von Kurt H. Johannsen ; hrsg. von Dieter Henrich ; bearb. von Christoph Althammer ... [et al.]</marc:subfield></marc:datafield><marc:datafield tag=\"250\" ind1=\" \" ind2=\" \"><marc:subfield code=\"a\">5., überarb. u. erweiterte Aufl.</marc:subfield></marc:datafield><marc:datafield tag=\"260\" ind1=\" \" ind2=\" \"><marc:subfield code=\"a\">München</marc:subfield><marc:subfield code=\"b\">Beck</marc:subfield><marc:subfield code=\"c\">2010</marc:subfield></marc:datafield><marc:datafield tag=\"300\" ind1=\" \" ind2=\" \"><marc:subfield code=\"a\">1944 S.</marc:subfield></marc:datafield><marc:datafield tag=\"500\" ind1=\" \" ind2=\" \"><marc:subfield code=\"a\">Frühere Aufl. mit d. Titel: Eherecht</marc:subfield></marc:datafield><marc:datafield tag=\"700\" ind1=\" \" ind2=\" \"><marc:subfield code=\"a\">Henrich, Dieter</marc:subfield></marc:datafield><marc:datafield tag=\"700\" ind1=\" \" ind2=\" \"><marc:subfield code=\"a\">Johannsen, Kurt H.</marc:subfield></marc:datafield><marc:datafield tag=\"700\" ind1=\"1\" ind2=\" \"><marc:subfield code=\"a\">Althammer, Christoph</marc:subfield><marc:subfield code=\"d\">1972-</marc:subfield><marc:subfield code=\"1\">(DE-588)129401579</marc:subfield></marc:datafield><marc:datafield tag=\"710\" ind1=\" \" ind2=\" \"><marc:subfield code=\"a\">Deutschland</marc:subfield><marc:subfield code=\"1\">(DE-588)4011882-4</marc:subfield></marc:datafield><marc:datafield tag=\"906\" ind1=\" \" ind2=\" \"><marc:subfield code=\"a\">Gesetze und Verordnungen = Lois, règlements</marc:subfield></marc:datafield><marc:datafield tag=\"651\" ind1=\" \" ind2=\"7\"><marc:subfield code=\"a\">Deutschland</marc:subfield><marc:subfield code=\"1\">(DE-588)4011882-4</marc:subfield><marc:subfield code=\"2\">gnd</marc:subfield></marc:datafield><marc:datafield tag=\"650\" ind1=\" \" ind2=\"7\"><marc:subfield code=\"a\">Ehescheidungsrecht</marc:subfield><marc:subfield code=\"1\">(DE-588)4070670-9</marc:subfield><marc:subfield code=\"2\">gnd</marc:subfield></marc:datafield><marc:datafield tag=\"655\" ind1=\" \" ind2=\"7\"><marc:subfield code=\"a\">Kommentar</marc:subfield><marc:subfield code=\"2\">gnd</marc:subfield></marc:datafield><marc:datafield tag=\"856\" ind1=\" \" ind2=\"C\"><marc:subfield code=\"u\">http://aleph.unisg.ch/hsgscan/hm00266300.pdf</marc:subfield><marc:subfield code=\"z\">download (pdf)</marc:subfield></marc:datafield><marc:datafield tag=\"949\" ind1=\" \" ind2=\" \"><marc:subfield code=\"b\">HSG</marc:subfield><marc:subfield code=\"c\">OG</marc:subfield><marc:subfield code=\"j\">PU 2615 J65 (5)</marc:subfield><marc:subfield code=\"p\">HM00266300</marc:subfield><marc:subfield code=\"q\">000495412</marc:subfield><marc:subfield code=\"r\">000010</marc:subfield><marc:subfield code=\"0\">HSG</marc:subfield><marc:subfield code=\"1\">Upper Floor</marc:subfield><marc:subfield code=\"3\">BOOK</marc:subfield><marc:subfield code=\"4\">15</marc:subfield><marc:subfield code=\"6\"/></marc:datafield></marc:record>";




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
