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

create table tb_job_config (
    job_name varchar(1000) not null primary key,
    job_type varchar(30) not null,
    stock_name varchar(10) not null,
    service varchar(30) not null,
    interval smallint not null,
    stat varchar(1) not null,
    cre_on timestamp not null,
    upd_on timestamp not null
);

CREATE TABLE tb_strat_macd (
    stock_typ varchar(10) not null,
    stock_name varchar(10) not null,
    epoch_time bigint not null,
    macd decimal(100,4) not null,
    sig decimal(100,4) not null,
    primary key (stock_typ, stock_name, epoch_time)
);