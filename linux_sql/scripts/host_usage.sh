#!/bin/bash
#setup argument
psql_host=$1
psql_port=$2
db_name=$3
psql_user=$4
psql_password=$5
#validate arguments - if less than 5 throw error
if [ "$#" -ne 5 ]; then
	echo "Illegal number of parameters"
	exit 1
fi
# get usage specs and assign to useful variables
vmstat_mb=$(vmstat --unit M)
memory_free=$(echo "$vmstat_mb" | awk '{print $4}' | tail -n1 | xargs)
cpu_idle=$(echo "$vmstat_mb" | awk '{print $15}' | tail -n1 | xargs)
cpu_kernel=$(echo "$vmstat_mb" | awk '{print $13}' | tail -n1 | xargs)
disk_io=$(vmstat -d | awk '{print $10}' | tail -n1 | xargs)
disk_available=$(df -BM | grep -E "/dev/sda2" | awk '{print $4}' | xargs)
timestamp=$(date '+%Y/%m/%d %H:%M:%S')
# match hostname to host info table - satisfy foreign key constraint
hostname=$(hostname -f)
export host_id="(SELECT id FROM host_info WHERE host_info.hostname='$hostname');"
# insert the data into host_usage table (crontab job will automate this every minute)
insert_stmt="INSERT INTO host_usage(timestamp, host_id, memory_free, cpu_idle, cpu_kernel, disk_io, disk_available) VALUES ('$timestamp','2','$memory_free', '$cpu_idle', '$cpu_kernel', '$disk_io', '${disk_available::-1}')"
export PGPASSWORD=$psql_password
psql -h $psql_host -p $psql_port -d $db_name -U $psql_user -c "$insert_stmt"
exit $?