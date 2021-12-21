#!/usr/bin/env bash

set -u

SCRIPT=${1}
WAIT_INTERVAL_IN_SECONDS=${2:-10}
MAX_TRIES=${3:-18}

TRIES=0

until [[ "${TRIES}" -ge "${MAX_TRIES}" ]]
do
  bash ${SCRIPT}

  if [ "$?" = 0 ] ; then
    echo -e "\033[0;32;1mPASSED\033[0m"
    exit 0
  fi

  TRIES=$((TRIES+1))
  echo "[Try ${TRIES} / ${MAX_TRIES}] Retrying in ${WAIT_INTERVAL_IN_SECONDS} seconds..."
  sleep ${WAIT_INTERVAL_IN_SECONDS}
done

echo -e "\033[0;31;1mFAILED\033[0m"
exit 1
