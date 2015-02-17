#!/bin/sh
# Bootstraps gradle task options for automatic releases

if [[ $# -lt 3 ]]
then
  echo "Usage: release.sh [Scope; valid are major,minor,patch] [Stage; valid are final, milestone, dev] [Track; valid are alpha, production, beta]"
  echo "Eg. command ./release minor milesone alpha"
  exit
fi

SCOPE=$1
STAGE=$2
TRACK=$3

echo "Releasing..."

./gradlew clean releaseApp -Prelease.scope=$SCOPE -Prelease.stage=$STAGE -PuploadTrack=$TRACK

echo "Done!"