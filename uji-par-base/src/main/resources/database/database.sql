--------------------------------------------------------
--  File created - viernes-diciembre-28-2012   
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
	"ASIENTOS_NUMERADOS" NUMBER(1,0) DEFAULT 0, 
	"PORCENTAJE_IVA" NUMBER, 
	"IVA_SGAE" NUMBER, 
	"RETENCION_SGAE" NUMBER
   ) ;
--------------------------------------------------------
--  DDL for Table PAR_LOCALIZACIONES
--------------------------------------------------------

  CREATE TABLE "PAR_LOCALIZACIONES" 
   (	"ID" NUMBER,
    "CODIGO" VARCHAR2(255),  
	"NOMBRE_ES" VARCHAR2(255), 
	"TOTAL_ENTRADAS" NUMBER, 
	"NOMBRE_VA" VARCHAR2(255)
   ) ;
--------------------------------------------------------
--  DDL for Table PAR_PLANTILLAS
--------------------------------------------------------

  CREATE TABLE "PAR_PLANTILLAS" 
   (	"ID" NUMBER, 
	"NOMBRE" VARCHAR2(255)
   ) ;
   
   INSERT INTO "PAR_PLANTILLAS" (ID, NOMBRE) VALUES ('-1', '- Sense plantilla');
--------------------------------------------------------
--  DDL for Table PAR_PRECIOS_PLANTILLA
--------------------------------------------------------

  CREATE TABLE "PAR_PRECIOS_PLANTILLA" 
   (	"ID" NUMBER, 
	"LOCALIZACION_ID" NUMBER, 
	"PLANTILLA_ID" NUMBER, 
	"PRECIO" NUMBER, 
	"DESCUENTO" NUMBER, 
	"INVITACION" NUMBER
   ) ;
--------------------------------------------------------
--  DDL for Table PAR_PRECIOS_SESION
--------------------------------------------------------

  CREATE TABLE "PAR_PRECIOS_SESION" 
   (	"ID" NUMBER, 
	"LOCALIZACION_ID" NUMBER, 
	"SESION_ID" NUMBER, 
	"PRECIO" NUMBER, 
	"DESCUENTO" NUMBER, 
	"INVITACION" NUMBER
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
	"CANAL_INTERNET" NUMBER(1,0) DEFAULT 1, 
	"CANAL_TAQUILLA" NUMBER(1,0) DEFAULT 1, 
	"PLANTILLA_ID" NUMBER
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
	"NOMBRE" VARCHAR2(255), 
	"USUARIO" VARCHAR2(255), 
	"MAIL" VARCHAR2(255)
   ) ;
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
	"PRECIO" NUMBER
   ) ;
--------------------------------------------------------
--  DDL for Table PAR_COMPRAS
--------------------------------------------------------

	CREATE TABLE "PAR_COMPRAS" 
   (	"ID" NUMBER, 
    "SESION_ID" NUMBER, 
	"NOMBRE" VARCHAR2(255 BYTE), 
	"APELLIDOS" VARCHAR2(255 BYTE), 
	"TFNO" VARCHAR2(255 BYTE), 
	"EMAIL" VARCHAR2(255 BYTE), 
	"FECHA" TIMESTAMP (6), 
	"TAQUILLA" NUMBER(1,0) DEFAULT 0,
	"IMPORTE" NUMBER
   );
