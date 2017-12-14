create database stock_app;
create role stockapp with login password 'travis_test';
grant all privileges on database stock_app to stockapp;
