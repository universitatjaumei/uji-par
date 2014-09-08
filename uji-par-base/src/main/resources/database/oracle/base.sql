--------------------------------------------------------
--  File created - lunes-septiembre-01-2014   
--------------------------------------------------------
  DROP TABLE "UJI_PARANIMF"."PAR_BUTACAS" cascade constraints;
  DROP TABLE "UJI_PARANIMF"."PAR_CINES" cascade constraints;
  DROP TABLE "UJI_PARANIMF"."PAR_COMPRAS" cascade constraints;
  DROP TABLE "UJI_PARANIMF"."PAR_ENVIOS" cascade constraints;
  DROP TABLE "UJI_PARANIMF"."PAR_ENVIOS_SESIONES" cascade constraints;
  DROP TABLE "UJI_PARANIMF"."PAR_EVENTOS" cascade constraints;
  DROP TABLE "UJI_PARANIMF"."PAR_LOCALIZACIONES" cascade constraints;
  DROP TABLE "UJI_PARANIMF"."PAR_MAILS" cascade constraints;
  DROP TABLE "UJI_PARANIMF"."PAR_PLANTAS_SALA" cascade constraints;
  DROP TABLE "UJI_PARANIMF"."PAR_PLANTILLAS" cascade constraints;
  DROP TABLE "UJI_PARANIMF"."PAR_PRECIOS_PLANTILLA" cascade constraints;
  DROP TABLE "UJI_PARANIMF"."PAR_PRECIOS_SESION" cascade constraints;
  DROP TABLE "UJI_PARANIMF"."PAR_SALAS" cascade constraints;
  DROP TABLE "UJI_PARANIMF"."PAR_SESIONES" cascade constraints;
  DROP TABLE "UJI_PARANIMF"."PAR_TARIFAS" cascade constraints;
  DROP TABLE "UJI_PARANIMF"."PAR_TIPOS_EVENTO" cascade constraints;
  DROP TABLE "UJI_PARANIMF"."PAR_USUARIOS" cascade constraints;
  DROP SYNONYM "UJI_PARANIMF"."APA_VW_PERSONAS_ITEMS";
  DROP SEQUENCE "UJI_PARANIMF"."HIBERNATE_SEQUENCE";
--------------------------------------------------------
--  DDL for Sequence HIBERNATE_SEQUENCE
--------------------------------------------------------

   CREATE SEQUENCE  "UJI_PARANIMF"."HIBERNATE_SEQUENCE"  MINVALUE 1 MAXVALUE 999999999999999999999999999 INCREMENT BY 1 START WITH 1402 CACHE 20 NOORDER  NOCYCLE
--------------------------------------------------------
--  DDL for Table PAR_BUTACAS
--------------------------------------------------------

  CREATE TABLE "UJI_PARANIMF"."PAR_BUTACAS" 
   (	"ID" NUMBER, 
	"SESION_ID" NUMBER, 
	"LOCALIZACION_ID" NUMBER, 
	"COMPRA_ID" NUMBER, 
	"FILA" VARCHAR2(255), 
	"NUMERO" VARCHAR2(255), 
	"TIPO" VARCHAR2(255), 
	"PRECIO" NUMBER, 
	"ANULADA" NUMBER(1,0) DEFAULT 0, 
	"PRESENTADA" TIMESTAMP (6)
   );
--------------------------------------------------------
--  DDL for Table PAR_CINES
--------------------------------------------------------

  CREATE TABLE "UJI_PARANIMF"."PAR_CINES" 
   (	"ID" NUMBER, 
	"CODIGO" VARCHAR2(6), 
	"NOMBRE" VARCHAR2(400), 
	"CIF" VARCHAR2(30), 
	"DIRECCION" VARCHAR2(500), 
	"COD_MUNICIPIO" VARCHAR2(400), 
	"NOM_MUNICIPIO" VARCHAR2(400), 
	"CP" VARCHAR2(400), 
	"EMPRESA" VARCHAR2(400), 
	"COD_REGISTRO" VARCHAR2(400), 
	"TFNO" VARCHAR2(400), 
	"IVA" NUMBER
   );
--------------------------------------------------------
--  DDL for Table PAR_COMPRAS
--------------------------------------------------------

  CREATE TABLE "UJI_PARANIMF"."PAR_COMPRAS" 
   (	"ID" NUMBER, 
	"NOMBRE" VARCHAR2(255), 
	"APELLIDOS" VARCHAR2(255), 
	"TFNO" VARCHAR2(255), 
	"EMAIL" VARCHAR2(255), 
	"FECHA" TIMESTAMP (6), 
	"TAQUILLA" NUMBER(1,0) DEFAULT 0, 
	"IMPORTE" NUMBER, 
	"SESION_ID" NUMBER, 
	"CODIGO_PAGO_TARJETA" VARCHAR2(255), 
	"PAGADA" NUMBER(1,0) DEFAULT 0, 
	"UUID" VARCHAR2(36), 
	"CODIGO_PAGO_PASARELA" VARCHAR2(255), 
	"DIRECCION" VARCHAR2(255), 
	"POBLACION" VARCHAR2(255), 
	"CP" VARCHAR2(255), 
	"PROVINCIA" VARCHAR2(255), 
	"INFO_PERIODICA" NUMBER(1,0) DEFAULT 0, 
	"RESERVA" NUMBER(1,0) DEFAULT 0, 
	"DESDE" TIMESTAMP (6), 
	"HASTA" TIMESTAMP (6), 
	"OBSERVACIONES_RESERVA" VARCHAR2(36), 
	"ANULADA" NUMBER(1,0) DEFAULT 0, 
	"RECIBO_PINPAD" VARCHAR2(3500 CHAR), 
	"CADUCADA" NUMBER(1,0), 
	"REFERENCIA_PAGO" VARCHAR2(400)
   );
