
create database gestion_instalacion;
use gestion_instalacion;


CREATE TABLE SPRING_SESSION (
    PRIMARY_ID CHAR(36) NOT NULL,
    SESSION_ID CHAR(36) NOT NULL,
    CREATION_TIME BIGINT NOT NULL,
    LAST_ACCESS_TIME BIGINT NOT NULL,
    MAX_INACTIVE_INTERVAL INT NOT NULL,
    EXPIRY_TIME BIGINT NOT NULL,
    PRINCIPAL_NAME VARCHAR(100),
    CONSTRAINT SPRING_SESSION_PK PRIMARY KEY (PRIMARY_ID)
) ENGINE=InnoDB;

CREATE UNIQUE INDEX SPRING_SESSION_IX1 ON SPRING_SESSION (SESSION_ID);
CREATE INDEX SPRING_SESSION_IX2 ON SPRING_SESSION (EXPIRY_TIME);
CREATE INDEX SPRING_SESSION_IX3 ON SPRING_SESSION (PRINCIPAL_NAME);


CREATE TABLE SPRING_SESSION_ATTRIBUTES (
    SESSION_PRIMARY_ID CHAR(36) NOT NULL,
    ATTRIBUTE_NAME VARCHAR(200) NOT NULL,
    ATTRIBUTE_BYTES BLOB NOT NULL,
    CONSTRAINT SPRING_SESSION_ATTRIBUTES_PK PRIMARY KEY (SESSION_PRIMARY_ID, ATTRIBUTE_NAME),
    CONSTRAINT SPRING_SESSION_ATTRIBUTES_FK FOREIGN KEY (SESSION_PRIMARY_ID) REFERENCES SPRING_SESSION(PRIMARY_ID) ON DELETE CASCADE
) ENGINE=InnoDB;


CREATE TABLE revision_info (
    rev INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    revtstmp BIGINT,
    id_usuario int
);


create table usuario(
	id INT  not null auto_increment primary key,
	uuid CHAR(36) NOT NULL DEFAULT (UUID()),
    nombre varchar(255) not null,
    email varchar(255) not null unique,
	password varchar(255) not null,
    activo TINYINT(1) not null default(1),
    id_usuario_creacion int  ,
    id_usuario_modificacion int ,
    fecha_creacion datetime not null default now(),
    fecha_modificacion datetime not null default now()
);

CREATE TABLE usuario_historico (
    id INT NOT NULL,
    uuid CHAR(36),
    nombre VARCHAR(255),
    email VARCHAR(255),
    password VARCHAR(255),
    activo TINYINT(1),
    id_usuario_creacion INT,
    id_usuario_modificacion INT,
    fecha_creacion DATETIME,
    fecha_modificacion DATETIME,

    REV INT NOT NULL,
    REVTYPE TINYINT NOT NULL,

    PRIMARY KEY (id, REV),
    CONSTRAINT usuario_historico_rev FOREIGN KEY (REV) REFERENCES revision_info(rev)
);



insert into usuario(nombre, email, password)
values ('admin','admin@gestioninstalacion.com','password');



alter table usuario modify id_usuario_creacion int default(1);
alter table usuario modify id_usuario_modificacion int default(1);



create table rol(
	id INT  not null auto_increment primary key,
	uuid CHAR(36) NOT NULL DEFAULT (UUID()),
    nombre varchar(255) not null unique,
    activo TINYINT(1) not null default(1) ,
    id_usuario_creacion int  default(1),
    id_usuario_modificacion int  default(1),
    fecha_creacion datetime not null default now(),
    fecha_modificacion datetime not null default now()
);

CREATE TABLE rol_historico (
    id INT NOT NULL,
    uuid CHAR(36),
    nombre VARCHAR(255),
    activo TINYINT(1),
    id_usuario_creacion INT,
    id_usuario_modificacion INT,
    fecha_creacion DATETIME,
    fecha_modificacion DATETIME,

    REV INT NOT NULL,
    REVTYPE TINYINT NOT NULL,

    PRIMARY KEY (id, REV),
    CONSTRAINT rol_historico_rev FOREIGN KEY (REV) REFERENCES revision_info(rev)
);

insert into rol(nombre)
values ('ADMINISTRADOR'),('USUARIO_CLIENTE'),('USUARIO_EMPRESA'),('USUARIO_SEDE'),('USUARIO_INSTALACION');

alter table rol add foreign key fk_rol_usuario_creacion (id_usuario_creacion) references usuario(id);
alter table rol add foreign key fk_rol_usuario_modificacion (id_usuario_modificacion) references usuario(id);


CREATE TABLE usuario_rol (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    uuid CHAR(36) NOT NULL DEFAULT (UUID()),
    id_usuario INT NOT NULL,
    id_rol INT NOT NULL,
    activo TINYINT(1) NOT NULL DEFAULT 1,
    id_usuario_creacion INT DEFAULT 1,
    id_usuario_modificacion INT DEFAULT 1,
    fecha_creacion DATETIME NOT NULL DEFAULT NOW(),
    fecha_modificacion DATETIME NOT NULL DEFAULT NOW()
);

CREATE TABLE usuario_rol_historico (
    id INT NOT NULL,
    uuid CHAR(36),
    id_usuario INT  ,
    id_rol INT  ,
    activo TINYINT(1),
    id_usuario_creacion INT,
    id_usuario_modificacion INT,
    fecha_creacion DATETIME,
    fecha_modificacion DATETIME,

    REV INT NOT NULL,
    REVTYPE TINYINT NOT NULL,

    PRIMARY KEY (id, REV),
    CONSTRAINT usuario_rol_historico_rev FOREIGN KEY (REV) REFERENCES revision_info(rev)
);

alter table usuario_rol add foreign key fk_usuario_rol_usuario (id_usuario) references usuario(id);
alter table usuario_rol add foreign key fk_usuario_rol_rol (id_rol) references rol(id);
alter table usuario_rol add foreign key fk_usuario_rol_usuario_creacion (id_usuario_creacion) references usuario(id);
alter table usuario_rol add foreign key fk_usuario_rol_usuario_modificacion (id_usuario_modificacion) references usuario(id);

INSERT INTO usuario_rol (id_usuario, id_rol) VALUES (
    (SELECT id FROM usuario WHERE nombre = 'admin'), (SELECT id FROM rol WHERE nombre = 'ADMINISTRADOR')
);



create table comunidad_autonoma(
	id INT  not null auto_increment primary key,
	uuid CHAR(36) NOT NULL DEFAULT (UUID()),
    codigo_ine int not null unique,
    nombre varchar(255) not null unique,
    activo TINYINT(1) not null default(1) ,
    id_usuario_creacion int not null default(1),
    id_usuario_modificacion int not null default(1),
    fecha_creacion datetime not null default now(),
    fecha_modificacion datetime not null default now()
);

