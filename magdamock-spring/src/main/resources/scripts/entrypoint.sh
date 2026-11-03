#!/bin/bash

# This entrypoint will first patch the XSD files (e.g. to ensure that testnumbers for INSZ are allowed)
# It is assumed that the XSD files are mounted on /data-internal/magda.xsd

pwd_before=$PWD

cd /data-internal
/data-internal/scripts/update-xsd-files.sh

cd $pwd_before
cd /

JAR_PATH=$(find target -name "*.jar" ! -name "*-plain.jar")

java -jar $JAR_PATH