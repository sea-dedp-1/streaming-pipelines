# streaming-data-pipeline
Streaming pipeline repo for data engineering training program

See producers and consumers set up README in their respective directories

## Enable pre-push hooks

```bash
git config --local core.hooksPath .githooks/
```

## Local environment setup

**Pre-requisites:**
- sbt is installed
- docker is installed

To spin up a local environment, use
```shell
make dev
```

The above command will create Docker containers for running and testing this setup on your local machine.

To spin down the environment, use
```shell
make down_dev
```

### Health checks on local environment

#### For hadoop
```shell
make hadoop_healthcheck
```

#### For yarn
```shell
make yarn_healthcheck
```

## Testing

### End-to-End (e2e) testing

To run the e2e tests, use
```shell
make run_e2e
```

To spin up the e2e environment, use
```shell
make e2e
```

To spin down the e2e environment, use
```shell
make down_e2e
```