CREATE TABLE comunidad_autonoma_historico (
    id INT NOT NULL,
    uuid CHAR(36),
    codigo_ine int ,
    nombre varchar(255),
    activo TINYINT(1),
    id_usuario_creacion INT,
    id_usuario_modificacion INT,
    fecha_creacion DATETIME,
    fecha_modificacion DATETIME,

    REV INT NOT NULL,
    REVTYPE TINYINT NOT NULL,

    PRIMARY KEY (id, REV),
    CONSTRAINT comunidad_autonoma_historico_rev FOREIGN KEY (REV) REFERENCES revision_info(rev)
);

alter table comunidad_autonoma add foreign key fk_comunidad_autonoma_usuario_creacion (id_usuario_creacion) references usuario(id);
alter table comunidad_autonoma add foreign key fk_comunidad_autonoma_usuario_modificacion (id_usuario_modificacion) references usuario(id);



insert into comunidad_autonoma(codigo_ine,nombre,id_usuario_creacion,id_usuario_modificacion)values
(1,'Andalucía',1,1),
(2,'Aragón',1,1),
(3,'Asturias, Principado de',1,1),
(4,'Balears, Illes',1,1),
(5,'Canarias',1,1),
(6,'Cantabria',1,1),
(7,'Castilla y León',1,1),
(8,'Castilla - La Mancha',1,1),
(9,'Cataluña',1,1),
(10,'Comunitat Valenciana',1,1),
(11,'Extremadura',1,1),
(12,'Galicia',1,1),
(13,'Madrid, Comunidad de',1,1),
(14,'Murcia, Región de',1,1),
(15,'Navarra, Comunidad Foral de',1,1),
(16,'País Vasco',1,1),
(17,'Rioja, La',1,1),
(18,'Ceuta',1,1),
(19,'Melilla',1,1);


create table provincia(
	id INT  not null auto_increment primary key,
	uuid CHAR(36) NOT NULL DEFAULT (UUID()),
    id_comunidad_autonoma int not null,
    codigo_ine int not null unique,
    nombre varchar(255) not null unique,
    activo TINYINT(1) not null default(1) ,
    id_usuario_creacion int not null default(1),
    id_usuario_modificacion int not null default(1),
    fecha_creacion datetime not null default now(),
    fecha_modificacion datetime not null default now()
);

CREATE TABLE provincia_historico (
    id INT NOT NULL,
    uuid CHAR(36),
    id_comunidad_autonoma int,
    codigo_ine int ,
    nombre varchar(255),
    activo TINYINT(1),
    id_usuario_creacion INT,
    id_usuario_modificacion INT,
    fecha_creacion DATETIME,
    fecha_modificacion DATETIME,

    REV INT NOT NULL,
    REVTYPE TINYINT NOT NULL,

    PRIMARY KEY (id, REV),
    CONSTRAINT provincia_historico_rev FOREIGN KEY (REV) REFERENCES revision_info(rev)
);

alter table provincia add foreign key fk_provincia_usuario_creacion (id_usuario_creacion) references usuario(id);
alter table provincia add foreign key fk_provincia_usuario_modificacion (id_usuario_modificacion) references usuario(id);
alter table provincia add foreign key fk_provincia_comunidad_autonoma (id_comunidad_autonoma) references comunidad_autonoma(id);

insert into provincia(id_comunidad_autonoma, codigo_ine,nombre,id_usuario_creacion, id_usuario_modificacion) values
((select id from comunidad_autonoma where codigo_ine =1),4,'Almería',1,1),
((select id from comunidad_autonoma where codigo_ine =1),11,'Cádiz',1,1),
((select id from comunidad_autonoma where codigo_ine =1),14,'Córdoba',1,1),
((select id from comunidad_autonoma where codigo_ine =1),18,'Granada',1,1),
((select id from comunidad_autonoma where codigo_ine =1),21,'Huelva',1,1),
((select id from comunidad_autonoma where codigo_ine =1),23,'Jaén',1,1),
((select id from comunidad_autonoma where codigo_ine =1),29,'Málaga',1,1),
((select id from comunidad_autonoma where codigo_ine =1),41,'Sevilla',1,1),
((select id from comunidad_autonoma where codigo_ine =2),22,'Huesca',1,1),
((select id from comunidad_autonoma where codigo_ine =2),44,'Teruel',1,1),
((select id from comunidad_autonoma where codigo_ine =2),50,'Zaragoza',1,1),
((select id from comunidad_autonoma where codigo_ine =3),33,'Asturias',1,1),
((select id from comunidad_autonoma where codigo_ine =4),7,'Balears, Illes',1,1),
((select id from comunidad_autonoma where codigo_ine =5),35,'Palmas, Las',1,1),
((select id from comunidad_autonoma where codigo_ine =5),38,'Santa Cruz de Tenerife',1,1),
((select id from comunidad_autonoma where codigo_ine =6),39,'Cantabria',1,1),
((select id from comunidad_autonoma where codigo_ine =7),5,'Ávila',1,1),
((select id from comunidad_autonoma where codigo_ine =7),9,'Burgos',1,1),
((select id from comunidad_autonoma where codigo_ine =7),24,'León',1,1),
((select id from comunidad_autonoma where codigo_ine =7),34,'Palencia',1,1),
((select id from comunidad_autonoma where codigo_ine =7),37,'Salamanca',1,1),
((select id from comunidad_autonoma where codigo_ine =7),40,'Segovia',1,1),
((select id from comunidad_autonoma where codigo_ine =7),42,'Soria',1,1),
((select id from comunidad_autonoma where codigo_ine =7),47,'Valladolid',1,1),
((select id from comunidad_autonoma where codigo_ine =7),49,'Zamora',1,1),
((select id from comunidad_autonoma where codigo_ine =8),2,'Albacete',1,1),
((select id from comunidad_autonoma where codigo_ine =8),13,'Ciudad Real',1,1),
((select id from comunidad_autonoma where codigo_ine =8),16,'Cuenca',1,1),
((select id from comunidad_autonoma where codigo_ine =8),19,'Guadalajara',1,1),
((select id from comunidad_autonoma where codigo_ine =8),45,'Toledo',1,1),
((select id from comunidad_autonoma where codigo_ine =9),8,'Barcelona',1,1),
((select id from comunidad_autonoma where codigo_ine =9),17,'Girona',1,1),
((select id from comunidad_autonoma where codigo_ine =9),25,'Lleida',1,1),
((select id from comunidad_autonoma where codigo_ine =9),43,'Tarragona',1,1),
((select id from comunidad_autonoma where codigo_ine =10),3,'Alicante/Alacant',1,1),
((select id from comunidad_autonoma where codigo_ine =10),12,'Castellón/Castelló',1,1),
((select id from comunidad_autonoma where codigo_ine =10),46,'Valencia/València',1,1),
((select id from comunidad_autonoma where codigo_ine =11),6,'Badajoz',1,1),
((select id from comunidad_autonoma where codigo_ine =11),10,'Cáceres',1,1),
((select id from comunidad_autonoma where codigo_ine =12),15,'Coruña, A',1,1),
((select id from comunidad_autonoma where codigo_ine =12),27,'Lugo',1,1),
((select id from comunidad_autonoma where codigo_ine =12),32,'Ourense',1,1),
((select id from comunidad_autonoma where codigo_ine =12),36,'Pontevedra',1,1),
((select id from comunidad_autonoma where codigo_ine =13),28,'Madrid',1,1),
((select id from comunidad_autonoma where codigo_ine =14),30,'Murcia',1,1),
((select id from comunidad_autonoma where codigo_ine =15),31,'Navarra',1,1),
((select id from comunidad_autonoma where codigo_ine =16),1,'Araba/Álava',1,1),
((select id from comunidad_autonoma where codigo_ine =16),48,'Bizkaia',1,1),
((select id from comunidad_autonoma where codigo_ine =16),20,'Gipuzkoa',1,1),
((select id from comunidad_autonoma where codigo_ine =17),26,'Rioja, La',1,1),
((select id from comunidad_autonoma where codigo_ine =18),51,'Ceuta',1,1),
((select id from comunidad_autonoma where codigo_ine =19),52,'Melilla',1,1);


