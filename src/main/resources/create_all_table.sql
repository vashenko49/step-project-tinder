create table tinder_account
(
    user_id           uuid      default uuid_generate_v4() primary key not null,
    email             varchar(64) unique                               not null,
    first_name        varchar(64)                                      not null,
    age               smallint,
    interests         varchar(512),
    gender            varchar(12),
    genderpartner     varchar(12),
    aboutMe           varchar(512),
    max_distance      integer   default 10                             not null,
    min_age           smallint  default 18                             not null,
    max_age           smallint  default 22                             not null,
    date_registration timestamp default current_timestamp              not null,
    password          varchar(526),
    last_visit        timestamp default current_timestamp              not null
);

create table images_account
(
    img_url           varchar(126) unique                      not null,
    tinder_account_id uuid references tinder_account (user_id) not null
);

create table matches
(
    match_id    uuid      default uuid_generate_v4() primary key not null,
    timestamp   timestamp default current_timestamp,
    first_like  uuid references tinder_account (user_id)         not null,
    second_like uuid references tinder_account (user_id)         not null
);

create table slided
(
    first_like  uuid references tinder_account (user_id) not null,
    second_like uuid references tinder_account (user_id) not null,
    result      bool                                     not null
);

create table chats
(
    chat_id uuid default uuid_generate_v4() primary key not null,
    from_id uuid references tinder_account (user_id)    not null,
    tou_id  uuid references tinder_account (user_id)    not null
);

create table messages
(
    messages_id  uuid      default uuid_generate_v4() primary key not null,
    chat_id      uuid references chats (chat_id)                  not null,
    message_text text                                             not null,
    read         bool      default false                          not null,
    time_send    timestamp default current_timestamp              not null
);


create index chats_user on chats using btree (from_id);
create index messages_on_chat on messages using btree (messages_id);