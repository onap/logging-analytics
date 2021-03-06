#!/bin/bash
#############################################################################
#
# Copyright © 2018 Amdocs.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#        http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#
#############################################################################
# LOG-905
# the manifest is manually maintained and does not drive the image tags in values.yaml
# 

usage() {
cat <<EOF
Usage: $0 [PARAMs]
example
sudo ./prepull_docker.sh -b casablanca -s nexus.onap.cloud:5000  -v true
-u                  : Display usage
-b [branch]         : branch = master or beijing or amsterdam or csablanca (required)
-s [server]         : server = IP or DNS name + port (required)
-v [true/false]     : validate
EOF
}

prepull() {
  sudo curl https://git.onap.org/integration/plain/version-manifest/src/main/resources/docker-manifest.csv?h=$BRANCH > docker-manifest-$BRANCH.csv
  # login twice - the first one periodically times out
  sudo docker login -u docker -p docker $SERVER
  sudo docker login -u docker -p docker $SERVER

  # this line from Gary Wu
  for IMAGE_TAG in $(tail -n +2 docker-manifest-$BRANCH.csv | tr ',' ':'); do
    dt="$(date +"%T")"
    echo "$dt: pulling $IMAGE_TAG"
    sudo docker pull $SERVER/$IMAGE_TAG
  done
}

BRANCH=
SERVER=nexus3.onap.org:10001
VALIDATE=false

while getopts ":b:s:v" PARAM; do
  case $PARAM in
    u)
      usage
      exit 1
      ;;
    b)
      BRANCH=${OPTARG}
      ;;
    s)
      SERVER=${OPTARG}
      ;;
    v)
      VALIDATE=${OPTARG}
      ;;
    ?)
      usage
      exit
      ;;
    esac
done

if [[ -z $BRANCH ]]; then
  usage
  exit 1
fi

prepull $BRANCH $SERVER

