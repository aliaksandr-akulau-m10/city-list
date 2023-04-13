create table if not exists city
(
    id    serial
        constraint city_pk
            not null primary key,
    name  varchar not null,
    photo varchar not null
);

create index if not exists city_name_index
    on city (name);
