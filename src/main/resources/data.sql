INSERT INTO user (username, password) values ('joma', '$2a$11$ryKbGq7EWGpJq3IUTMxV.OeWINSSl3s1aMjnFQ4jrxE096P2/NHy6');
INSERT INTO user (username, password) values ('mayuko', '$2a$11$Sn7VW.N5tcf5bo/SxnN8GeBMr3Zf8ESn1PMmrGOdJI45TcBuJkb2W');

INSERT INTO project (projectName, userNo) values ('inbox', 1);
INSERT INTO project (projectName, userNo) values ('english', 1);
INSERT INTO project (projectName, userNo) values ('inbox', 2);

INSERT INTO todo (text, projectNo, completed) values ('get my hands dirty', 1, 0);
INSERT INTO todo (text, projectNo, completed) values ('wash my car', 1, 0);
INSERT INTO todo (text, projectNo, completed) values ('completed things', 1, 1);