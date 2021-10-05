-- This is to create 2 tables and send into db
-- Assume db already created, create table
CREATE TABLE if not exists PUBLIC.host_info
    (
    id               SERIAL    NOT NULL,
    hostname         VARCHAR   NOT NULL,
    cpu_number       VARCHAR   NOT NULL,
    cpu_architecture VARCHAR   NOT NULL,
    cpu_model        VARCHAR   NOT NULL,
    cpu_mhz          VARCHAR   NOT NULL,
    L2_cache         VARCHAR  NOT NULL,
    total_mem        VARCHAR   NOT NULL,
    timestamp        TIMESTAMP NOT NULL,
    CONSTRAINT PK_host_info_id PRIMARY KEY (id),
    UNIQUE (hostname)
    );
--Insert values into table from host_info.sh, host_usage.sh

CREATE TABLE if not exists PUBLIC.host_usage
    (
    "timestamp"     TIMESTAMP NOT NULL,
    host_id         INTEGER NOT NULL,
    memory_free     VARCHAR NOT NULL,
    cpu_idle        VARCHAR NOT NULL,
    cpu_kernel      VARCHAR NOT NULL,
    disk_io         VARCHAR NOT NULL,
    disk_available  VARCHAR NOT NULL,
    CONSTRAINT fk_host_usage_host_id FOREIGN KEY (host_id) REFERENCES host_info (id)
    );
--The crontab job will add a row every minute