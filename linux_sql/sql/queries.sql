--Queries to apply to tables for LCA project

-- group by id for host_info
SELECT cpu_number, id, total_mem FROM host_info GROUP BY id ORDER BY total_mem;

-- Show 5 minute average for host_usage (based on 5 data points)
INSERT INTO host_usage (avg_used_mem_percentage) VALUES ((total_mem FROM host_info) - mem_free FROM host_usage)/ total_mem FROM host_info)
--function to round 5 minute intervals
CREATE FUNCTION round5(ts timestamp) RETURNS timestamp AS
$$
BEGIN
    RETURN date_trunc('hour', ts) + date_part('minute', ts):: int / 5 * interval '5 min';
END;
$$
    LANGUAGE PLPGSQL;
-- SELECT query to calculate used mem percentage (this one works)
-- Avg used mem is calculated as (total_mem - mem_free) / total_mem * 100
SELECT host_usage.host_id , round5(host_usage.timestamp) AS timestamp, CAST(AVG(100 - 100*host_usage.memory_free/host_info.total_mem) as DECIMAL(10,2)) as avg_mem_used_percentage
FROM host_usage join host_info on host_usage.host_id = host_info.id
GROUP BY host_usage.host_id, round5(host_usage.timestamp)
ORDER BY round5(host_usage.timestamp);