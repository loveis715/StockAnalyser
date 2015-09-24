-- The database has to be dropped first
drop database jewelry;

-- The login can now be dropped and recreated
drop user jewelry;
create user jewelry;

-- Password = jewelry
-- command: md5 -s jewelry
--          MD5 ("jewelry") = 3750c667d5cd8aecc0a9213b362066e9
--ALTER ROLE jewelry LOGIN ENCRYPTED PASSWORD '3750c667d5cd8aecc0a9213b362066e9' VALID UNTIL 'infinity';
ALTER ROLE jewelry WITH ENCRYPTED PASSWORD 'jewelry' VALID UNTIL 'infinity';
   
ALTER Role jewelry CREATEDB;

create database jewelry;