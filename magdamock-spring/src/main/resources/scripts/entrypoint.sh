#!/bin/bash

# This entrypoint will first patch the XSD files (e.g. to ensure that testnumbers for INSZ are allowed)
# It is assumed that the XSD files are mounted on /data/magda.xsd

pwd_before=$PWD

cd /data
/data/scripts/update-xsd-files.sh

cd $pwd_before
cd /

JAR_PATH=$(find target -name "*.jar" ! -name "*-plain.jar")

java -jar JAR_PATH