--------------------------------------------------------
--  DDL for Table PAR_ENVIOS
--------------------------------------------------------

  CREATE TABLE "UJI_PARANIMF"."PAR_ENVIOS" 
   (	"ID" NUMBER, 
	"FECHA_GENERACION_FICHERO" TIMESTAMP (6), 
	"FECHA_ENVIO_FICHERO" TIMESTAMP (6)
   );
--------------------------------------------------------
--  DDL for Table PAR_ENVIOS_SESIONES
--------------------------------------------------------

  CREATE TABLE "UJI_PARANIMF"."PAR_ENVIOS_SESIONES" 
   (	"ID" NUMBER, 
	"PAR_ENVIO_ID" NUMBER(*,0), 
	"PAR_SESION_ID" NUMBER(*,0), 
	"TIPO_ENVIO" VARCHAR2(2)
   );
--------------------------------------------------------
--  DDL for Table PAR_EVENTOS
--------------------------------------------------------

  CREATE TABLE "UJI_PARANIMF"."PAR_EVENTOS" 
   (	"ID" NUMBER, 
	"TITULO_ES" VARCHAR2(255), 
	"DESCRIPCION_ES" VARCHAR2(3500), 
	"COMPANYIA_ES" VARCHAR2(300), 
	"INTERPRETES_ES" VARCHAR2(3500), 
	"DURACION_ES" VARCHAR2(255), 
	"PREMIOS_ES" VARCHAR2(3500), 
	"CARACTERISTICAS_ES" VARCHAR2(3500), 
	"COMENTARIOS_ES" VARCHAR2(3500), 
	"IMAGEN" BLOB, 
	"TIPO_EVENTO_ID" NUMBER, 
	"IMAGEN_CONTENT_TYPE" VARCHAR2(255), 
	"IMAGEN_SRC" VARCHAR2(255), 
	"TITULO_VA" VARCHAR2(255), 
	"DESCRIPCION_VA" VARCHAR2(3500), 
	"COMPANYIA_VA" VARCHAR2(300), 
	"INTERPRETES_VA" VARCHAR2(3500), 
	"DURACION_VA" VARCHAR2(255), 
	"PREMIOS_VA" VARCHAR2(3500), 
	"CARACTERISTICAS_VA" VARCHAR2(3500), 
	"COMENTARIOS_VA" VARCHAR2(3500), 
	"ASIENTOS_NUMERADOS" NUMBER(1,0) DEFAULT 0, 
	"PORCENTAJE_IVA" NUMBER, 
	"IVA_SGAE" NUMBER, 
	"RETENCION_SGAE" NUMBER, 
	"RSS_ID" VARCHAR2(300), 
	"EXPEDIENTE" VARCHAR2(400), 
	"COD_DISTRI" VARCHAR2(400), 
	"NOM_DISTRI" VARCHAR2(400), 
	"NACIONALIDAD" VARCHAR2(400), 
	"VO" VARCHAR2(400), 
	"METRAJE" VARCHAR2(400), 
	"SUBTITULOS" VARCHAR2(400)
   );
--------------------------------------------------------
--  DDL for Table PAR_LOCALIZACIONES
--------------------------------------------------------

  CREATE TABLE "UJI_PARANIMF"."PAR_LOCALIZACIONES" 
   (	"ID" NUMBER, 
	"NOMBRE_ES" VARCHAR2(255), 
	"TOTAL_ENTRADAS" NUMBER, 
	"NOMBRE_VA" VARCHAR2(255), 
	"CODIGO" VARCHAR2(255), 
	"SALA_ID" NUMBER, 
	"INICIALES" VARCHAR2(100)
   );
--------------------------------------------------------
--  DDL for Table PAR_MAILS
--------------------------------------------------------

  CREATE TABLE "UJI_PARANIMF"."PAR_MAILS" 
   (	"ID" NUMBER, 
	"PARA" VARCHAR2(255), 
	"DE" VARCHAR2(255), 
	"TITULO" CLOB, 
	"TEXTO" CLOB, 
	"FECHA_CREADO" TIMESTAMP (6), 
	"FECHA_ENVIADO" TIMESTAMP (6),
	"COMPRA_UUID" VARCHAR(101)
   );
--------------------------------------------------------
--  DDL for Table PAR_PLANTAS_SALA
--------------------------------------------------------

  CREATE TABLE "UJI_PARANIMF"."PAR_PLANTAS_SALA" 
   (	"ID" NUMBER, 
	"NOMBRE" VARCHAR2(400), 
	"SALA_ID" NUMBER
   );
--------------------------------------------------------
--  DDL for Table PAR_PLANTILLAS
--------------------------------------------------------

  CREATE TABLE "UJI_PARANIMF"."PAR_PLANTILLAS" 
   (	"ID" NUMBER, 
	"NOMBRE" VARCHAR2(255), 
	"SALA_ID" NUMBER
   );
--------------------------------------------------------
--  DDL for Table PAR_PRECIOS_PLANTILLA
--------------------------------------------------------

  CREATE TABLE "UJI_PARANIMF"."PAR_PRECIOS_PLANTILLA" 
   (	"ID" NUMBER, 
	"LOCALIZACION_ID" NUMBER, 
	"PLANTILLA_ID" NUMBER, 
	"PRECIO" NUMBER, 
	"DESCUENTO" NUMBER, 
	"INVITACION" NUMBER, 
	"AULA_TEATRO" NUMBER, 
	"TARIFA_ID" NUMBER
   );
--------------------------------------------------------
--  DDL for Table PAR_PRECIOS_SESION
--------------------------------------------------------

  CREATE TABLE "UJI_PARANIMF"."PAR_PRECIOS_SESION" 
   (	"ID" NUMBER, 
	"LOCALIZACION_ID" NUMBER, 
	"SESION_ID" NUMBER, 
	"PRECIO" NUMBER, 
	"DESCUENTO" NUMBER, 
	"INVITACION" NUMBER, 
	"AULA_TEATRO" NUMBER, 
	"TARIFA_ID" NUMBER(*,0)
   );
