#!/usr/bin/env bash

set -e
./mvnw package
java -jar ./target/gsc-helper-1.0.0-launcher.jar