create table municipio(
	id INT  not null auto_increment primary key,
	uuid CHAR(36) NOT NULL DEFAULT (UUID()),
    id_provincia int not null,
    codigo_ine int not null ,
    dc int not null ,
    nombre varchar(255) not null ,
    activo TINYINT(1) not null default(1) ,
    id_usuario_creacion int not null default(1),
    id_usuario_modificacion int not null default(1),
    fecha_creacion datetime not null default now(),
    fecha_modificacion datetime not null default now()
);

CREATE TABLE municipio_historico (
    id INT NOT NULL,
    uuid CHAR(36),
    id_provincia int,
    codigo_ine int ,
    dc int ,
    nombre varchar(255),
    activo TINYINT(1),
    id_usuario_creacion INT,
    id_usuario_modificacion INT,
    fecha_creacion DATETIME,
    fecha_modificacion DATETIME,

    REV INT NOT NULL,
    REVTYPE TINYINT NOT NULL,

    PRIMARY KEY (id, REV),
    CONSTRAINT municipio_historico_rev FOREIGN KEY (REV) REFERENCES revision_info(rev)
);


alter table municipio add foreign key fk_municipio_usuario_creacion (id_usuario_creacion) references usuario(id);
alter table municipio add foreign key fk_municipio_usuario_modificacion (id_usuario_modificacion) references usuario(id);
alter table municipio add foreign key fk_municipio_provincia (id_provincia) references provincia(id);


create table permiso(
	id INT  not null auto_increment primary key,
	uuid CHAR(36) NOT NULL DEFAULT (UUID()),
    nombre varchar(255) not null unique,
    activo TINYINT(1) not null default(1) ,
    id_usuario_creacion int  default(1),
    id_usuario_modificacion int  default(1),
    fecha_creacion datetime not null default now(),
    fecha_modificacion datetime not null default now()
);

CREATE TABLE permiso_historico (
    id INT NOT NULL,
    uuid CHAR(36),
    nombre varchar(255),
    activo TINYINT(1),
    id_usuario_creacion INT,
    id_usuario_modificacion INT,
    fecha_creacion DATETIME,
    fecha_modificacion DATETIME,

    REV INT NOT NULL,
    REVTYPE TINYINT NOT NULL,

    PRIMARY KEY (id, REV),
    CONSTRAINT permiso_historico_rev FOREIGN KEY (REV) REFERENCES revision_info(rev)
);

alter table permiso add foreign key fk_permiso_usuario_creacion (id_usuario_creacion) references usuario(id);
alter table permiso add foreign key fk_permiso_usuario_modificacion (id_usuario_modificacion) references usuario(id);

insert into permiso(nombre) values
('GESTION_GLOBAL'),
('GESTION_EMPRESA'),
('GESTION_SEDE'),
('GESTION_INSTALACION'),
('MI_PERFIL'),
('GESTION_USUARIO_GLOBAL'),
('GESTION_USUARIO_EMPRESA'),
('GESTION_USUARIO_SEDE'),
('GESTION_USUARIO_INSTALACION'),
('GESTION_ROL'),
('GESTION_ROL_GLOBAL'),
('GESTION_ROL_EMPRESA'),
('GESTION_ROL_SEDE'),
('GESTION_ROL_INSTALACION'),
('GESTION_RESERVA'),
('GESTION_RESERVA_GLOBAL'),
('GESTION_RESERVA_EMPRESA'),
('GESTION_RESERVA_SEDE'),
('GESTION_RESERVA_INSTALACION'),
('GESTION_RESERVA_PROPIA'),
('GESTION_SANCION'),
('GESTION_SANCION_GLOBAL'),
('GESTION_SANCION_EMPRESA'),
('GESTION_SANCION_SEDE'),
('GESTION_SANCION_INSTALACION'),
('GESTION_SANCION_PROPIA');


create table rol_permiso(
	id INT  not null auto_increment primary key,
	uuid CHAR(36) NOT NULL DEFAULT (UUID()),
	id_rol int not null,
	id_permiso int not null,
    activo TINYINT(1) not null default(1) ,
    id_usuario_creacion int  default(1),
    id_usuario_modificacion int  default(1),
    fecha_creacion datetime not null default now(),
    fecha_modificacion datetime not null default now()
);

CREATE TABLE rol_permiso_historico (
    id INT NOT NULL,
    uuid CHAR(36),
    id_rol int ,
	id_permiso int ,
    activo TINYINT(1),
    id_usuario_creacion INT,
    id_usuario_modificacion INT,
    fecha_creacion DATETIME,
    fecha_modificacion DATETIME,

    REV INT NOT NULL,
    REVTYPE TINYINT NOT NULL,

    PRIMARY KEY (id, REV),
    CONSTRAINT rol_permiso_historico_rev FOREIGN KEY (REV) REFERENCES revision_info(rev)
);

alter table rol_permiso add foreign key fk_rol_permiso_usuario_creacion (id_usuario_creacion) references usuario(id);
alter table rol_permiso add foreign key fk_rol_permiso_usuario_modificacion (id_usuario_modificacion) references usuario(id);
alter table rol_permiso add foreign key fk_rol_permiso_rol (id_rol) references rol(id);
alter table rol_permiso add foreign key fk_rol_permiso_permiso (id_permiso) references permiso(id);