--------------------------------------------------------
--  DDL for Table PAR_SALAS
--------------------------------------------------------

  CREATE TABLE "UJI_PARANIMF"."PAR_SALAS" 
   (	"ID" NUMBER, 
	"CODIGO" VARCHAR2(6), 
	"NOMBRE" VARCHAR2(400), 
	"ASIENTOS" NUMBER, 
	"ASIENTO_DISC" NUMBER, 
	"ASIENTO_NORES" NUMBER, 
	"TIPO" VARCHAR2(30), 
	"FORMATO" VARCHAR2(500), 
	"SUBTITULO" VARCHAR2(400), 
	"CINE_ID" NUMBER
   );
--------------------------------------------------------
--  DDL for Table PAR_SESIONES
--------------------------------------------------------

  CREATE TABLE "UJI_PARANIMF"."PAR_SESIONES" 
   (	"ID" NUMBER, 
	"EVENTO_ID" NUMBER, 
	"FECHA_CELEBRACION" TIMESTAMP (6), 
	"FECHA_INICIO_VENTA_ONLINE" TIMESTAMP (6), 
	"FECHA_FIN_VENTA_ONLINE" TIMESTAMP (6), 
	"HORA_APERTURA" VARCHAR2(10), 
	"CANAL_INTERNET" NUMBER(1,0) DEFAULT 1, 
	"CANAL_TAQUILLA" NUMBER(1,0) DEFAULT 1, 
	"PLANTILLA_ID" NUMBER, 
	"NOMBRE" VARCHAR2(400), 
	"SALA_ID" NUMBER, 
	"FORMATO" VARCHAR2(400), 
	"VER_LING" VARCHAR2(400), 
	"RSS_ID" VARCHAR2(400), 
	"INCIDENCIA_ID" NUMBER
   );
--------------------------------------------------------
--  DDL for Table PAR_TARIFAS
--------------------------------------------------------

  CREATE TABLE "UJI_PARANIMF"."PAR_TARIFAS" 
   (	"ID" NUMBER, 
	"NOMBRE" VARCHAR2(100), 
	"ISPUBLICA" NUMBER(1,0) DEFAULT 1, 
	"DEFECTO" NUMBER(1,0) DEFAULT 0
   );
--------------------------------------------------------
--  DDL for Table PAR_TIPOS_EVENTO
--------------------------------------------------------

  CREATE TABLE "UJI_PARANIMF"."PAR_TIPOS_EVENTO" 
   (	"ID" NUMBER, 
	"NOMBRE_ES" VARCHAR2(50), 
	"NOMBRE_VA" VARCHAR2(50), 
	"EXPORTAR_ICAA" NUMBER(1,0) DEFAULT 0
   );
--------------------------------------------------------
--  DDL for Table PAR_USUARIOS
--------------------------------------------------------

  CREATE TABLE "UJI_PARANIMF"."PAR_USUARIOS" 
   (	"ID" NUMBER, 
	"NOMBRE" VARCHAR2(255), 
	"USUARIO" VARCHAR2(255), 
	"MAIL" VARCHAR2(255)
   );

---------------------------------------------------
--   END DATA FOR TABLE PAR_ENVIOS
---------------------------------------------------
---------------------------------------------------
--   DATA FOR TABLE PAR_LOCALIZACIONES
--   FILTER = none used
---------------------------------------------------
Insert into UJI_PARANIMF.PAR_LOCALIZACIONES (ID,NOMBRE_ES,TOTAL_ENTRADAS,NOMBRE_VA,CODIGO,SALA_ID,INICIALES) values (516,'Platea nivel 2',225,'Platea nivell 2','platea2',1,null);
Insert into UJI_PARANIMF.PAR_LOCALIZACIONES (ID,NOMBRE_ES,TOTAL_ENTRADAS,NOMBRE_VA,CODIGO,SALA_ID,INICIALES) values (517,'Discapacitados platea nivel 1',3,'Discapacitats platea nivell 1','discapacitados1',1,null);
Insert into UJI_PARANIMF.PAR_LOCALIZACIONES (ID,NOMBRE_ES,TOTAL_ENTRADAS,NOMBRE_VA,CODIGO,SALA_ID,INICIALES) values (518,'Discapacitados platea nivel 2',3,'Discapacitats platea nivell 2','discapacitados2',1,null);
Insert into UJI_PARANIMF.PAR_LOCALIZACIONES (ID,NOMBRE_ES,TOTAL_ENTRADAS,NOMBRE_VA,CODIGO,SALA_ID,INICIALES) values (47900,'Discapacitados anfiteatro',1,'Discapacitats anfiteatre','discapacitados3',1,null);
Insert into UJI_PARANIMF.PAR_LOCALIZACIONES (ID,NOMBRE_ES,TOTAL_ENTRADAS,NOMBRE_VA,CODIGO,SALA_ID,INICIALES) values (514,'Anfiteatro',144,'Amfiteatre','anfiteatro',1,null);
Insert into UJI_PARANIMF.PAR_LOCALIZACIONES (ID,NOMBRE_ES,TOTAL_ENTRADAS,NOMBRE_VA,CODIGO,SALA_ID,INICIALES) values (515,'Platea nivel 1',266,'Platea nivell 1','platea1',1,null);

Insert into UJI_PARANIMF.PAR_PLANTILLAS (ID, NOMBRE, SALA_ID) values (-1, '--Sense plantilla', 1);

Insert into UJI_PARANIMF.PAR_CINES (ID,CODIGO,NOMBRE,CIF,DIRECCION,COD_MUNICIPIO,NOM_MUNICIPIO,CP,EMPRESA,COD_REGISTRO,TFNO,IVA) values (1,'123','Paranimf','Q6250003H','Avinguda Sos Baynat S/N','TODO','Castelló de la Plana','12071','TODO','TODO','964 72 80 00',0);

