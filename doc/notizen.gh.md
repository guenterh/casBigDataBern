
Bedeutung der shellscripte im bin-Verzeuchnis
1) addLineBreak.sh
* hinzufügen eines line breaks mit Linux tr command
2) compact.sh
* eine Vielzahl von gezippten Dateien wir in eine grosse Datei überführt
3) hbase_create.sh
* in der HBase Datenbank wir mit dem hbase shell Command eine Tabelle miz bestimmten Eigenschaften erstellt.
Es werden die beiden column families prop und raw (für den Rohdatensatz) definiert 
4) hbase_create_simple.sh
* in der HBASE Datenbank wird eine Tabelle mit properties als family aber ohne eine property für den Rohdatensatz erstellt
5) hbase_create_uncompressed.sh
* HBase Tabelle ohne Kompression
6) hbase_drop.sh
* Löschen einer HBase Tabelle
7) hbase_list.sh
* Auflisten der Tabelle in einer HBase Datenbank
8) hbase_scan3rows.sh
* Auflisten der ersten 3 Spalten einer HBase Datenbank
9) hbase_truncate.sh
* Löschen des Inhalts einer HBase Tabelle
10) ingestAll.sh
* wichtig für uns!!
* Aufruf von job_cgIngest.sh $INSTITUTION marc21 cg
* es wird der Typ **org.culturegraph.mf.cluster.job.ingest.BibIngest** benutzt


