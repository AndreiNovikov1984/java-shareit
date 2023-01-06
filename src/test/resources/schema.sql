DROP TABLE IF EXISTS USERS cascade;
DROP TABLE IF EXISTS ITEMS cascade;
DROP TABLE IF EXISTS BOOKINGS cascade;
DROP TABLE IF EXISTS COMMENTS cascade;
DROP TABLE IF EXISTS REQUEST cascade;


create table IF NOT EXISTS USERS
(
    ID         LONG auto_increment
        primary key,
    USER_NAME  CHARACTER VARYING(50)  not null,
    USER_EMAIL CHARACTER VARYING(100) not null
        unique
);

create table IF NOT EXISTS ITEMS
(
    ID          LONG auto_increment primary key,
    ITEM_NAME   CHARACTER VARYING(100) not null,
    DESCRIPTION CHARACTER VARYING(500) not null,
    AVAILABLE   BOOLEAN                not null,
    OWNER_ID    LONG                   not null,
    REQUEST_ID  INTEGER,
    constraint ITEMS_USERS_USER_ID_FK
        foreign key (OWNER_ID) references USERS
);

create table IF NOT EXISTS BOOKINGS
(
    ID         LONG auto_increment
        primary key,
    START_DATE TIMESTAMP not null,
    END_DATE   TIMESTAMP not null,
    ITEM_ID    LONG      not null,
    BOOKER_ID  LONG      not null,
    STATUS     ENUM ('ALL', 'WAITING', 'APPROVED', 'REJECTED', 'CANCELED'),
    constraint BOOKINGS_ITEMS_ITEM_ID_FK
        foreign key (ITEM_ID) references ITEMS,
    constraint BOOKINGS_USERS_USER_ID_FK
        foreign key (BOOKER_ID) references USERS
);

create table IF NOT EXISTS REQUEST
(
    ID           LONG auto_increment primary key,
    DESCRIPTION  CHARACTER VARYING(1000) not null,
    REQUESTOR_ID LONG                    not null,
    CREATED      DATETIME                not null,
    constraint REQUEST_USERS_USER_ID_FK
        foreign key (REQUESTOR_ID) references USERS
);

create table IF NOT EXISTS COMMENTS
(
    ID        LONG auto_increment primary key,
    TEXT      CHARACTER VARYING(1000) not null,
    ITEM_ID   LONG                    not null,
    AUTHOR_ID LONG                    not null,
    CREATED   DATETIME                not null,
    constraint COMMENTS_ITEMS_ITEMS_ID_FK
        foreign key (ITEM_ID) references ITEMS,
    constraint COMMENTS_ITEMS_USERS_USER_ID_FK
        foreign key (AUTHOR_ID) references USERS
);
