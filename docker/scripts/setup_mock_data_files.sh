#!/bin/sh

echo "Setting last_updated timestamp for station information..."
python3 /scripts/set_last_updated_timestamp_to_now_for_station_info_and_status.py \
  /web/mock_station_information.json.template \
  /web/mock_station_information.json \

echo "Setting last_updated timestamp for station status..."
python3 /scripts/set_last_updated_timestamp_to_now_for_station_info_and_status.py \
  /web/mock_station_status.json.template \
  /web/mock_station_status.json

echo "Done."
