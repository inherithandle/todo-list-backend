INSERT INTO user (user_id, password, email) values ('joma', '$2a$11$qk30XEzncfCSRtz/KPKYceTFabDJD1WG8FC99yQ6Dz./P2edZ3zeS', 'joma@mock-mail.com');
INSERT INTO user (user_id, password, email) values ('mayuko', '$2a$11$Sn7VW.N5tcf5bo/SxnN8GeBMr3Zf8ESn1PMmrGOdJI45TcBuJkb2W', 'mayuko@mock-mail.com');
INSERT INTO user (user_id, password, email) values ('many_todos_user', 'YOU CANNOT LOGIN TO THIS USER', 'dummy-user@mock-mail.com');

INSERT INTO project (project_name, user_no) values ('inbox', 1);
INSERT INTO project (project_name, user_no) values ('english', 1);
INSERT INTO project (project_name, user_no) values ('inbox', 2);
INSERT INTO project (project_name, user_no) values ('inbox', 3);

INSERT INTO todo (text, project_no, completed, due_date) values ('get my hands dirty', 1, 0, now());
INSERT INTO todo (text, project_no, completed, due_date) values ('wash my car', 1, 0, now());
INSERT INTO todo (text, project_no, completed, due_date) values ('completed things', 1, 1, now());

INSERT INTO todo (text, project_no, completed, due_date) values ('todo 4 at page 0', 4, 1, '2023-03-01 00:00:00'); /* min date */
INSERT INTO todo (text, project_no, completed, due_date) values ('todo 3 at page 0', 4, 1, '2023-03-11 00:00:00');
INSERT INTO todo (text, project_no, completed, due_date) values ('todo 2 at page 0', 4, 1, '2023-03-21 00:00:00');
INSERT INTO todo (text, project_no, completed, due_date) values ('todo 1 at page 0', 4, 1, '2023-03-03 00:00:00');
INSERT INTO todo (text, project_no, completed, due_date) values ('todo 0 at page 0', 4, 1, '2023-03-07 00:00:00');
INSERT INTO todo (text, project_no, completed, due_date) values ('todo 5 at page 1', 4, 1, '2023-03-12 00:00:00');
INSERT INTO todo (text, project_no, completed, due_date) values ('todo 6 at page 1', 4, 1, '2023-03-19 00:00:00');
INSERT INTO todo (text, project_no, completed, due_date) values ('todo 7 at page 1', 4, 1, '2023-03-15 00:00:00');
INSERT INTO todo (text, project_no, completed, due_date) values ('todo 8 at page 1', 4, 1, '2023-03-29 00:00:00'); /* max date */
INSERT INTO todo (text, project_no, completed, due_date) values ('todo 9 at page 1', 4, 1, '2023-03-24 00:00:00');
