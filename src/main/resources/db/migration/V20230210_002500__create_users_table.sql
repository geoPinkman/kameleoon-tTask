create table users(
    id serial primary key not null ,
    created_at timestamp not null,
    name varchar(32) not null,
    email varchar(64) not null unique,
    password varchar(255) not null
);