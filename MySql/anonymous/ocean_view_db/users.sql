create table users
(
    id         int auto_increment
        primary key,
    username   varchar(50)                         not null,
    password   varchar(100)                        not null,
    full_name  varchar(100)                        not null,
    email      varchar(100)                        null,
    created_at timestamp default CURRENT_TIMESTAMP null,
    constraint username
        unique (username)
);

