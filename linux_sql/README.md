# Linux Cluster Monitoring Agent
This project is under development. Since this project follows the GitFlow, the final work will be merged to the master branch after Team Code Team.
# Introduction
This project is to monitor the resource usage of the machines running on a Linux cluster. There are 10 machines connected with a switch, communicated through internal IPv4 addresses. They want to record the hardware specs and resource usage in real-time.
The users can access this data in a common postgresql instance database, and check usage by minute basis and average over 5 minutes.
I installed IntelliJ Ultimate IDE on my virtual machine to optimize the management and visualization of code.
From the CentOS terminal, I used docker utility to create a container that runs a postgresql instance.
Then to grab the specs and usage, I created 2 bash scripts called host_info.sh and host_usage. These tables use functions to grab the relevant system data. Both scripts contain insert statements to push data to postgresql database.
The host_usage script has a crontab file that runs it every minute to populate the host_usage table in the database with one more row. I also created a SQL query to output the average resource usage in 5 minute groups using group by and average SQL functions.

# Quick Start
Starting the PSQL instance and populating the database
- Start docker container from terminal
>docker container start jrvs-psql
- Connect to PSQL instance (need password)
>psql -h localhost -U postgres -W
> 
> Connect to host_agent >> \c host_agent
- Now in database (PUBLIC is default Schema)
>Run ddl.sql to build tables
>
From terminal run bash scripts to populate tables
>./host_info.sh "localhost" 5432 "host_agent" 
     "postgres" "password"

>./host_usage.sh [same arguments]

>Run crontab on host_usage.sh

>Run queries from query.sql to answer business questions

# Implemenation
1. We wrote `psql_docker.sh` to run a docker container which is an instance of psql called `jrvs-psql`
2. We wrote two bash scripts to parse specs and resource usage from the host machine called `host_info.sh` and `host_usage.sh`
3. We created tables for the respective data using sql DDL and ran `ddl.sql` in the database
4. The shell scripts were ran to populate the tables and crontab was used to automate `host_usage.sh` execution every minute
## Architecture
Cluster diagram with three Linux hosts, a DB, and agents.
![Architecture](/assets/cluster_architecture.drawio.pdf)
## Scripts
- psql_docker.sh
  - Checks status of or starts docker container with given username and password, ensures the correct number of arguments. Runs the PSQL instance from the image on docker hub.
- host_info.sh
  -  Parses the specs of the host machine and inserts into table host_info in database
- host_usage.sh
  -  Parses the resources used in the host machine and inserts into table host_usage
- crontab
  - Automates running of host_usage.sh every minute
- queries.sql
  - Query 1 selects from host_info a list of hosts ordered by total memory
    - This provides an overview of the number of cpus and total memory for the whole cluster
  - Query 2 selects from host_usage the average resources used in 5 minute intervals
    - This provides real-time resource usage reference
  - Query 3 selects from host_usage where host_usage is not receiving new records
    - This provides a warning if the monitoring agent has failed
## Database Modeling
All users at cluster terminals can access the database given the username and password. 
- `host_info`
  Primary key constraint is imposed to ensure that each row is unique.
  Unique hostname is imposed, ensuring that the host running the instance can see only the data for the host machine.
- `host_usage`
  Foreign key constraint is imposed to reference the host_id from the host_info table. Then it ensures again that the host machine referenced in host_info is the same as that referenced in host_usage.
# Test
Bash scripts were tested using PSQL CLI to ensure that correct data was entered into rows of host_info & host_usage
Result: total_mem column was in kb, this was fixed to be integer in Mb
Result 2: host_id does not reference id properly, input dummy integer '2'.
Queries were tested by running them in host_agent database in psql CLI.
Result: OK for all queries. 

# Deployment
All scripts were merged into release branch on GitHub
For user to run, they can download the scripts and follow Quick Start guideline
# Improvements
Write at least three things you want to improve
e.g.
- add gui to improve visualization of data in graph form
- add error warning strings to sql code
- optimize warning query to select when crontab is running but records are not being added (currently only checks for < 3 entries in 5 minute interval which captures sign-on / sign-off times)
