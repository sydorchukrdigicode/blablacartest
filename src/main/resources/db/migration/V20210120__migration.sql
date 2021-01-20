CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE IF NOT EXISTS departments
(
    department_id UUID DEFAULT uuid_generate_v4(),
    "name"        VARCHAR(255),

    PRIMARY KEY (department_id)
);

CREATE TABLE IF NOT EXISTS users
(
    user_id       UUID DEFAULT uuid_generate_v4(),
    "name"        VARCHAR(255),
    department_id UUID,

    PRIMARY KEY (user_id),
    CONSTRAINT fk_users_department_id
        FOREIGN KEY (department_id)
            REFERENCES departments (department_id)
            ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS tasks
(
    task_id       UUID DEFAULT uuid_generate_v4(),
    "name"        VARCHAR(255),
    details       VARCHAR(255),
    status        VARCHAR(255),
    creation_time TIMESTAMPTZ,
    creator_id    UUID,
    assigner_id   UUID,

    PRIMARY KEY (task_id),
    CONSTRAINT fk_tasks_creator_id
        FOREIGN KEY (creator_id)
            REFERENCES users (user_id)
            ON DELETE SET NULL,
    CONSTRAINT fk_tasks_assigner_id
        FOREIGN KEY (assigner_id)
            REFERENCES users (user_id)
            ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS attachments
(
    attachment_id UUID DEFAULT uuid_generate_v4(),
    task_id       UUID,
    file_name      VARCHAR(255),
    link          VARCHAR(255),
    creation_time TIMESTAMPTZ,

    PRIMARY KEY (attachment_id),
    CONSTRAINT fk_attachments_task_id
        FOREIGN KEY (task_id)
            REFERENCES tasks (task_id)
            ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS comments
(
    comment_id    UUID DEFAULT uuid_generate_v4(),
    task_id       UUID,
    value         VARCHAR(255),
    author_id     UUID,
    creation_time TIMESTAMPTZ,

    PRIMARY KEY (comment_id),
    CONSTRAINT fk_comments_task_id
        FOREIGN KEY (task_id)
            REFERENCES tasks (task_id)
            ON DELETE SET NULL,
    CONSTRAINT fk_comments_author_id
        FOREIGN KEY (author_id)
            REFERENCES users (user_id)
            ON DELETE SET NULL
);