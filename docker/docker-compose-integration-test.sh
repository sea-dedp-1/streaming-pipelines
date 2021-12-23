#!/bin/bash

docker-compose --project-name=streamingdatapipeline \
  -f ./docker/docker-compose.yml \
  -f ./docker/docker-compose.integration-test.yml \
  $@
