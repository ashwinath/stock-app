insert into tb_system_config (sys_cd, key, value, cre_on, upd_on)
values ('GDAX', 'ENDPOINT_URI', 'https://api.gdax.com/products/%s/candles?start=%s&end=%s&granularity=', current_timestamp, current_timestamp);

insert into tb_system_config (sys_cd, key, value, cre_on, upd_on)
values ('GDAX', 'GRANULARITY', '900', current_timestamp, current_timestamp);
