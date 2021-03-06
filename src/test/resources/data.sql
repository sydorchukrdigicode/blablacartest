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

