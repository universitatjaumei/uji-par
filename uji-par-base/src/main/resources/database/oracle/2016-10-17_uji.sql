CREATE TABLE par_reports
(
  "id"    NUMBER,
  sala_id NUMBER,
  clase   VARCHAR2(255),
  tipo    VARCHAR2(100),
  CONSTRAINT "PAR_REPORTS_PK" PRIMARY KEY ("id"),
  CONSTRAINT "PAR_REPORTS_FK1" FOREIGN KEY (sala_id)
  REFERENCES PAR_SALAS ("ID") ON DELETE CASCADE ENABLE
);

CREATE TABLE par_salas_usuarios
(
  "id"       NUMBER,
  usuario_id INTEGER,
  sala_id    INTEGER,
  CONSTRAINT "PAR_SALAS_USUARIOS_PK" PRIMARY KEY ("id"),
  CONSTRAINT "PAR_SALAS_USUARIOS_US_FK1" FOREIGN KEY (usuario_id)
  REFERENCES PAR_USUARIOS ("ID") ON DELETE CASCADE ENABLE,
  CONSTRAINT "PAR_SALAS_USUARIOS_CINES_FK1" FOREIGN KEY (sala_id)
  REFERENCES PAR_SALAS ("ID") ON DELETE CASCADE ENABLE
);

INSERT INTO par_version_bbdd (VERSION) VALUES ('2016-05-27.sql');

ALTER TABLE PAR_EVENTOS ADD (
CINE_ID NUMBER NULL
);

ALTER TABLE PAR_EVENTOS ADD CONSTRAINT PAR_EVENTOS_CINES_FK1 FOREIGN KEY (CINE_ID)
REFERENCES PAR_CINES (ID) ON DELETE CASCADE ENABLE;

ALTER TABLE PAR_USUARIOS ADD (
URL VARCHAR2(255) NULL
);

ALTER TABLE PAR_TARIFAS ADD (
CINE_ID NUMBER NULL
);

ALTER TABLE PAR_TARIFAS ADD CONSTRAINT PAR_TARIFAS_CINES_FK1 FOREIGN KEY (CINE_ID)
REFERENCES PAR_CINES (ID) ON DELETE CASCADE ENABLE;

ALTER TABLE PAR_TIPOS_EVENTO ADD (
CINE_ID NUMBER NULL
);

ALTER TABLE PAR_TIPOS_EVENTO ADD CONSTRAINT PAR_TIPOS_EVENTO_CINES_FK1 FOREIGN KEY (CINE_ID)
REFERENCES PAR_CINES (ID) ON DELETE CASCADE ENABLE;

ALTER TABLE PAR_SALAS ADD (
ASIENTOS_NUMERADOS NUMBER(1, 0) DEFAULT 1
);

INSERT INTO par_version_bbdd (VERSION) VALUES ('2016-06-14.SQL');

ALTER TABLE PAR_EVENTOS ADD (
PROMOTOR VARCHAR2(255) NULL,
NIF_PROMOTOR VARCHAR2(255) NULL
);

INSERT INTO par_version_bbdd (VERSION) VALUES ('2016-07-08.SQL');

ALTER TABLE PAR_EVENTOS ADD (
"IMAGEN_PUBLI" BLOB,
"IMAGEN_PUBLI_CONTENT_TYPE" VARCHAR2(255),
"IMAGEN_PUBLI_SRC" VARCHAR2(255)
);

INSERT INTO par_version_bbdd (VERSION) VALUES ('2016-07-20.SQL');

ALTER TABLE PAR_CINES ADD (
"URL_PUBLIC" VARCHAR2(255),
"URL_PRIVACIDAD" VARCHAR2(255),
"URL_COMO_LLEGAR" VARCHAR2(255),
"MAIL_FROM" VARCHAR2(255),
"URL_PIE_ENTRADA" VARCHAR2(255),
"LOGO_REPORT" VARCHAR2(255)
);

ALTER TABLE PAR_MAILS ADD (
"URL_PUBLIC" VARCHAR2(255),
"URL_PIE_ENTRADA" VARCHAR2(255)
);

