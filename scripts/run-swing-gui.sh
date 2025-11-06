#!/usr/bin/env bash

set -e
./mvnw --projects gsc-helper-swing-gui --quiet compile exec:java
