create table if not exists MPA
(
    ID   INTEGER auto_increment,
    NAME CHARACTER VARYING(100),
    constraint "MPA_PK"
        primary key (ID)
);

create table if not exists FILMS
(
    ID           INTEGER auto_increment,
    NAME         CHARACTER VARYING      not null,
    DESCRIPTION  CHARACTER VARYING(200) not null,
    DURATION     INTEGER                not null,
    RELEASE_DATE TIMESTAMP              not null,
    RATING_ID    INTEGER                        ,
    constraint FILMS_PK
        primary key (ID),
    constraint "FILM_MPA_FK"
        foreign key (ID) references MPA
);

create table if not exists FRIENDSHIP
(
    USER_ID      INTEGER   not null,
    FRIEND_ID    INTEGER   not null,
    STATUS       BOOLEAN default FALSE,
    CREATED_FROM TIMESTAMP not null
);

create table if not exists GENRE
(
    ID   INTEGER auto_increment,
    NAME CHARACTER VARYING not null,
    constraint GENRE_PK
        primary key (ID)
);

create table if not exists FILM_GENRE
(
    FILM_ID  INTEGER not null,
    GENRE_ID INTEGER not null,
    constraint FILM_GENRE_PK
        primary key (FILM_ID, GENRE_ID),
    constraint "film_genre_FILMS_ID_fk"
        foreign key (FILM_ID) references FILMS
            on update cascade on delete cascade,
    constraint "film_genre_GENRE_ID_fk"
        foreign key (GENRE_ID) references GENRE
);

create table if not exists USERS
(
    ID       INTEGER auto_increment,
    EMAIL    CHARACTER VARYING not null,
    LOGIN    CHARACTER VARYING not null,
    NAME     CHARACTER VARYING not null,
    BIRTHDAY TIMESTAMP         not null,
    constraint USERS_PK
        primary key (ID)
);

create table if not exists FILM_LIKES
(
    FILM_ID   INTEGER   not null,
    USER_ID   INTEGER   not null,
    DATE_LIKE TIMESTAMP not null,
    constraint FILM_LIKES_PK
        primary key (FILM_ID),
    constraint "film_likes_FILMS_ID_fk"
        foreign key (FILM_ID) references FILMS,
    constraint "film_likes_USERS_ID_fk"
        foreign key (USER_ID) references USERS
            on update cascade on delete cascade
);


