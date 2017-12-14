psql -f sql/create_database.sql -U postgres -d stock_app;
psql -f sql/quartz_scheduler.sql -U postgres -d stock_app;
psql -f sql/tables.sql -U postgres -d stock_app;
psql -f sql/dl_jobs.sql -U postgres -d stock_app;
psql -f sql/system_config.sql -U postgres -d stock_app;
