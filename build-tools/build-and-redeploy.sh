#!/bin/bash
readonly PROJECT_NAME="mdevoc"

cd $(dirname "$0")
cd ..

mvn clean install

running_containers=$(docker ps -a | grep $PROJECT_NAME)

if [[ ! -z "$running_containers" ]]; then
  docker stop $(docker ps -a | grep $PROJECT_NAME | awk '{ print $1 }')
  docker rm $(docker ps -a | grep $PROJECT_NAME | awk '{ print $1 }')
  docker network prune -f
fi

docker-compose -p $PROJECT_NAME up -d