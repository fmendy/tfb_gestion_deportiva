/**************************************************************
***************************************************************
**************************************************************/
insert into empresa (nombre, email, cif, url, descripcion) values 
('VamosFIT','vamosfit@apm-tfb-calermany-2026.com','D85354041','www.vamosfit.tfb','Empresa líder del sector deportivo en la península');

insert into sede (id_empresa, id_municipio, direccion, latitud, longitud, nombre,  url, email, descripcion) values
((select id from empresa where nombre = 'VamosFIT'), (select id from municipio where nombre = 'Madrid'),'Calle San Agustin, 3, 28014 Madrid, España',
'40.41517748601217', '-3.697032734932634', 'VamosFIT Madrid','www.vamosfit_madird.tfb', 'vamosfit_madrid@apm-tfb-calermany-2026.com',
'Ahora en Madrid, VamosFIT Madrid');

insert into instalacion (id_sede, id_instalacion_tipo, nombre, descripcion) values 
((select id from sede where nombre = 'VamosFIT Madrid'), (select id from instalacion_tipo where nombre = 'PISTA PADEL'), 'Pista de Padel 1', 'Pista de padel cubierta' ),
((select id from sede where nombre = 'VamosFIT Madrid'), (select id from instalacion_tipo where nombre = 'PISTA PADEL'), 'Pista de Padel 2', 'Pista de padel cubierta' ),
((select id from sede where nombre = 'VamosFIT Madrid'), (select id from instalacion_tipo where nombre = 'PISTA PADEL'), 'Pista de Padel 3', 'Pista de padel cubierta' ),
((select id from sede where nombre = 'VamosFIT Madrid'), (select id from instalacion_tipo where nombre = 'PISTA PADEL'), 'Pista de Padel 4', 'Pista de padel cubierta' ),
((select id from sede where nombre = 'VamosFIT Madrid'), (select id from instalacion_tipo where nombre = 'PISCINA'), 'Piscina cubierta', 'Piscina de dimensiones olimpicas' );

insert into usuario(nombre, email, password) values
('Gestor Vamos Fit','gestor_vamosfit@apm-tfb-calermany-2026.com','$2a$10$DFFs70DvnosU6HFN1B0yv.zgeW/li41vxmvhr1B44dZNKptdCdcVu');
insert  into usuario_rol(id_usuario, id_rol)values
((select id from usuario where nombre = 'Gestor Vamos Fit'), (select id from rol where nombre = 'USUARIO_EMPRESA'));
insert into usuario_empresa (id_usuario, id_empresa) values 
((select id from usuario where nombre = 'Gestor Vamos Fit'),(select id from empresa where nombre = 'VamosFIT'));

insert into usuario(nombre, email, password) values
('Gestor Vamos Fit Madrid','gestor_vamosfit_madrid@apm-tfb-calermany-2026.com','$2a$10$DFFs70DvnosU6HFN1B0yv.zgeW/li41vxmvhr1B44dZNKptdCdcVu');
insert  into usuario_rol(id_usuario, id_rol)values
((select id from usuario where nombre = 'Gestor Vamos Fit Madrid'), (select id from rol where nombre = 'USUARIO_SEDE'));

insert into usuario_sede (id_usuario, id_sede) values 
((select id from usuario where nombre = 'Gestor Vamos Fit Madrid'),(select id from sede where nombre = 'VamosFIT Madrid'));

insert into usuario(nombre, email, password) values
('Gestor Vamos Fit Madrid Padel','gestor_vamosfit_madrid_padel@apm-tfb-calermany-2026.com','$2a$10$DFFs70DvnosU6HFN1B0yv.zgeW/li41vxmvhr1B44dZNKptdCdcVu');
insert  into usuario_rol(id_usuario, id_rol)values
((select id from usuario where nombre = 'Gestor Vamos Fit Madrid Padel'), (select id from rol where nombre = 'USUARIO_INSTALACION'));
insert into usuario_instalacion (id_usuario, id_instalacion) values 
((select id from usuario where nombre = 'Gestor Vamos Fit Madrid Padel'),
	(select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'VamosFIT Madrid' and i.nombre = 'Pista de Padel 1')),
((select id from usuario where nombre = 'Gestor Vamos Fit Madrid Padel'),
	(select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'VamosFIT Madrid' and i.nombre = 'Pista de Padel 2')),
((select id from usuario where nombre = 'Gestor Vamos Fit Madrid Padel'),
	(select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'VamosFIT Madrid' and i.nombre = 'Pista de Padel 3')),
((select id from usuario where nombre = 'Gestor Vamos Fit Madrid Padel'),
	(select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'VamosFIT Madrid' and i.nombre = 'Pista de Padel 4'));

insert into usuario(nombre, email, password) values
('Gestor Vamos Fit Madrid Piscina','gestor_vamosfit_madrid_piscina@apm-tfb-calermany-2026.com','$2a$10$DFFs70DvnosU6HFN1B0yv.zgeW/li41vxmvhr1B44dZNKptdCdcVu');

insert into usuario_instalacion (id_usuario, id_instalacion) values 
((select id from usuario where nombre = 'Gestor Vamos Fit Madrid Piscina'),
	(select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'VamosFIT Madrid' and i.nombre = 'Piscina cubierta'));





insert into instalacion_configuracion_reserva (id_instalacion, duracion_min, duracion_max, intervalo_horario) values 
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'VamosFIT Madrid' and i.nombre = 'Pista de Padel 1'),60,90,15),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'VamosFIT Madrid' and i.nombre = 'Pista de Padel 2'),60,90,15),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'VamosFIT Madrid' and i.nombre = 'Pista de Padel 3'),60,90,15),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'VamosFIT Madrid' and i.nombre = 'Pista de Padel 4'),60,90,15),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'VamosFIT Madrid' and i.nombre = 'Piscina cubierta'),60,90,15);

insert into instalacion_horario(id_instalacion, dia_semana, hora_inicio, hora_fin) values 
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'VamosFIT Madrid' and i.nombre = 'Pista de Padel 1'),1,'08:00:00','22:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'VamosFIT Madrid' and i.nombre = 'Pista de Padel 1'),2,'08:00:00','22:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'VamosFIT Madrid' and i.nombre = 'Pista de Padel 1'),3,'08:00:00','22:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'VamosFIT Madrid' and i.nombre = 'Pista de Padel 1'),4,'08:00:00','22:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'VamosFIT Madrid' and i.nombre = 'Pista de Padel 1'),5,'08:00:00','22:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'VamosFIT Madrid' and i.nombre = 'Pista de Padel 1'),6,'10:00:00','20:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'VamosFIT Madrid' and i.nombre = 'Pista de Padel 1'),7,'10:00:00','14:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'VamosFIT Madrid' and i.nombre = 'Pista de Padel 2'),1,'08:00:00','22:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'VamosFIT Madrid' and i.nombre = 'Pista de Padel 2'),2,'08:00:00','22:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'VamosFIT Madrid' and i.nombre = 'Pista de Padel 2'),3,'08:00:00','22:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'VamosFIT Madrid' and i.nombre = 'Pista de Padel 2'),4,'08:00:00','22:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'VamosFIT Madrid' and i.nombre = 'Pista de Padel 2'),5,'08:00:00','22:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'VamosFIT Madrid' and i.nombre = 'Pista de Padel 2'),6,'10:00:00','20:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'VamosFIT Madrid' and i.nombre = 'Pista de Padel 2'),7,'10:00:00','14:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'VamosFIT Madrid' and i.nombre = 'Pista de Padel 3'),1,'08:00:00','22:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'VamosFIT Madrid' and i.nombre = 'Pista de Padel 3'),2,'08:00:00','22:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'VamosFIT Madrid' and i.nombre = 'Pista de Padel 3'),3,'08:00:00','22:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'VamosFIT Madrid' and i.nombre = 'Pista de Padel 3'),4,'08:00:00','22:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'VamosFIT Madrid' and i.nombre = 'Pista de Padel 3'),5,'08:00:00','22:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'VamosFIT Madrid' and i.nombre = 'Pista de Padel 3'),6,'10:00:00','20:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'VamosFIT Madrid' and i.nombre = 'Pista de Padel 3'),7,'10:00:00','14:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'VamosFIT Madrid' and i.nombre = 'Pista de Padel 4'),1,'08:00:00','22:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'VamosFIT Madrid' and i.nombre = 'Pista de Padel 4'),2,'08:00:00','22:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'VamosFIT Madrid' and i.nombre = 'Pista de Padel 4'),3,'08:00:00','22:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'VamosFIT Madrid' and i.nombre = 'Pista de Padel 4'),4,'08:00:00','22:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'VamosFIT Madrid' and i.nombre = 'Pista de Padel 4'),5,'08:00:00','22:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'VamosFIT Madrid' and i.nombre = 'Pista de Padel 4'),6,'10:00:00','20:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'VamosFIT Madrid' and i.nombre = 'Pista de Padel 4'),7,'10:00:00','14:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'VamosFIT Madrid' and i.nombre = 'Piscina cubierta'),1,'08:00:00','22:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'VamosFIT Madrid' and i.nombre = 'Piscina cubierta'),2,'08:00:00','22:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'VamosFIT Madrid' and i.nombre = 'Piscina cubierta'),3,'08:00:00','22:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'VamosFIT Madrid' and i.nombre = 'Piscina cubierta'),4,'08:00:00','22:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'VamosFIT Madrid' and i.nombre = 'Piscina cubierta'),5,'08:00:00','22:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'VamosFIT Madrid' and i.nombre = 'Piscina cubierta'),6,'10:00:00','20:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'VamosFIT Madrid' and i.nombre = 'Piscina cubierta'),7,'10:00:00','14:00:00');
/**************************************************************
***************************************************************
**************************************************************/
insert into empresa (nombre, email, cif, url, descripcion) values 
('Ayuntamiento Siero','ayutamiento_siero@apm-tfb-calermany-2026.com','J76037514','www.siero.tfb','Tus instalaciones deportivas municipales en el concejo de Siero');
insert into sede (id_empresa, id_municipio, direccion, latitud, longitud, nombre,  url, email, descripcion) values
((select id from empresa where nombre = 'Ayuntamiento Siero'), (select id from municipio where nombre = 'Siero'),'Polideportivo de Pola de Siero, Carretera de San Sebastián a Santiago de Compostela, 33519 Siero, España',
'43.39297152851873', '-5.66033244745077', 'Complejo Municipal de Pola de Siero','www.siero.tfb', 'siero_pola_siero@apm-tfb-calermany-2026.com',
'Instalaciones Municipales del Ayuntamiento de Siero en la Localidad de Pola de Siero');



