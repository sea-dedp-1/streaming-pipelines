dev:
	./sbin/buildAndRunLocal.sh

down_dev:
	docker-compose \
	  --project-name streamingdatapipeline \
	  --project-directory docker \
	  -f docker/docker-compose.yml \
	  down --volumes --remove-orphans

kafka_shell:
	docker exec -it streamingdatapipeline_kafka_1 /bin/bash

zookeeper_shell:
	docker exec -it streamingdatapipeline_zookeeper_1 zkCli.sh -server localhost:2181

zk_shell: zookeeper_shell

hadoop_ls_root:
	docker exec -it streamingdatapipeline_hadoop_1 /usr/local/hadoop/bin/hadoop fs -ls /

hadoop_healthcheck:
	curl -sS --fail http://localhost:50070 > /dev/null
yarn_healthcheck:
	curl -sS --fail http://localhost:8088/cluster > /dev/null

e2e:
	docker-compose \
	  --project-name=streamingdatapipeline \
	  --project-directory docker \
	  -f docker/docker-compose.yml \
	  -f docker/docker-compose.e2e.yml \
	  up --build -d

down_e2e: down_dev
