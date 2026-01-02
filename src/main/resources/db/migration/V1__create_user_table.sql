CREATE TABLE users(
    id uuid PRIMARY KEY,
    name varchar(255) not null,
    email varchar(180) not null unique,
    password varchar(255) not null,

    role varchar(50) not null
            constraint chk_users_role
            check (role in ('USER', 'ADMIN')),
    enabled boolean not null default true,

    created_at timestamp not null,
    updated_at timestamp
)