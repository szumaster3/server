#!/bin/bash

mvn install:install-file \
  -Dfile=submodules/530-consts/target/530-Consts-1.0.0.jar \
  -DgroupId=org.rsps \
  -DartifactId=530-Consts \
  -Dversion=1.0.0 \
  -Dpackaging=jar \
  -DgeneratePom=true