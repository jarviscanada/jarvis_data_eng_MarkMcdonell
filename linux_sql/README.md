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
Use markdown code block for your quick-start commands
> Start a psql instance using psql_docker.sh
  Psql -h
- Create tables using ddl.sql
  >CREATE PUBLIC.host_info {Hardware specs columns
  Constraints (Primary key constraint,
  Unique hostname)}  
  >CREATE PUBLIC.host_usage {Resource usage columns Constraints (foreign key constraint)}
- Insert hardware specs data into the DB using host_info.sh
- Insert hardware usage data into the DB using host_usage.sh
- Crontab setup
  In terminal:
  Crontab -e
  Shebang ? path to script you want to run ? &> insert into temporary file

# Implemenation
Discuss how you implement the project.
## Architecture
Draw a cluster diagram with three Linux hosts, a DB, and agents (use draw.io website). Image must be saved to the `assets` directory.

## Scripts
Shell script description and usage (use markdown code block for script usage)
- psql_docker.sh
- host_info.sh
- host_usage.sh
- crontab
- queries.sql (describe what business problem you are trying to resolve)

## Database Modeling
Describe the schema of each table using markdown table syntax (do not put any sql code)
- `host_info`
  Primary key constraint is imposed to ensure that each row is unique.
  Unique hostname is imposed, ensuring that the host running the instance can see only the data for the host machine.
- `host_usage`
  Foreign key constraint is imposed to reference the host_id from the host_info table. Then it ensures again that the host machine referenced in host_info is the same as that referenced in host_usage.
# Test
How did you test your bash scripts and SQL queries? What was the result?

# Deployment
How did you deploy your app? (e.g. Github, crontab, docker)

# Improvements
Write at least three things you want to improve
e.g.
- add gui to improve visualization of data in graph form
- add error warnings to sql code
