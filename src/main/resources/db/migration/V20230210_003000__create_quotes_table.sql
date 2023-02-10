create table quotes(
    id serial primary key not null ,
    created_at timestamp not null,
    updated_at timestamp,
    content text not null,
    user_id integer not null,
    vote_id integer not null,
    foreign key (user_id) references users (id)
        on DELETE cascade
        on UPDATE restrict,
    foreign key (vote_id) references votes (id)
        on DELETE cascade
        on UPDATE restrict
);