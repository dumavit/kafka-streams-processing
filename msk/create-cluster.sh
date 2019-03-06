#!/usr/bin/env bash

aws kafka create-cluster --cluster-name "UCUStreamingClass" --broker-node-group-info file://brokernodegroupinfo.json --kafka-version "2.1.0" --number-of-broker-nodes 3 --enhanced-monitoring "PER_BROKER" --region us-east-1

