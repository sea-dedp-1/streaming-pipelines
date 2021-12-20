set -eux
./integration-tests/test-kafka-producer.sh -t station_data_marseille
./integration-tests/test-kafka-producer.sh -t station_data_sf
./integration-tests/test-kafka-producer.sh -t station_information
./integration-tests/test-kafka-producer.sh -t station_status