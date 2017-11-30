create table tb_stock_hist (
    stock_typ varchar(10) not null,
    stock_name varchar(10) not null,
    epoch_time bigint not null,
    time timestamp not null,
    low decimal(100,4) not null,
    high decimal(100,4) not null,
    open decimal(100,4) not null,
    close decimal(100,4) not null,
    volume bigint not null,
    cre_on timestamp not null,
    upd_on timestamp not null,
    primary key (stock_typ, stock_name, epoch_time)
);

create table tb_stock_staging (
    stock_typ varchar(10) not null,
    stock_name varchar(10) not null,
    epoch_time bigint not null,
    proc_flg varchar(1) not null,
    proc_on timestamp,
    err_msg varchar(1000),
    retry_cnt smallint not null,
    cre_on timestamp not null,
    upd_on timestamp not null,
    primary key (stock_typ, stock_name, epoch_time)
);

create table tb_stock_schedule (
    stock_typ varchar(10) not null,
    stock_name varchar(10) not null,
    job_typ varchar(10) not null,
    next_start_dt timestamp not null,
    cre_on timestamp not null,
    upd_on timestamp not null,
    primary key (stock_typ, stock_name, job_typ)
);

create table tb_system_config (
    id bigserial not null primary key,
    sys_cd varchar(30) not null,
    key varchar(30) not null,
    value varchar(2000) not null,
    cre_on timestamp not null,
    upd_on timestamp not null
);

create index ix_system_config on tb_system_config(sys_cd, key);