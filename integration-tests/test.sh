#!/usr/bin/env bash

while getopts "t:" flag
do
    case "${flag}" in
        t) TOPIC=${OPTARG};;
    esac
done

touch $TOPIC.txt

echo "Starting docker exec command..."

KAFKA_COMMAND="kafka-console-consumer.sh --zookeeper zookeeper:2181 --topic $TOPIC"

docker exec streamingdatapipeline_kafka_1 /bin/bash -c "$KAFKA_COMMAND" > $TOPIC.txt &

IS_FOUND=false
for i in {1..10}
  do
      echo "Checking for streaming data..."
      echo "This is check number $i"
  if grep -q "metadata" $TOPIC.txt
  then
      echo "$TOPIC streaming data is found!"
      IS_FOUND=true
      break
  else
      echo "$TOPIC streaming data is not found..."
      echo "Continuing to check..."
  fi
  sleep 10
done

rm $TOPIC.txt

pkill -f $KAFKA_COMMAND

if [ "$IS_FOUND" = true ]
then
  exit 0
else
  echo "Integration test has failed for topic: $TOPIC"
  exit 1
fi