CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) CHECK(role in ('ROLE_USER', 'ROLE_ADMIN'))
);

CREATE TABLE IF NOT EXISTS tasks (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    status VARCHAR(50) NOT NULL,
    priority VARCHAR(50) NOT NULL,
    author_id BIGINT NOT NULL,
    FOREIGN KEY (author_id) REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS comments (
    id BIGSERIAL PRIMARY KEY,
    text TEXT NOT NULL,
    task_id BIGINT NOT NULL,
    author_id BIGINT NOT NULL,
    FOREIGN KEY (task_id) REFERENCES tasks (id),
    FOREIGN KEY (author_id) REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS task_assignees (
    task_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    PRIMARY KEY (task_id, user_id),
    FOREIGN KEY (task_id) REFERENCES tasks (id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);
