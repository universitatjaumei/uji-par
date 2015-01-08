--------------------------------------------------------
--  File created - lunes-septiembre-01-2014   
--------------------------------------------------------
  DROP TABLE "PAR_BUTACAS" cascade constraints;
  DROP TABLE "PAR_CINES" cascade constraints;
  DROP TABLE "PAR_COMPRAS" cascade constraints;
  DROP TABLE "PAR_ENVIOS" cascade constraints;
  DROP TABLE "PAR_ENVIOS_SESIONES" cascade constraints;
  DROP TABLE "PAR_EVENTOS" cascade constraints;
  DROP TABLE "PAR_LOCALIZACIONES" cascade constraints;
  DROP TABLE "PAR_MAILS" cascade constraints;
  DROP TABLE "PAR_PLANTAS_SALA" cascade constraints;
  DROP TABLE "PAR_PLANTILLAS" cascade constraints;
  DROP TABLE "PAR_PRECIOS_PLANTILLA" cascade constraints;
  DROP TABLE "PAR_PRECIOS_SESION" cascade constraints;
  DROP TABLE "PAR_SALAS" cascade constraints;
  DROP TABLE "PAR_SESIONES" cascade constraints;
  DROP TABLE "PAR_TARIFAS" cascade constraints;
  DROP TABLE "PAR_TIPOS_EVENTO" cascade constraints;
  DROP TABLE "PAR_USUARIOS" cascade constraints;
  DROP SYNONYM "APA_VW_PERSONAS_ITEMS";
  DROP SEQUENCE "HIBERNATE_SEQUENCE";
--------------------------------------------------------
--  DDL for Sequence HIBERNATE_SEQUENCE
--------------------------------------------------------

   CREATE SEQUENCE  "HIBERNATE_SEQUENCE"  MINVALUE 1 MAXVALUE 999999999999999999999999999 INCREMENT BY 1 START WITH 1402 CACHE 20 NOORDER  NOCYCLE;
--------------------------------------------------------
--  DDL for Table PAR_BUTACAS
--------------------------------------------------------

  CREATE TABLE "PAR_BUTACAS" 
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

  CREATE TABLE "PAR_CINES" 
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

  CREATE TABLE "PAR_COMPRAS" 
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

  CREATE TABLE "PAR_ENVIOS" 
   (	"ID" NUMBER, 
	"FECHA_GENERACION_FICHERO" TIMESTAMP (6), 
	"FECHA_ENVIO_FICHERO" TIMESTAMP (6)
   );
--------------------------------------------------------
--  DDL for Table PAR_ENVIOS_SESIONES
--------------------------------------------------------

  CREATE TABLE "PAR_ENVIOS_SESIONES" 
   (	"ID" NUMBER, 
	"PAR_ENVIO_ID" NUMBER(*,0), 
	"PAR_SESION_ID" NUMBER(*,0), 
	"TIPO_ENVIO" VARCHAR2(2)
   );
--------------------------------------------------------
--  DDL for Table PAR_EVENTOS
--------------------------------------------------------

  CREATE TABLE "PAR_EVENTOS" 
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

  CREATE TABLE "PAR_LOCALIZACIONES" 
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

  CREATE TABLE "PAR_MAILS" 
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

  CREATE TABLE "PAR_PLANTAS_SALA" 
   (	"ID" NUMBER, 
	"NOMBRE" VARCHAR2(400), 
	"SALA_ID" NUMBER
   );
--------------------------------------------------------
--  DDL for Table PAR_PLANTILLAS
--------------------------------------------------------

  CREATE TABLE "PAR_PLANTILLAS" 
   (	"ID" NUMBER, 
	"NOMBRE" VARCHAR2(255), 
	"SALA_ID" NUMBER
   );
--------------------------------------------------------
--  DDL for Table PAR_PRECIOS_PLANTILLA
--------------------------------------------------------

  CREATE TABLE "PAR_PRECIOS_PLANTILLA" 
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

  CREATE TABLE "PAR_PRECIOS_SESION" 
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

  CREATE TABLE "PAR_SALAS" 
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

  CREATE TABLE "PAR_SESIONES" 
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

  CREATE TABLE "PAR_TARIFAS" 
   (	"ID" NUMBER, 
	"NOMBRE" VARCHAR2(100), 
	"ISPUBLICA" NUMBER(1,0) DEFAULT 1, 
	"DEFECTO" NUMBER(1,0) DEFAULT 0
   );
