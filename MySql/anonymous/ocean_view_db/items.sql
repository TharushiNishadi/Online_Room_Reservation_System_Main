create table items
(
    id             int auto_increment
        primary key,
    name           varchar(100)                        not null,
    description    text                                null,
    unit_price     decimal(10, 2)                      not null,
    stock_quantity int       default 0                 null,
    created_at     timestamp default CURRENT_TIMESTAMP null,
    updated_at     timestamp default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP
);

