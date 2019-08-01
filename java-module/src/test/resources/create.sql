create table test (
    id varchar(255) not null,
    header varchar(255),
    type varchar(255),
    playload varchar(255),
    primary key (id)
);

INSERT INTO test (id, header, type, playload) VALUES ('1', 'tenant:mobile', 'string', 'testing');