INSERT INTO rol_permiso(id_rol,id_permiso) values
((select id from rol where nombre = 'ADMINISTRADOR'), (select id from permiso where nombre = 'GESTION_GLOBAL')),
((select id from rol where nombre = 'ADMINISTRADOR'), (select id from permiso where nombre = 'GESTION_EMPRESA')),
((select id from rol where nombre = 'ADMINISTRADOR'), (select id from permiso where nombre = 'GESTION_SEDE')),
((select id from rol where nombre = 'ADMINISTRADOR'), (select id from permiso where nombre = 'GESTION_INSTALACION')),
((select id from rol where nombre = 'ADMINISTRADOR'), (select id from permiso where nombre = 'MI_PERFIL')),
((select id from rol where nombre = 'ADMINISTRADOR'), (select id from permiso where nombre = 'GESTION_USUARIO_GLOBAL')),
((select id from rol where nombre = 'ADMINISTRADOR'), (select id from permiso where nombre = 'GESTION_USUARIO_EMPRESA')),
((select id from rol where nombre = 'ADMINISTRADOR'), (select id from permiso where nombre = 'GESTION_USUARIO_SEDE')),
((select id from rol where nombre = 'ADMINISTRADOR'), (select id from permiso where nombre = 'GESTION_USUARIO_INSTALACION')),
((select id from rol where nombre = 'ADMINISTRADOR'), (select id from permiso where nombre = 'GESTION_ROL')),
((select id from rol where nombre = 'ADMINISTRADOR'), (select id from permiso where nombre = 'GESTION_ROL_GLOBAL')),
((select id from rol where nombre = 'ADMINISTRADOR'), (select id from permiso where nombre = 'GESTION_ROL_EMPRESA')),
((select id from rol where nombre = 'ADMINISTRADOR'), (select id from permiso where nombre = 'GESTION_ROL_SEDE')),
((select id from rol where nombre = 'ADMINISTRADOR'), (select id from permiso where nombre = 'GESTION_ROL_INSTALACION')),
((select id from rol where nombre = 'ADMINISTRADOR'), (select id from permiso where nombre = 'GESTION_RESERVA')),
((select id from rol where nombre = 'ADMINISTRADOR'), (select id from permiso where nombre = 'GESTION_RESERVA_GLOBAL')),
((select id from rol where nombre = 'ADMINISTRADOR'), (select id from permiso where nombre = 'GESTION_RESERVA_EMPRESA')),
((select id from rol where nombre = 'ADMINISTRADOR'), (select id from permiso where nombre = 'GESTION_RESERVA_SEDE')),
((select id from rol where nombre = 'ADMINISTRADOR'), (select id from permiso where nombre = 'GESTION_RESERVA_INSTALACION')),
((select id from rol where nombre = 'ADMINISTRADOR'), (select id from permiso where nombre = 'GESTION_RESERVA_PROPIA')),
((select id from rol where nombre = 'ADMINISTRADOR'), (select id from permiso where nombre = 'GESTION_SANCION')),
((select id from rol where nombre = 'ADMINISTRADOR'), (select id from permiso where nombre = 'GESTION_SANCION_GLOBAL')),
((select id from rol where nombre = 'ADMINISTRADOR'), (select id from permiso where nombre = 'GESTION_SANCION_EMPRESA')),
((select id from rol where nombre = 'ADMINISTRADOR'), (select id from permiso where nombre = 'GESTION_SANCION_SEDE')),
((select id from rol where nombre = 'ADMINISTRADOR'), (select id from permiso where nombre = 'GESTION_SANCION_INSTALACION')),
((select id from rol where nombre = 'ADMINISTRADOR'), (select id from permiso where nombre = 'GESTION_SANCION_PROPIA'));


INSERT INTO rol_permiso(id_rol,id_permiso) values
((select id from rol where nombre = 'USUARIO_EMPRESA'), (select id from permiso where nombre = 'GESTION_EMPRESA')),
((select id from rol where nombre = 'USUARIO_EMPRESA'), (select id from permiso where nombre = 'GESTION_SEDE')),
((select id from rol where nombre = 'USUARIO_EMPRESA'), (select id from permiso where nombre = 'GESTION_INSTALACION')),
((select id from rol where nombre = 'USUARIO_EMPRESA'), (select id from permiso where nombre = 'MI_PERFIL')),
((select id from rol where nombre = 'USUARIO_EMPRESA'), (select id from permiso where nombre = 'GESTION_USUARIO_EMPRESA')),
((select id from rol where nombre = 'USUARIO_EMPRESA'), (select id from permiso where nombre = 'GESTION_USUARIO_SEDE')),
((select id from rol where nombre = 'USUARIO_EMPRESA'), (select id from permiso where nombre = 'GESTION_USUARIO_INSTALACION')),
((select id from rol where nombre = 'USUARIO_EMPRESA'), (select id from permiso where nombre = 'GESTION_ROL')),
((select id from rol where nombre = 'USUARIO_EMPRESA'), (select id from permiso where nombre = 'GESTION_ROL_SEDE')),
((select id from rol where nombre = 'USUARIO_EMPRESA'), (select id from permiso where nombre = 'GESTION_ROL_INSTALACION')),
((select id from rol where nombre = 'USUARIO_EMPRESA'), (select id from permiso where nombre = 'GESTION_RESERVA')),
((select id from rol where nombre = 'USUARIO_EMPRESA'), (select id from permiso where nombre = 'GESTION_RESERVA_EMPRESA')),
((select id from rol where nombre = 'USUARIO_EMPRESA'), (select id from permiso where nombre = 'GESTION_RESERVA_SEDE')),
((select id from rol where nombre = 'USUARIO_EMPRESA'), (select id from permiso where nombre = 'GESTION_RESERVA_INSTALACION')),
((select id from rol where nombre = 'USUARIO_EMPRESA'), (select id from permiso where nombre = 'GESTION_SANCION')),
((select id from rol where nombre = 'USUARIO_EMPRESA'), (select id from permiso where nombre = 'GESTION_SANCION_EMPRESA')),
((select id from rol where nombre = 'USUARIO_EMPRESA'), (select id from permiso where nombre = 'GESTION_SANCION_SEDE')),
((select id from rol where nombre = 'USUARIO_EMPRESA'), (select id from permiso where nombre = 'GESTION_SANCION_INSTALACION'));

INSERT INTO rol_permiso(id_rol,id_permiso) values
((select id from rol where nombre = 'USUARIO_SEDE'), (select id from permiso where nombre = 'GESTION_SEDE')),
((select id from rol where nombre = 'USUARIO_SEDE'), (select id from permiso where nombre = 'GESTION_INSTALACION')),
((select id from rol where nombre = 'USUARIO_SEDE'), (select id from permiso where nombre = 'MI_PERFIL')),
((select id from rol where nombre = 'USUARIO_SEDE'), (select id from permiso where nombre = 'GESTION_USUARIO_SEDE')),
((select id from rol where nombre = 'USUARIO_SEDE'), (select id from permiso where nombre = 'GESTION_USUARIO_INSTALACION')),
((select id from rol where nombre = 'USUARIO_SEDE'), (select id from permiso where nombre = 'GESTION_ROL')),
((select id from rol where nombre = 'USUARIO_SEDE'), (select id from permiso where nombre = 'GESTION_ROL_INSTALACION')),
((select id from rol where nombre = 'USUARIO_SEDE'), (select id from permiso where nombre = 'GESTION_RESERVA')),
((select id from rol where nombre = 'USUARIO_SEDE'), (select id from permiso where nombre = 'GESTION_RESERVA_SEDE')),
((select id from rol where nombre = 'USUARIO_SEDE'), (select id from permiso where nombre = 'GESTION_RESERVA_INSTALACION')),
((select id from rol where nombre = 'USUARIO_SEDE'), (select id from permiso where nombre = 'GESTION_SANCION')),
((select id from rol where nombre = 'USUARIO_SEDE'), (select id from permiso where nombre = 'GESTION_SANCION_SEDE')),
((select id from rol where nombre = 'USUARIO_SEDE'), (select id from permiso where nombre = 'GESTION_SANCION_INSTALACION'));



