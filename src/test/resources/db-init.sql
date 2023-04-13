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

insert into city (name, photo)
select * from (select 'Tokyo',
                      'https://upload.wikimedia.org/wikipedia/commons/thumb/b/b2/Skyscrapers_of_Shinjuku_2009_January.jpg/500px-Skyscrapers_of_Shinjuku_2009_January.jpg'
               union all
               select 'Jakarta',
                      'https://upload.wikimedia.org/wikipedia/commons/thumb/f/f6/Jakarta_Pictures-1.jpg/327px-Jakarta_Pictures-1.jpg'
               union all
               select 'Delhi', 'https://upload.wikimedia.org/wikipedia/commons/thumb/5/55/IN-DL.svg/439px-IN-DL.svg.png'
               union all
               select 'Mumbai',
                      'https://upload.wikimedia.org/wikipedia/commons/thumb/1/14/Mumbai_Skyline_at_Night.jpg/500px-Mumbai_Skyline_at_Night.jpg'
               union all
               select 'Manila',
                      'https://upload.wikimedia.org/wikipedia/commons/thumb/0/06/Manila_Cathedral_Facade_at_Sunset.jpg/500px-Manila_Cathedral_Facade_at_Sunset.jpg') as q
where not exists(select 1 from city);