Insert into UJI_PARANIMF.PAR_TARIFAS (ID,NOMBRE,ISPUBLICA,DEFECTO) values (1,'General',1,1);
Insert into UJI_PARANIMF.PAR_TARIFAS (ID,NOMBRE,ISPUBLICA,DEFECTO) values (2,'Descompte',1,0);
Insert into UJI_PARANIMF.PAR_TARIFAS (ID,NOMBRE,ISPUBLICA,DEFECTO) values (3,'Invitació',0,0);
Insert into UJI_PARANIMF.PAR_TARIFAS (ID,NOMBRE,ISPUBLICA,DEFECTO) values (4,'Aula Teatre',1,0);

Insert into UJI_PARANIMF.PAR_SALAS (ID,CODIGO,NOMBRE,ASIENTOS,ASIENTO_DISC,ASIENTO_NORES,TIPO,FORMATO,SUBTITULO,CINE_ID) values (1,'TODO','Paranimf',641,6,0,'COMERCIAL','DIGITAL','false',1);


  ALTER TABLE "UJI_PARANIMF"."PAR_BUTACAS" ADD CONSTRAINT "PAR_BUTACAS_PK" PRIMARY KEY ("ID") ENABLE;
 
  ALTER TABLE "UJI_PARANIMF"."PAR_BUTACAS" MODIFY ("COMPRA_ID" NOT NULL ENABLE);
 
  ALTER TABLE "UJI_PARANIMF"."PAR_BUTACAS" MODIFY ("ID" NOT NULL ENABLE);
 
  ALTER TABLE "UJI_PARANIMF"."PAR_BUTACAS" MODIFY ("PRECIO" NOT NULL ENABLE);

