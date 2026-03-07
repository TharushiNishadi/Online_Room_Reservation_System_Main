create table bill_items
(
    id          int auto_increment
        primary key,
    bill_id     int            not null,
    item_id     int            not null,
    quantity    int            not null,
    unit_price  decimal(10, 2) not null,
    total_price decimal(10, 2) not null,
    constraint bill_items_ibfk_1
        foreign key (bill_id) references bills (id)
            on delete cascade,
    constraint bill_items_ibfk_2
        foreign key (item_id) references items (id)
);

create index bill_id
    on bill_items (bill_id);

create index item_id
    on bill_items (item_id);

