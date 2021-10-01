#!/bin/bash
#setup argument
psql_host=$1
psql_port=$2
db_name=$3
psql_user=$4
psql_password=$5
#validate arguments
if [ "$#" -ne 5 ]; then
	echo "Illegal number of parameters"
	exit 1
fi
get_hostname(){
	hostname=$(hostname -fqdn)
}
#get hardware specs and assign to useful variables
lscpu_out=$(lscpu)
meminfo=$(cat /proc/meminfo)
hardware(){
  echo "$lscpu_out" | grep -E "$*"| awk 'BEGIN { FS = ":"} ;{print $2}' | xargs
  }
cpu_number=$(hardware "CPU:")
cpu_architecture=$(hardware "Architecture:")
cpu_model=$(hardware "Model name:")
cpu_mhz=$(hardware "CPU MHz:")
L2_cache=$(hardware "L2 cache:")
total_mem=$(echo "$meminfo" | grep -E "MemTotal:" | awk '{print $2}' | xargs)

# get usage specs and assign to useful variables
vmstat_mb=$(vmstat --unit M)
memory_free=$(echo "$vmstat_mb" | awk '{print $4}' | tail -n1 | xargs)
cpu_idle=$(echo "$vmstat_mb" | awk '{print $15}' | tail -n1 | xargs)
cpu_kernel=$(echo "$vmstat_mb" | awk '{print $13}' | tail -n1 | xargs)
disk_io=$(vmstat -d | awk '{print $10}' | tail -n1 | xargs)
disk_available=$(df -BM | grep -E "/dev/sda2" | awk '{print $4}' | xargs)
#time - to get from vmstat it has to be from double call, did not work
# time1=$(vmstat -t | awk '{print $18}')
# time2=$(vmstat -t | awk '{print $19}')
# timestamp=$($time1\s$time2)
timestamp=$(date '+%Y/%m/%d %H:%M:%S') # easier solution

host_id="SELECT id FROM host_info WHERE hostname='$hostname')";
insert_stmt="INSERT INTO host_usage(timestamp, host_id, memory_free, cpu_idle, cpu_kernel, disk_io, disk_available)
VALUES('$timestamp','$host_id', '$memory_free', '$cpu_idle', '$cpu_kernel', '$disk_io', '$disk_available'))
export PGPASSWORD=$psql_password
psql -h $psql_host" -p "$psql_port" -d "$db_name" -U "$psql_user" -c $insert_stmt
exit $?
