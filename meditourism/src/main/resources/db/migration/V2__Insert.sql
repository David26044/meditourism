INSERT INTO roles (id, name, description)
VALUES (1, 'ADMIN', 'Administrator role'),
       (2, 'USER', 'Regular user role');

INSERT INTO users (email, name, password, is_verified, role_id)
VALUES ('admin@example.com',
        'Demo Admin',
        '$2a$12$jX6O17Dcu62ZBBk2qUTGxOsVUHJEOc/c.k6Ny1Kd6UDsG3VId82Ve',
        TRUE,
        1);