INSERT INTO rol_permiso(id_rol,id_permiso) values
((select id from rol where nombre = 'USUARIO_INSTALACION'), (select id from permiso where nombre = 'GESTION_INSTALACION')),
((select id from rol where nombre = 'USUARIO_INSTALACION'), (select id from permiso where nombre = 'MI_PERFIL')),
((select id from rol where nombre = 'USUARIO_INSTALACION'), (select id from permiso where nombre = 'GESTION_USUARIO_INSTALACION')),
((select id from rol where nombre = 'USUARIO_INSTALACION'), (select id from permiso where nombre = 'GESTION_RESERVA')),
((select id from rol where nombre = 'USUARIO_INSTALACION'), (select id from permiso where nombre = 'GESTION_RESERVA_INSTALACION')),
((select id from rol where nombre = 'USUARIO_INSTALACION'), (select id from permiso where nombre = 'GESTION_SANCION')),
((select id from rol where nombre = 'USUARIO_INSTALACION'), (select id from permiso where nombre = 'GESTION_SANCION_INSTALACION'));


INSERT INTO rol_permiso(id_rol,id_permiso) values
((select id from rol where nombre = 'USUARIO_CLIENTE'), (select id from permiso where nombre = 'MI_PERFIL')),
((select id from rol where nombre = 'USUARIO_CLIENTE'), (select id from permiso where nombre = 'GESTION_RESERVA')),
((select id from rol where nombre = 'USUARIO_CLIENTE'), (select id from permiso where nombre = 'GESTION_RESERVA_PROPIA')),
((select id from rol where nombre = 'USUARIO_CLIENTE'), (select id from permiso where nombre = 'GESTION_SANCION')),
((select id from rol where nombre = 'USUARIO_CLIENTE'), (select id from permiso where nombre = 'GESTION_SANCION_PROPIA'));

create table empresa(
	id INT  not null auto_increment primary key,
	uuid CHAR(36) NOT NULL DEFAULT (UUID()),
    nombre varchar(255) not null ,
    email varchar(255) not null,
    cif varchar(255) not null,
    logo varchar(255) null,
    url varchar(255),
    descripcion varchar(1250) null,
    activo TINYINT(1) not null default(1) ,
    id_usuario_creacion int not null default(1),
    id_usuario_modificacion int not null default(1),
    fecha_creacion datetime not null default now(),
    fecha_modificacion datetime not null default now()
);

CREATE TABLE empresa_historico (
    id INT NOT NULL,
    uuid CHAR(36),
    nombre varchar(255),
    email varchar(255) ,
    cif varchar(255),
    logo varchar(255) ,
    url varchar(255),
    descripcion varchar(1250) null,
    activo TINYINT(1),
    id_usuario_creacion INT,
    id_usuario_modificacion INT,
    fecha_creacion DATETIME,
    fecha_modificacion DATETIME,

    REV INT NOT NULL,
    REVTYPE TINYINT NOT NULL,

    PRIMARY KEY (id, REV),
    CONSTRAINT empresa_historico_rev FOREIGN KEY (REV) REFERENCES revision_info(rev)
);

alter table empresa add foreign key fk_empresa_usuario_creacion (id_usuario_creacion) references usuario(id);
alter table empresa add foreign key fk_empresa_usuario_modificacion (id_usuario_modificacion) references usuario(id);

create table sede(
	id INT  not null auto_increment primary key,
	uuid CHAR(36) NOT NULL DEFAULT (UUID()),
    id_empresa int not null,
    id_municipio int not null,
    direccion varchar(350) null,
    latitud varchar(255) not null,
    longitud varchar(255) not null,
    nombre varchar(255) not null,
    email varchar(255) not null,
    logo varchar(255) null,
    url varchar(255),
    descripcion varchar(1250) null,
    activo TINYINT(1) not null default(1) ,
    id_usuario_creacion int not null default(1),
    id_usuario_modificacion int not null default(1),
    fecha_creacion datetime not null default now(),
    fecha_modificacion datetime not null default now()
);

CREATE TABLE sede_historico (
    id INT NOT NULL,
    uuid CHAR(36),
    id_empresa int ,
    id_municipio int ,
    direccion varchar(350) null,
    latitud varchar(255)  null,
    longitud varchar(255)  null,
    nombre varchar(255)  null,
    email varchar(255)  null,
    logo varchar(255) null,
    url varchar(255),
    activo TINYINT(1),
    id_usuario_creacion INT,
    id_usuario_modificacion INT,
    fecha_creacion DATETIME,
    fecha_modificacion DATETIME,

    REV INT NOT NULL,
    REVTYPE TINYINT NOT NULL,

    PRIMARY KEY (id, REV),
    CONSTRAINT sede_historico_rev FOREIGN KEY (REV) REFERENCES revision_info(rev)
);

alter table sede add foreign key fk_sede_usuario_creacion (id_usuario_creacion) references usuario(id);
alter table sede add foreign key fk_sede_usuario_modificacion (id_usuario_modificacion) references usuario(id);
alter table sede add foreign key fk_sede_empresa (id_empresa) references empresa(id);
alter table sede add foreign key fk_sede_municipio (id_municipio) references municipio(id);


create table instalacion_tipo(
	id INT  not null auto_increment primary key,
	uuid CHAR(36) NOT NULL DEFAULT (UUID()),
    nombre varchar(255) not null,
    descripcion varchar(1250) null,
    activo TINYINT(1) not null default(1) ,
    id_usuario_creacion int not null default(1),
    id_usuario_modificacion int not null default(1),
    fecha_creacion datetime not null default now(),
    fecha_modificacion datetime not null default now()
);

CREATE TABLE instalacion_tipo_historico (
    id INT NOT NULL,
    uuid CHAR(36),
    nombre varchar(255)  null,
    descripcion varchar(1250) null,
    activo TINYINT(1),
    id_usuario_creacion INT,
    id_usuario_modificacion INT,
    fecha_creacion DATETIME,
    fecha_modificacion DATETIME,

    REV INT NOT NULL,
    REVTYPE TINYINT NOT NULL,

    PRIMARY KEY (id, REV),
    CONSTRAINT instalacion_tipo_historico_rev FOREIGN KEY (REV) REFERENCES revision_info(rev)
);

alter table instalacion_tipo add foreign key fk_instalacion_tipo_usuario_creacion (id_usuario_creacion) references usuario(id);
alter table instalacion_tipo add foreign key fk_instalacion_tipo_usuario_modificacion (id_usuario_modificacion) references usuario(id);

insert into instalacion_tipo(nombre) values 
('GIMNASIO'),('PISCINA'),('PISTA PADEL'),('PISTA POLIDEPORTIVA'),('SAUNA'),('VELÓDROMO'),('PISTA DE TENIS'),('SALA MULTIUSOS');