insert into instalacion (id_sede, id_instalacion_tipo, nombre, descripcion) values 
((select id from sede where nombre = 'Complejo Municipal de Pola de Siero'), (select id from instalacion_tipo where nombre = 'PISTA PADEL'), 'Pista de Padel 1', 'Pista de padel cubierta' ),
((select id from sede where nombre = 'Complejo Municipal de Pola de Siero'), (select id from instalacion_tipo where nombre = 'PISTA PADEL'), 'Pista de Padel 2', 'Pista de padel cubierta' ),
((select id from sede where nombre = 'Complejo Municipal de Pola de Siero'), (select id from instalacion_tipo where nombre = 'PISCINA'), 'Piscina cubierta', 'Piscina cubierta' ),
((select id from sede where nombre = 'Complejo Municipal de Pola de Siero'), (select id from instalacion_tipo where nombre = 'PISCINA'), 'Piscina aire libre', 'Piscina al aire libre' ),
((select id from sede where nombre = 'Complejo Municipal de Pola de Siero'), (select id from instalacion_tipo where nombre = 'GIMNASIO'), 'Gimanasio', '' ),
((select id from sede where nombre = 'Complejo Municipal de Pola de Siero'), (select id from instalacion_tipo where nombre = 'PISTA POLIDEPORTIVA'), 'Pista', 'Cancha deportiva de usos multiples' );


insert into usuario(nombre, email, password) values
('Gestor Ayuntamiento Siero','gestor_ayuntamientosiero@apm-tfb-calermany-2026.com','$2a$10$DFFs70DvnosU6HFN1B0yv.zgeW/li41vxmvhr1B44dZNKptdCdcVu');
insert  into usuario_rol(id_usuario, id_rol)values
((select id from usuario where nombre = 'Gestor Ayuntamiento Siero'), (select id from rol where nombre = 'USUARIO_EMPRESA'));
insert into usuario_empresa (id_usuario, id_empresa) values 
((select id from usuario where nombre = 'Gestor Ayuntamiento Siero'),(select id from empresa where nombre = 'Ayuntamiento Siero'));


insert into usuario(nombre, email, password) values
('Gestor Ayuntamiento Siero Pola de Siero','gestor_ayuntamientosiero_polasiero@apm-tfb-calermany-2026.com','$2a$10$DFFs70DvnosU6HFN1B0yv.zgeW/li41vxmvhr1B44dZNKptdCdcVu');
insert  into usuario_rol(id_usuario, id_rol)values
((select id from usuario where nombre = 'Gestor Ayuntamiento Siero Pola de Siero'), (select id from rol where nombre = 'USUARIO_SEDE'));

insert into usuario_sede (id_usuario, id_sede) values 
((select id from usuario where nombre = 'Gestor Ayuntamiento Siero Pola de Siero'),(select id from sede where nombre = 'Complejo Municipal de Pola de Siero'));

insert into usuario(nombre, email, password) values
('Gestor Ayuntamiento Siero Pola de Siero Padel','gestor_ayuntamientosiero_polasiero_padel@apm-tfb-calermany-2026.com','$2a$10$DFFs70DvnosU6HFN1B0yv.zgeW/li41vxmvhr1B44dZNKptdCdcVu');

insert  into usuario_rol(id_usuario, id_rol)values
((select id from usuario where nombre = 'Gestor Ayuntamiento Siero Pola de Siero Padel'), (select id from rol where nombre = 'USUARIO_INSTALACION'));

insert into usuario_instalacion (id_usuario, id_instalacion) values 
((select id from usuario where nombre = 'Gestor Ayuntamiento Siero Pola de Siero Padel'),
	(select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Complejo Municipal de Pola de Siero' and i.nombre = 'Pista de Padel 1')),
((select id from usuario where nombre = 'Gestor Ayuntamiento Siero Pola de Siero Padel'),
	(select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Complejo Municipal de Pola de Siero' and i.nombre = 'Pista de Padel 2'));



insert into usuario(nombre, email, password) values
('Gestor Ayuntamiento Siero Pola de Siero Piscina','gestor_ayuntamientosiero_polasiero_piscina@apm-tfb-calermany-2026.com','$2a$10$DFFs70DvnosU6HFN1B0yv.zgeW/li41vxmvhr1B44dZNKptdCdcVu');

insert  into usuario_rol(id_usuario, id_rol)values
((select id from usuario where nombre = 'Gestor Ayuntamiento Siero Pola de Siero Piscina'), (select id from rol where nombre = 'USUARIO_INSTALACION'));

insert into usuario_instalacion (id_usuario, id_instalacion) values 
((select id from usuario where nombre = 'Gestor Ayuntamiento Siero Pola de Siero Piscina'),
	(select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Complejo Municipal de Pola de Siero' and i.nombre = 'Piscina cubierta')),
((select id from usuario where nombre = 'Gestor Ayuntamiento Siero Pola de Siero Piscina'),
	(select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Complejo Municipal de Pola de Siero' and i.nombre = 'Piscina aire libre'));



insert into instalacion_horario(id_instalacion, dia_semana, hora_inicio, hora_fin) values 
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Complejo Municipal de Pola de Siero' and i.nombre = 'Pista de Padel 1'),1,'10:00:00','20:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Complejo Municipal de Pola de Siero' and i.nombre = 'Pista de Padel 1'),2,'10:00:00','20:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Complejo Municipal de Pola de Siero' and i.nombre = 'Pista de Padel 1'),3,'10:00:00','20:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Complejo Municipal de Pola de Siero' and i.nombre = 'Pista de Padel 1'),4,'10:00:00','20:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Complejo Municipal de Pola de Siero' and i.nombre = 'Pista de Padel 1'),5,'10:00:00','20:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Complejo Municipal de Pola de Siero' and i.nombre = 'Pista de Padel 1'),6,'10:00:00','20:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Complejo Municipal de Pola de Siero' and i.nombre = 'Pista de Padel 1'),7,'08:00:00','14:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Complejo Municipal de Pola de Siero' and i.nombre = 'Pista de Padel 2'),1,'10:00:00','20:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Complejo Municipal de Pola de Siero' and i.nombre = 'Pista de Padel 2'),2,'10:00:00','20:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Complejo Municipal de Pola de Siero' and i.nombre = 'Pista de Padel 2'),3,'10:00:00','20:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Complejo Municipal de Pola de Siero' and i.nombre = 'Pista de Padel 2'),4,'10:00:00','20:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Complejo Municipal de Pola de Siero' and i.nombre = 'Pista de Padel 2'),5,'10:00:00','20:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Complejo Municipal de Pola de Siero' and i.nombre = 'Pista de Padel 2'),6,'10:00:00','20:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Complejo Municipal de Pola de Siero' and i.nombre = 'Pista de Padel 2'),7,'08:00:00','14:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Complejo Municipal de Pola de Siero' and i.nombre = 'Piscina cubierta'),1,'10:00:00','20:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Complejo Municipal de Pola de Siero' and i.nombre = 'Piscina cubierta'),2,'10:00:00','20:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Complejo Municipal de Pola de Siero' and i.nombre = 'Piscina cubierta'),3,'10:00:00','20:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Complejo Municipal de Pola de Siero' and i.nombre = 'Piscina cubierta'),4,'10:00:00','20:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Complejo Municipal de Pola de Siero' and i.nombre = 'Piscina cubierta'),5,'10:00:00','20:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Complejo Municipal de Pola de Siero' and i.nombre = 'Piscina cubierta'),6,'10:00:00','20:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Complejo Municipal de Pola de Siero' and i.nombre = 'Piscina cubierta'),7,'08:00:00','14:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Complejo Municipal de Pola de Siero' and i.nombre = 'Piscina aire libre'),1,'10:00:00','20:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Complejo Municipal de Pola de Siero' and i.nombre = 'Piscina aire libre'),2,'10:00:00','20:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Complejo Municipal de Pola de Siero' and i.nombre = 'Piscina aire libre'),3,'10:00:00','20:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Complejo Municipal de Pola de Siero' and i.nombre = 'Piscina aire libre'),4,'10:00:00','20:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Complejo Municipal de Pola de Siero' and i.nombre = 'Piscina aire libre'),5,'10:00:00','20:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Complejo Municipal de Pola de Siero' and i.nombre = 'Piscina aire libre'),6,'10:00:00','20:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Complejo Municipal de Pola de Siero' and i.nombre = 'Piscina aire libre'),7,'08:00:00','14:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Complejo Municipal de Pola de Siero' and i.nombre = 'Gimanasio'),1,'10:00:00','20:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Complejo Municipal de Pola de Siero' and i.nombre = 'Gimanasio'),2,'10:00:00','20:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Complejo Municipal de Pola de Siero' and i.nombre = 'Gimanasio'),3,'10:00:00','20:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Complejo Municipal de Pola de Siero' and i.nombre = 'Gimanasio'),4,'10:00:00','20:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Complejo Municipal de Pola de Siero' and i.nombre = 'Gimanasio'),5,'10:00:00','20:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Complejo Municipal de Pola de Siero' and i.nombre = 'Gimanasio'),6,'10:00:00','20:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Complejo Municipal de Pola de Siero' and i.nombre = 'Gimanasio'),7,'08:00:00','14:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Complejo Municipal de Pola de Siero' and i.nombre = 'Pista'),1,'10:00:00','20:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Complejo Municipal de Pola de Siero' and i.nombre = 'Pista'),2,'10:00:00','20:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Complejo Municipal de Pola de Siero' and i.nombre = 'Pista'),3,'10:00:00','20:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Complejo Municipal de Pola de Siero' and i.nombre = 'Pista'),4,'10:00:00','20:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Complejo Municipal de Pola de Siero' and i.nombre = 'Pista'),5,'10:00:00','20:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Complejo Municipal de Pola de Siero' and i.nombre = 'Pista'),6,'10:00:00','20:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Complejo Municipal de Pola de Siero' and i.nombre = 'Pista'),7,'08:00:00','14:00:00');


insert into instalacion_configuracion_reserva (id_instalacion, duracion_min, duracion_max, intervalo_horario) values 
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Complejo Municipal de Pola de Siero' and i.nombre = 'Pista de Padel 1'),60,90,15),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Complejo Municipal de Pola de Siero' and i.nombre = 'Pista de Padel 2'),60,90,15),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Complejo Municipal de Pola de Siero' and i.nombre = 'Piscina cubierta'),15,45,15),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Complejo Municipal de Pola de Siero' and i.nombre = 'Piscina aire libre'),15,45,15),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Complejo Municipal de Pola de Siero' and i.nombre = 'Pista'),60,60,30),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Complejo Municipal de Pola de Siero' and i.nombre = 'Gimanasio'),30,60,15);


insert into sede (id_empresa, id_municipio, direccion, latitud, longitud, nombre,  url, email, descripcion) values
((select id from empresa where nombre = 'Ayuntamiento Siero'), (select id from municipio where nombre = 'Siero'),'Polideportivo de El Berrón, Avenida de Los Campones, 33186 Siero, España',
'43.38360686398542', '-5.70787393390313', 'Complejo Municipal de El Berrón','www.siero.tfb', 'siero_el_berron@apm-tfb-calermany-2026.com',
'Instalaciones Municipales del Ayuntamiento de Siero en la Localidad de El Berrón');

insert into instalacion (id_sede, id_instalacion_tipo, nombre, descripcion) values 
((select id from sede where nombre = 'Complejo Municipal de El Berrón'), (select id from instalacion_tipo where nombre = 'SAUNA'), 'Sauna', '' ),
((select id from sede where nombre = 'Complejo Municipal de El Berrón'), (select id from instalacion_tipo where nombre = 'GIMNASIO'), 'Gimanasio', '' ),
((select id from sede where nombre = 'Complejo Municipal de El Berrón'), (select id from instalacion_tipo where nombre = 'PISTA POLIDEPORTIVA'), 'Pista', 'Cancha deportiva de usos multiples' );



insert into usuario(nombre, email, password) values
('Gestor Ayuntamiento Siero El Berrón','gestor_ayuntamientosiero_elberron@apm-tfb-calermany-2026.com','$2a$10$DFFs70DvnosU6HFN1B0yv.zgeW/li41vxmvhr1B44dZNKptdCdcVu');
insert  into usuario_rol(id_usuario, id_rol)values
((select id from usuario where nombre = 'Gestor Ayuntamiento Siero El Berrón'), (select id from rol where nombre = 'USUARIO_SEDE'));

insert into usuario_sede (id_usuario, id_sede) values 
((select id from usuario where nombre = 'Gestor Ayuntamiento Siero El Berrón'),(select id from sede where nombre = 'Complejo Municipal de El Berrón'));




insert into instalacion_configuracion_reserva (id_instalacion, duracion_min, duracion_max, intervalo_horario) values 
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Complejo Municipal de El Berrón' and i.nombre = 'Sauna'),20,60,20),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Complejo Municipal de El Berrón' and i.nombre = 'Gimanasio'),15,60,15),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Complejo Municipal de El Berrón' and i.nombre = 'Pista'),60,120,30);

