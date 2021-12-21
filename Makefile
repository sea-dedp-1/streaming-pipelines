dev:
	./sbin/buildAndRunLocal.sh

down_dev:
	./docker/docker-compose.sh -f docker/docker-compose.yml down --volumes

hadoop_healthcheck:
	curl -sS --fail http://localhost:50070 > /dev/null
yarn_healthcheck:
	curl -sS --fail http://localhost:8088/cluster > /dev/null

up_integration_test:
	./docker/docker-compose.sh -f docker/docker-compose.yml -f docker/docker-compose.integration-test.yml up --build -d

down_integration_test:
	./docker/docker-compose.sh -f docker/docker-compose.yml -f docker/docker-compose.integration-test.yml down --volumes

integration_test: down_integration_test up_integration_test
run_integration_test:
	./integration-tests/run-all-integration-tests.sh

up_e2e:
	./docker/docker-compose.sh -f docker/docker-compose.yml -f docker/docker-compose.e2e.yml up --build -d

down_e2e:
	./docker/docker-compose.sh -f docker/docker-compose.yml -f docker/docker-compose.e2e.yml down --volumes
.PHONY:e2e
e2e: down_e2e up_e2e
run_e2e:
	sh ./e2e/run-all-e2e-tests.sh