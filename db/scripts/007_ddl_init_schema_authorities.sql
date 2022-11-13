create table if not exists authorities (
    id serial primary key,
    authority varchar(50) not null unique
);