insert into instalacion_horario(id_instalacion, dia_semana, hora_inicio, hora_fin) values 
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Complejo Municipal de El Berrón' and i.nombre = 'Sauna'),1,'10:00:00','20:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Complejo Municipal de El Berrón' and i.nombre = 'Sauna'),2,'10:00:00','20:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Complejo Municipal de El Berrón' and i.nombre = 'Sauna'),3,'10:00:00','20:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Complejo Municipal de El Berrón' and i.nombre = 'Sauna'),4,'10:00:00','20:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Complejo Municipal de El Berrón' and i.nombre = 'Sauna'),5,'10:00:00','20:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Complejo Municipal de El Berrón' and i.nombre = 'Sauna'),6,'10:00:00','20:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Complejo Municipal de El Berrón' and i.nombre = 'Sauna'),7,'08:00:00','14:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Complejo Municipal de El Berrón' and i.nombre = 'Gimanasio'),1,'10:00:00','20:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Complejo Municipal de El Berrón' and i.nombre = 'Gimanasio'),2,'10:00:00','20:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Complejo Municipal de El Berrón' and i.nombre = 'Gimanasio'),3,'10:00:00','20:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Complejo Municipal de El Berrón' and i.nombre = 'Gimanasio'),4,'10:00:00','20:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Complejo Municipal de El Berrón' and i.nombre = 'Gimanasio'),5,'10:00:00','20:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Complejo Municipal de El Berrón' and i.nombre = 'Gimanasio'),6,'10:00:00','20:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Complejo Municipal de El Berrón' and i.nombre = 'Gimanasio'),7,'08:00:00','14:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Complejo Municipal de El Berrón' and i.nombre = 'Pista'),1,'10:00:00','20:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Complejo Municipal de El Berrón' and i.nombre = 'Pista'),2,'10:00:00','20:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Complejo Municipal de El Berrón' and i.nombre = 'Pista'),3,'10:00:00','20:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Complejo Municipal de El Berrón' and i.nombre = 'Pista'),4,'10:00:00','20:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Complejo Municipal de El Berrón' and i.nombre = 'Pista'),5,'10:00:00','20:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Complejo Municipal de El Berrón' and i.nombre = 'Pista'),6,'10:00:00','20:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Complejo Municipal de El Berrón' and i.nombre = 'Pista'),7,'08:00:00','14:00:00');


insert into sede (id_empresa, id_municipio, direccion, latitud, longitud, nombre,  url, email, descripcion) values
((select id from empresa where nombre = 'Ayuntamiento Siero'), (select id from municipio where nombre = 'Siero'),'Pisicina Climatizada de Lugones, Avenida del Conde de Santa Bárbara, 33420 Siero, España',
'43.406093317049994', '-5.811088930392856', 'Complejo Municipal de Lugones','www.siero.tfb', 'siero_siero@apm-tfb-calermany-2026.com',
'Instalaciones Municipales del Ayuntamiento de Siero en la Localidad de Lugones');

insert into usuario(nombre, email, password) values
('Gestor Ayuntamiento Siero Lugones','gestor_ayuntamientosiero_lugones@apm-tfb-calermany-2026.com','$2a$10$DFFs70DvnosU6HFN1B0yv.zgeW/li41vxmvhr1B44dZNKptdCdcVu');
insert  into usuario_rol(id_usuario, id_rol)values
((select id from usuario where nombre = 'Gestor Ayuntamiento Siero Lugones'), (select id from rol where nombre = 'USUARIO_SEDE'));

insert into usuario_sede (id_usuario, id_sede) values 
((select id from usuario where nombre = 'Gestor Ayuntamiento Siero Lugones'),(select id from sede where nombre = 'Complejo Municipal de Lugones'));

insert into instalacion (id_sede, id_instalacion_tipo, nombre, descripcion) values 
((select id from sede where nombre = 'Complejo Municipal de Lugones'), (select id from instalacion_tipo where nombre = 'PISTA PADEL'), 'Pista de Padel 1', 'Pista cubierta' ),
((select id from sede where nombre = 'Complejo Municipal de Lugones'), (select id from instalacion_tipo where nombre = 'PISTA PADEL'), 'Pista de Padel 2', 'Pista cubierta' ),
((select id from sede where nombre = 'Complejo Municipal de Lugones'), (select id from instalacion_tipo where nombre = 'PISTA PADEL'), 'Pista de Padel 3', 'Pista descubierta' ),
((select id from sede where nombre = 'Complejo Municipal de Lugones'), (select id from instalacion_tipo where nombre = 'PISTA PADEL'), 'Pista de Padel 4', 'Pista descubierta' ),
((select id from sede where nombre = 'Complejo Municipal de Lugones'), (select id from instalacion_tipo where nombre = 'PISTA DE TENIS'), 'Pista de Tenis 1', 'Pista descubierta' ),
((select id from sede where nombre = 'Complejo Municipal de Lugones'), (select id from instalacion_tipo where nombre = 'SAUNA'), 'Sauna', '' ),
((select id from sede where nombre = 'Complejo Municipal de Lugones'), (select id from instalacion_tipo where nombre = 'PISCINA'), 'Piscina cubierta', 'Piscina cubierta' ),
((select id from sede where nombre = 'Complejo Municipal de Lugones'), (select id from instalacion_tipo where nombre = 'PISCINA'), 'Piscina aire libre', 'Piscina al aire libre' ),
((select id from sede where nombre = 'Complejo Municipal de Lugones'), (select id from instalacion_tipo where nombre = 'GIMNASIO'), 'Gimanasio', '' ),
((select id from sede where nombre = 'Complejo Municipal de Lugones'), (select id from instalacion_tipo where nombre = 'PISTA POLIDEPORTIVA'), 'Pista', 'Cancha deportiva de usos multiples' );

