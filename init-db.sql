create table if not exists city
(
    id    serial
        constraint city_pk
            primary key,
    name  varchar not null,
    photo varchar not null
);

create unique index if not exists city_id_uindex
    on city (id);