--------------------------------------------------------
--  DDL for Table PAR_TIPOS_EVENTO
--------------------------------------------------------

  CREATE TABLE "PAR_TIPOS_EVENTO" 
   (	"ID" NUMBER, 
	"NOMBRE_ES" VARCHAR2(50), 
	"NOMBRE_VA" VARCHAR2(50), 
	"EXPORTAR_ICAA" NUMBER(1,0) DEFAULT 0
   );
--------------------------------------------------------
--  DDL for Table PAR_USUARIOS
--------------------------------------------------------

  CREATE TABLE "PAR_USUARIOS" 
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

  ALTER TABLE "PAR_BUTACAS" ADD CONSTRAINT "PAR_BUTACAS_PK" PRIMARY KEY ("ID") ENABLE;
 
  ALTER TABLE "PAR_BUTACAS" MODIFY ("COMPRA_ID" NOT NULL ENABLE);
 
  ALTER TABLE "PAR_BUTACAS" MODIFY ("ID" NOT NULL ENABLE);
 
  ALTER TABLE "PAR_BUTACAS" MODIFY ("PRECIO" NOT NULL ENABLE);

--------------------------------------------------------
--  Constraints for Table PAR_CINES
--------------------------------------------------------

  ALTER TABLE "PAR_CINES" ADD CONSTRAINT "PAR_CINES_PK" PRIMARY KEY ("ID") ENABLE;
 
  ALTER TABLE "PAR_CINES" MODIFY ("ID" NOT NULL ENABLE);
 
  ALTER TABLE "PAR_CINES" MODIFY ("CODIGO" NOT NULL ENABLE);
 
  ALTER TABLE "PAR_CINES" MODIFY ("NOMBRE" NOT NULL ENABLE);
 
  ALTER TABLE "PAR_CINES" MODIFY ("CIF" NOT NULL ENABLE);
 
  ALTER TABLE "PAR_CINES" MODIFY ("DIRECCION" NOT NULL ENABLE);
 
  ALTER TABLE "PAR_CINES" MODIFY ("COD_MUNICIPIO" NOT NULL ENABLE);
 
  ALTER TABLE "PAR_CINES" MODIFY ("NOM_MUNICIPIO" NOT NULL ENABLE);
 
  ALTER TABLE "PAR_CINES" MODIFY ("CP" NOT NULL ENABLE);
 
  ALTER TABLE "PAR_CINES" MODIFY ("EMPRESA" NOT NULL ENABLE);
 
  ALTER TABLE "PAR_CINES" MODIFY ("COD_REGISTRO" NOT NULL ENABLE);
 
  ALTER TABLE "PAR_CINES" MODIFY ("TFNO" NOT NULL ENABLE);
 
  ALTER TABLE "PAR_CINES" MODIFY ("IVA" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table PAR_COMPRAS
--------------------------------------------------------

  ALTER TABLE "PAR_COMPRAS" ADD CONSTRAINT "PAR_COMPRAS_PK" PRIMARY KEY ("ID") ENABLE;
 
  ALTER TABLE "PAR_COMPRAS" MODIFY ("FECHA" NOT NULL ENABLE);
 
  ALTER TABLE "PAR_COMPRAS" MODIFY ("TAQUILLA" NOT NULL ENABLE);
 
  ALTER TABLE "PAR_COMPRAS" MODIFY ("SESION_ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table PAR_ENVIOS
--------------------------------------------------------

  ALTER TABLE "PAR_ENVIOS" ADD CONSTRAINT "PAR_ENVIOS_PK" PRIMARY KEY ("ID") ENABLE;
 
  ALTER TABLE "PAR_ENVIOS" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table PAR_ENVIOS_SESIONES
--------------------------------------------------------

  ALTER TABLE "PAR_ENVIOS_SESIONES" ADD CONSTRAINT "PAR_ENVIOS_SESIONES_PK" PRIMARY KEY ("ID") ENABLE;
 
  ALTER TABLE "PAR_ENVIOS_SESIONES" MODIFY ("ID" NOT NULL ENABLE);
 
  ALTER TABLE "PAR_ENVIOS_SESIONES" MODIFY ("TIPO_ENVIO" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table PAR_EVENTOS
--------------------------------------------------------

  ALTER TABLE "PAR_EVENTOS" ADD CONSTRAINT "PAR_EVENTOS_PK" PRIMARY KEY ("ID") ENABLE;
 
  ALTER TABLE "PAR_EVENTOS" MODIFY ("ID" NOT NULL ENABLE);
 
  ALTER TABLE "PAR_EVENTOS" MODIFY ("TITULO_ES" NOT NULL ENABLE);
 
  ALTER TABLE "PAR_EVENTOS" MODIFY ("TIPO_EVENTO_ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table PAR_LOCALIZACIONES
--------------------------------------------------------

  ALTER TABLE "PAR_LOCALIZACIONES" ADD CONSTRAINT "PAR_LOCALIZACIONES_CODIGO_UK" UNIQUE ("CODIGO") ENABLE;
 
  ALTER TABLE "PAR_LOCALIZACIONES" ADD CONSTRAINT "PAR_LOCALIZACIONES_PK" PRIMARY KEY ("ID") ENABLE;
 
  ALTER TABLE "PAR_LOCALIZACIONES" MODIFY ("ID" NOT NULL ENABLE);
 
  ALTER TABLE "PAR_LOCALIZACIONES" MODIFY ("NOMBRE_ES" NOT NULL ENABLE);
 
  ALTER TABLE "PAR_LOCALIZACIONES" MODIFY ("CODIGO" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table PAR_MAILS
--------------------------------------------------------

  ALTER TABLE "PAR_MAILS" ADD CONSTRAINT "PAR_MAILS_PK" PRIMARY KEY ("ID") ENABLE;
 
  ALTER TABLE "PAR_MAILS" MODIFY ("PARA" NOT NULL ENABLE);
 
  ALTER TABLE "PAR_MAILS" MODIFY ("DE" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table PAR_PLANTAS_SALA
--------------------------------------------------------

  ALTER TABLE "PAR_PLANTAS_SALA" ADD CONSTRAINT "PAR_PLANTAS_SALA_PK" PRIMARY KEY ("ID") ENABLE;
 
  ALTER TABLE "PAR_PLANTAS_SALA" MODIFY ("ID" NOT NULL ENABLE);
 
  ALTER TABLE "PAR_PLANTAS_SALA" MODIFY ("NOMBRE" NOT NULL ENABLE);
 
  ALTER TABLE "PAR_PLANTAS_SALA" MODIFY ("SALA_ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table PAR_PLANTILLAS
--------------------------------------------------------

  ALTER TABLE "PAR_PLANTILLAS" ADD CONSTRAINT "PAR_PLANTILLAS_PK" PRIMARY KEY ("ID") ENABLE;
 
  ALTER TABLE "PAR_PLANTILLAS" ADD CONSTRAINT "PAR_PLANTILLAS_UK1" UNIQUE ("NOMBRE") ENABLE;
 
  ALTER TABLE "PAR_PLANTILLAS" MODIFY ("ID" NOT NULL ENABLE);
 
  ALTER TABLE "PAR_PLANTILLAS" MODIFY ("NOMBRE" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table PAR_PRECIOS_PLANTILLA
--------------------------------------------------------

  ALTER TABLE "PAR_PRECIOS_PLANTILLA" ADD CONSTRAINT "PAR_PRECIOS_PLANTILLA_PK" PRIMARY KEY ("ID") ENABLE;
 
  ALTER TABLE "PAR_PRECIOS_PLANTILLA" MODIFY ("ID" NOT NULL ENABLE);
 
  ALTER TABLE "PAR_PRECIOS_PLANTILLA" MODIFY ("LOCALIZACION_ID" NOT NULL ENABLE);
 
  ALTER TABLE "PAR_PRECIOS_PLANTILLA" MODIFY ("PLANTILLA_ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table PAR_PRECIOS_SESION
--------------------------------------------------------

  ALTER TABLE "PAR_PRECIOS_SESION" ADD CONSTRAINT "PAR_PRECIOS_SESION_PK" PRIMARY KEY ("ID") ENABLE;
 
  ALTER TABLE "PAR_PRECIOS_SESION" MODIFY ("ID" NOT NULL ENABLE);
 
  ALTER TABLE "PAR_PRECIOS_SESION" MODIFY ("LOCALIZACION_ID" NOT NULL ENABLE);
 
  ALTER TABLE "PAR_PRECIOS_SESION" MODIFY ("SESION_ID" NOT NULL ENABLE);
 
  ALTER TABLE "PAR_PRECIOS_SESION" ADD UNIQUE ("LOCALIZACION_ID", "SESION_ID") ENABLE;
 
  ALTER TABLE "PAR_PRECIOS_SESION" ADD UNIQUE ("LOCALIZACION_ID", "SESION_ID", "TARIFA_ID") ENABLE;
--------------------------------------------------------
--  Constraints for Table PAR_SALAS
--------------------------------------------------------

  ALTER TABLE "PAR_SALAS" ADD CONSTRAINT "PAR_SALAS_PK" PRIMARY KEY ("ID") ENABLE;
 
  ALTER TABLE "PAR_SALAS" MODIFY ("ID" NOT NULL ENABLE);
 
  ALTER TABLE "PAR_SALAS" MODIFY ("CODIGO" NOT NULL ENABLE);
 
  ALTER TABLE "PAR_SALAS" MODIFY ("NOMBRE" NOT NULL ENABLE);
 
  ALTER TABLE "PAR_SALAS" MODIFY ("ASIENTOS" NOT NULL ENABLE);
 
  ALTER TABLE "PAR_SALAS" MODIFY ("ASIENTO_DISC" NOT NULL ENABLE);
 
  ALTER TABLE "PAR_SALAS" MODIFY ("ASIENTO_NORES" NOT NULL ENABLE);
 
  ALTER TABLE "PAR_SALAS" MODIFY ("TIPO" NOT NULL ENABLE);
 
  ALTER TABLE "PAR_SALAS" MODIFY ("FORMATO" NOT NULL ENABLE);
 
  ALTER TABLE "PAR_SALAS" MODIFY ("SUBTITULO" NOT NULL ENABLE);
 
  ALTER TABLE "PAR_SALAS" MODIFY ("CINE_ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table PAR_SESIONES
--------------------------------------------------------

  ALTER TABLE "PAR_SESIONES" ADD CONSTRAINT "PAR_SESION_PK" PRIMARY KEY ("ID") ENABLE;
 
  ALTER TABLE "PAR_SESIONES" MODIFY ("ID" NOT NULL ENABLE);
 
  ALTER TABLE "PAR_SESIONES" MODIFY ("EVENTO_ID" NOT NULL ENABLE);
 
  ALTER TABLE "PAR_SESIONES" MODIFY ("FECHA_CELEBRACION" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table PAR_TARIFAS
--------------------------------------------------------

  ALTER TABLE "PAR_TARIFAS" ADD CONSTRAINT "PAR_TARIFAS_pkey" PRIMARY KEY ("ID") ENABLE;
 
  ALTER TABLE "PAR_TARIFAS" MODIFY ("ID" NOT NULL ENABLE);
 
  ALTER TABLE "PAR_TARIFAS" MODIFY ("NOMBRE" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table PAR_TIPOS_EVENTO
--------------------------------------------------------

  ALTER TABLE "PAR_TIPOS_EVENTO" ADD CONSTRAINT "PAR_TIPOS_EVENTO_PK" PRIMARY KEY ("ID") ENABLE;
 
  ALTER TABLE "PAR_TIPOS_EVENTO" MODIFY ("ID" NOT NULL ENABLE);
 
  ALTER TABLE "PAR_TIPOS_EVENTO" MODIFY ("NOMBRE_ES" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table PAR_USUARIOS
--------------------------------------------------------

  ALTER TABLE "PAR_USUARIOS" ADD CONSTRAINT "PAR_USERS_PK" PRIMARY KEY ("ID") ENABLE;
 
  ALTER TABLE "PAR_USUARIOS" ADD CONSTRAINT "PAR_USERS_UK1" UNIQUE ("USUARIO") ENABLE;
 
  ALTER TABLE "PAR_USUARIOS" MODIFY ("ID" NOT NULL ENABLE);
 
  ALTER TABLE "PAR_USUARIOS" MODIFY ("NOMBRE" NOT NULL ENABLE);
 
  ALTER TABLE "PAR_USUARIOS" MODIFY ("USUARIO" NOT NULL ENABLE);
 
  ALTER TABLE "PAR_USUARIOS" MODIFY ("MAIL" NOT NULL ENABLE);
--------------------------------------------------------
--  DDL for Index PAR_BUTACAS_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "PAR_BUTACAS_PK" ON "PAR_BUTACAS" ("ID");
--------------------------------------------------------
--  DDL for Index PAR_BUTACAS_UK
--------------------------------------------------------

  CREATE UNIQUE INDEX "PAR_BUTACAS_UK" ON "PAR_BUTACAS" ("SESION_ID", "LOCALIZACION_ID", NVL2("FILA","FILA",TO_CHAR("ID"+1000)), NVL2("NUMERO","NUMERO",TO_CHAR("ID"+1000)), DECODE("ANULADA",1,TO_CHAR("ID"+1000),'1'));
--------------------------------------------------------
--  DDL for Index PAR_CINES_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "PAR_CINES_PK" ON "PAR_CINES" ("ID");
--------------------------------------------------------
--  DDL for Index PAR_COMPRAS_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "PAR_COMPRAS_PK" ON "PAR_COMPRAS" ("ID");
--------------------------------------------------------
--  DDL for Index PAR_ENVIOS_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "PAR_ENVIOS_PK" ON "PAR_ENVIOS" ("ID");
--------------------------------------------------------
--  DDL for Index PAR_ENVIOS_SESIONES_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "PAR_ENVIOS_SESIONES_PK" ON "PAR_ENVIOS_SESIONES" ("ID");
--------------------------------------------------------
--  DDL for Index PAR_EVENTOS_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "PAR_EVENTOS_PK" ON "PAR_EVENTOS" ("ID");
--------------------------------------------------------
--  DDL for Index PAR_LOCALIZACIONES_CODIGO_UK
--------------------------------------------------------

  CREATE UNIQUE INDEX "PAR_LOCALIZACIONES_CODIGO_UK" ON "PAR_LOCALIZACIONES" ("CODIGO");
--------------------------------------------------------
--  DDL for Index PAR_LOCALIZACIONES_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "PAR_LOCALIZACIONES_PK" ON "PAR_LOCALIZACIONES" ("ID");
--------------------------------------------------------
--  DDL for Index PAR_MAILS_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "PAR_MAILS_PK" ON "PAR_MAILS" ("ID");
--------------------------------------------------------
--  DDL for Index PAR_PLANTAS_SALA_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "PAR_PLANTAS_SALA_PK" ON "PAR_PLANTAS_SALA" ("ID");
--------------------------------------------------------
--  DDL for Index PAR_PLANTILLAS_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "PAR_PLANTILLAS_PK" ON "PAR_PLANTILLAS" ("ID");
--------------------------------------------------------
--  DDL for Index PAR_PLANTILLAS_UK1
--------------------------------------------------------

  CREATE UNIQUE INDEX "PAR_PLANTILLAS_UK1" ON "PAR_PLANTILLAS" ("NOMBRE");
--------------------------------------------------------
--  DDL for Index PAR_PRECIOS_PLANTILLA_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "PAR_PRECIOS_PLANTILLA_PK" ON "PAR_PRECIOS_PLANTILLA" ("ID");
--------------------------------------------------------
--  DDL for Index PAR_PRECIOS_SESION_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "PAR_PRECIOS_SESION_PK" ON "PAR_PRECIOS_SESION" ("ID");
--------------------------------------------------------
--  DDL for Index PAR_SALAS_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "PAR_SALAS_PK" ON "PAR_SALAS" ("ID");
--------------------------------------------------------
--  DDL for Index PAR_SESION_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "PAR_SESION_PK" ON "PAR_SESIONES" ("ID");
--------------------------------------------------------
--  DDL for Index PAR_TARIFAS_pkey
--------------------------------------------------------

  CREATE UNIQUE INDEX "PAR_TARIFAS_pkey" ON "PAR_TARIFAS" ("ID");
--------------------------------------------------------
--  DDL for Index PAR_TIPOS_EVENTO_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "PAR_TIPOS_EVENTO_PK" ON "PAR_TIPOS_EVENTO" ("ID");
--------------------------------------------------------
--  DDL for Index PAR_USERS_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "PAR_USERS_PK" ON "PAR_USUARIOS" ("ID");
--------------------------------------------------------
--  DDL for Index PAR_USERS_UK1
--------------------------------------------------------

  CREATE UNIQUE INDEX "PAR_USERS_UK1" ON "PAR_USUARIOS" ("USUARIO");
--------------------------------------------------------
--  Ref Constraints for Table PAR_BUTACAS
--------------------------------------------------------

  ALTER TABLE "PAR_BUTACAS" ADD CONSTRAINT "PAR_BUTACAS_CO_FK1" FOREIGN KEY ("COMPRA_ID")
	  REFERENCES "PAR_COMPRAS" ("ID") ON DELETE CASCADE ENABLE;

  ALTER TABLE "PAR_BUTACAS" ADD CONSTRAINT "PAR_BUTACAS_LO_FK1" FOREIGN KEY ("LOCALIZACION_ID")
	  REFERENCES "PAR_LOCALIZACIONES" ("ID") ENABLE;

  ALTER TABLE "PAR_BUTACAS" ADD CONSTRAINT "PAR_BUTACAS_SE_FK1" FOREIGN KEY ("SESION_ID")
	  REFERENCES "PAR_SESIONES" ("ID") ON DELETE CASCADE ENABLE;

--------------------------------------------------------
--  Ref Constraints for Table PAR_COMPRAS
--------------------------------------------------------

  ALTER TABLE "PAR_COMPRAS" ADD CONSTRAINT "PAR_COMPRAS_SE_FK1" FOREIGN KEY ("SESION_ID")
	  REFERENCES "PAR_SESIONES" ("ID") ON DELETE CASCADE ENABLE;

--------------------------------------------------------
--  Ref Constraints for Table PAR_ENVIOS_SESIONES
--------------------------------------------------------

  ALTER TABLE "PAR_ENVIOS_SESIONES" ADD CONSTRAINT "PAR_ENV_SESS_PAR_ENVIO_ID_fkey" FOREIGN KEY ("PAR_ENVIO_ID")
	  REFERENCES "PAR_ENVIOS" ("ID") ON DELETE CASCADE ENABLE;

  ALTER TABLE "PAR_ENVIOS_SESIONES" ADD CONSTRAINT "PAR_ENV_SES_PAR_SESION_ID_fkey" FOREIGN KEY ("PAR_SESION_ID")
	  REFERENCES "PAR_SESIONES" ("ID") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table PAR_EVENTOS
--------------------------------------------------------

  ALTER TABLE "PAR_EVENTOS" ADD CONSTRAINT "PAR_EVENTOS_PAR_TIPOS_EVE_FK1" FOREIGN KEY ("TIPO_EVENTO_ID")
	  REFERENCES "PAR_TIPOS_EVENTO" ("ID") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table PAR_LOCALIZACIONES
--------------------------------------------------------

  ALTER TABLE "PAR_LOCALIZACIONES" ADD CONSTRAINT "PAR_LOCALIZACIONES_SALAS_fkey" FOREIGN KEY ("SALA_ID")
	  REFERENCES "PAR_SALAS" ("ID") ON DELETE CASCADE ENABLE;

--------------------------------------------------------
--  Ref Constraints for Table PAR_PLANTAS_SALA
--------------------------------------------------------

  ALTER TABLE "PAR_PLANTAS_SALA" ADD CONSTRAINT "PAR_PLANTAS_SALA_PAR_SALAS_FK1" FOREIGN KEY ("SALA_ID")
	  REFERENCES "PAR_PLANTAS_SALA" ("ID") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table PAR_PLANTILLAS
--------------------------------------------------------

  ALTER TABLE "PAR_PLANTILLAS" ADD CONSTRAINT "PAR_PLANTILLAS_SALAS_fkey" FOREIGN KEY ("SALA_ID")
	  REFERENCES "PAR_SALAS" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table PAR_PRECIOS_PLANTILLA
--------------------------------------------------------

  ALTER TABLE "PAR_PRECIOS_PLANTILLA" ADD CONSTRAINT "PAR_PRECIOS_PLANTILLA_PAR_FK1" FOREIGN KEY ("LOCALIZACION_ID")
	  REFERENCES "PAR_LOCALIZACIONES" ("ID") ENABLE;

  ALTER TABLE "PAR_PRECIOS_PLANTILLA" ADD CONSTRAINT "PAR_PRECIOS_PLANTILLA_PAR_FK2" FOREIGN KEY ("PLANTILLA_ID")
	  REFERENCES "PAR_PLANTILLAS" ("ID") ON DELETE CASCADE ENABLE;

  ALTER TABLE "PAR_PRECIOS_PLANTILLA" ADD CONSTRAINT "PAR_PRECIOS_PLANTILLA_fk3" FOREIGN KEY ("TARIFA_ID")
	  REFERENCES "PAR_TARIFAS" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table PAR_PRECIOS_SESION
--------------------------------------------------------

  ALTER TABLE "PAR_PRECIOS_SESION" ADD CONSTRAINT "PAR_PRECIOS_SESION_PAR_LO_FK1" FOREIGN KEY ("LOCALIZACION_ID")
	  REFERENCES "PAR_LOCALIZACIONES" ("ID") ENABLE;

  ALTER TABLE "PAR_PRECIOS_SESION" ADD CONSTRAINT "PAR_PRECIOS_SESION_PAR_SE_FK1" FOREIGN KEY ("SESION_ID")
	  REFERENCES "PAR_SESIONES" ("ID") ON DELETE CASCADE ENABLE;

  ALTER TABLE "PAR_PRECIOS_SESION" ADD CONSTRAINT "PAR_PRECIOS_SESION_par_ta_fk2" FOREIGN KEY ("TARIFA_ID")
	  REFERENCES "PAR_TARIFAS" ("ID") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table PAR_SALAS
--------------------------------------------------------

  ALTER TABLE "PAR_SALAS" ADD CONSTRAINT "PAR_CINES_PAR_SALAS_FK1" FOREIGN KEY ("CINE_ID")
	  REFERENCES "PAR_CINES" ("ID") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table PAR_SESIONES
--------------------------------------------------------

  ALTER TABLE "PAR_SESIONES" ADD CONSTRAINT "PAR_SESIONES_PAR_EVENTOS_FK1" FOREIGN KEY ("EVENTO_ID")
	  REFERENCES "PAR_EVENTOS" ("ID") ON DELETE CASCADE ENABLE;

  ALTER TABLE "PAR_SESIONES" ADD CONSTRAINT "PAR_SESIONES_PAR_PLANTILL_FK1" FOREIGN KEY ("PLANTILLA_ID")
	  REFERENCES "PAR_PLANTILLAS" ("ID") ENABLE;

  ALTER TABLE "PAR_SESIONES" ADD CONSTRAINT "PAR_SESIONES_PAR_SALAS_FK1" FOREIGN KEY ("SALA_ID")
	  REFERENCES "PAR_SALAS" ("ID") ENABLE;



CREATE TABLE "PAR_SESIONES_FORMATO_IDI_ICAA"
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

CREATE TABLE "PAR_ADMINISTRADORES"
(
  	"PER_ID" NUMBER NOT NULL,
    CONSTRAINT "PAR_ADMINISTRADORES_PKEY" PRIMARY KEY ("PER_ID")
);
ALTER TABLE PAR_BUTACAS ADD ("PRESENTADA" TIMESTAMP (6));
ALTER TABLE PAR_PRECIOS_SESION ADD ("AULA_TEATRO" NUMBER);

CREATE TABLE "PAR_EVENTOS_MULTISESION"
(
   "ID" NUMBER,
   "EVENTO_ID" NUMBER,
   "EVENTO_HIJO_ID" NUMBER
);

ALTER TABLE "PAR_EVENTOS_MULTISESION" ADD CONSTRAINT "PAR_EVENTOS_MULTISESION_PK" PRIMARY KEY ("ID") ENABLE;
ALTER TABLE "PAR_EVENTOS_MULTISESION" MODIFY ("EVENTO_ID" NOT NULL ENABLE);
ALTER TABLE "PAR_EVENTOS_MULTISESION" MODIFY ("EVENTO_HIJO_ID" NOT NULL ENABLE);
ALTER TABLE "PAR_EVENTOS_MULTISESION" ADD CONSTRAINT "PAR_EVENTOS_MULTISESION_FK1" FOREIGN KEY ("EVENTO_ID")
	  REFERENCES "PAR_EVENTOS" ("ID");
ALTER TABLE "PAR_EVENTOS_MULTISESION" ADD CONSTRAINT "PAR_EVENTOS_MULTISESION_FK2" FOREIGN KEY ("EVENTO_HIJO_ID")
	  REFERENCES "PAR_EVENTOS" ("ID");

ALTER TABLE "PAR_EVENTOS" ADD (FORMATO VARCHAR(400));
UPDATE PAR_EVENTOS e SET (FORMATO) = (select s.formato from PAR_SESIONES s WHERE s.EVENTO_ID = e.ID and rownum = 1)
ALTER TABLE PAR_SESIONES DROP COLUMN FORMATO;
ALTER TABLE "PAR_EVENTOS_MULTISESION" ADD (VER_LING VARCHAR(400));

CREATE TABLE "PAR_VERSION_BBDD"
(
   "VERSION" VARCHAR(100)
);
ALTER TABLE "PAR_VERSION_BBDD" ADD CONSTRAINT "PAR_VERSION_BBDD_PK" PRIMARY KEY ("VERSION") ENABLE;
INSERT INTO par_version_bbdd (VERSION) VALUES ('2014-09-24.SQL');

ALTER TABLE "PAR_SESIONES" ADD (ANULADA NUMBER(1,0) DEFAULT 0);
INSERT INTO par_version_bbdd (VERSION) VALUES ('2014-10-08.SQL');

ALTER TABLE PAR_BUTACAS ADD (ID_ENTRADA INT);
ALTER TABLE PAR_BUTACAS ADD CONSTRAINT par_butacas_id_entrada_key UNIQUE (ID_ENTRADA);
UPDATE PAR_BUTACAS SET ID_ENTRADA = ID;

INSERT INTO par_version_bbdd (VERSION) VALUES ('2014-11-07.SQL');

ALTER TABLE PAR_SALAS ADD (HTML_TEMPLATE_NAME VARCHAR2(400) DEFAULT 'butacasFragment' NOT NULL);

INSERT INTO par_version_bbdd (VERSION) VALUES ('2014-12-16.SQL');

CREATE TABLE "PAR_ABONOS"
   (	"ID" NUMBER,
	"NOMBRE" VARCHAR2(255) NOT NULL,
	"PLANTILLA_ID" NUMBER NOT NULL,
	"ANULADO"	NUMBER(1,0),
	CONSTRAINT "PAR_ABONOS_PKEY" PRIMARY KEY ("ID"),
  CONSTRAINT "PAR_ABONOS_PAR_PLANTILLAS_FK1" FOREIGN KEY ("PLANTILLA_ID")
        REFERENCES "PAR_PLANTILLAS" ("ID") ENABLE
);

CREATE TABLE "PAR_SESIONES_ABONOS"
   (	"ID" NUMBER,
	    "SESION_ID" NUMBER NOT NULL,
	    "ABONO_ID" NUMBER NOT NULL,
	CONSTRAINT "PAR_SESIONES_ABONOS_PKEY" PRIMARY KEY ("ID"),
  CONSTRAINT "PAR_SES_ABONOS_SESIONES_FK1" FOREIGN KEY ("SESION_ID")
        REFERENCES "PAR_SESIONES" ("ID") ON DELETE CASCADE ENABLE,
  CONSTRAINT "PAR_SES_ABONOS_ABONOS_FK1" FOREIGN KEY ("ABONO_ID")
          REFERENCES "PAR_ABONOS" ("ID") ENABLE
);

CREATE TABLE "PAR_ABONADOS"
   (	"ID" NUMBER,
	    "ABONO_ID" NUMBER NOT NULL,
	    "NOMBRE" VARCHAR2(255 BYTE),
	    "APELLIDOS"	VARCHAR2(255 BYTE),
      "TFNO"	VARCHAR2(255 BYTE),
      "EMAIL"	VARCHAR2(255 BYTE),
      "FECHA"	TIMESTAMP(6),
      "IMPORTE"	NUMBER,
      "DIRECCION"	VARCHAR2(255 BYTE),
      "POBLACION"	VARCHAR2(255 BYTE),
      "CP"	VARCHAR2(255 BYTE),
      "PROVINCIA"	VARCHAR2(255 BYTE),
      "INFO_PERIODICA"	NUMBER(1,0),
      "ANULADO"	NUMBER(1,0),
      "ABONADOS"	VARCHAR2(3500 CHAR),
	CONSTRAINT "PAR_ABONADOS_PKEY" PRIMARY KEY ("ID"),
  CONSTRAINT "PAR_ABONADOS_ABONOS_FK1" FOREIGN KEY ("ABONO_ID")
        REFERENCES "PAR_ABONOS" ("ID") ENABLE
);

ALTER TABLE "PAR_COMPRAS" ADD (ABONADO_ID NUMBER);
ALTER TABLE "PAR_COMPRAS" ADD CONSTRAINT "PAR_COMPRAS_ABONADOS_FK1" FOREIGN KEY ("ABONADO_ID") REFERENCES "PAR_ABONADOS" ("ID") ENABLE;

INSERT INTO par_version_bbdd (VERSION) VALUES ('2014-12-17.SQL');

CREATE TABLE "PAR_TPVS"
(
  "ID" NUMBER,
  "NOMBRE" VARCHAR2(255) NOT NULL,
  "CODE" VARCHAR2(255) NOT NULL,
  "CURRENCY" VARCHAR2(255) NOT NULL,
  "TERMINAL" VARCHAR2(255) NOT NULL,
  "TRANSACTION_CODE" VARCHAR2(255) NOT NULL,
  "ORDER_PREFIX" VARCHAR2(255) NOT NULL,
  "LANG_CA_CODE" VARCHAR2(255) NOT NULL,
  "LANG_ES_CODE" VARCHAR2(255) NOT NULL,
  "URL" VARCHAR2(255) NOT NULL,
  "WSDL_URL" VARCHAR2(255) NOT NULL,
  "DEFAULT_TPV" NUMBER(1,0) DEFAULT 0,
  CONSTRAINT "PAR_TPVS_PKEY" PRIMARY KEY ("ID")
);

ALTER TABLE PAR_EVENTOS ADD (
  TPV_ID integer DEFAULT 0
);

ALTER TABLE PAR_EVENTOS ADD CONSTRAINT "PAR_EVENTOS_TPVS_FK1" FOREIGN KEY ("TPV_ID") REFERENCES "PAR_TPVS" ("ID") ENABLE;

INSERT INTO par_version_bbdd (VERSION) VALUES ('2014-12-18.SQL');