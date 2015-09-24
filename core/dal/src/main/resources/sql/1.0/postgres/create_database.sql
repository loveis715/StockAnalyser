--drop user jewelry;
create user jewelry;

-- Password = jewelry
-- command: md5 -s jewelry
--          MD5 ("jewelry") = 3750c667d5cd8aecc0a9213b362066e9
--ALTER ROLE jewelry LOGIN ENCRYPTED PASSWORD '3750c667d5cd8aecc0a9213b362066e9' VALID UNTIL 'infinity';
ALTER ROLE jewelry WITH ENCRYPTED PASSWORD 'jewelry' VALID UNTIL 'infinity';

ALTER Role jewelry CREATEDB;

--drop database jewelry;
create database jewelry;