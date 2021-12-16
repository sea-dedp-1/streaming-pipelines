dev:
	./sbin/buildAndRunLocal.sh

down_dev:
	./docker/docker-compose.sh -f docker/docker-compose.yml down --volumes

hadoop_healthcheck:
	curl -sS --fail http://localhost:50070 > /dev/null
yarn_healthcheck:
	curl -sS --fail http://localhost:8088/cluster > /dev/null

e2e:
	./docker/docker-compose.sh -f docker/docker-compose.yml -f docker/docker-compose.e2e.yml up --build -d

down_e2e: down_dev
