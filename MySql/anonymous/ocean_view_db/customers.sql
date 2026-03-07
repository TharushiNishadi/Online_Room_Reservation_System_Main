create table customers
(
    id         int auto_increment
        primary key,
    name       varchar(100)                        not null,
    email      varchar(100)                        null,
    phone      varchar(20)                         null,
    address    text                                null,
    created_at timestamp default CURRENT_TIMESTAMP null,
    updated_at timestamp default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP
);
