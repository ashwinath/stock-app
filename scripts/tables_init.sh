psql -f sql/create_database.sql -U postgres;
psql -f sql/quartz_scheduler.sql -U postgres;
psql -f sql/tables.sql -U postgres;
psql -f sql/dl_jobs.sql -U postgres;
psql -f sql/system_config.sql -U postgres;
