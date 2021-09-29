#!/bin/bash
# Assign arguments
cmd=$1
db_username=$2
db_password=$3
# Check docker status OR start docker
sudo systemctl status docker || systemctl start docker
# Check container status
docker container inspect jrvs-psql
container_status=$?
# Check if container already running
case $cmd in
 create)

 if [ $container_status -eq 0 ]; then
   echo 'Container already exists'
   exit 1
 fi

 if [ $3 -ne 3 ]; then
   echo 'Create requires username and password'
   exit 1
 fi
# Create volume and container
 docker volume create pgdata
 docker run --name jrvs-psql -e POSTGRES_PASSWORD=$PGPASSWORD \
 -d -v pgdata:/var/lib/postgresql/data -p 5432:5432 postgres
 exit $?
 ;;

 start|stop)
 # If no container, prompt to create one and exit
 if [ $container_status != 0 ]; then
   echo 'Missing container: use create first'
   exit 1
 fi

 docker container $cmd jrvs-psql
 exit $?
 ;;

 *)
 echo 'Illegal command'
 echo 'Commands: start|stop|create'
 exit 1
 ;;
esac

exit 0



