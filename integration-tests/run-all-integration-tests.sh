set -eux
./integration-tests/test.sh -t station_data_marseille
./integration-tests/test.sh -t station_data_sf
./integration-tests/test.sh -t station_information
./integration-tests/test.sh -t station_status