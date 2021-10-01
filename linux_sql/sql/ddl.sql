-- This is to create 2 tables and send into db
-- Assume db already created, create table
CREATE TABLE if not exists PUBLIC.host_info
    (
    id               SERIAL    NOT NULL,
    hostname         VARCHAR   NOT NULL,
    cpu_number       INTEGER   NOT NULL,
    cpu_architecture VARCHAR   NOT NULL,
    cpu_model        VARCHAR   NOT NULL,
    cpu_mhz          VARCHAR   NOT NULL,
    L2_cache         INTEGER   NOT NULL,
    total_mem        INTEGER   NOT NULL,
    timestamp        TIMESTAMP NOT NULL,
    CONSTRAINT PK_host_info_id PRIMARY KEY (id),
    UNIQUE (hostname)
);
--Insert values into table from host_info.sh, host_usage.sh
INSERT INTO host_info
    VALUES (id, hostname, cpu_number, cpu_architecture, cpu_model, cpu_mhz, L2_cache, total_mem, timestamp)
;
CREATE TABLE if not exists PUBLIC.host_usage
    (
        "timestamp"     TIMESTAMP NOT NULL,
        host_id         SERIAL NOT NULL,
        memory_free     INTEGER NOT NULL,
        cpu_idle        INTEGER NOT NULL,
        cpu_kernel      INTEGER NOT NULL,
        disk_io         INTEGER NOT NULL,
        disk_available  INTEGER NOT NULL,
        FOREIGN KEY (host_id) REFERENCES host_info (hostname)
);
INSERT INTO host_usage
    VALUES ("timestamp", host_id, memory_free, cpu_idle, cpu_kernel, disk_io, disk_available)