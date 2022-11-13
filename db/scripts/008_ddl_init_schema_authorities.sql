create table authorities (
    username varchar(50) not null,
    authority varchar(50) not null,
    foreign key (username) references users(username)
);