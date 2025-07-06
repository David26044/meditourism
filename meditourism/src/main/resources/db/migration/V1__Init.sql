CREATE TABLE roles (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    description VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    is_verified BOOLEAN DEFAULT FALSE,
    role_id BIGINT NOT NULL,
    CONSTRAINT fk_role FOREIGN KEY (role_id) REFERENCES roles(id)
);

CREATE TABLE notifications (
    id SERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    title TEXT,
    content TEXT,
    date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_user
        FOREIGN KEY (user_id) REFERENCES users(id)
            ON DELETE CASCADE
);

CREATE TABLE blocked_users (
    user_id BIGINT PRIMARY KEY,
    reason VARCHAR(255) NOT NULL,
    date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_blocked_user
        FOREIGN KEY (user_id) REFERENCES users(id)
            ON DELETE CASCADE
);


CREATE TABLE treatments (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    description TEXT
);

CREATE TABLE clinics (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    email VARCHAR(255) UNIQUE,
    address VARCHAR(255)
);

CREATE TABLE clinic_treatment (
    clinic_id BIGINT NOT NULL,
    treatment_id BIGINT NOT NULL,
    price NUMERIC(10, 2),

    PRIMARY KEY (clinic_id, treatment_id),
    CONSTRAINT fk_clinic
        FOREIGN KEY (clinic_id) REFERENCES clinics(id)
            ON DELETE CASCADE,

    CONSTRAINT fk_treatment
        FOREIGN KEY (treatment_id) REFERENCES treatments(id)
            ON DELETE CASCADE
);

CREATE TABLE reviews (
    id SERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    clinic_id BIGINT NOT NULL,
    content TEXT NOT NULL,
    date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    rating INTEGER,

    CONSTRAINT fk_user
        FOREIGN KEY (user_id) REFERENCES users(id)
            ON DELETE CASCADE,

    CONSTRAINT fk_clinic
        FOREIGN KEY (clinic_id) REFERENCES clinics(id)
            ON DELETE CASCADE
);


CREATE TABLE comments (
    id SERIAL PRIMARY KEY,
    review_id BIGINT NOT NULL,
    father_comment_id BIGINT,
    user_id BIGINT NOT NULL,
    content TEXT NOT NULL,
    date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_review
        FOREIGN KEY (review_id) REFERENCES reviews(id)
            ON DELETE CASCADE,

    CONSTRAINT fk_father_comment
            FOREIGN KEY (father_comment_id) REFERENCES comments(id)
                      ON DELETE CASCADE,

    CONSTRAINT fk_user
            FOREIGN KEY (user_id) REFERENCES users(id)
                      ON DELETE CASCADE
);

CREATE TABLE community_rules (
    id SERIAL PRIMARY KEY,
    rule_text TEXT NOT NULL
);

CREATE TABLE reports (
    id SERIAL PRIMARY KEY,
    reporter_user BIGINT NOT NULL,
    target_review_id BIGINT,
    target_comment_id BIGINT,
    community_rule_id BIGINT NOT NULL,
    reason TEXT NOT NULL,

    CONSTRAINT fk_reporter_user
        FOREIGN KEY (reporter_user) REFERENCES users(id)
            ON DELETE CASCADE,

    CONSTRAINT fk_target_review
        FOREIGN KEY (target_review_id) REFERENCES reviews(id)
            ON DELETE CASCADE,

    CONSTRAINT fk_target_comment
        FOREIGN KEY (target_comment_id) REFERENCES comments(id)
            ON DELETE CASCADE,

    CONSTRAINT fk_community_rule
        FOREIGN KEY (community_rule_id) REFERENCES community_rules(id)
            ON DELETE CASCADE
);

CREATE TABLE contact_forms (
    id SERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    treatment_id BIGINT NOT NULL,
    preferred_clinic_id BIGINT NOT NULL,
    email VARCHAR(255) NOT NULL,
    message TEXT NOT NULL,
    number VARCHAR(255),
    date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_user
        FOREIGN KEY (user_id) REFERENCES users(id)
            ON DELETE CASCADE,
    CONSTRAINT fk_treatment
        FOREIGN KEY (treatment_id) REFERENCES treatments(id)
            ON DELETE CASCADE,
    CONSTRAINT fk_clinic
        FOREIGN KEY (preferred_clinic_id) REFERENCES clinics(id)
            ON DELETE CASCADE
);