CREATE TABLE PAR_TPVS_CINES (
  id      INTEGER NOT NULL,
  tpv_id  INTEGER NOT NULL,
  cine_id INTEGER NOT NULL,
  CONSTRAINT "PAR_TPVS_CINES_PK" PRIMARY KEY (id),
  CONSTRAINT "PAR_TPVS_CINES_UQ" UNIQUE (tpv_id, cine_id),
  CONSTRAINT "PAR_TPVS_CINES_TPV_FK" FOREIGN KEY (tpv_id)
  REFERENCES PAR_TPVS ("ID") ON DELETE CASCADE ENABLE,
  CONSTRAINT "PAR_TPVS_CINES_CINE_FK" FOREIGN KEY (cine_id)
  REFERENCES PAR_CINES ("ID") ON DELETE CASCADE ENABLE
);

ALTER TABLE PAR_TPVS DROP COLUMN default_tpv;

UPDATE PAR_CINES
SET
  URL_PUBLIC      = 'http://ujiapps.uji.es/par-public',
  URL_PRIVACIDAD  = 'http://www.uji.es/CA/organs/sg/polgen.thtml',
  URL_COMO_LLEGAR = 'http://www.uji.es/bin/asc/paranimf/comarrib.pdf',
  MAIL_FROM       = 'no_reply@uji.es',
  URL_PIE_ENTRADA = 'http://ujiapps.uji.es/serveis/scp/disseny/publicitat/paranimf/publi.jpg',
  LOGO_REPORT     = 'uji_logo_color.png';

UPDATE PAR_MAILS
SET
  URL_PUBLIC      = 'http://ujiapps.uji.es/par-public',
  URL_PIE_ENTRADA = 'http://ujiapps.uji.es/serveis/scp/disseny/publicitat/paranimf/publi.jpg';

ALTER TABLE PAR_CINES MODIFY URL_PUBLIC DEFAULT NULL NOT NULL;
ALTER TABLE PAR_CINES MODIFY URL_PRIVACIDAD DEFAULT NULL NOT NULL;
ALTER TABLE PAR_CINES MODIFY URL_COMO_LLEGAR DEFAULT NULL NOT NULL;
ALTER TABLE PAR_CINES MODIFY MAIL_FROM DEFAULT NULL NOT NULL;
ALTER TABLE PAR_CINES MODIFY URL_PIE_ENTRADA DEFAULT NULL NOT NULL;
ALTER TABLE PAR_CINES MODIFY LOGO_REPORT DEFAULT NULL NOT NULL;
ALTER TABLE PAR_MAILS MODIFY URL_PUBLIC DEFAULT NULL NOT NULL;
ALTER TABLE PAR_MAILS MODIFY URL_PIE_ENTRADA DEFAULT NULL NOT NULL;

INSERT INTO par_version_bbdd (VERSION) VALUES ('2016-08-25.SQL');

ALTER TABLE PAR_CINES ADD (
"API_KEY" VARCHAR2(255)
);

UPDATE PAR_CINES
SET
  API_KEY = 'kajshdka4losdfl2_$';

INSERT INTO par_version_bbdd (VERSION) VALUES ('2016-09-09.SQL');

ALTER TABLE PAR_CINES ADD (BUTACASENTRADOENDISTINTOCOLOR NUMBER(1, 0));

UPDATE PAR_CINES
SET
  BUTACASENTRADOENDISTINTOCOLOR = 0;

INSERT INTO par_version_bbdd (VERSION) VALUES ('2016-09-16.SQL');

UPDATE PAR_EVENTOS
SET CINE_ID = 1;
UPDATE PAR_TIPOS_EVENTO
SET CINE_ID = 1;

