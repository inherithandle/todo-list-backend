INSERT INTO user (user_id, password, email) values ('joma', '$2a$11$qk30XEzncfCSRtz/KPKYceTFabDJD1WG8FC99yQ6Dz./P2edZ3zeS', 'joma@mock-mail.com');
INSERT INTO user (user_id, password, email) values ('mayuko', '$2a$11$Sn7VW.N5tcf5bo/SxnN8GeBMr3Zf8ESn1PMmrGOdJI45TcBuJkb2W', 'mayuko@mock-mail.com');

INSERT INTO project (project_name, user_no) values ('inbox', 1);
INSERT INTO project (project_name, user_no) values ('english', 1);
INSERT INTO project (project_name, user_no) values ('inbox', 2);

INSERT INTO todo (text, project_no, completed, due_date) values ('get my hands dirty', 1, 0, now());
INSERT INTO todo (text, project_no, completed, due_date) values ('wash my car', 1, 0, now());
INSERT INTO todo (text, project_no, completed, due_date) values ('completed things', 1, 1, now());