create table instalacion(
	id INT  not null auto_increment primary key,
	uuid CHAR(36) NOT NULL DEFAULT (UUID()),
    id_sede int not null,
    id_instalacion_tipo int not null,
    nombre varchar(255) not null,
    descripcion varchar(1250) null,
    activo TINYINT(1) not null default(1) ,
    id_usuario_creacion int not null default(1),
    id_usuario_modificacion int not null default(1),
    fecha_creacion datetime not null default now(),
    fecha_modificacion datetime not null default now()
);

CREATE TABLE instalacion_historico (
    id INT NOT NULL,
    uuid CHAR(36),
   	id_sede int  null,
    id_instalacion_tipo int  null,
    nombre varchar(255)  null,
    descripcion varchar(1250) null,
    activo TINYINT(1),
    id_usuario_creacion INT,
    id_usuario_modificacion INT,
    fecha_creacion DATETIME,
    fecha_modificacion DATETIME,

    REV INT NOT NULL,
    REVTYPE TINYINT NOT NULL,

    PRIMARY KEY (id, REV),
    CONSTRAINT instalacion_historico_rev FOREIGN KEY (REV) REFERENCES revision_info(rev)
);

alter table instalacion add foreign key fk_instalacion_usuario_creacion (id_usuario_creacion) references usuario(id);
alter table instalacion add foreign key fk_instalacion_usuario_modificacion (id_usuario_modificacion) references usuario(id);
alter table instalacion add foreign key fk_instalacion_sede (id_sede) references sede(id);
alter table instalacion add foreign key fk_instalacion_instalacion_tipo (id_instalacion_tipo) references instalacion_tipo(id);

create table imagen(
	id INT  not null auto_increment primary key,
	uuid CHAR(36) NOT NULL DEFAULT (UUID()),
    id_empresa int null ,
    id_sede int  null,
    id_instalacion int  null,
    url varchar(255) not null,
    activo TINYINT(1) not null default(1) ,
    id_usuario_creacion int not null default(1),
    id_usuario_modificacion int not null default(1),
    fecha_creacion datetime not null default now(),
    fecha_modificacion datetime not null default now()
);

CREATE TABLE imagen_historico (
    id INT NOT NULL,
    uuid CHAR(36),
   	id_empresa int null ,
    id_sede int  null,
    id_instalacion int  null,
    url varchar(255)  null,
    activo TINYINT(1),
    id_usuario_creacion INT,
    id_usuario_modificacion INT,
    fecha_creacion DATETIME,
    fecha_modificacion DATETIME,

    REV INT NOT NULL,
    REVTYPE TINYINT NOT NULL,

    PRIMARY KEY (id, REV),
    CONSTRAINT imagen_historico_rev FOREIGN KEY (REV) REFERENCES revision_info(rev)
);


alter table imagen add foreign key fk_imagen_usuario_creacion (id_usuario_creacion) references usuario(id);
alter table imagen add foreign key fk_imagen_usuario_modificacion (id_usuario_modificacion) references usuario(id);
alter table imagen add foreign key fk_imagen_empresa (id_empresa) references empresa(id);
alter table imagen add foreign key fk_imagen_sede (id_sede) references sede(id);
alter table imagen add foreign key fk_imagen_instalacion (id_instalacion) references instalacion(id);
alter table imagen add CONSTRAINT chk_imagen_relacion CHECK ((id_empresa IS NOT NULL) +(id_sede IS NOT NULL) +(id_instalacion IS NOT NULL) = 1);


create table instalacion_horario (
	id INT  not null auto_increment primary key,
	uuid CHAR(36) NOT NULL DEFAULT (UUID()),
    id_instalacion int not null,
    dia_semana int not null,
    hora_inicio TIME not null,
    hora_fin TIME not null,
    activo TINYINT(1) not null default(1) ,
    id_usuario_creacion int not null default(1),
    id_usuario_modificacion int not null default(1),
    fecha_creacion datetime not null default now(),
    fecha_modificacion datetime not null default now()
);

CREATE TABLE instalacion_horario_historico (
    id INT NOT NULL,
    uuid CHAR(36),
   	id_instalacion int  null,
    dia_semana int  null,
    hora_inicio TIME  null,
    hora_fin TIME  null,
    activo TINYINT(1),
    id_usuario_creacion INT,
    id_usuario_modificacion INT,
    fecha_creacion DATETIME,
    fecha_modificacion DATETIME,

    REV INT NOT NULL,
    REVTYPE TINYINT NOT NULL,

    PRIMARY KEY (id, REV),
    CONSTRAINT instalacion_horario_historico_rev FOREIGN KEY (REV) REFERENCES revision_info(rev)
);

alter table instalacion_horario add foreign key fk_instalacion_horario_usuario_creacion (id_usuario_creacion) references usuario(id);
alter table instalacion_horario add foreign key fk_instalacion_horario_usuario_modificacion (id_usuario_modificacion) references usuario(id);
alter table instalacion_horario add foreign key fk_instalacion_horario_instalacion (id_instalacion) references instalacion(id);

create table instalacion_horario_especial (
	id INT  not null auto_increment primary key,
	uuid CHAR(36) NOT NULL DEFAULT (UUID()),
    id_instalacion int not null,
    fecha DATE not null,
    hora_inicio TIME  null,
    hora_fin TIME  null,
    cerrado TINYINT(1) not null default(0),
    activo TINYINT(1) not null default(1) ,
    id_usuario_creacion int not null default(1),
    id_usuario_modificacion int not null default(1),
    fecha_creacion datetime not null default now(),
    fecha_modificacion datetime not null default now()
);

CREATE TABLE instalacion_horario_especial_historico (
    id INT NOT NULL,
    uuid CHAR(36),
    id_instalacion int not null,
    fecha DATE not null,
    hora_inicio TIME  null,
    hora_fin TIME  null,
    cerrado TINYINT(1) not null default(0),
    activo TINYINT(1),
    id_usuario_creacion INT,
    id_usuario_modificacion INT,
    fecha_creacion DATETIME,
    fecha_modificacion DATETIME,

    REV INT NOT NULL,
    REVTYPE TINYINT NOT NULL,

    PRIMARY KEY (id, REV),
    CONSTRAINT instalacion_horario_especial_historico_rev FOREIGN KEY (REV) REFERENCES revision_info(rev)
);

alter table instalacion_horario_especial add foreign key fk_instalacion_horario_especial_usuario_creacion (id_usuario_creacion) references usuario(id);
alter table instalacion_horario_especial add foreign key fk_instalacion_horario_especial_usuario_modificacion (id_usuario_modificacion) references usuario(id);
alter table instalacion_horario_especial add foreign key fk_instalacion_horario_especial_instalacion (id_instalacion) references instalacion(id);