INSERT INTO PAR_REPORTS("id", sala_id, tipo, clase) values (1, 1, 'ENTRADATAQUILLA', 'es.uji.apps.par.report.EntradaTaquillaReport');
INSERT INTO PAR_REPORTS("id", sala_id, tipo, clase) values (2, 1, 'ENTRADAONLINE', 'es.uji.apps.par.report.EntradaReport');
INSERT INTO PAR_REPORTS("id", sala_id, tipo, clase) values (3, 1, 'pdfTaquilla', 'es.uji.apps.par.report.InformeTaquillaReport');
INSERT INTO PAR_REPORTS("id", sala_id, tipo, clase) values (4, 1, 'pdfEfectiu', 'es.uji.apps.par.report.InformeEfectivoReport');
INSERT INTO PAR_REPORTS("id", sala_id, tipo, clase) values (5, 1, 'pdfTpv', 'es.uji.apps.par.report.InformeTaquillaTpvSubtotalesReport');
INSERT INTO PAR_REPORTS("id", sala_id, tipo, clase) values (6, 1, 'pdfSGAE', 'es.uji.apps.par.report.InformeEventosReport');
INSERT INTO PAR_REPORTS("id", sala_id, tipo, clase) values (7, 1, 'pdfSesion', 'es.uji.apps.par.report.InformeSesionReport');

INSERT INTO PAR_USUARIOS (ID, NOMBRE, USUARIO, MAIL, URL) VALUES (1, 'palos', 'palos', 'palos@uji.es', NULL);
INSERT INTO PAR_USUARIOS (ID, NOMBRE, USUARIO, MAIL, URL) VALUES (2, 'al204488', 'al204488', 'al204488@uji.es', NULL);
INSERT INTO PAR_USUARIOS (ID, NOMBRE, USUARIO, MAIL, URL) VALUES (3, 'dobon', 'dobon', 'dobon@uji.es', NULL);
INSERT INTO PAR_USUARIOS (ID, NOMBRE, USUARIO, MAIL, URL) VALUES (4, 'Ricardo Borillo', 'borillo', 'borillo@uji.es', NULL);
INSERT INTO PAR_USUARIOS (ID, NOMBRE, USUARIO, MAIL, URL) VALUES (5, 'Nicolás Manero', 'nmanero', 'nmanero@uji.es', NULL);
INSERT INTO PAR_USUARIOS (ID, NOMBRE, USUARIO, MAIL, URL) VALUES (6, 'vicianov', 'vicianov', 'vicianov@uji.es', NULL);
INSERT INTO PAR_USUARIOS (ID, NOMBRE, USUARIO, MAIL, URL) VALUES (7, 'claramun', 'claramun', 'claramun@uji.es', NULL);
INSERT INTO PAR_USUARIOS (ID, NOMBRE, USUARIO, MAIL, URL) VALUES (8, 'Sergio Gragera', 'al081561', 'al081561@uji.es', NULL);
INSERT INTO PAR_USUARIOS (ID, NOMBRE, USUARIO, MAIL, URL) VALUES (9, 'mcerisue', 'mcerisue', 'mcerisue@uji.es', NULL);
INSERT INTO PAR_USUARIOS (ID, NOMBRE, USUARIO, MAIL, URL) VALUES (10, 'Public', 'public', 'info@4tic.com', 'ujiapps.uji.es');

INSERT INTO PAR_SALAS_USUARIOS ("id", USUARIO_ID, SALA_ID) VALUES (1, 1, 1);
INSERT INTO PAR_SALAS_USUARIOS ("id", USUARIO_ID, SALA_ID) VALUES (2, 2, 1);
INSERT INTO PAR_SALAS_USUARIOS ("id", USUARIO_ID, SALA_ID) VALUES (3, 3, 1);
INSERT INTO PAR_SALAS_USUARIOS ("id", USUARIO_ID, SALA_ID) VALUES (4, 4, 1);
INSERT INTO PAR_SALAS_USUARIOS ("id", USUARIO_ID, SALA_ID) VALUES (5, 5, 1);
INSERT INTO PAR_SALAS_USUARIOS ("id", USUARIO_ID, SALA_ID) VALUES (6, 6, 1);
INSERT INTO PAR_SALAS_USUARIOS ("id", USUARIO_ID, SALA_ID) VALUES (7, 7, 1);
INSERT INTO PAR_SALAS_USUARIOS ("id", USUARIO_ID, SALA_ID) VALUES (8, 8, 1);
INSERT INTO PAR_SALAS_USUARIOS ("id", USUARIO_ID, SALA_ID) VALUES (9, 9, 1);
INSERT INTO PAR_SALAS_USUARIOS ("id", USUARIO_ID, SALA_ID) VALUES (10, 10, 1);

INSERT INTO PAR_TPVS_CINES (ID, TPV_ID, CINE_ID) VALUES (1, 1, 1);