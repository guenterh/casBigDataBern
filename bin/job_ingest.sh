#!/bin/bash

if [ $# -ne 5 ]
then
  echo "Usage: `basename $0` INPUT_PATH PREFIX FORMAT MORPH_DEF TABLE_NAME"
  exit 65
fi

hadoop jar $HOME/jobs/$CULTUREGRAPH_JOB_JAR org.culturegraph.mf.cluster.job.ingest.BibIngest -D cg.output.table=$5 -D cg.input.path=$1 -D cg.format=$3  -D cg.ingest.prefix=$2 -D cg.store_rawdata=false -D cg.morphdef=$4