create table instalacion_configuracion_reserva (
	id INT  not null auto_increment primary key,
	uuid CHAR(36) NOT NULL DEFAULT (UUID()),
    id_instalacion int not null,
    duracion_min int not null,
    duracion_max int not null,
    intervalo_horario int not null,
    activo TINYINT(1) not null default(1) ,
    id_usuario_creacion int not null default(1),
    id_usuario_modificacion int not null default(1),
    fecha_creacion datetime not null default now(),
    fecha_modificacion datetime not null default now()
);

CREATE TABLE instalacion_configuracion_reserva_historico (
    id INT NOT NULL,
    uuid CHAR(36),
    id_instalacion int  null,
    duracion_min int  null,
    duracion_max int  null,
    intervalo_horario int not null,
    activo TINYINT(1),
    id_usuario_creacion INT,
    id_usuario_modificacion INT,
    fecha_creacion DATETIME,
    fecha_modificacion DATETIME,

    REV INT NOT NULL,
    REVTYPE TINYINT NOT NULL,

    PRIMARY KEY (id, REV),
    CONSTRAINT instalacion_configuracion_reserva_historico_rev FOREIGN KEY (REV) REFERENCES revision_info(rev)
);

alter table instalacion_configuracion_reserva add foreign key fk_instalacion_configuracion_reserva_usuario_creacion (id_usuario_creacion) references usuario(id);
alter table instalacion_configuracion_reserva add foreign key fk_instalacion_configuracion_reserva_usuario_modificacion (id_usuario_modificacion) references usuario(id);
alter table instalacion_configuracion_reserva add foreign key fk_instalacion_configuracion_reserva_instalacion (id_instalacion) references instalacion(id);


create table reserva_estado(
	id INT  not null auto_increment primary key,
	uuid CHAR(36) NOT NULL DEFAULT (UUID()),
    nombre varchar(255) not null ,
    activo TINYINT(1) not null default(1) ,
    id_usuario_creacion int not null default(1),
    id_usuario_modificacion int not null default(1),
    fecha_creacion datetime not null default now(),
    fecha_modificacion datetime not null default now()
);

CREATE TABLE reserva_estado_historico (
    id INT NOT NULL,
    uuid CHAR(36),
    nombre varchar(255)  null ,
    activo TINYINT(1),
    id_usuario_creacion INT,
    id_usuario_modificacion INT,
    fecha_creacion DATETIME,
    fecha_modificacion DATETIME,

    REV INT NOT NULL,
    REVTYPE TINYINT NOT NULL,

    PRIMARY KEY (id, REV),
    CONSTRAINT reserva_estado_historico_rev FOREIGN KEY (REV) REFERENCES revision_info(rev)
);

alter table reserva_estado add foreign key fk_reserva_estado_usuario_creacion (id_usuario_creacion) references usuario(id);
alter table reserva_estado add foreign key fk_reserva_estado_usuario_modificacion (id_usuario_modificacion) references usuario(id);

insert into reserva_estado(nombre) values 
('PENDIENTE'),('APROBADA'),('COMPLETADA'),('CANCELADA POR USUARIO'),('CANCELADA POR EMPRESA'),('DENEGADA'),('INCOMPLETADA'), ('CANCELADA POR SANCION');

create table reserva(
	id INT  not null auto_increment primary key,
	uuid CHAR(36) NOT NULL DEFAULT (UUID()),
    id_reserva_estado int not null,
    id_instalacion int not null,
    fecha DATE not null,
    hora_inicio TIME  null,
    hora_fin TIME  null,
    activo TINYINT(1) not null default(1) ,
    id_usuario_creacion int not null default(1),
    id_usuario_modificacion int not null default(1),
    fecha_creacion datetime not null default now(),
    fecha_modificacion datetime not null default now()
);

CREATE TABLE reserva_historico (
    id INT NOT NULL,
    uuid CHAR(36),
    id_reserva_estado int  null,
    id_instalacion int  null,
    fecha DATE  null,
    hora_inicio TIME  null,
    hora_fin TIME  null,
    activo TINYINT(1),
    id_usuario_creacion INT,
    id_usuario_modificacion INT,
    fecha_creacion DATETIME,
    fecha_modificacion DATETIME,

    REV INT NOT NULL,
    REVTYPE TINYINT NOT NULL,

    PRIMARY KEY (id, REV),
    CONSTRAINT reserva_historico_rev FOREIGN KEY (REV) REFERENCES revision_info(rev)
);

alter table reserva add foreign key fk_reserva_usuario_creacion (id_usuario_creacion) references usuario(id);
alter table reserva add foreign key fk_reserva_usuario_modificacion (id_usuario_modificacion) references usuario(id);
alter table reserva add foreign key fk_reserva_instalacion (id_instalacion) references instalacion(id);
alter table reserva add foreign key fk_reserva_reserva_estado (id_reserva_estado) references reserva_estado(id);

create table usuario_empresa(
	id INT  not null auto_increment primary key,
	uuid CHAR(36) NOT NULL DEFAULT (UUID()),
    id_usuario int not null,
    id_empresa int not null,
    activo TINYINT(1) not null default(1) ,
    id_usuario_creacion int not null default(1),
    id_usuario_modificacion int not null default(1),
    fecha_creacion datetime not null default now(),
    fecha_modificacion datetime not null default now()
);

CREATE TABLE usuario_empresa_historico (
    id INT NOT NULL,
    uuid CHAR(36),
    id_usuario int  null,
    id_empresa int  null,
    activo TINYINT(1),
    id_usuario_creacion INT,
    id_usuario_modificacion INT,
    fecha_creacion DATETIME,
    fecha_modificacion DATETIME,

    REV INT NOT NULL,
    REVTYPE TINYINT NOT NULL,

    PRIMARY KEY (id, REV),
    CONSTRAINT usuario_empresa_historico_rev FOREIGN KEY (REV) REFERENCES revision_info(rev)
);

alter table usuario_empresa add foreign key fk_usuario_empresa_usuario_creacion (id_usuario_creacion) references usuario(id);
alter table usuario_empresa add foreign key fk_usuario_empresa_modificacion (id_usuario_modificacion) references usuario(id);
alter table usuario_empresa add foreign key fk_usuario_empresa_empresa (id_empresa) references empresa(id);
alter table usuario_empresa add foreign key fk_usuario_empresa_usuario (id_usuario) references usuario(id);

create table usuario_sede(
	id INT  not null auto_increment primary key,
	uuid CHAR(36) NOT NULL DEFAULT (UUID()),
    id_usuario int not null,
    id_sede int not null,
    activo TINYINT(1) not null default(1) ,
    id_usuario_creacion int not null default(1),
    id_usuario_modificacion int not null default(1),
    fecha_creacion datetime not null default now(),
    fecha_modificacion datetime not null default now()
);

CREATE TABLE usuario_sede_historico (
    id INT NOT NULL,
    uuid CHAR(36),
    id_usuario int  null,
    id_sede int  null,
    activo TINYINT(1),
    id_usuario_creacion INT,
    id_usuario_modificacion INT,
    fecha_creacion DATETIME,
    fecha_modificacion DATETIME,

    REV INT NOT NULL,
    REVTYPE TINYINT NOT NULL,

    PRIMARY KEY (id, REV),
    CONSTRAINT usuario_sede_historico_rev FOREIGN KEY (REV) REFERENCES revision_info(rev)
);

