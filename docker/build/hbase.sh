#!/bin/bash

function progress() {
    local GREEN CLEAN
    GREEN='\033[0;32m'
    CLEAN='\033[0m'
    printf "\n${GREEN}$@  ${CLEAN}\n" >&2
}

set -e

# Docker image prefix
REGPREFIX=szmengran
VERSION=$CLOUD_VERSION

cd ../hbase
mvn package
progress "Building hbase image ..."
docker tag $(docker build -t ${REGPREFIX}/hbase -q .) ${REGPREFIX}/hbase:${VERSION}
cd -