insert into instalacion_configuracion_reserva (id_instalacion, duracion_min, duracion_max, intervalo_horario) values 
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Complejo Municipal de Lugones' and i.nombre = 'Pista de Padel 1'),60,90,15),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Complejo Municipal de Lugones' and i.nombre = 'Pista de Padel 2'),60,90,15),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Complejo Municipal de Lugones' and i.nombre = 'Pista de Padel 3'),60,90,15),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Complejo Municipal de Lugones' and i.nombre = 'Pista de Padel 4'),60,90,15),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Complejo Municipal de Lugones' and i.nombre = 'Pista de Tenis 1'),60,90,15),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Complejo Municipal de Lugones' and i.nombre = 'Piscina cubierta'),20,60,20),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Complejo Municipal de Lugones' and i.nombre = 'Piscina aire libre'),20,60,20),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Complejo Municipal de Lugones' and i.nombre = 'Sauna'),20,60,20),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Complejo Municipal de Lugones' and i.nombre = 'Gimanasio'),15,60,15),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Complejo Municipal de Lugones' and i.nombre = 'Pista'),60,120,30);

insert into instalacion_horario(id_instalacion, dia_semana, hora_inicio, hora_fin) values 
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Complejo Municipal de Lugones' and i.nombre = 'Pista de Padel 1'),1,'10:00:00','20:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Complejo Municipal de Lugones' and i.nombre = 'Pista de Padel 1'),2,'10:00:00','20:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Complejo Municipal de Lugones' and i.nombre = 'Pista de Padel 1'),3,'10:00:00','20:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Complejo Municipal de Lugones' and i.nombre = 'Pista de Padel 1'),4,'10:00:00','20:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Complejo Municipal de Lugones' and i.nombre = 'Pista de Padel 1'),5,'10:00:00','20:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Complejo Municipal de Lugones' and i.nombre = 'Pista de Padel 1'),6,'10:00:00','20:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Complejo Municipal de Lugones' and i.nombre = 'Pista de Padel 1'),7,'08:00:00','14:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Complejo Municipal de Lugones' and i.nombre = 'Pista de Padel 2'),1,'10:00:00','20:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Complejo Municipal de Lugones' and i.nombre = 'Pista de Padel 2'),2,'10:00:00','20:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Complejo Municipal de Lugones' and i.nombre = 'Pista de Padel 2'),3,'10:00:00','20:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Complejo Municipal de Lugones' and i.nombre = 'Pista de Padel 2'),4,'10:00:00','20:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Complejo Municipal de Lugones' and i.nombre = 'Pista de Padel 2'),5,'10:00:00','20:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Complejo Municipal de Lugones' and i.nombre = 'Pista de Padel 2'),6,'10:00:00','20:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Complejo Municipal de Lugones' and i.nombre = 'Pista de Padel 2'),7,'08:00:00','14:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Complejo Municipal de Lugones' and i.nombre = 'Pista de Padel 3'),1,'10:00:00','20:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Complejo Municipal de Lugones' and i.nombre = 'Pista de Padel 3'),2,'10:00:00','20:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Complejo Municipal de Lugones' and i.nombre = 'Pista de Padel 3'),3,'10:00:00','20:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Complejo Municipal de Lugones' and i.nombre = 'Pista de Padel 3'),4,'10:00:00','20:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Complejo Municipal de Lugones' and i.nombre = 'Pista de Padel 3'),5,'10:00:00','20:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Complejo Municipal de Lugones' and i.nombre = 'Pista de Padel 3'),6,'10:00:00','20:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Complejo Municipal de Lugones' and i.nombre = 'Pista de Padel 3'),7,'08:00:00','14:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Complejo Municipal de Lugones' and i.nombre = 'Pista de Padel 4'),1,'10:00:00','20:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Complejo Municipal de Lugones' and i.nombre = 'Pista de Padel 4'),2,'10:00:00','20:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Complejo Municipal de Lugones' and i.nombre = 'Pista de Padel 4'),3,'10:00:00','20:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Complejo Municipal de Lugones' and i.nombre = 'Pista de Padel 4'),4,'10:00:00','20:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Complejo Municipal de Lugones' and i.nombre = 'Pista de Padel 4'),5,'10:00:00','20:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Complejo Municipal de Lugones' and i.nombre = 'Pista de Padel 4'),6,'10:00:00','20:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Complejo Municipal de Lugones' and i.nombre = 'Pista de Padel 4'),7,'08:00:00','14:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Complejo Municipal de Lugones' and i.nombre = 'Pista de Tenis 1'),1,'10:00:00','20:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Complejo Municipal de Lugones' and i.nombre = 'Pista de Tenis 1'),2,'10:00:00','20:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Complejo Municipal de Lugones' and i.nombre = 'Pista de Tenis 1'),3,'10:00:00','20:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Complejo Municipal de Lugones' and i.nombre = 'Pista de Tenis 1'),4,'10:00:00','20:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Complejo Municipal de Lugones' and i.nombre = 'Pista de Tenis 1'),5,'10:00:00','20:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Complejo Municipal de Lugones' and i.nombre = 'Pista de Tenis 1'),6,'10:00:00','20:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Complejo Municipal de Lugones' and i.nombre = 'Piscina aire libre'),3,'10:00:00','18:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Complejo Municipal de Lugones' and i.nombre = 'Piscina aire libre'),4,'10:00:00','18:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Complejo Municipal de Lugones' and i.nombre = 'Piscina aire libre'),5,'10:00:00','18:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Complejo Municipal de Lugones' and i.nombre = 'Piscina aire libre'),6,'10:00:00','18:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Complejo Municipal de Lugones' and i.nombre = 'Piscina aire libre'),7,'10:00:00','14:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Complejo Municipal de Lugones' and i.nombre = 'Piscina cubierta'),3,'10:00:00','18:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Complejo Municipal de Lugones' and i.nombre = 'Piscina cubierta'),4,'10:00:00','18:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Complejo Municipal de Lugones' and i.nombre = 'Piscina cubierta'),5,'10:00:00','18:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Complejo Municipal de Lugones' and i.nombre = 'Piscina cubierta'),6,'10:00:00','18:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Complejo Municipal de Lugones' and i.nombre = 'Piscina cubierta'),7,'10:00:00','14:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Complejo Municipal de Lugones' and i.nombre = 'Sauna'),1,'10:00:00','20:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Complejo Municipal de Lugones' and i.nombre = 'Sauna'),2,'10:00:00','20:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Complejo Municipal de Lugones' and i.nombre = 'Sauna'),3,'10:00:00','20:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Complejo Municipal de Lugones' and i.nombre = 'Sauna'),4,'10:00:00','20:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Complejo Municipal de Lugones' and i.nombre = 'Sauna'),5,'10:00:00','20:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Complejo Municipal de Lugones' and i.nombre = 'Sauna'),6,'10:00:00','20:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Complejo Municipal de Lugones' and i.nombre = 'Sauna'),7,'08:00:00','14:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Complejo Municipal de Lugones' and i.nombre = 'Gimanasio'),1,'10:00:00','20:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Complejo Municipal de Lugones' and i.nombre = 'Gimanasio'),2,'10:00:00','20:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Complejo Municipal de Lugones' and i.nombre = 'Gimanasio'),3,'10:00:00','20:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Complejo Municipal de Lugones' and i.nombre = 'Gimanasio'),4,'10:00:00','20:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Complejo Municipal de Lugones' and i.nombre = 'Gimanasio'),5,'10:00:00','20:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Complejo Municipal de Lugones' and i.nombre = 'Gimanasio'),6,'10:00:00','20:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Complejo Municipal de Lugones' and i.nombre = 'Gimanasio'),7,'08:00:00','14:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Complejo Municipal de Lugones' and i.nombre = 'Pista'),1,'10:00:00','20:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Complejo Municipal de Lugones' and i.nombre = 'Pista'),2,'10:00:00','20:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Complejo Municipal de Lugones' and i.nombre = 'Pista'),3,'10:00:00','20:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Complejo Municipal de Lugones' and i.nombre = 'Pista'),4,'10:00:00','20:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Complejo Municipal de Lugones' and i.nombre = 'Pista'),5,'10:00:00','20:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Complejo Municipal de Lugones' and i.nombre = 'Pista'),6,'10:00:00','20:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Complejo Municipal de Lugones' and i.nombre = 'Pista'),7,'08:00:00','14:00:00');





/**************************************************************
***************************************************************
**************************************************************/
insert into empresa (nombre, email, cif, url, descripcion) values 
('BajaFIT','baja_fit@apm-tfb-calermany-2026.com','D85758332','www.baja_fit.tfb','Baja Fit, nuevo concepto de bienestar, baja de peso con nosotros');
insert into sede (id_empresa, id_municipio, direccion, latitud, longitud, nombre,  url, email, descripcion) values
((select id from empresa where nombre = 'BajaFIT'), (select id from municipio where nombre = 'Barcelona'),
'Plaça de Nemesi Ponsati, 08004 Barcelona, España',
'41.36455578179243', '2.15576266674438', 'Campo de Montjuict','www.baja_fit.tfb', 'baja_fit_barcelona@apm-tfb-calermany-2026.com',
'En pleno centro de Barcelona, la ciudad de Gaudí, tu centro de entrenamiento');

