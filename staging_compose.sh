#!/usr/bin/env bash

set -e  # exit immediately if a command exits with a non-zero status
set -x  # print all executed commands on terminal

export $(grep -v '^#' .env | xargs)

SERVICE_NAME=$1

if [ "$SERVICE_NAME" != "streaming-app" ] && [ "$SERVICE_NAME" != "weather-provider" ] && [ "$SERVICE_NAME" != "solar-panel-emulator" ]
then
        echo "should supply name of the service [streaming-app|weather-provider|solar-panel-emulator]";
        exit 1;
fi

shift # shift arguments

ecs-cli compose \
   --cluster-config ucu-class \
   --region us-east-1 \
   --debug \
   --file staging-$SERVICE_NAME.yml \
   --project-name $STUDENT_NAME-$SERVICE_NAME \
   service "$@"
