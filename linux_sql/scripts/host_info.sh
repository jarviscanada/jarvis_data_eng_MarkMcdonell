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

hostname=$(hostname -f)
#get hardware specs with function and assign to useful variables
lscpu_out=$(lscpu)
meminfo=$(cat /proc/meminfo)
hardware(){
  echo "$lscpu_out" | grep -E "$*"| awk 'BEGIN { FS = ":" } ;{print $2}' | xargs
}
cpu_number=$(hardware "^CPU\(s\):")
cpu_architecture=$(hardware "Architecture:")
cpu_model=$(hardware "Model name:")
cpu_mhz=$(hardware "CPU MHz:")
L2_cache=$(hardware "^L2 cache:")
# in Mb
total_mem=$(echo "$meminfo" | grep -E "MemTotal:" | awk '{printf( "%d\n", $2 /1024)}' | xargs)
# time - to get from vmstat it has to be from double call ... did not work
# time1=$(vmstat -t | awk '{print $18}')
# time2=$(vmstat -t | awk '{print $19}')
# timestamp=$($time1\s$time2)
timestamp=$(date '+%Y/%m/%d %H:%M:%S') # easier solution
host_id="(SELECT id FROM host_info WHERE hostname='$hostname')";
# insert statement to be used by PSQL
insert_stmt="INSERT INTO host_info (id, hostname, cpu_number, cpu_architecture, cpu_model, cpu_mhz, L2_cache, total_mem, timestamp) VALUES ( '2', '$hostname','$cpu_number', '$cpu_architecture', '$cpu_model', '$cpu_mhz', '${L2_cache::-1}', '$total_mem', '$timestamp')"
export PGPASSWORD=$psql_password
psql -h $psql_host -p $psql_port -d $db_name -U $psql_user -c "$insert_stmt"
exit $?