insert into usuario(nombre, email, password) values
('Gestor Baja Fit','gestor_bajafit@apm-tfb-calermany-2026.com','$2a$10$DFFs70DvnosU6HFN1B0yv.zgeW/li41vxmvhr1B44dZNKptdCdcVu');
insert  into usuario_rol(id_usuario, id_rol)values
((select id from usuario where nombre = 'Gestor Baja Fit'), (select id from rol where nombre = 'USUARIO_EMPRESA'));
insert into usuario_empresa (id_usuario, id_empresa) values 
((select id from usuario where nombre = 'Gestor Baja Fit'),(select id from empresa where nombre = 'BajaFIT'));


insert into usuario(nombre, email, password) values
('Gestor Baja Fit Campo de Montjuict','gestor_bajafit_campomontjuict@apm-tfb-calermany-2026.com','$2a$10$DFFs70DvnosU6HFN1B0yv.zgeW/li41vxmvhr1B44dZNKptdCdcVu');
insert  into usuario_rol(id_usuario, id_rol)values
((select id from usuario where nombre = 'Gestor Baja Fit Campo de Montjuict'), (select id from rol where nombre = 'USUARIO_SEDE'));
insert into usuario_sede (id_usuario, id_sede) values 
((select id from usuario where nombre = 'Gestor Baja Fit Campo de Montjuict'),(select id from sede where nombre = 'Campo de Montjuict'));


insert into instalacion (id_sede, id_instalacion_tipo, nombre, descripcion) values 
((select id from sede where nombre = 'Campo de Montjuict'), (select id from instalacion_tipo where nombre = 'PISTA PADEL'), 'Pista de Padel 1', 'Pista cubierta' ),
((select id from sede where nombre = 'Campo de Montjuict'), (select id from instalacion_tipo where nombre = 'PISTA PADEL'), 'Pista de Padel 2', 'Pista cubierta' ),
((select id from sede where nombre = 'Campo de Montjuict'), (select id from instalacion_tipo where nombre = 'PISTA DE TENIS'), 'Pista de Tenis 1', 'Pista descubierta' ),
((select id from sede where nombre = 'Campo de Montjuict'), (select id from instalacion_tipo where nombre = 'PISTA DE TENIS'), 'Pista de Tenis 2', 'Pista descubierta' ),
((select id from sede where nombre = 'Campo de Montjuict'), (select id from instalacion_tipo where nombre = 'SAUNA'), 'Sauna Masculina', '' ),
((select id from sede where nombre = 'Campo de Montjuict'), (select id from instalacion_tipo where nombre = 'SAUNA'), 'Sauna Femenina', '' ),
((select id from sede where nombre = 'Campo de Montjuict'), (select id from instalacion_tipo where nombre = 'GIMNASIO'), 'Gimnasio', '' );

insert into instalacion_configuracion_reserva (id_instalacion, duracion_min, duracion_max, intervalo_horario) values 
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Campo de Montjuict' and i.nombre = 'Pista de Padel 1'),60,90,15),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Campo de Montjuict' and i.nombre = 'Pista de Padel 2'),60,90,15),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Campo de Montjuict' and i.nombre = 'Pista de Tenis 1'),60,90,15),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Campo de Montjuict' and i.nombre = 'Pista de Tenis 2'),60,90,15),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Campo de Montjuict' and i.nombre = 'Sauna Masculina'),20,60,20),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Campo de Montjuict' and i.nombre = 'Sauna Femenina'),20,60,20),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Campo de Montjuict' and i.nombre = 'Gimnasio'),15,60,15);


insert into usuario(nombre, email, password) values
('Gestor Baja Fit Campo de Montjuict Padel','gestor_bajafit_campomontjuict_padel@apm-tfb-calermany-2026.com','$2a$10$DFFs70DvnosU6HFN1B0yv.zgeW/li41vxmvhr1B44dZNKptdCdcVu');

insert  into usuario_rol(id_usuario, id_rol)values
((select id from usuario where nombre = 'Gestor Baja Fit Campo de Montjuict Padel'), (select id from rol where nombre = 'USUARIO_INSTALACION'));