--------------------------------------------------------
--  DDL for Table PAR_LOCALIZACION_OCUPADAS
--------------------------------------------------------   
  CREATE TABLE "PAR_LOCALIZACION_OCUPADAS" 
   (	
    "ID" NUMBER,
	"SESION_ID" NUMBER, 
	"LOCALIZACION_ID" NUMBER, 
	"OCUPADAS" NUMBER 
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

  ALTER TABLE "PAR_LOCALIZACIONES" ADD CONSTRAINT "PAR_LOCALIZACIONES_CODIGO_UK" UNIQUE ("CODIGO") ENABLE;
   
  ALTER TABLE "PAR_LOCALIZACIONES" MODIFY ("CODIGO" NOT NULL ENABLE);
  
  ALTER TABLE "PAR_LOCALIZACIONES" MODIFY ("NOMBRE_ES" NOT NULL ENABLE);
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
 
  ALTER TABLE "PAR_PRECIOS_PLANTILLA" ADD CONSTRAINT "PAR_PRECIOS_PLANTILLA_UK1" UNIQUE ("LOCALIZACION_ID", "PLANTILLA_ID") ENABLE;
 
  ALTER TABLE "PAR_PRECIOS_PLANTILLA" MODIFY ("ID" NOT NULL ENABLE);
 
  ALTER TABLE "PAR_PRECIOS_PLANTILLA" MODIFY ("LOCALIZACION_ID" NOT NULL ENABLE);
 
  ALTER TABLE "PAR_PRECIOS_PLANTILLA" MODIFY ("PLANTILLA_ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table PAR_PRECIOS_SESION
--------------------------------------------------------

  ALTER TABLE "PAR_PRECIOS_SESION" ADD CONSTRAINT "PAR_PRECIOS_SESION_PK" PRIMARY KEY ("ID") ENABLE;
 
  ALTER TABLE "PAR_PRECIOS_SESION" ADD CONSTRAINT "PAR_PRECIOS_SESION_UK1" UNIQUE ("SESION_ID", "LOCALIZACION_ID") ENABLE;
 
  ALTER TABLE "PAR_PRECIOS_SESION" MODIFY ("ID" NOT NULL ENABLE);
 
  ALTER TABLE "PAR_PRECIOS_SESION" MODIFY ("LOCALIZACION_ID" NOT NULL ENABLE);
 
  ALTER TABLE "PAR_PRECIOS_SESION" MODIFY ("SESION_ID" NOT NULL ENABLE);
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

  ALTER TABLE "PAR_USUARIOS" ADD CONSTRAINT "" PRIMARY KEY ("ID") ENABLE;
 
  ALTER TABLE "PAR_USUARIOS" ADD CONSTRAINT "PAR_USERS_UK1" UNIQUE ("USUARIO") ENABLE;
 
  ALTER TABLE "PAR_USUARIOS" MODIFY ("ID" NOT NULL ENABLE);
 
  ALTER TABLE "PAR_USUARIOS" MODIFY ("NOMBRE" NOT NULL ENABLE);
 
  ALTER TABLE "PAR_USUARIOS" MODIFY ("USUARIO" NOT NULL ENABLE);
 
  ALTER TABLE "PAR_USUARIOS" MODIFY ("MAIL" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table PAR_BUTACAS
--------------------------------------------------------

  ALTER TABLE "PAR_BUTACAS" ADD CONSTRAINT "PAR_BUTACAS_PK" PRIMARY KEY ("ID") ENABLE;
 
  -- Permite que unique por (sesion, localizacion, fila, numero) pero sin dar problemas cuando fila=null y numero=null (las butacas no numeradas)
  ALTER TABLE "PAR_BUTACAS" ADD CONSTRAINT "PAR_BUTACAS_UK1" UNIQUE ("SESION_ID", "LOCALIZACION_ID", (NVL2(fila, fila, id)), (NVL2(numero, numero, id))) ENABLE;
  
  ALTER TABLE "PAR_BUTACAS" MODIFY ("COMPRA_ID" NOT NULL ENABLE);
 
  ALTER TABLE "PAR_BUTACAS" MODIFY ("ID" NOT NULL ENABLE);
 
  ALTER TABLE "PAR_BUTACAS" MODIFY ("FILA" NOT NULL ENABLE);
 
  ALTER TABLE "PAR_BUTACAS" MODIFY ("NUMERO" NOT NULL ENABLE);
  
  ALTER TABLE "PAR_BUTACAS" MODIFY ("PRECIO" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table PAR_COMPRAS
--------------------------------------------------------

  ALTER TABLE "PAR_COMPRAS" ADD CONSTRAINT "PAR_COMPRAS_PK" PRIMARY KEY ("ID");
  
  ALTER TABLE "PAR_COMPRAS" MODIFY ("FECHA" NOT NULL ENABLE);
  
  ALTER TABLE "PAR_COMPRAS" MODIFY ("TAQUILLA" NOT NULL ENABLE);

--------------------------------------------------------
--  Constraints for Table PAR_LOCALIZACION_OCUPADAS
--------------------------------------------------------

  ALTER TABLE "PAR_LOCALIZACION_OCUPADAS" ADD CONSTRAINT "PAR_LOCALIZACION_OCUPADAS_PK" PRIMARY KEY ("ID") ENABLE;
 
  ALTER TABLE "PAR_LOCALIZACION_OCUPADAS" ADD CONSTRAINT "PAR_LOCALIZACION_OCUPADAS_UK1" UNIQUE ("SESION_ID", "LOCALIZACION_ID") ENABLE;
 
  ALTER TABLE "PAR_LOCALIZACION_OCUPADAS" MODIFY ("ID" NOT NULL ENABLE);
 
  ALTER TABLE "PAR_LOCALIZACION_OCUPADAS" MODIFY ("LOCALIZACION_ID" NOT NULL ENABLE);
 
  ALTER TABLE "PAR_LOCALIZACION_OCUPADAS" MODIFY ("SESION_ID" NOT NULL ENABLE);
  
  ALTER TABLE "PAR_LOCALIZACION_OCUPADAS" MODIFY ("OCUPADAS" NOT NULL ENABLE);          
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
--  DDL for Index PAR_PLANTILLAS_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "PAR_PLANTILLAS_PK" ON "PAR_PLANTILLAS" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index PAR_PLANTILLAS_UK1
--------------------------------------------------------

  CREATE UNIQUE INDEX "PAR_PLANTILLAS_UK1" ON "PAR_PLANTILLAS" ("NOMBRE") 
  ;
--------------------------------------------------------
--  DDL for Index PAR_PRECIOS_PLANTILLA_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "PAR_PRECIOS_PLANTILLA_PK" ON "PAR_PRECIOS_PLANTILLA" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index PAR_PRECIOS_PLANTILLA_UK1
--------------------------------------------------------

  CREATE UNIQUE INDEX "PAR_PRECIOS_PLANTILLA_UK1" ON "PAR_PRECIOS_PLANTILLA" ("LOCALIZACION_ID", "PLANTILLA_ID") 
  ;
--------------------------------------------------------
--  DDL for Index PAR_PRECIOS_SESION_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "PAR_PRECIOS_SESION_PK" ON "PAR_PRECIOS_SESION" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index PAR_PRECIOS_SESION_UK1
--------------------------------------------------------

  CREATE UNIQUE INDEX "PAR_PRECIOS_SESION_UK1" ON "PAR_PRECIOS_SESION" ("SESION_ID", "LOCALIZACION_ID") 
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
--  DDL for Index 
--------------------------------------------------------

  CREATE UNIQUE INDEX "PAR_USERS_PK" ON "PAR_USUARIOS" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index PAR_USERS_UK1
--------------------------------------------------------

  CREATE UNIQUE INDEX "PAR_USERS_UK1" ON "PAR_USUARIOS" ("USUARIO") 
  ;
--------------------------------------------------------
--  DDL for Index PAR_BUTACAS_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "PAR_BUTACAS_PK" ON "PAR_BUTACAS" ("ID") 
  ;  
--------------------------------------------------------
--  DDL for Index PAR_COMPRAS_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "PAR_COMPRAS_PK" ON "PAR_COMPRAS" ("ID") 
  ;    

--------------------------------------------------------
--  DDL for Index PAR_LOCALIZACION_OCUPADAS_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "PAR_LOCALIZACION_OCUPADAS_PK" ON "PAR_LOCALIZACION_OCUPADAS" ("ID") 
  ;  
--------------------------------------------------------
--  Ref Constraints for Table PAR_EVENTOS
--------------------------------------------------------

  ALTER TABLE "PAR_EVENTOS" ADD CONSTRAINT "PAR_EVENTOS_PAR_TIPOS_EVE_FK1" FOREIGN KEY ("TIPO_EVENTO_ID")
	  REFERENCES "PAR_TIPOS_EVENTO" ("ID") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table PAR_PRECIOS_PLANTILLA
--------------------------------------------------------

  ALTER TABLE "PAR_PRECIOS_PLANTILLA" ADD CONSTRAINT "PAR_PRECIOS_PLANTILLA_PAR_FK1" FOREIGN KEY ("LOCALIZACION_ID")
	  REFERENCES "PAR_LOCALIZACIONES" ("ID") ENABLE;
 
  ALTER TABLE "PAR_PRECIOS_PLANTILLA" ADD CONSTRAINT "PAR_PRECIOS_PLANTILLA_PAR_FK2" FOREIGN KEY ("PLANTILLA_ID")
	  REFERENCES "PAR_PLANTILLAS" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table PAR_PRECIOS_SESION
--------------------------------------------------------

  ALTER TABLE "PAR_PRECIOS_SESION" ADD CONSTRAINT "PAR_PRECIOS_SESION_PAR_LO_FK1" FOREIGN KEY ("LOCALIZACION_ID")
	  REFERENCES "PAR_LOCALIZACIONES" ("ID") ENABLE;
 
  ALTER TABLE "PAR_PRECIOS_SESION" ADD CONSTRAINT "PAR_PRECIOS_SESION_PAR_SE_FK1" FOREIGN KEY ("SESION_ID")
	  REFERENCES "PAR_SESIONES" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table PAR_SESIONES
--------------------------------------------------------

  ALTER TABLE "PAR_SESIONES" ADD CONSTRAINT "PAR_SESIONES_PAR_EVENTOS_FK1" FOREIGN KEY ("EVENTO_ID")
	  REFERENCES "PAR_EVENTOS" ("ID") ON DELETE CASCADE ENABLE;
 
  ALTER TABLE "PAR_SESIONES" ADD CONSTRAINT "PAR_SESIONES_PAR_PLANTILL_FK1" FOREIGN KEY ("PLANTILLA_ID")
	  REFERENCES "PAR_PLANTILLAS" ("ID") ENABLE;

--------------------------------------------------------
--  Ref Constraints for Table PAR_COMPRAS
-------------------------------------------------------
	  
  ALTER TABLE "PAR_COMPRAS" ADD CONSTRAINT "PAR_COMPRAS_SE_FK1" FOREIGN KEY ("SESION_ID")
	  REFERENCES "PAR_SESIONES" ("ID") ON DELETE CASCADE ENABLE;	
	  	  
--------------------------------------------------------
--  Ref Constraints for Table PAR_BUTACAS
--------------------------------------------------------

  ALTER TABLE "PAR_BUTACAS" ADD CONSTRAINT "PAR_BUTACAS_LO_FK1" FOREIGN KEY ("LOCALIZACION_ID")
	  REFERENCES "PAR_LOCALIZACIONES" ("ID") ENABLE;
 
  ALTER TABLE "PAR_BUTACAS" ADD CONSTRAINT "PAR_BUTACAS_SE_FK1" FOREIGN KEY ("SESION_ID")
	  REFERENCES "PAR_SESIONES" ("ID") ON DELETE CASCADE ENABLE;	
	  
  ALTER TABLE "PAR_BUTACAS" ADD CONSTRAINT "PAR_BUTACAS_CO_FK1" FOREIGN KEY ("COMPRA_ID")
	  REFERENCES "PAR_COMPRAS" ("ID") ON DELETE CASCADE ENABLE;		   
	  
--------------------------------------------------------
--  Ref Constraints for Table PAR_LOCALIZACION_OCUPADAS
--------------------------------------------------------

  ALTER TABLE "PAR_LOCALIZACION_OCUPADAS" ADD CONSTRAINT "PAR_LOCALIZACION_OCUPADAS_PAR_LO_FK1" FOREIGN KEY ("LOCALIZACION_ID")
	  REFERENCES "PAR_LOCALIZACIONES" ("ID") ENABLE;
 
  ALTER TABLE "PAR_LOCALIZACION_OCUPADAS" ADD CONSTRAINT "PAR_LOCALIZACION_OCUPADAS_PAR_SE_FK1" FOREIGN KEY ("SESION_ID")
	  REFERENCES "PAR_SESIONES" ("ID") ON DELETE CASCADE ENABLE;
	  	   