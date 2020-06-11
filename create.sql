create table project (project_no bigint not null auto_increment, project_name varchar(255), user_no bigint, primary key (project_no)) engine=InnoDB;
create table todo (id bigint not null auto_increment, completed bit not null, due_date TIMESTAMP, text varchar(255), project_no bigint, primary key (id)) engine=InnoDB;
create table user (user_no bigint not null auto_increment, email varchar(255), password varchar(255), user_id varchar(255), primary key (user_no)) engine=InnoDB;
create table user_token (id bigint not null auto_increment, access_token varchar(255) not null, user_no bigint, primary key (id)) engine=InnoDB;
alter table project add constraint FKabxytecub9mwp1bnifc86lhop foreign key (user_no) references user (user_no);
alter table todo add constraint FKj64vtmsd4yrvs14146wbqltnf foreign key (project_no) references project (project_no);
alter table user_token add constraint FKcxa1c3od3w5eccr10hu7en2g9 foreign key (user_no) references user (user_no);