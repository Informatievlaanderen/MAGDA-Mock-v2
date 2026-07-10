#!/bin/bash

# include the common functions
source "commons.sh"

# allow major, minor or release increments
versionDigitToIncrement=${1:-1}

# prepare git
prepare_git

# start gitflow release
echo "Starting release process"
mvn -B com.amashchenko.maven.plugin:gitflow-maven-plugin:1.19.0:release \
  -s .m2/settings.xml \
  -Dverbose="true" \
  -DproductionBranch="master" \
  -DdevelopmentBranch="develop" \
  -DversionTagPrefix="" \
  -DpreReleaseGoals="" \
  -DpostReleaseGoals="clean deploy -Pfat-jar -Pdocker,docker-release" \
  -DversionsForceUpdate="true" \
  -DversionDigitToIncrement=$versionDigitToIncrement \
  -DargLine="-B -U -s .m2/settings.xml"
