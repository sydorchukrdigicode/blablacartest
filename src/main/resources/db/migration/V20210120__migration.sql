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

CREATE INDEX IF NOT EXISTS users_ix1 ON users (department_id);

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

CREATE INDEX IF NOT EXISTS tasks_ix1 ON tasks (creator_id);
CREATE INDEX IF NOT EXISTS tasks_ix2 ON tasks (assigner_id);

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

CREATE INDEX IF NOT EXISTS attachments_ix1 ON attachments (task_id);

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

CREATE INDEX IF NOT EXISTS comments_ix1 ON comments (task_id);
CREATE INDEX IF NOT EXISTS comments_ix2 ON comments (author_id);


-----------------

delete from comments;
delete from attachments;
delete from tasks;
delete from users;
delete from departments;

INSERT INTO departments (department_id, name)
VALUES ('460632f0-7a32-48b1-9186-324c0468b911', 'department-1');

INSERT INTO users (user_id, name, department_id)
VALUES ('460632f0-7a32-48b1-9186-324c0468b912', 'user-1', '460632f0-7a32-48b1-9186-324c0468b911');

INSERT INTO users (user_id, name, department_id)
VALUES ('460632f0-7a32-48b1-9186-324c0468b913', 'user-2', '460632f0-7a32-48b1-9186-324c0468b911');

INSERT INTO tasks (task_id, name, details, status, creation_time, creator_id, assigner_id)
VALUES ('460632f0-7a32-48b1-9186-324c0468b910', 'name-1', 'some-details', 'some-status',
        '1970-01-01 00:17:40.000000Z', '460632f0-7a32-48b1-9186-324c0468b912',
        '460632f0-7a32-48b1-9186-324c0468b913');

INSERT INTO tasks (task_id, name, details, status, creation_time, creator_id, assigner_id)
VALUES ('460632f0-7a32-48b1-9186-324c0468b900', 'name-2', 'some-details', 'some-status',
        '1970-01-01 00:16:40.000000Z', '460632f0-7a32-48b1-9186-324c0468b912',
        '460632f0-7a32-48b1-9186-324c0468b912');

INSERT INTO attachments (attachment_id, task_id, file_name, link, creation_time)
VALUES ('460632f0-7a32-48b1-9186-324c0468b914', '460632f0-7a32-48b1-9186-324c0468b910'
       , 'file-name-1', 'some-link-1', '1970-01-01 00:16:40.000000Z');

INSERT INTO attachments (attachment_id, task_id, file_name, link, creation_time)
VALUES ('460632f0-7a32-48b1-9186-324c0468b915', '460632f0-7a32-48b1-9186-324c0468b910'
       , 'file-name-2', 'some-link-2', '1970-01-01 00:16:40.000000Z');


INSERT INTO comments (comment_id, task_id, value, author_id, creation_time)
VALUES ('460632f0-7a32-48b1-9186-324c0468b916', '460632f0-7a32-48b1-9186-324c0468b910'
       , 'comment-1', '460632f0-7a32-48b1-9186-324c0468b912', '1970-01-01 00:16:40.000000Z');

INSERT INTO comments (comment_id, task_id, value, author_id, creation_time)
VALUES ('460632f0-7a32-48b1-9186-324c0468b917', '460632f0-7a32-48b1-9186-324c0468b910'
       , 'comment-2', '460632f0-7a32-48b1-9186-324c0468b913', '1970-01-01 00:17:40.000000Z');

INSERT INTO comments (comment_id, task_id, value, author_id, creation_time)
VALUES ('460632f0-7a32-48b1-9186-324c0468b918', '460632f0-7a32-48b1-9186-324c0468b910'
       , 'comment-3', '460632f0-7a32-48b1-9186-324c0468b912', '1970-01-01 00:18:40.000000Z');

INSERT INTO comments (comment_id, task_id, value, author_id, creation_time)
VALUES ('460632f0-7a32-48b1-9186-324c0468b919', '460632f0-7a32-48b1-9186-324c0468b900'
       , 'comment-4', '460632f0-7a32-48b1-9186-324c0468b912', '1970-01-01 00:16:40.000000Z');