insert into usuario_instalacion (id_usuario, id_instalacion) values 
((select id from usuario where nombre = 'Gestor Baja Fit Campo de Montjuict Padel'),
	(select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Campo de Montjuict' and i.nombre = 'Pista de Padel 1')),
((select id from usuario where nombre = 'Gestor Baja Fit Campo de Montjuict Padel'),
	(select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Campo de Montjuict' and i.nombre = 'Pista de Padel 1'));
    
    
insert into usuario(nombre, email, password) values
('Gestor Baja Fit Campo de Montjuict Tenis','gestor_bajafit_campomontjuict_tenis@apm-tfb-calermany-2026.com','$2a$10$DFFs70DvnosU6HFN1B0yv.zgeW/li41vxmvhr1B44dZNKptdCdcVu');

insert  into usuario_rol(id_usuario, id_rol)values
((select id from usuario where nombre = 'Gestor Baja Fit Campo de Montjuict Tenis'), (select id from rol where nombre = 'USUARIO_INSTALACION'));

insert into usuario_instalacion (id_usuario, id_instalacion) values 
((select id from usuario where nombre = 'Gestor Baja Fit Campo de Montjuict Tenis'),
	(select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Campo de Montjuict' and i.nombre = 'Pista de Tenis 1')),
((select id from usuario where nombre = 'Gestor Baja Fit Campo de Montjuict Tenis'),
	(select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Campo de Montjuict' and i.nombre = 'Pista de Tenis 2'));
    
    
    
insert into usuario(nombre, email, password) values
('Gestor Baja Fit Campo de Montjuict Sauna','gestor_bajafit_campomontjuict_sauna@apm-tfb-calermany-2026.com','$2a$10$DFFs70DvnosU6HFN1B0yv.zgeW/li41vxmvhr1B44dZNKptdCdcVu');

insert  into usuario_rol(id_usuario, id_rol)values
((select id from usuario where nombre = 'Gestor Baja Fit Campo de Montjuict Sauna'), (select id from rol where nombre = 'USUARIO_INSTALACION'));

insert into usuario_instalacion (id_usuario, id_instalacion) values 
((select id from usuario where nombre = 'Gestor Baja Fit Campo de Montjuict Sauna'),
	(select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Campo de Montjuict' and i.nombre = 'Sauna Femenina')),
((select id from usuario where nombre = 'Gestor Baja Fit Campo de Montjuict Sauna'),
	(select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Campo de Montjuict' and i.nombre = 'Sauna Masculina'));
    
    


insert into instalacion_horario(id_instalacion, dia_semana, hora_inicio, hora_fin) values 
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Campo de Montjuict' and i.nombre = 'Pista de Padel 1'),1,'08:00:00','14:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Campo de Montjuict' and i.nombre = 'Pista de Padel 1'),1,'17:00:00','22:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Campo de Montjuict' and i.nombre = 'Pista de Padel 1'),2,'08:00:00','14:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Campo de Montjuict' and i.nombre = 'Pista de Padel 1'),2,'17:00:00','22:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Campo de Montjuict' and i.nombre = 'Pista de Padel 1'),3,'08:00:00','14:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Campo de Montjuict' and i.nombre = 'Pista de Padel 1'),3,'17:00:00','22:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Campo de Montjuict' and i.nombre = 'Pista de Padel 1'),4,'08:00:00','14:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Campo de Montjuict' and i.nombre = 'Pista de Padel 1'),4,'17:00:00','22:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Campo de Montjuict' and i.nombre = 'Pista de Padel 1'),5,'08:00:00','14:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Campo de Montjuict' and i.nombre = 'Pista de Padel 1'),5,'17:00:00','22:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Campo de Montjuict' and i.nombre = 'Pista de Padel 2'),1,'08:00:00','14:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Campo de Montjuict' and i.nombre = 'Pista de Padel 2'),1,'17:00:00','22:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Campo de Montjuict' and i.nombre = 'Pista de Padel 2'),2,'08:00:00','14:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Campo de Montjuict' and i.nombre = 'Pista de Padel 2'),2,'17:00:00','22:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Campo de Montjuict' and i.nombre = 'Pista de Padel 2'),3,'08:00:00','14:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Campo de Montjuict' and i.nombre = 'Pista de Padel 2'),3,'17:00:00','22:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Campo de Montjuict' and i.nombre = 'Pista de Padel 2'),4,'08:00:00','14:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Campo de Montjuict' and i.nombre = 'Pista de Padel 2'),4,'17:00:00','22:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Campo de Montjuict' and i.nombre = 'Pista de Padel 2'),5,'08:00:00','14:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Campo de Montjuict' and i.nombre = 'Pista de Padel 2'),5,'17:00:00','22:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Campo de Montjuict' and i.nombre = 'Pista de Tenis 1'),1,'08:00:00','14:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Campo de Montjuict' and i.nombre = 'Pista de Tenis 1'),1,'17:00:00','22:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Campo de Montjuict' and i.nombre = 'Pista de Tenis 1'),2,'08:00:00','14:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Campo de Montjuict' and i.nombre = 'Pista de Tenis 1'),2,'17:00:00','22:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Campo de Montjuict' and i.nombre = 'Pista de Tenis 1'),3,'08:00:00','14:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Campo de Montjuict' and i.nombre = 'Pista de Tenis 1'),3,'17:00:00','22:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Campo de Montjuict' and i.nombre = 'Pista de Tenis 1'),4,'08:00:00','14:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Campo de Montjuict' and i.nombre = 'Pista de Tenis 1'),4,'17:00:00','22:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Campo de Montjuict' and i.nombre = 'Pista de Tenis 1'),5,'08:00:00','14:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Campo de Montjuict' and i.nombre = 'Pista de Tenis 1'),5,'17:00:00','22:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Campo de Montjuict' and i.nombre = 'Pista de Tenis 2'),1,'08:00:00','14:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Campo de Montjuict' and i.nombre = 'Pista de Tenis 2'),1,'17:00:00','22:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Campo de Montjuict' and i.nombre = 'Pista de Tenis 2'),2,'08:00:00','14:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Campo de Montjuict' and i.nombre = 'Pista de Tenis 2'),2,'17:00:00','22:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Campo de Montjuict' and i.nombre = 'Pista de Tenis 2'),3,'08:00:00','14:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Campo de Montjuict' and i.nombre = 'Pista de Tenis 2'),3,'17:00:00','22:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Campo de Montjuict' and i.nombre = 'Pista de Tenis 2'),4,'08:00:00','14:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Campo de Montjuict' and i.nombre = 'Pista de Tenis 2'),4,'17:00:00','22:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Campo de Montjuict' and i.nombre = 'Pista de Tenis 2'),5,'08:00:00','14:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Campo de Montjuict' and i.nombre = 'Pista de Tenis 2'),5,'17:00:00','22:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Campo de Montjuict' and i.nombre = 'Sauna Masculina'),1,'10:00:00','14:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Campo de Montjuict' and i.nombre = 'Sauna Masculina'),2,'10:00:00','14:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Campo de Montjuict' and i.nombre = 'Sauna Masculina'),3,'10:00:00','14:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Campo de Montjuict' and i.nombre = 'Sauna Masculina'),4,'10:00:00','14:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Campo de Montjuict' and i.nombre = 'Sauna Masculina'),5,'10:00:00','14:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Campo de Montjuict' and i.nombre = 'Sauna Femenina'),1,'10:00:00','14:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Campo de Montjuict' and i.nombre = 'Sauna Femenina'),2,'10:00:00','14:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Campo de Montjuict' and i.nombre = 'Sauna Femenina'),3,'10:00:00','14:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Campo de Montjuict' and i.nombre = 'Sauna Femenina'),4,'10:00:00','14:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Campo de Montjuict' and i.nombre = 'Sauna Femenina'),5,'10:00:00','14:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Campo de Montjuict' and i.nombre = 'Gimnasio'),1,'10:00:00','14:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Campo de Montjuict' and i.nombre = 'Gimnasio'),2,'10:00:00','14:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Campo de Montjuict' and i.nombre = 'Gimnasio'),3,'10:00:00','14:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Campo de Montjuict' and i.nombre = 'Gimnasio'),4,'10:00:00','14:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Campo de Montjuict' and i.nombre = 'Gimnasio'),5,'10:00:00','14:00:00');




insert into sede (id_empresa, id_municipio, direccion, latitud, longitud, nombre,  url, email, descripcion) values
((select id from empresa where nombre = 'BajaFIT'), (select id from municipio where nombre = 'Barcelona'),
'Provençals del Poblenou, Sant Martí, 08020 Barcelona',
'41.418341', '2.207365', 'Barcelona Norte','www.baja_fit.tfb', 'baja_fit_barcelona_norte@apm-tfb-calermany-2026.com',
'En el norte de Barcelona, la ciudad de Gaudí, tu centro de entrenamiento más coqueto');

insert into usuario(nombre, email, password) values
('Gestor Baja Fit Barcelona Norte','gestor_bajafit_barcelonanorte@apm-tfb-calermany-2026.com','$2a$10$DFFs70DvnosU6HFN1B0yv.zgeW/li41vxmvhr1B44dZNKptdCdcVu');
insert  into usuario_rol(id_usuario, id_rol)values
((select id from usuario where nombre = 'Gestor Baja Fit Barcelona Norte'), (select id from rol where nombre = 'USUARIO_SEDE'));
insert into usuario_sede (id_usuario, id_sede) values 
((select id from usuario where nombre = 'Gestor Baja Fit Barcelona Norte'),(select id from sede where nombre = 'Barcelona Norte'));

insert into instalacion (id_sede, id_instalacion_tipo, nombre, descripcion) values 
((select id from sede where nombre = 'Barcelona Norte'), (select id from instalacion_tipo where nombre = 'PISTA PADEL'), 'Pista de Padel 1', 'Pista cubierta' ),
((select id from sede where nombre = 'Barcelona Norte'), (select id from instalacion_tipo where nombre = 'PISTA PADEL'), 'Pista de Padel 2', 'Pista cubierta' ),
((select id from sede where nombre = 'Barcelona Norte'), (select id from instalacion_tipo where nombre = 'PISTA DE TENIS'), 'Pista de Tenis 1', 'Pista descubierta' ),
((select id from sede where nombre = 'Barcelona Norte'), (select id from instalacion_tipo where nombre = 'PISTA DE TENIS'), 'Pista de Tenis 2', 'Pista descubierta' ),
((select id from sede where nombre = 'Barcelona Norte'), (select id from instalacion_tipo where nombre = 'SAUNA'), 'Sauna Masculina', '' ),
((select id from sede where nombre = 'Barcelona Norte'), (select id from instalacion_tipo where nombre = 'SAUNA'), 'Sauna Femenina', '' ),
((select id from sede where nombre = 'Barcelona Norte'), (select id from instalacion_tipo where nombre = 'GIMNASIO'), 'Gimnasio', '' );


insert into usuario(nombre, email, password) values
('Gestor Baja Fit Barcelona Norte Padel','gestor_bajafit_barcelonanorte_padel@apm-tfb-calermany-2026.com','$2a$10$DFFs70DvnosU6HFN1B0yv.zgeW/li41vxmvhr1B44dZNKptdCdcVu');

insert  into usuario_rol(id_usuario, id_rol)values
((select id from usuario where nombre = 'Gestor Baja Fit Barcelona Norte Padel'), (select id from rol where nombre = 'USUARIO_INSTALACION'));

insert into usuario_instalacion (id_usuario, id_instalacion) values 
((select id from usuario where nombre = 'Gestor Baja Fit Barcelona Norte Padel'),
	(select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Barcelona Norte' and i.nombre = 'Pista de Padel 1')),
((select id from usuario where nombre = 'Gestor Baja Fit Barcelona Norte Padel'),
	(select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Barcelona Norte' and i.nombre = 'Pista de Padel 2')),
((select id from usuario where nombre = 'Gestor Baja Fit Barcelona Norte Padel'),
	(select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Barcelona Norte' and i.nombre = 'Pista de Tenis 1')),
((select id from usuario where nombre = 'Gestor Baja Fit Barcelona Norte Padel'),
	(select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Barcelona Norte' and i.nombre = 'Pista de Tenis 2'));
    
    
insert into usuario(nombre, email, password) values
('Gestor Baja Fit Barcelona Norte Sauna','gestor_bajafit_barcelonanorte_sauna@apm-tfb-calermany-2026.com','$2a$10$DFFs70DvnosU6HFN1B0yv.zgeW/li41vxmvhr1B44dZNKptdCdcVu');

insert  into usuario_rol(id_usuario, id_rol)values
((select id from usuario where nombre = 'Gestor Baja Fit Barcelona Norte Sauna'), (select id from rol where nombre = 'USUARIO_INSTALACION'));

insert into usuario_instalacion (id_usuario, id_instalacion) values 
((select id from usuario where nombre = 'Gestor Baja Fit Barcelona Norte Sauna'),
	(select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Barcelona Norte' and i.nombre = 'Sauna Masculina')),
((select id from usuario where nombre = 'Gestor Baja Fit Barcelona Norte Sauna'),
	(select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Barcelona Norte' and i.nombre = 'Sauna Femenina'));

insert into instalacion_configuracion_reserva (id_instalacion, duracion_min, duracion_max, intervalo_horario) values 
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Barcelona Norte' and i.nombre = 'Pista de Padel 1'),60,90,15),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Barcelona Norte' and i.nombre = 'Pista de Padel 2'),60,90,15),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Barcelona Norte' and i.nombre = 'Pista de Tenis 1'),60,90,15),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Barcelona Norte' and i.nombre = 'Pista de Tenis 2'),60,90,15),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Barcelona Norte' and i.nombre = 'Sauna Masculina'),20,60,20),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Barcelona Norte' and i.nombre = 'Sauna Femenina'),20,60,20),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Barcelona Norte' and i.nombre = 'Gimnasio'),15,60,15);

insert into instalacion_horario(id_instalacion, dia_semana, hora_inicio, hora_fin) values 
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Barcelona Norte' and i.nombre = 'Pista de Padel 1'),1,'08:00:00','14:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Barcelona Norte' and i.nombre = 'Pista de Padel 1'),1,'17:00:00','22:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Barcelona Norte' and i.nombre = 'Pista de Padel 1'),2,'08:00:00','14:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Barcelona Norte' and i.nombre = 'Pista de Padel 1'),2,'17:00:00','22:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Barcelona Norte' and i.nombre = 'Pista de Padel 1'),3,'08:00:00','14:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Barcelona Norte' and i.nombre = 'Pista de Padel 1'),3,'17:00:00','22:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Barcelona Norte' and i.nombre = 'Pista de Padel 1'),4,'08:00:00','14:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Barcelona Norte' and i.nombre = 'Pista de Padel 1'),4,'17:00:00','22:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Barcelona Norte' and i.nombre = 'Pista de Padel 1'),5,'08:00:00','14:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Barcelona Norte' and i.nombre = 'Pista de Padel 1'),5,'17:00:00','22:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Barcelona Norte' and i.nombre = 'Pista de Padel 2'),1,'08:00:00','14:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Barcelona Norte' and i.nombre = 'Pista de Padel 2'),1,'17:00:00','22:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Barcelona Norte' and i.nombre = 'Pista de Padel 2'),2,'08:00:00','14:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Barcelona Norte' and i.nombre = 'Pista de Padel 2'),2,'17:00:00','22:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Barcelona Norte' and i.nombre = 'Pista de Padel 2'),3,'08:00:00','14:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Barcelona Norte' and i.nombre = 'Pista de Padel 2'),3,'17:00:00','22:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Barcelona Norte' and i.nombre = 'Pista de Padel 2'),4,'08:00:00','14:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Barcelona Norte' and i.nombre = 'Pista de Padel 2'),4,'17:00:00','22:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Barcelona Norte' and i.nombre = 'Pista de Padel 2'),5,'08:00:00','14:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Barcelona Norte' and i.nombre = 'Pista de Padel 2'),5,'17:00:00','22:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Barcelona Norte' and i.nombre = 'Pista de Tenis 1'),1,'08:00:00','14:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Barcelona Norte' and i.nombre = 'Pista de Tenis 1'),1,'17:00:00','22:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Barcelona Norte' and i.nombre = 'Pista de Tenis 1'),2,'08:00:00','14:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Barcelona Norte' and i.nombre = 'Pista de Tenis 1'),2,'17:00:00','22:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Barcelona Norte' and i.nombre = 'Pista de Tenis 1'),3,'08:00:00','14:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Barcelona Norte' and i.nombre = 'Pista de Tenis 1'),3,'17:00:00','22:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Barcelona Norte' and i.nombre = 'Pista de Tenis 1'),4,'08:00:00','14:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Barcelona Norte' and i.nombre = 'Pista de Tenis 1'),4,'17:00:00','22:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Barcelona Norte' and i.nombre = 'Pista de Tenis 1'),5,'08:00:00','14:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Barcelona Norte' and i.nombre = 'Pista de Tenis 1'),5,'17:00:00','22:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Barcelona Norte' and i.nombre = 'Pista de Tenis 2'),1,'08:00:00','14:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Barcelona Norte' and i.nombre = 'Pista de Tenis 2'),1,'17:00:00','22:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Barcelona Norte' and i.nombre = 'Pista de Tenis 2'),2,'08:00:00','14:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Barcelona Norte' and i.nombre = 'Pista de Tenis 2'),2,'17:00:00','22:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Barcelona Norte' and i.nombre = 'Pista de Tenis 2'),3,'08:00:00','14:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Barcelona Norte' and i.nombre = 'Pista de Tenis 2'),3,'17:00:00','22:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Barcelona Norte' and i.nombre = 'Pista de Tenis 2'),4,'08:00:00','14:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Barcelona Norte' and i.nombre = 'Pista de Tenis 2'),4,'17:00:00','22:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Barcelona Norte' and i.nombre = 'Pista de Tenis 2'),5,'08:00:00','14:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Barcelona Norte' and i.nombre = 'Pista de Tenis 2'),5,'17:00:00','22:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Barcelona Norte' and i.nombre = 'Sauna Masculina'),1,'10:00:00','14:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Barcelona Norte' and i.nombre = 'Sauna Masculina'),2,'10:00:00','14:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Barcelona Norte' and i.nombre = 'Sauna Masculina'),3,'10:00:00','14:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Barcelona Norte' and i.nombre = 'Sauna Masculina'),4,'10:00:00','14:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Barcelona Norte' and i.nombre = 'Sauna Masculina'),5,'10:00:00','14:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Barcelona Norte' and i.nombre = 'Sauna Femenina'),1,'10:00:00','14:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Barcelona Norte' and i.nombre = 'Sauna Femenina'),2,'10:00:00','14:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Barcelona Norte' and i.nombre = 'Sauna Femenina'),3,'10:00:00','14:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Barcelona Norte' and i.nombre = 'Sauna Femenina'),4,'10:00:00','14:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Barcelona Norte' and i.nombre = 'Sauna Femenina'),5,'10:00:00','14:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Barcelona Norte' and i.nombre = 'Gimnasio'),1,'10:00:00','14:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Barcelona Norte' and i.nombre = 'Gimnasio'),2,'10:00:00','14:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Barcelona Norte' and i.nombre = 'Gimnasio'),3,'10:00:00','14:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Barcelona Norte' and i.nombre = 'Gimnasio'),4,'10:00:00','14:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Barcelona Norte' and i.nombre = 'Gimnasio'),5,'10:00:00','14:00:00');


/**************************************************************
***************************************************************
**************************************************************/
insert into empresa (nombre, email, cif, url, descripcion) values 
('GoGYM','go_gym@apm-tfb-calermany-2026.com','K50185016','www.go_gym.tfb','GO GYM, gimnasios de última generación, todo para el cuerpo y la mente');

insert into sede (id_empresa, id_municipio, direccion, latitud, longitud, nombre,  url, email, descripcion) values
((select id from empresa where nombre = 'GoGYM'), (select id from municipio where nombre = 'Barcelona'),
'Calle de Mallorca, 436-442, Eixample, 08013 Barcelona',
'41.404802', '2.177139', 'GoGYM Barcelona','www.go_gym.tfb', 'go_gym_barcelona@apm-tfb-calermany-2026.com',
'');




insert into usuario(nombre, email, password) values
('Gestor GoGYM','gestor_gogym@apm-tfb-calermany-2026.com','$2a$10$DFFs70DvnosU6HFN1B0yv.zgeW/li41vxmvhr1B44dZNKptdCdcVu');
insert  into usuario_rol(id_usuario, id_rol)values
((select id from usuario where nombre = 'Gestor GoGYM'), (select id from rol where nombre = 'USUARIO_EMPRESA'));
insert into usuario_empresa (id_usuario, id_empresa) values 
((select id from usuario where nombre = 'Gestor GoGYM'),(select id from empresa where nombre = 'GoGYM'));

insert into instalacion (id_sede, id_instalacion_tipo, nombre, descripcion) values 
((select id from sede where nombre = 'GoGYM Barcelona'), (select id from instalacion_tipo where nombre = 'PISTA PADEL'), 'Pista de Padel 1', 'Pista cubierta' ),
((select id from sede where nombre = 'GoGYM Barcelona'), (select id from instalacion_tipo where nombre = 'PISTA PADEL'), 'Pista de Padel 2', 'Pista cubierta' ),
((select id from sede where nombre = 'GoGYM Barcelona'), (select id from instalacion_tipo where nombre = 'SALA MULTIUSOS'), 'Sala multiusos', 'Sala multiusos' ),
((select id from sede where nombre = 'GoGYM Barcelona'), (select id from instalacion_tipo where nombre = 'SAUNA'), 'Sauna Masculina', '' ),
((select id from sede where nombre = 'GoGYM Barcelona'), (select id from instalacion_tipo where nombre = 'SAUNA'), 'Sauna Femenina', '' ),
((select id from sede where nombre = 'GoGYM Barcelona'), (select id from instalacion_tipo where nombre = 'GIMNASIO'), 'Gimnasio', '' );

insert into instalacion_configuracion_reserva (id_instalacion, duracion_min, duracion_max, intervalo_horario) values 
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'GoGYM Barcelona' and i.nombre = 'Pista de Padel 1'),60,90,15),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'GoGYM Barcelona' and i.nombre = 'Pista de Padel 2'),60,90,15),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'GoGYM Barcelona' and i.nombre = 'Sala multiusos'),60,120,30),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'GoGYM Barcelona' and i.nombre = 'Sauna Masculina'),20,60,20),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'GoGYM Barcelona' and i.nombre = 'Sauna Femenina'),20,60,20),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'GoGYM Barcelona' and i.nombre = 'Gimnasio'),15,60,15);

insert into instalacion_horario(id_instalacion, dia_semana, hora_inicio, hora_fin) values 
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'GoGYM Barcelona' and i.nombre = 'Pista de Padel 1'),1,'08:00:00','22:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'GoGYM Barcelona' and i.nombre = 'Pista de Padel 1'),2,'08:00:00','22:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'GoGYM Barcelona' and i.nombre = 'Pista de Padel 1'),3,'08:00:00','22:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'GoGYM Barcelona' and i.nombre = 'Pista de Padel 1'),4,'08:00:00','22:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'GoGYM Barcelona' and i.nombre = 'Pista de Padel 1'),5,'08:00:00','22:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'GoGYM Barcelona' and i.nombre = 'Pista de Padel 1'),6,'10:00:00','20:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'GoGYM Barcelona' and i.nombre = 'Pista de Padel 1'),7,'10:00:00','16:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'GoGYM Barcelona' and i.nombre = 'Pista de Padel 2'),1,'08:00:00','22:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'GoGYM Barcelona' and i.nombre = 'Pista de Padel 2'),2,'08:00:00','22:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'GoGYM Barcelona' and i.nombre = 'Pista de Padel 2'),3,'08:00:00','22:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'GoGYM Barcelona' and i.nombre = 'Pista de Padel 2'),4,'08:00:00','22:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'GoGYM Barcelona' and i.nombre = 'Pista de Padel 2'),5,'08:00:00','22:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'GoGYM Barcelona' and i.nombre = 'Pista de Padel 2'),6,'10:00:00','20:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'GoGYM Barcelona' and i.nombre = 'Pista de Padel 2'),7,'10:00:00','16:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'GoGYM Barcelona' and i.nombre = 'Sala multiusos'),1,'08:00:00','22:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'GoGYM Barcelona' and i.nombre = 'Sala multiusos'),2,'08:00:00','22:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'GoGYM Barcelona' and i.nombre = 'Sala multiusos'),3,'08:00:00','22:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'GoGYM Barcelona' and i.nombre = 'Sala multiusos'),4,'08:00:00','22:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'GoGYM Barcelona' and i.nombre = 'Sala multiusos'),5,'08:00:00','22:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'GoGYM Barcelona' and i.nombre = 'Sala multiusos'),6,'10:00:00','20:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'GoGYM Barcelona' and i.nombre = 'Sala multiusos'),7,'10:00:00','16:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'GoGYM Barcelona' and i.nombre = 'Sauna Masculina'),1,'08:00:00','22:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'GoGYM Barcelona' and i.nombre = 'Sauna Masculina'),2,'08:00:00','22:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'GoGYM Barcelona' and i.nombre = 'Sauna Masculina'),3,'08:00:00','22:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'GoGYM Barcelona' and i.nombre = 'Sauna Femenina'),4,'08:00:00','22:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'GoGYM Barcelona' and i.nombre = 'Sauna Femenina'),5,'08:00:00','22:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'GoGYM Barcelona' and i.nombre = 'Sauna Femenina'),6,'10:00:00','20:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'GoGYM Barcelona' and i.nombre = 'Sauna Femenina'),7,'10:00:00','16:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'GoGYM Barcelona' and i.nombre = 'Gimnasio'),1,'08:00:00','22:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'GoGYM Barcelona' and i.nombre = 'Gimnasio'),2,'08:00:00','22:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'GoGYM Barcelona' and i.nombre = 'Gimnasio'),3,'08:00:00','22:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'GoGYM Barcelona' and i.nombre = 'Gimnasio'),4,'08:00:00','22:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'GoGYM Barcelona' and i.nombre = 'Gimnasio'),5,'08:00:00','22:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'GoGYM Barcelona' and i.nombre = 'Gimnasio'),6,'10:00:00','20:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'GoGYM Barcelona' and i.nombre = 'Gimnasio'),7,'10:00:00','16:00:00');


