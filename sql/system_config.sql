insert into tb_system_config (sys_cd, key, value, cre_on, upd_on)
values ('GDAX', 'ENDPOINT_URI', 'https://api.gdax.com/products/%s/candles?start=%s&end=%s&granularity=86400', current_timestamp, current_timestamp);

insert into tb_system_config (sys_cd, key, value, cre_on, upd_on)
values ('GDAX', 'GRANULARITY', '600', current_timestamp, current_timestamp);