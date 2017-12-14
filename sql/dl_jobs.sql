insert into tb_job_config (job_name, job_type, stock_name, service, interval, stat, cre_on, upd_on)
values ('DL.GDAX.LTC-USD', 'CRYPTO', 'LTC-USD', 'GDAX', 60, 'A', current_timestamp, current_timestamp);
insert into tb_stock_schedule (stock_typ, stock_name, job_typ, next_start_dt, cre_on, upd_on)
values ('CRYPTO', 'LTC-USD', 'DL', date('2017-01-01'), current_timestamp, current_timestamp);

insert into tb_job_config (job_name, job_type, stock_name, service, interval, stat, cre_on, upd_on)
values ('DL.GDAX.ETH-USD', 'CRYPTO', 'ETH-USD', 'GDAX', 60, 'A', current_timestamp, current_timestamp);
insert into tb_stock_schedule (stock_typ, stock_name, job_typ, next_start_dt, cre_on, upd_on)
values ('CRYPTO', 'ETH-USD', 'DL', date('2017-01-01'), current_timestamp, current_timestamp);

insert into tb_job_config (job_name, job_type, stock_name, service, interval, stat, cre_on, upd_on)
values ('DL.GDAX.BTC-USD', 'CRYPTO', 'BTC-USD', 'GDAX', 60, 'A', current_timestamp, current_timestamp);
insert into tb_stock_schedule (stock_typ, stock_name, job_typ, next_start_dt, cre_on, upd_on)
values ('CRYPTO', 'BTC-USD', 'DL', date('2017-01-01'), current_timestamp, current_timestamp);
