--------------------------------------------------------
--  File created - viernes-diciembre-07-2012   
--------------------------------------------------------
--------------------------------------------------------
--  DDL for Sequence HIBERNATE_SEQUENCE
--------------------------------------------------------

   CREATE SEQUENCE  "HIBERNATE_SEQUENCE"  MINVALUE 1 MAXVALUE 999999999999999999999999999 INCREMENT BY 1 START WITH 0 CACHE 20 NOORDER  NOCYCLE ;
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
	"ASIENTOS_NUMERADOS" NUMBER(1,0),
	"PORCENTAJE_IVA" NUMBER,
	"IVA_SGAE" NUMBER,
	"RETENCION_SGAE" NUMBER
   ) ;
--------------------------------------------------------
--  DDL for Table PAR_LOCALIZACIONES
--------------------------------------------------------

  CREATE TABLE "PAR_LOCALIZACIONES" 
   (	"ID" NUMBER, 
	"NOMBRE_ES" VARCHAR2(255), 
	"TOTAL_ENTRADAS" NUMBER, 
	"NOMBRE_VA" VARCHAR2(255)
   ) ;
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
	"CANAL_INTERNET" NUMBER(1,0), 
	"CANAL_TAQUILLA" NUMBER(1,0)
   ) ;
--------------------------------------------------------
--  DDL for Table PAR_TIPOS_EVENTO
--------------------------------------------------------

  CREATE TABLE "PAR_TIPOS_EVENTO" 
   (	"ID" NUMBER, 
	"NOMBRE_ES" VARCHAR2(50), 
	"NOMBRE_VA" VARCHAR2(50)
   ) ;
--------------------------------------------------------
--  DDL for Table PAR_USUARIOS
--------------------------------------------------------

  CREATE TABLE "PAR_USUARIOS" 
   (	"ID" NUMBER, 
	"NOMBRE" VARCHAR2(20), 
	"USUARIO" VARCHAR2(20), 
	"MAIL" VARCHAR2(20)
   ) ;
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

  ALTER TABLE "PAR_LOCALIZACIONES" ADD CONSTRAINT "PAR_LOCALIZACIONES_PK" PRIMARY KEY ("ID") ENABLE;
 
  ALTER TABLE "PAR_LOCALIZACIONES" MODIFY ("ID" NOT NULL ENABLE);
 
  ALTER TABLE "PAR_LOCALIZACIONES" MODIFY ("NOMBRE_ES" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table PAR_SESIONES
--------------------------------------------------------

  ALTER TABLE "PAR_SESIONES" ADD CONSTRAINT "PAR_SESION_PK" PRIMARY KEY ("ID") ENABLE;
 
  ALTER TABLE "PAR_SESIONES" MODIFY ("ID" NOT NULL ENABLE);
 
  ALTER TABLE "PAR_SESIONES" MODIFY ("EVENTO_ID" NOT NULL ENABLE);
 
  ALTER TABLE "PAR_SESIONES" MODIFY ("FECHA_CELEBRACION" NOT NULL ENABLE);
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
--  DDL for Index PAR_EVENTOS_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "PAR_EVENTOS_PK" ON "PAR_EVENTOS" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index PAR_LOCALIZACIONES_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "PAR_LOCALIZACIONES_PK" ON "PAR_LOCALIZACIONES" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index PAR_SESION_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "PAR_SESION_PK" ON "PAR_SESIONES" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index PAR_TIPOS_EVENTO_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "PAR_TIPOS_EVENTO_PK" ON "PAR_TIPOS_EVENTO" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index PAR_USERS_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "PAR_USERS_PK" ON "PAR_USUARIOS" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index PAR_USERS_UK1
--------------------------------------------------------

  CREATE UNIQUE INDEX "PAR_USERS_UK1" ON "PAR_USUARIOS" ("USUARIO") 
  ;
--------------------------------------------------------
--  Ref Constraints for Table PAR_EVENTOS
--------------------------------------------------------

  ALTER TABLE "PAR_EVENTOS" ADD CONSTRAINT "PAR_EVENTOS_PAR_TIPOS_EVE_FK1" FOREIGN KEY ("TIPO_EVENTO_ID")
	  REFERENCES "PAR_TIPOS_EVENTO" ("ID") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table PAR_SESIONES
--------------------------------------------------------

  ALTER TABLE "PAR_SESIONES" ADD CONSTRAINT "PAR_SESIONES_PAR_EVENTOS_FK1" FOREIGN KEY ("EVENTO_ID")
	  REFERENCES "PAR_EVENTOS" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  DDL for Trigger PAR_EVENTOS_TRIGGER
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PAR_EVENTOS_TRIGGER" 
BEFORE INSERT
ON par_eventos
REFERENCING NEW AS NEW
FOR EACH ROW
BEGIN
SELECT hibernate_sequence.nextval INTO :NEW.ID FROM dual;
END;
/
ALTER TRIGGER "PAR_EVENTOS_TRIGGER" ENABLE;
--------------------------------------------------------
--  DDL for Trigger PAR_LOCALIZACIONES_TRG
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PAR_LOCALIZACIONES_TRG" 
BEFORE INSERT ON PAR_LOCALIZACIONES
FOR EACH ROW 
BEGIN
  SELECT HIBERNATE_SEQUENCE.NEXTVAL INTO :NEW.ID FROM DUAL;
END;

/
ALTER TRIGGER "PAR_LOCALIZACIONES_TRG" ENABLE;
--------------------------------------------------------
--  DDL for Trigger PAR_SESIONES_TRIGGER
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PAR_SESIONES_TRIGGER" 
BEFORE INSERT
ON par_sesiones
REFERENCING NEW AS NEW
FOR EACH ROW
BEGIN
SELECT hibernate_sequence.nextval INTO :NEW.ID FROM dual;
END;
/
ALTER TRIGGER "PAR_SESIONES_TRIGGER" ENABLE;
--------------------------------------------------------
--  DDL for Trigger PAR_TIPOS_EVENTO_TRIGGER
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PAR_TIPOS_EVENTO_TRIGGER" 
BEFORE INSERT
ON par_tipos_evento
REFERENCING NEW AS NEW
FOR EACH ROW
BEGIN
SELECT hibernate_sequence.nextval INTO :NEW.ID FROM dual;
END;
/
ALTER TRIGGER "PAR_TIPOS_EVENTO_TRIGGER" ENABLE;
--------------------------------------------------------
--  DDL for Trigger PAR_USUARIOS_TRIGGER
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PAR_USUARIOS_TRIGGER" 
BEFORE INSERT
ON par_usuarios
REFERENCING NEW AS NEW
FOR EACH ROW
BEGIN
SELECT hibernate_sequence.nextval INTO :NEW.ID FROM dual;
END;
/
ALTER TRIGGER "PAR_USUARIOS_TRIGGER" ENABLE;