alter table usuario_sede add foreign key fk_usuario_sede_usuario_creacion (id_usuario_creacion) references usuario(id);
alter table usuario_sede add foreign key fk_usuario_sede_modificacion (id_usuario_modificacion) references usuario(id);
alter table usuario_sede add foreign key fk_usuario_sede_sede(id_sede) references sede(id);
alter table usuario_sede add foreign key fk_usuario_sede_usuario(id_usuario) references usuario(id);


create table usuario_instalacion(
	id INT  not null auto_increment primary key,
	uuid CHAR(36) NOT NULL DEFAULT (UUID()),
    id_usuario int not null,
    id_instalacion int not null,
    activo TINYINT(1) not null default(1) ,
    id_usuario_creacion int not null default(1),
    id_usuario_modificacion int not null default(1),
    fecha_creacion datetime not null default now(),
    fecha_modificacion datetime not null default now()
);

CREATE TABLE usuario_instalacion_historico (
    id INT NOT NULL,
    uuid CHAR(36),
    id_usuario int  null,
    id_instalacion int  null,
    activo TINYINT(1),
    id_usuario_creacion INT,
    id_usuario_modificacion INT,
    fecha_creacion DATETIME,
    fecha_modificacion DATETIME,

    REV INT NOT NULL,
    REVTYPE TINYINT NOT NULL,

    PRIMARY KEY (id, REV),
    CONSTRAINT usuario_instalacion_historico_rev FOREIGN KEY (REV) REFERENCES revision_info(rev)
);


alter table usuario_instalacion add foreign key fk_usuario_instalacion_usuario_creacion (id_usuario_creacion) references usuario(id);
alter table usuario_instalacion add foreign key fk_usuario_instalacion_modificacion (id_usuario_modificacion) references usuario(id);
alter table usuario_instalacion add foreign key fk_usuario_instalacion_instalacion (id_instalacion) references instalacion(id);
alter table usuario_instalacion add foreign key fk_usuario_instalacion_usuario (id_usuario) references usuario(id);


create table instalacion_horario_bloqueado(
	id INT  not null auto_increment primary key,
	uuid CHAR(36) NOT NULL DEFAULT (UUID()),
    id_instalacion int not null,
    fecha DATE not null,
    hora_inicio TIME  null,
    hora_fin TIME  null,
    activo TINYINT(1) not null default(1) ,
    id_usuario_creacion int not null default(1),
    id_usuario_modificacion int not null default(1),
    fecha_creacion datetime not null default now(),
    fecha_modificacion datetime not null default now()
);


CREATE TABLE usuario_horario_bloqueado_historico (
    id INT NOT NULL,
    uuid CHAR(36),
    id_instalacion int  null,
    fecha DATE  null,
    hora_inicio TIME  null,
    hora_fin TIME  null,
    activo TINYINT(1),
    id_usuario_creacion INT,
    id_usuario_modificacion INT,
    fecha_creacion DATETIME,
    fecha_modificacion DATETIME,

    REV INT NOT NULL,
    REVTYPE TINYINT NOT NULL,

    PRIMARY KEY (id, REV),
    CONSTRAINT usuario_horario_bloqueado_historico_rev FOREIGN KEY (REV) REFERENCES revision_info(rev)
);

alter table instalacion_horario_bloqueado add foreign key fk_instalacion_horario_bloqueado_usuario_creacion (id_usuario_creacion) references usuario(id);
alter table instalacion_horario_bloqueado add foreign key fk_instalacion_horario_bloqueado_modificacion (id_usuario_modificacion) references usuario(id);
alter table instalacion_horario_bloqueado add foreign key fk_instalacion_horario_bloqueado_instalacion (id_instalacion) references instalacion(id);


create table sancion_tipo(
	id INT  not null auto_increment primary key,
	uuid CHAR(36) NOT NULL DEFAULT (UUID()),
    nombre varchar(255) not null ,
    activo TINYINT(1) not null default(1) ,
    id_usuario_creacion int not null default(1),
    id_usuario_modificacion int not null default(1),
    fecha_creacion datetime not null default now(),
    fecha_modificacion datetime not null default now()
);

alter table sancion_tipo add foreign key fk_sancion_tipo_usuario_creacion (id_usuario_creacion) references usuario(id);
alter table sancion_tipo add foreign key fk_sancion_tipo_usuario_modificacion (id_usuario_modificacion) references usuario(id);

CREATE TABLE sancion_tipo_historico (
    id INT NOT NULL,
    uuid CHAR(36),
    nombre varchar(255)  null ,
    activo TINYINT(1),
    id_usuario_creacion INT,
    id_usuario_modificacion INT,
    fecha_creacion DATETIME,
    fecha_modificacion DATETIME,

    REV INT NOT NULL,
    REVTYPE TINYINT NOT NULL,

    PRIMARY KEY (id, REV),
    CONSTRAINT sancion_tipo_historico_rev FOREIGN KEY (REV) REFERENCES revision_info(rev)
);

insert into sancion_tipo(nombre) values 
('FALTA DE RESPETO'),('DESPERFECTOS EN LAS INSTALACIONES'), ('NO COMPARECENCIA');

create table sancion(
	id INT  not null auto_increment primary key,
	uuid CHAR(36) NOT NULL DEFAULT (UUID()),
    id_usuario int not null,
    id_reserva int not null,
    id_sancion_tipo int not null,
    fecha_inicio DATE not null ,
    fecha_fin DATE not null,
    descripcion varchar(1250) null,
    activo TINYINT(1) not null default(1) ,
    id_usuario_creacion int not null default(1),
    id_usuario_modificacion int not null default(1),
    fecha_creacion datetime not null default now(),
    fecha_modificacion datetime not null default now()
);

CREATE TABLE sancion_historico (
    id INT NOT NULL,
    uuid CHAR(36),
    id_usuario int  null,
    id_reserva int  null,
    id_sancion_tipo int  null,
    fecha_inicio DATE  null ,
    fecha_fin DATE  null,
    descripcion varchar(1250) null,
    activo TINYINT(1),
    id_usuario_creacion INT,
    id_usuario_modificacion INT,
    fecha_creacion DATETIME,
    fecha_modificacion DATETIME,

    REV INT NOT NULL,
    REVTYPE TINYINT NOT NULL,

    PRIMARY KEY (id, REV),
    CONSTRAINT sancion_historico_rev FOREIGN KEY (REV) REFERENCES revision_info(rev)
);

alter table sancion add foreign key fk_sancion_usuario_creacion (id_usuario_creacion) references usuario(id);
alter table sancion add foreign key fk_sancion_usuario_modificacion (id_usuario_modificacion) references usuario(id);
alter table sancion add foreign key fk_sancion_usuario(id_usuario) references usuario(id);
alter table sancion add foreign key fk_sancion_reserva (id_reserva) references reserva(id);
alter table sancion add foreign key fk_sancion_tipo (id_sancion_tipo) references sancion_tipo(id);