insert into sede (id_empresa, id_municipio, direccion, latitud, longitud, nombre,  url, email, descripcion) values
((select id from empresa where nombre = 'GoGYM'), (select id from municipio where nombre = 'Madrid'),
'C. de Alfonso XII, 1V, Retiro, 28009 Madrid',
'40.412368', '-3.686392', 'GoGYM Madrid','www.go_gym.tfb', 'go_gym_madrid@apm-tfb-calermany-2026.com',
'');

insert into instalacion (id_sede, id_instalacion_tipo, nombre, descripcion) values 
((select id from sede where nombre = 'GoGYM Madrid'), (select id from instalacion_tipo where nombre = 'PISTA PADEL'), 'Pista de Padel 1', 'Pista cubierta' ),
((select id from sede where nombre = 'GoGYM Madrid'), (select id from instalacion_tipo where nombre = 'PISTA PADEL'), 'Pista de Padel 2', 'Pista cubierta' ),
((select id from sede where nombre = 'GoGYM Madrid'), (select id from instalacion_tipo where nombre = 'SALA MULTIUSOS'), 'Sala multiusos', 'Sala multiusos' ),
((select id from sede where nombre = 'GoGYM Madrid'), (select id from instalacion_tipo where nombre = 'GIMNASIO'), 'Gimnasio', '' );

