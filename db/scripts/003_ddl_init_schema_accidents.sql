create table if not exists accidents
(
    id   serial primary key,
    name varchar(2000),
    text text,
    address varchar(2000),
    type_id int references types(id)

);