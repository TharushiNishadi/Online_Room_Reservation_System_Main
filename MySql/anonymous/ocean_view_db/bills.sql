create table bills
(
    id           int auto_increment
        primary key,
    bill_number  varchar(50)                         not null,
    customer_id  int                                 not null,
    total_amount decimal(10, 2)                      not null,
    bill_date    date                                not null,
    created_by   int                                 not null,
    created_at   timestamp default CURRENT_TIMESTAMP null,
    constraint bill_number
        unique (bill_number),
    constraint bills_ibfk_1
        foreign key (customer_id) references customers (id),
    constraint bills_ibfk_2
        foreign key (created_by) references users (id)
);

create index created_by
    on bills (created_by);

create index customer_id
    on bills (customer_id);