insert into instalacion_configuracion_reserva (id_instalacion, duracion_min, duracion_max, intervalo_horario) values 
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'GoGYM Madrid' and i.nombre = 'Pista de Padel 1'),60,90,15),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'GoGYM Madrid' and i.nombre = 'Pista de Padel 2'),60,90,15),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'GoGYM Madrid' and i.nombre = 'Sala multiusos'),60,120,30),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'GoGYM Madrid' and i.nombre = 'Gimnasio'),15,60,15);


insert into instalacion_horario(id_instalacion, dia_semana, hora_inicio, hora_fin) values 
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'GoGYM Madrid' and i.nombre = 'Pista de Padel 1'),1,'08:00:00','22:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'GoGYM Madrid' and i.nombre = 'Pista de Padel 1'),2,'08:00:00','22:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'GoGYM Madrid' and i.nombre = 'Pista de Padel 1'),3,'08:00:00','22:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'GoGYM Madrid' and i.nombre = 'Pista de Padel 1'),4,'08:00:00','22:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'GoGYM Madrid' and i.nombre = 'Pista de Padel 1'),5,'08:00:00','22:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'GoGYM Madrid' and i.nombre = 'Pista de Padel 1'),6,'10:00:00','20:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'GoGYM Madrid' and i.nombre = 'Pista de Padel 1'),7,'10:00:00','16:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'GoGYM Madrid' and i.nombre = 'Pista de Padel 2'),1,'08:00:00','22:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'GoGYM Madrid' and i.nombre = 'Pista de Padel 2'),2,'08:00:00','22:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'GoGYM Madrid' and i.nombre = 'Pista de Padel 2'),3,'08:00:00','22:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'GoGYM Madrid' and i.nombre = 'Pista de Padel 2'),4,'08:00:00','22:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'GoGYM Madrid' and i.nombre = 'Pista de Padel 2'),5,'08:00:00','22:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'GoGYM Madrid' and i.nombre = 'Pista de Padel 2'),6,'10:00:00','20:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'GoGYM Madrid' and i.nombre = 'Pista de Padel 2'),7,'10:00:00','16:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'GoGYM Madrid' and i.nombre = 'Sala multiusos'),1,'08:00:00','22:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'GoGYM Madrid' and i.nombre = 'Sala multiusos'),2,'08:00:00','22:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'GoGYM Madrid' and i.nombre = 'Sala multiusos'),3,'08:00:00','22:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'GoGYM Madrid' and i.nombre = 'Sala multiusos'),4,'08:00:00','22:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'GoGYM Madrid' and i.nombre = 'Sala multiusos'),5,'08:00:00','22:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'GoGYM Madrid' and i.nombre = 'Sala multiusos'),6,'10:00:00','20:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'GoGYM Madrid' and i.nombre = 'Sala multiusos'),7,'10:00:00','16:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'GoGYM Madrid' and i.nombre = 'Gimnasio'),1,'08:00:00','22:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'GoGYM Madrid' and i.nombre = 'Gimnasio'),2,'08:00:00','22:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'GoGYM Madrid' and i.nombre = 'Gimnasio'),3,'08:00:00','22:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'GoGYM Madrid' and i.nombre = 'Gimnasio'),4,'08:00:00','22:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'GoGYM Madrid' and i.nombre = 'Gimnasio'),5,'08:00:00','22:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'GoGYM Madrid' and i.nombre = 'Gimnasio'),6,'10:00:00','20:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'GoGYM Madrid' and i.nombre = 'Gimnasio'),7,'10:00:00','16:00:00');




/**************************************************************
***************************************************************
**************************************************************/
insert into empresa (nombre, email, cif, url, descripcion) values 
('Benidorm Fitness','benidorm_fitness@apm-tfb-calermany-2026.com','L61174736','www.benidorm_fitness.tfb','En Benidorm, costa Blanca, tu lugar de entramiento perfecto');


insert into sede (id_empresa, id_municipio, direccion, latitud, longitud, nombre,  url, email, descripcion) values
((select id from empresa where nombre = 'Benidorm Fitness'), (select id from municipio where nombre = 'Benidorm'),
'Benidorm, 03502, Alicante',
'38.547536', '-0.139383', 'Benidorm Fitness Center','www.benidorm_fitness.tfb', 'benidorm_fitness@apm-tfb-calermany-2026.com',
'Tu local de confianza');

insert into instalacion (id_sede, id_instalacion_tipo, nombre, descripcion) values 
((select id from sede where nombre = 'Benidorm Fitness Center'), (select id from instalacion_tipo where nombre = 'GIMNASIO'), 'Gimnasio', '' );

insert into instalacion_configuracion_reserva (id_instalacion, duracion_min, duracion_max, intervalo_horario) values 
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Benidorm Fitness Center' and i.nombre = 'Gimnasio'),15,60,15);

insert into instalacion_horario(id_instalacion, dia_semana, hora_inicio, hora_fin) values 
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Benidorm Fitness Center' and i.nombre = 'Gimnasio'),1,'08:00:00','22:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Benidorm Fitness Center' and i.nombre = 'Gimnasio'),2,'08:00:00','22:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Benidorm Fitness Center' and i.nombre = 'Gimnasio'),3,'08:00:00','22:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Benidorm Fitness Center' and i.nombre = 'Gimnasio'),4,'08:00:00','22:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Benidorm Fitness Center' and i.nombre = 'Gimnasio'),6,'08:00:00','22:00:00'),
((select i.id from instalacion i inner join sede s on s.id = i.id_sede where s.nombre = 'Benidorm Fitness Center' and i.nombre = 'Gimnasio'),7,'08:00:00','22:00:00');


insert into usuario(nombre, email, password) values
('Gestor Benidorm Fitness','gestor_benidormfitness@apm-tfb-calermany-2026.com','$2a$10$DFFs70DvnosU6HFN1B0yv.zgeW/li41vxmvhr1B44dZNKptdCdcVu');
insert  into usuario_rol(id_usuario, id_rol)values
((select id from usuario where nombre = 'Gestor Benidorm Fitness'), (select id from rol where nombre = 'USUARIO_EMPRESA'));
insert into usuario_empresa (id_usuario, id_empresa) values 
((select id from usuario where nombre = 'Gestor Benidorm Fitness'),(select id from empresa where nombre = 'Benidorm Fitness'));