--------------------------------------------------------
--  Constraints for Table PAR_CINES
--------------------------------------------------------

  ALTER TABLE "UJI_PARANIMF"."PAR_CINES" ADD CONSTRAINT "PAR_CINES_PK" PRIMARY KEY ("ID") ENABLE;
 
  ALTER TABLE "UJI_PARANIMF"."PAR_CINES" MODIFY ("ID" NOT NULL ENABLE);
 
  ALTER TABLE "UJI_PARANIMF"."PAR_CINES" MODIFY ("CODIGO" NOT NULL ENABLE);
 
  ALTER TABLE "UJI_PARANIMF"."PAR_CINES" MODIFY ("NOMBRE" NOT NULL ENABLE);
 
  ALTER TABLE "UJI_PARANIMF"."PAR_CINES" MODIFY ("CIF" NOT NULL ENABLE);
 
  ALTER TABLE "UJI_PARANIMF"."PAR_CINES" MODIFY ("DIRECCION" NOT NULL ENABLE);
 
  ALTER TABLE "UJI_PARANIMF"."PAR_CINES" MODIFY ("COD_MUNICIPIO" NOT NULL ENABLE);
 
  ALTER TABLE "UJI_PARANIMF"."PAR_CINES" MODIFY ("NOM_MUNICIPIO" NOT NULL ENABLE);
 
  ALTER TABLE "UJI_PARANIMF"."PAR_CINES" MODIFY ("CP" NOT NULL ENABLE);
 
  ALTER TABLE "UJI_PARANIMF"."PAR_CINES" MODIFY ("EMPRESA" NOT NULL ENABLE);
 
  ALTER TABLE "UJI_PARANIMF"."PAR_CINES" MODIFY ("COD_REGISTRO" NOT NULL ENABLE);
 
  ALTER TABLE "UJI_PARANIMF"."PAR_CINES" MODIFY ("TFNO" NOT NULL ENABLE);
 
  ALTER TABLE "UJI_PARANIMF"."PAR_CINES" MODIFY ("IVA" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table PAR_COMPRAS
--------------------------------------------------------

  ALTER TABLE "UJI_PARANIMF"."PAR_COMPRAS" ADD CONSTRAINT "PAR_COMPRAS_PK" PRIMARY KEY ("ID") ENABLE;
 
  ALTER TABLE "UJI_PARANIMF"."PAR_COMPRAS" MODIFY ("FECHA" NOT NULL ENABLE);
 
  ALTER TABLE "UJI_PARANIMF"."PAR_COMPRAS" MODIFY ("TAQUILLA" NOT NULL ENABLE);
 
  ALTER TABLE "UJI_PARANIMF"."PAR_COMPRAS" MODIFY ("SESION_ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table PAR_ENVIOS
--------------------------------------------------------

  ALTER TABLE "UJI_PARANIMF"."PAR_ENVIOS" ADD CONSTRAINT "PAR_ENVIOS_PK" PRIMARY KEY ("ID") ENABLE;
 
  ALTER TABLE "UJI_PARANIMF"."PAR_ENVIOS" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table PAR_ENVIOS_SESIONES
--------------------------------------------------------

  ALTER TABLE "UJI_PARANIMF"."PAR_ENVIOS_SESIONES" ADD CONSTRAINT "PAR_ENVIOS_SESIONES_PK" PRIMARY KEY ("ID") ENABLE;
 
  ALTER TABLE "UJI_PARANIMF"."PAR_ENVIOS_SESIONES" MODIFY ("ID" NOT NULL ENABLE);
 
  ALTER TABLE "UJI_PARANIMF"."PAR_ENVIOS_SESIONES" MODIFY ("TIPO_ENVIO" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table PAR_EVENTOS
--------------------------------------------------------

  ALTER TABLE "UJI_PARANIMF"."PAR_EVENTOS" ADD CONSTRAINT "PAR_EVENTOS_PK" PRIMARY KEY ("ID") ENABLE;
 
  ALTER TABLE "UJI_PARANIMF"."PAR_EVENTOS" MODIFY ("ID" NOT NULL ENABLE);
 
  ALTER TABLE "UJI_PARANIMF"."PAR_EVENTOS" MODIFY ("TITULO_ES" NOT NULL ENABLE);
 
  ALTER TABLE "UJI_PARANIMF"."PAR_EVENTOS" MODIFY ("TIPO_EVENTO_ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table PAR_LOCALIZACIONES
--------------------------------------------------------

  ALTER TABLE "UJI_PARANIMF"."PAR_LOCALIZACIONES" ADD CONSTRAINT "PAR_LOCALIZACIONES_CODIGO_UK" UNIQUE ("CODIGO") ENABLE;
 
  ALTER TABLE "UJI_PARANIMF"."PAR_LOCALIZACIONES" ADD CONSTRAINT "PAR_LOCALIZACIONES_PK" PRIMARY KEY ("ID") ENABLE;
 
  ALTER TABLE "UJI_PARANIMF"."PAR_LOCALIZACIONES" MODIFY ("ID" NOT NULL ENABLE);
 
  ALTER TABLE "UJI_PARANIMF"."PAR_LOCALIZACIONES" MODIFY ("NOMBRE_ES" NOT NULL ENABLE);
 
  ALTER TABLE "UJI_PARANIMF"."PAR_LOCALIZACIONES" MODIFY ("CODIGO" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table PAR_MAILS
--------------------------------------------------------

  ALTER TABLE "UJI_PARANIMF"."PAR_MAILS" ADD CONSTRAINT "PAR_MAILS_PK" PRIMARY KEY ("ID") ENABLE;
 
  ALTER TABLE "UJI_PARANIMF"."PAR_MAILS" MODIFY ("PARA" NOT NULL ENABLE);
 
  ALTER TABLE "UJI_PARANIMF"."PAR_MAILS" MODIFY ("DE" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table PAR_PLANTAS_SALA
--------------------------------------------------------

  ALTER TABLE "UJI_PARANIMF"."PAR_PLANTAS_SALA" ADD CONSTRAINT "PAR_PLANTAS_SALA_PK" PRIMARY KEY ("ID") ENABLE;
 
  ALTER TABLE "UJI_PARANIMF"."PAR_PLANTAS_SALA" MODIFY ("ID" NOT NULL ENABLE);
 
  ALTER TABLE "UJI_PARANIMF"."PAR_PLANTAS_SALA" MODIFY ("NOMBRE" NOT NULL ENABLE);
 
  ALTER TABLE "UJI_PARANIMF"."PAR_PLANTAS_SALA" MODIFY ("SALA_ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table PAR_PLANTILLAS
--------------------------------------------------------

  ALTER TABLE "UJI_PARANIMF"."PAR_PLANTILLAS" ADD CONSTRAINT "PAR_PLANTILLAS_PK" PRIMARY KEY ("ID") ENABLE;
 
  ALTER TABLE "UJI_PARANIMF"."PAR_PLANTILLAS" ADD CONSTRAINT "PAR_PLANTILLAS_UK1" UNIQUE ("NOMBRE") ENABLE;
 
  ALTER TABLE "UJI_PARANIMF"."PAR_PLANTILLAS" MODIFY ("ID" NOT NULL ENABLE);
 
  ALTER TABLE "UJI_PARANIMF"."PAR_PLANTILLAS" MODIFY ("NOMBRE" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table PAR_PRECIOS_PLANTILLA
--------------------------------------------------------

  ALTER TABLE "UJI_PARANIMF"."PAR_PRECIOS_PLANTILLA" ADD CONSTRAINT "PAR_PRECIOS_PLANTILLA_PK" PRIMARY KEY ("ID") ENABLE;
 
  ALTER TABLE "UJI_PARANIMF"."PAR_PRECIOS_PLANTILLA" MODIFY ("ID" NOT NULL ENABLE);
 
  ALTER TABLE "UJI_PARANIMF"."PAR_PRECIOS_PLANTILLA" MODIFY ("LOCALIZACION_ID" NOT NULL ENABLE);
 
  ALTER TABLE "UJI_PARANIMF"."PAR_PRECIOS_PLANTILLA" MODIFY ("PLANTILLA_ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table PAR_PRECIOS_SESION
--------------------------------------------------------

  ALTER TABLE "UJI_PARANIMF"."PAR_PRECIOS_SESION" ADD CONSTRAINT "PAR_PRECIOS_SESION_PK" PRIMARY KEY ("ID") ENABLE;
 
  ALTER TABLE "UJI_PARANIMF"."PAR_PRECIOS_SESION" MODIFY ("ID" NOT NULL ENABLE);
 
  ALTER TABLE "UJI_PARANIMF"."PAR_PRECIOS_SESION" MODIFY ("LOCALIZACION_ID" NOT NULL ENABLE);
 
  ALTER TABLE "UJI_PARANIMF"."PAR_PRECIOS_SESION" MODIFY ("SESION_ID" NOT NULL ENABLE);
 
  ALTER TABLE "UJI_PARANIMF"."PAR_PRECIOS_SESION" ADD UNIQUE ("LOCALIZACION_ID", "SESION_ID") ENABLE;
 
  ALTER TABLE "UJI_PARANIMF"."PAR_PRECIOS_SESION" ADD UNIQUE ("LOCALIZACION_ID", "SESION_ID", "TARIFA_ID") ENABLE;
--------------------------------------------------------
--  Constraints for Table PAR_SALAS
--------------------------------------------------------

  ALTER TABLE "UJI_PARANIMF"."PAR_SALAS" ADD CONSTRAINT "PAR_SALAS_PK" PRIMARY KEY ("ID") ENABLE;
 
  ALTER TABLE "UJI_PARANIMF"."PAR_SALAS" MODIFY ("ID" NOT NULL ENABLE);
 
  ALTER TABLE "UJI_PARANIMF"."PAR_SALAS" MODIFY ("CODIGO" NOT NULL ENABLE);
 
  ALTER TABLE "UJI_PARANIMF"."PAR_SALAS" MODIFY ("NOMBRE" NOT NULL ENABLE);
 
  ALTER TABLE "UJI_PARANIMF"."PAR_SALAS" MODIFY ("ASIENTOS" NOT NULL ENABLE);
 
  ALTER TABLE "UJI_PARANIMF"."PAR_SALAS" MODIFY ("ASIENTO_DISC" NOT NULL ENABLE);
 
  ALTER TABLE "UJI_PARANIMF"."PAR_SALAS" MODIFY ("ASIENTO_NORES" NOT NULL ENABLE);
 
  ALTER TABLE "UJI_PARANIMF"."PAR_SALAS" MODIFY ("TIPO" NOT NULL ENABLE);
 
  ALTER TABLE "UJI_PARANIMF"."PAR_SALAS" MODIFY ("FORMATO" NOT NULL ENABLE);
 
  ALTER TABLE "UJI_PARANIMF"."PAR_SALAS" MODIFY ("SUBTITULO" NOT NULL ENABLE);
 
  ALTER TABLE "UJI_PARANIMF"."PAR_SALAS" MODIFY ("CINE_ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table PAR_SESIONES
--------------------------------------------------------

  ALTER TABLE "UJI_PARANIMF"."PAR_SESIONES" ADD CONSTRAINT "PAR_SESION_PK" PRIMARY KEY ("ID") ENABLE;
 
  ALTER TABLE "UJI_PARANIMF"."PAR_SESIONES" MODIFY ("ID" NOT NULL ENABLE);
 
  ALTER TABLE "UJI_PARANIMF"."PAR_SESIONES" MODIFY ("EVENTO_ID" NOT NULL ENABLE);
 
  ALTER TABLE "UJI_PARANIMF"."PAR_SESIONES" MODIFY ("FECHA_CELEBRACION" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table PAR_TARIFAS
--------------------------------------------------------

  ALTER TABLE "UJI_PARANIMF"."PAR_TARIFAS" ADD CONSTRAINT "PAR_TARIFAS_pkey" PRIMARY KEY ("ID") ENABLE;
 
  ALTER TABLE "UJI_PARANIMF"."PAR_TARIFAS" MODIFY ("ID" NOT NULL ENABLE);
 
  ALTER TABLE "UJI_PARANIMF"."PAR_TARIFAS" MODIFY ("NOMBRE" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table PAR_TIPOS_EVENTO
--------------------------------------------------------

  ALTER TABLE "UJI_PARANIMF"."PAR_TIPOS_EVENTO" ADD CONSTRAINT "PAR_TIPOS_EVENTO_PK" PRIMARY KEY ("ID") ENABLE;
 
  ALTER TABLE "UJI_PARANIMF"."PAR_TIPOS_EVENTO" MODIFY ("ID" NOT NULL ENABLE);
 
  ALTER TABLE "UJI_PARANIMF"."PAR_TIPOS_EVENTO" MODIFY ("NOMBRE_ES" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table PAR_USUARIOS
--------------------------------------------------------

  ALTER TABLE "UJI_PARANIMF"."PAR_USUARIOS" ADD CONSTRAINT "PAR_USERS_PK" PRIMARY KEY ("ID") ENABLE;
 
  ALTER TABLE "UJI_PARANIMF"."PAR_USUARIOS" ADD CONSTRAINT "PAR_USERS_UK1" UNIQUE ("USUARIO") ENABLE;
 
  ALTER TABLE "UJI_PARANIMF"."PAR_USUARIOS" MODIFY ("ID" NOT NULL ENABLE);
 
  ALTER TABLE "UJI_PARANIMF"."PAR_USUARIOS" MODIFY ("NOMBRE" NOT NULL ENABLE);
 
  ALTER TABLE "UJI_PARANIMF"."PAR_USUARIOS" MODIFY ("USUARIO" NOT NULL ENABLE);
 
  ALTER TABLE "UJI_PARANIMF"."PAR_USUARIOS" MODIFY ("MAIL" NOT NULL ENABLE);
--------------------------------------------------------
--  DDL for Index PAR_BUTACAS_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "UJI_PARANIMF"."PAR_BUTACAS_PK" ON "UJI_PARANIMF"."PAR_BUTACAS" ("ID");
--------------------------------------------------------
--  DDL for Index PAR_BUTACAS_UK
--------------------------------------------------------

  CREATE UNIQUE INDEX "UJI_PARANIMF"."PAR_BUTACAS_UK" ON "UJI_PARANIMF"."PAR_BUTACAS" ("SESION_ID", "LOCALIZACION_ID", NVL2("FILA","FILA",TO_CHAR("ID"+1000)), NVL2("NUMERO","NUMERO",TO_CHAR("ID"+1000)), DECODE("ANULADA",1,TO_CHAR("ID"+1000),'1'));
--------------------------------------------------------
--  DDL for Index PAR_CINES_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "UJI_PARANIMF"."PAR_CINES_PK" ON "UJI_PARANIMF"."PAR_CINES" ("ID");
--------------------------------------------------------
--  DDL for Index PAR_COMPRAS_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "UJI_PARANIMF"."PAR_COMPRAS_PK" ON "UJI_PARANIMF"."PAR_COMPRAS" ("ID");
--------------------------------------------------------
--  DDL for Index PAR_ENVIOS_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "UJI_PARANIMF"."PAR_ENVIOS_PK" ON "UJI_PARANIMF"."PAR_ENVIOS" ("ID");
--------------------------------------------------------
--  DDL for Index PAR_ENVIOS_SESIONES_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "UJI_PARANIMF"."PAR_ENVIOS_SESIONES_PK" ON "UJI_PARANIMF"."PAR_ENVIOS_SESIONES" ("ID");
--------------------------------------------------------
--  DDL for Index PAR_EVENTOS_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "UJI_PARANIMF"."PAR_EVENTOS_PK" ON "UJI_PARANIMF"."PAR_EVENTOS" ("ID");
--------------------------------------------------------
--  DDL for Index PAR_LOCALIZACIONES_CODIGO_UK
--------------------------------------------------------

  CREATE UNIQUE INDEX "UJI_PARANIMF"."PAR_LOCALIZACIONES_CODIGO_UK" ON "UJI_PARANIMF"."PAR_LOCALIZACIONES" ("CODIGO");
--------------------------------------------------------
--  DDL for Index PAR_LOCALIZACIONES_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "UJI_PARANIMF"."PAR_LOCALIZACIONES_PK" ON "UJI_PARANIMF"."PAR_LOCALIZACIONES" ("ID");
--------------------------------------------------------
--  DDL for Index PAR_MAILS_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "UJI_PARANIMF"."PAR_MAILS_PK" ON "UJI_PARANIMF"."PAR_MAILS" ("ID");
--------------------------------------------------------
--  DDL for Index PAR_PLANTAS_SALA_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "UJI_PARANIMF"."PAR_PLANTAS_SALA_PK" ON "UJI_PARANIMF"."PAR_PLANTAS_SALA" ("ID");
--------------------------------------------------------
--  DDL for Index PAR_PLANTILLAS_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "UJI_PARANIMF"."PAR_PLANTILLAS_PK" ON "UJI_PARANIMF"."PAR_PLANTILLAS" ("ID");
--------------------------------------------------------
--  DDL for Index PAR_PLANTILLAS_UK1
--------------------------------------------------------

  CREATE UNIQUE INDEX "UJI_PARANIMF"."PAR_PLANTILLAS_UK1" ON "UJI_PARANIMF"."PAR_PLANTILLAS" ("NOMBRE");
--------------------------------------------------------
--  DDL for Index PAR_PRECIOS_PLANTILLA_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "UJI_PARANIMF"."PAR_PRECIOS_PLANTILLA_PK" ON "UJI_PARANIMF"."PAR_PRECIOS_PLANTILLA" ("ID");
--------------------------------------------------------
--  DDL for Index PAR_PRECIOS_SESION_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "UJI_PARANIMF"."PAR_PRECIOS_SESION_PK" ON "UJI_PARANIMF"."PAR_PRECIOS_SESION" ("ID");
--------------------------------------------------------
--  DDL for Index PAR_SALAS_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "UJI_PARANIMF"."PAR_SALAS_PK" ON "UJI_PARANIMF"."PAR_SALAS" ("ID");
--------------------------------------------------------
--  DDL for Index PAR_SESION_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "UJI_PARANIMF"."PAR_SESION_PK" ON "UJI_PARANIMF"."PAR_SESIONES" ("ID");
--------------------------------------------------------
--  DDL for Index PAR_TARIFAS_pkey
--------------------------------------------------------

  CREATE UNIQUE INDEX "UJI_PARANIMF"."PAR_TARIFAS_pkey" ON "UJI_PARANIMF"."PAR_TARIFAS" ("ID");
--------------------------------------------------------
--  DDL for Index PAR_TIPOS_EVENTO_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "UJI_PARANIMF"."PAR_TIPOS_EVENTO_PK" ON "UJI_PARANIMF"."PAR_TIPOS_EVENTO" ("ID");
--------------------------------------------------------
--  DDL for Index PAR_USERS_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "UJI_PARANIMF"."PAR_USERS_PK" ON "UJI_PARANIMF"."PAR_USUARIOS" ("ID");
--------------------------------------------------------
--  DDL for Index PAR_USERS_UK1
--------------------------------------------------------

  CREATE UNIQUE INDEX "UJI_PARANIMF"."PAR_USERS_UK1" ON "UJI_PARANIMF"."PAR_USUARIOS" ("USUARIO");
--------------------------------------------------------
--  Ref Constraints for Table PAR_BUTACAS
--------------------------------------------------------

  ALTER TABLE "UJI_PARANIMF"."PAR_BUTACAS" ADD CONSTRAINT "PAR_BUTACAS_CO_FK1" FOREIGN KEY ("COMPRA_ID");
	  REFERENCES "UJI_PARANIMF"."PAR_COMPRAS" ("ID") ON DELETE CASCADE ENABLE;
 
  ALTER TABLE "UJI_PARANIMF"."PAR_BUTACAS" ADD CONSTRAINT "PAR_BUTACAS_LO_FK1" FOREIGN KEY ("LOCALIZACION_ID");
	  REFERENCES "UJI_PARANIMF"."PAR_LOCALIZACIONES" ("ID") ENABLE;
 
  ALTER TABLE "UJI_PARANIMF"."PAR_BUTACAS" ADD CONSTRAINT "PAR_BUTACAS_SE_FK1" FOREIGN KEY ("SESION_ID");
	  REFERENCES "UJI_PARANIMF"."PAR_SESIONES" ("ID") ON DELETE CASCADE ENABLE;

--------------------------------------------------------
--  Ref Constraints for Table PAR_COMPRAS
--------------------------------------------------------

  ALTER TABLE "UJI_PARANIMF"."PAR_COMPRAS" ADD CONSTRAINT "PAR_COMPRAS_SE_FK1" FOREIGN KEY ("SESION_ID");
	  REFERENCES "UJI_PARANIMF"."PAR_SESIONES" ("ID") ON DELETE CASCADE ENABLE;

--------------------------------------------------------
--  Ref Constraints for Table PAR_ENVIOS_SESIONES
--------------------------------------------------------

  ALTER TABLE "UJI_PARANIMF"."PAR_ENVIOS_SESIONES" ADD CONSTRAINT "PAR_ENV_SESS_PAR_ENVIO_ID_fkey" FOREIGN KEY ("PAR_ENVIO_ID");
	  REFERENCES "UJI_PARANIMF"."PAR_ENVIOS" ("ID") ON DELETE CASCADE ENABLE;
 
  ALTER TABLE "UJI_PARANIMF"."PAR_ENVIOS_SESIONES" ADD CONSTRAINT "PAR_ENV_SES_PAR_SESION_ID_fkey" FOREIGN KEY ("PAR_SESION_ID");
	  REFERENCES "UJI_PARANIMF"."PAR_SESIONES" ("ID") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table PAR_EVENTOS
--------------------------------------------------------

  ALTER TABLE "UJI_PARANIMF"."PAR_EVENTOS" ADD CONSTRAINT "PAR_EVENTOS_PAR_TIPOS_EVE_FK1" FOREIGN KEY ("TIPO_EVENTO_ID");
	  REFERENCES "UJI_PARANIMF"."PAR_TIPOS_EVENTO" ("ID") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table PAR_LOCALIZACIONES
--------------------------------------------------------

  ALTER TABLE "UJI_PARANIMF"."PAR_LOCALIZACIONES" ADD CONSTRAINT "PAR_LOCALIZACIONES_SALAS_fkey" FOREIGN KEY ("SALA_ID");
	  REFERENCES "UJI_PARANIMF"."PAR_SALAS" ("ID") ON DELETE CASCADE ENABLE;

--------------------------------------------------------
--  Ref Constraints for Table PAR_PLANTAS_SALA
--------------------------------------------------------

  ALTER TABLE "UJI_PARANIMF"."PAR_PLANTAS_SALA" ADD CONSTRAINT "PAR_PLANTAS_SALA_PAR_SALAS_FK1" FOREIGN KEY ("SALA_ID");
	  REFERENCES "UJI_PARANIMF"."PAR_PLANTAS_SALA" ("ID") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table PAR_PLANTILLAS
--------------------------------------------------------

  ALTER TABLE "UJI_PARANIMF"."PAR_PLANTILLAS" ADD CONSTRAINT "PAR_PLANTILLAS_SALAS_fkey" FOREIGN KEY ("SALA_ID");
	  REFERENCES "UJI_PARANIMF"."PAR_SALAS" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table PAR_PRECIOS_PLANTILLA
--------------------------------------------------------

  ALTER TABLE "UJI_PARANIMF"."PAR_PRECIOS_PLANTILLA" ADD CONSTRAINT "PAR_PRECIOS_PLANTILLA_PAR_FK1" FOREIGN KEY ("LOCALIZACION_ID");
	  REFERENCES "UJI_PARANIMF"."PAR_LOCALIZACIONES" ("ID") ENABLE;
 
  ALTER TABLE "UJI_PARANIMF"."PAR_PRECIOS_PLANTILLA" ADD CONSTRAINT "PAR_PRECIOS_PLANTILLA_PAR_FK2" FOREIGN KEY ("PLANTILLA_ID");
	  REFERENCES "UJI_PARANIMF"."PAR_PLANTILLAS" ("ID") ON DELETE CASCADE ENABLE;
 
  ALTER TABLE "UJI_PARANIMF"."PAR_PRECIOS_PLANTILLA" ADD CONSTRAINT "PAR_PRECIOS_PLANTILLA_fk3" FOREIGN KEY ("TARIFA_ID");
	  REFERENCES "UJI_PARANIMF"."PAR_TARIFAS" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table PAR_PRECIOS_SESION
--------------------------------------------------------

  ALTER TABLE "UJI_PARANIMF"."PAR_PRECIOS_SESION" ADD CONSTRAINT "PAR_PRECIOS_SESION_PAR_LO_FK1" FOREIGN KEY ("LOCALIZACION_ID");
	  REFERENCES "UJI_PARANIMF"."PAR_LOCALIZACIONES" ("ID") ENABLE;
 
  ALTER TABLE "UJI_PARANIMF"."PAR_PRECIOS_SESION" ADD CONSTRAINT "PAR_PRECIOS_SESION_PAR_SE_FK1" FOREIGN KEY ("SESION_ID");
	  REFERENCES "UJI_PARANIMF"."PAR_SESIONES" ("ID") ON DELETE CASCADE ENABLE;
 
  ALTER TABLE "UJI_PARANIMF"."PAR_PRECIOS_SESION" ADD CONSTRAINT "PAR_PRECIOS_SESION_par_ta_fk2" FOREIGN KEY ("TARIFA_ID");
	  REFERENCES "UJI_PARANIMF"."PAR_TARIFAS" ("ID") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table PAR_SALAS
--------------------------------------------------------

  ALTER TABLE "UJI_PARANIMF"."PAR_SALAS" ADD CONSTRAINT "PAR_CINES_PAR_SALAS_FK1" FOREIGN KEY ("CINE_ID");
	  REFERENCES "UJI_PARANIMF"."PAR_CINES" ("ID") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table PAR_SESIONES
--------------------------------------------------------

  ALTER TABLE "UJI_PARANIMF"."PAR_SESIONES" ADD CONSTRAINT "PAR_SESIONES_PAR_EVENTOS_FK1" FOREIGN KEY ("EVENTO_ID");
	  REFERENCES "UJI_PARANIMF"."PAR_EVENTOS" ("ID") ON DELETE CASCADE ENABLE;
 
  ALTER TABLE "UJI_PARANIMF"."PAR_SESIONES" ADD CONSTRAINT "PAR_SESIONES_PAR_PLANTILL_FK1" FOREIGN KEY ("PLANTILLA_ID");
	  REFERENCES "UJI_PARANIMF"."PAR_PLANTILLAS" ("ID") ENABLE;
 
  ALTER TABLE "UJI_PARANIMF"."PAR_SESIONES" ADD CONSTRAINT "PAR_SESIONES_PAR_SALAS_FK1" FOREIGN KEY ("SALA_ID");
	  REFERENCES "UJI_PARANIMF"."PAR_SALAS" ("ID") ENABLE;


	  
CREATE TABLE "UJI_PARANIMF"."PAR_SESIONES_FORMATO_IDI_ICAA"
(
  "ID" NUMBER NOT NULL,
  "FORMATO" varchar2(400) NOT NULL,
  "VER_LING" varchar2(400) NOT NULL,
  "EVENTO_ID" NUMBER NOT NULL,
  CONSTRAINT "par_ses_for_idi_icaa_pkey" PRIMARY KEY ("ID"),
  CONSTRAINT "par_ses_icaa_ev_id_fkey" FOREIGN KEY ("EVENTO_ID")
      REFERENCES "PAR_EVENTOS" ("ID")
      ON DELETE CASCADE ENABLE
);

--------------------------------------------------------
--  DDL for Synonymn APA_VW_PERSONAS_ITEMS
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "UJI_PARANIMF"."APA_VW_PERSONAS_ITEMS" FOR "UJI_APA"."APA_VW_PERSONAS_ITEMS";
