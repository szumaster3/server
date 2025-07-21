#!/bin/bash

set -e  # Exit

# git submodules
echo "[SCRIPT] Initializing submodules..."
git submodule update --init --recursive

# constants
echo "[SCRIPT] Building submodule 530-consts..."
cd submodules/530-consts
mvn clean package -DskipTests

# local maven
echo "[SCRIPT] Installing 530-Consts JAR to local Maven repository..."
mvn install:install-file \
  -Dfile=target/530-Consts-1.0.0.jar \
  -DgroupId=org.rsps \
  -DartifactId=530-Consts \
  -Dversion=1.0.0 \
  -Dpackaging=jar \
  -DgeneratePom=true

# build
cd ../../
echo "[SCRIPT] Building the main project..."
mvn clean install -DskipTests

echo "[SCRIPT] Initializing complete."