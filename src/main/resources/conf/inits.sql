--初始化sql管理表
CREATE TABLE dbgl_sql (
  uuid number,
  key varchar2(100),
  value varchar2(4000),
  lrrq date DEFAULT sysdate,
  type varchar2(100)
);
create sequence seq_dbgl_sql;
INSERT INTO dbgl_sql VALUES (seq_dbgl_sql.nextval, 'sql_del', 'delete from dbgl_sql where 1=1 and key=?', '', '删除');
INSERT INTO dbgl_sql VALUES (seq_dbgl_sql.nextval, 'sql_update', 'update dbgl_sql set VALUE=? where 1=1 and key=?', '', '修改');
INSERT INTO dbgl_sql VALUES (seq_dbgl_sql.nextval, 'login_query', 'select * from fast_user where 1=1 and uno=? and upwd=?', '', '查询');
INSERT INTO dbgl_sql VALUES (seq_dbgl_sql.nextval, 'gncd_query', 'select * from fast_gncd where 1=1 and (-sjgndm=? -)order by px', '', '查询');
INSERT INTO dbgl_sql VALUES (seq_dbgl_sql.nextval, 'sql_key', 'select  distinct TYPE from dbgl_sql', '', '查询');
INSERT INTO dbgl_sql VALUES (seq_dbgl_sql.nextval, 'sql_query', 'select * from dbgl_sql a where 1=1 and (-a.TYPE=?-)  and (-a.key like ''%''||?||''%''-)', '', '查询');
INSERT INTO dbgl_sql VALUES (seq_dbgl_sql.nextval, 'sql_add', 'insert into dbgl_sql(uuid,type,key,value) values(seq_dbgl_sql.nextval,?,?,?)', '', '添加');
INSERT INTO dbgl_sql VALUES (seq_dbgl_sql.nextval, 'cd_test', 'update fast_gncd set yxbz=? where gndm=?', '', '修改');
INSERT INTO dbgl_sql VALUES (seq_dbgl_sql.nextval, 'cd_addmain', 'insert into fast_gncd (uuid,gndm, gnmc, sjgndm, px,url,yxbz) VALUES (seq_fast_gncd.nextval,?, ?, ?,?,?,?)', '', '添加');
INSERT INTO dbgl_sql VALUES (seq_dbgl_sql.nextval, 'cd_query', 'select * from fast_gncd where sjgndm=0', '', '查询');
INSERT INTO dbgl_sql VALUES (seq_dbgl_sql.nextval, 'cd_del', 'delete from fast_gncd where gndm=?', '', '删除');
INSERT INTO dbgl_sql VALUES (seq_dbgl_sql.nextval, 'user_query', 'select * from fast_user where 1=1 and uno like ''%''||?||''%'' and uname like ''%''||?||''%''', '', '查询');
INSERT INTO dbgl_sql VALUES (seq_dbgl_sql.nextval, 'user_add', 'insert into fast_user(uuid,uno,uname,upwd,yxbz) values(seq_fast_user.nextval,?,?,?,?)', '', '添加');
INSERT INTO dbgl_sql VALUES (seq_dbgl_sql.nextval, 'user_update', 'update fast_user set uno=?,uname=?,upwd=?,yxbz=? where uuid=?', '', '修改');
INSERT INTO dbgl_sql VALUES (seq_dbgl_sql.nextval, 'user_del', 'delete from fast_user where uuid=?', '', '删除');

--初始化功能菜单表
CREATE TABLE fast_gncd (
  uuid number,
  gndm varchar2(100),
  gnmc varchar2(100),
  sjgndm varchar2(100),
  px number,
  url varchar2(100),
  yxbz varchar2(2) DEFAULT 'Y'
);
create sequence seq_fast_gncd;
INSERT INTO fast_gncd VALUES (seq_fast_gncd.nextval, '1', '开发设置', '0', '1', '', 'Y');
INSERT INTO fast_gncd VALUES (seq_fast_gncd.nextval, '11', '菜单管理', '1', '1', 'jsp/xtgl/Cd.jsp', 'Y');
INSERT INTO fast_gncd VALUES (seq_fast_gncd.nextval, '12', '人员管理', '1', '2', 'jsp/xtgl/User.jsp', 'Y');
INSERT INTO fast_gncd VALUES (seq_fast_gncd.nextval, '13', 'Sql管理', '1', '3', 'jsp/xtgl/Sql.jsp', 'Y');
INSERT INTO fast_gncd VALUES (seq_fast_gncd.nextval, '10', '在线代码', '1', '4', 'jsp/file.jsp', 'Y');

--初始化用户管理表
CREATE TABLE fast_user (
  uuid number,
  uno varchar2(100),
  uname varchar2(100),
  upwd varchar2(100),
  yxbz varchar2(2) DEFAULT 'Y'
);
create sequence seq_fast_user;
INSERT INTO fast_user VALUES (seq_fast_user.nextval, 'admin', '总管理员', 'e10adc3949ba59abbe56e057f20f883e', 'Y');
INSERT INTO fast_user VALUES (seq_fast_user.nextval, '123123', '123123', '202cb962ac59075b964b07152d234b70', 'Y');

--初始化文件管理表
CREATE TABLE fast_wjgl (
  wjid number,
  lx varchar2(255),
  name varchar2(255),
  url varchar2(255),
  type varchar2(255),
  path varchar2(255)
);
create sequence seq_fast_wjgl;
INSERT INTO fast_wjgl VALUES (seq_fast_wjgl.nextval, 'rygl', 'tx', 'http://localhost:8080/xwcs/upload/rygl/1558275177717.jpg', '.jpg', '/upload/rygl/1558275177717.jpg');


CREATE TABLE fast_zdycx (
  id number,
  zdytj varchar2(255),
  nsrmc varchar2(500) ,
  nsrsbh varchar2(255)
);