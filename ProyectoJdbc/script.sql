
---- mysql -----

CREATE TABLE TBL_ROL ( 
	ID_ROL bigint(20) NOT NULL auto_increment, 
	NAME VARCHAR(45), 
	DESCRIPTION VARCHAR(255) ,
	PRIMARY KEY  (ID_ROL)
)ENGINE=INNODB;

CREATE TABLE TBL_USER ( 
	ID_USER  bigint(20) NOT NULL auto_increment, 
	EXPIRING TIMESTAMP, 
	PASSWORD VARCHAR(255), 
	NAME VARCHAR(255) ,
	PRIMARY KEY  (ID_USER)
)ENGINE=INNODB;

CREATE TABLE TBL_ROL_USER ( 
	ID_USER  bigint(20) NOT NULL, 
	ID_ROL  bigint(20) NOT NULL,
	FOREIGN KEY (ID_USER) REFERENCES TBL_USER (ID_USER),
	FOREIGN KEY (ID_ROL) REFERENCES TBL_ROL (ID_ROL)
)ENGINE=INNODB


 

---- postgresql-----

-- SEQUENCES :  <TABLE_NAME>_<COLUMN_NAME>_seq

CREATE TABLE tbl_rol ( 
	id_rol bigserial NOT NULL primary key,    
	name character varying(45), 
	description character varying(255) 
)

CREATE TABLE tbl_user
(
  id_user bigserial NOT NULL primary key,    
  expiring timestamp without time zone,
  password character varying(255),
  name character varying(255)
)

CREATE TABLE tbl_rol_user ( 
	id_user bigint NOT NULL  references tbl_user(id_user), 
	id_rol  bigint NOT NULL  references tbl_rol(id_rol)
)

