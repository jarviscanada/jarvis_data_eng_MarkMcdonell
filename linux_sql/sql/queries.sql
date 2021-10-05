--Queries to apply to host_usage table for LCA project

--
SELECT (cpu_number, id, total_mem) FROM host_info GROUP BY cpu_number  ORDER BY total_mem;
SELECT date_trunc('hour', timestamp) + date_part('minute', timestamp):: int/5 * interval '5 min'
FROM host_usage;
