# create table address
# (
#     id      int auto_increment
#         primary key,
#     country varchar(45) not null,
#     city    varchar(45) not null
# );
#
# create table admin
# (
#     id       int auto_increment
#         primary key,
#     login    varchar(45) not null,
#     password varchar(45) not null
# );
#
# create table person
# (
#     id      int auto_increment
#         primary key,
#     name    varchar(45) null,
#     surname varchar(45) null,
#     age     varchar(45) null
# );
#
# create table teacher
# (
#     id         int auto_increment
#         primary key,
#     language      varchar(45) null,
#     login      varchar(45) not null,
#     password   varchar(45) not null,
#     address_id int         not null,
#     human_id   int         not null,
#     constraint fk_referee_Human1
#         foreign key (human_id) references person (id),
#     constraint fk_referee_address1
#         foreign key (address_id) references address (id)
# );
#
# create index fk_referee_Human1_idx
#     on teacher (human_id);
#
# create index fk_referee_address1_idx
#     on teacher (address_id);
#
# create table student
# (
#     id         int auto_increment
#         primary key,
#     language      varchar(45) not null,
#     address_id int         not null,
#     human_id   int         not null,
#     constraint fk_student_Person1
#         foreign key (human_id) references person (id),
#     constraint fk_student_address
#         foreign key (address_id) references address (id)
# );
#
# INSERT INTO ADMIN(id,password,login)
# VALUES ('1','123','123')
