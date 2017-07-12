DROP TABLE IF EXISTS people;
DROP TABLE IF EXISTS messages;
DROP TABLE IF EXISTS followers;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS authorities;

-- Feel free to augment or modify these schemas (and the corresponding data) as you see fit!

CREATE TABLE people (
    id IDENTITY,
    handle VARCHAR,
    name VARCHAR
);


create table users(
	id bigint auto_increment, 
	username varchar(255), 
    password varchar(255),
    person_id NUMBER REFERENCES people (id),
    enabled boolean);
    
create table authorities(
	username  varchar(255),
	authority  varchar(255), 
    UNIQUE(username,authority));

CREATE TABLE messages (
    id IDENTITY,
    person_id NUMBER REFERENCES people (id),
    content VARCHAR
);

CREATE TABLE followers (
    id IDENTITY,
    person_id NUMBER REFERENCES people (id),
    follower_person_id NUMBER REFERENCES people (id)
);
