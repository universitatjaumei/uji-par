-- DATOS EVENTOS

ALTER TABLE "PAR_EVENTOS" ADD (SUBTITULOS VARCHAR(400));

-- DATOS SESIONES

ALTER TABLE "PAR_SESIONES" ADD (VER_LING VARCHAR(400));
ALTER TABLE "PAR_SESIONES" ADD (RSS_ID VARCHAR(400));

-- TIPOS EVENTO

ALTER TABLE "PAR_TIPOS_EVENTO" ADD ("EXPORTAR_ICAA" NUMBER(1,0) DEFAULT 0);

-- ENVIOS

CREATE TABLE "PAR_ENVIOS"
(    
  "ID" NUMBER, 
  "FECHA_GENERACION_FICHERO" TIMESTAMP (6),
  "FECHA_ENVIO_FICHERO" TIMESTAMP (6)
);

ALTER TABLE "PAR_ENVIOS" ADD CONSTRAINT "PAR_ENVIOS_PK" PRIMARY KEY ("ID") ENABLE;
ALTER TABLE "PAR_ENVIOS" MODIFY ("ID" NOT NULL ENABLE);

-- ENVIOS SESIONES

CREATE TABLE "PAR_ENVIOS_SESIONES"
(
  "ID" NUMBER,
  "PAR_ENVIO_ID" integer,
  "PAR_SESION_ID" integer,
  "TIPO_ENVIO" VARCHAR(2)
 );
 
 ALTER TABLE "PAR_ENVIOS_SESIONES" ADD CONSTRAINT "PAR_ENVIOS_SESIONES_PK" PRIMARY KEY ("ID" ) ENABLE;
 ALTER TABLE "PAR_ENVIOS_SESIONES" MODIFY ("ID" NOT NULL ENABLE);
 ALTER TABLE "PAR_ENVIOS_SESIONES" MODIFY ("TIPO_ENVIO" NOT NULL ENABLE);
 
 ALTER TABLE "PAR_ENVIOS_SESIONES" ADD CONSTRAINT "PAR_ENV_SESS_PAR_ENVIO_ID_fkey" FOREIGN KEY ("PAR_ENVIO_ID")
      REFERENCES "PAR_ENVIOS" ("ID") ON DELETE CASCADE ENABLE;
 ALTER TABLE "PAR_ENVIOS_SESIONES" ADD CONSTRAINT "PAR_ENV_SES_PAR_SESION_ID_fkey" FOREIGN KEY ("PAR_SESION_ID")
      REFERENCES "PAR_SESIONES" ("ID") ENABLE;
      
      
 -- LOCALIZACIONES

ALTER TABLE "PAR_LOCALIZACIONES" ADD ("SALA_ID" NUMBER);
ALTER TABLE "PAR_LOCALIZACIONES" ADD CONSTRAINT "PAR_LOCALIZACIONES_SALAS_fkey" FOREIGN KEY ("SALA_ID") 
	REFERENCES "PAR_SALAS" ("ID") ON DELETE CASCADE ENABLE;
	
	
--PLANTILLAS

ALTER TABLE "PAR_PLANTILLAS" ADD ("SALA_ID" NUMBER);
ALTER TABLE "PAR_PLANTILLAS" ADD CONSTRAINT "PAR_PLANTILLAS_SALAS_fkey" FOREIGN KEY ("SALA_ID") 
	REFERENCES "PAR_SALAS" ("ID") ON DELETE CASCADE ENABLE;


	CREATE TABLE PAR_TARIFAS
(
  "ID" NUMBER,
  "NOMBRE" VARCHAR(100),
  "ISPUBLICA" NUMBER(1,0) DEFAULT 1,
  "DEFECTO" NUMBER(1,0) DEFAULT 0,
  CONSTRAINT "PAR_TARIFAS_pkey" PRIMARY KEY (ID)
);

ALTER TABLE "PAR_TARIFAS" ADD CONSTRAINT "PAR_TARIFAS_PK" PRIMARY KEY ("ID" ) ENABLE;
ALTER TABLE "PAR_TARIFAS" MODIFY ("ID" NOT NULL ENABLE);
ALTER TABLE "PAR_TARIFAS" MODIFY ("NOMBRE" NOT NULL ENABLE);

ALTER TABLE "PAR_PRECIOS_PLANTILLA" ADD("TARIFA_ID" NUMBER);
ALTER TABLE "PAR_PRECIOS_PLANTILLA" ADD CONSTRAINT "PAR_PRECIOS_PLANTILLA_fk3" FOREIGN KEY ("TARIFA_ID") 
	REFERENCES "PAR_TARIFAS" ("ID") ON DELETE CASCADE ENABLE;

--IMPORTANTE, SABER EN LA UJI COMO SE LLAMA ESTA CONSTRAIN
ALTER TABLE "PAR_PRECIOS_PLANTILLA" DROP CONSTRAINT "PAR_PRECIOS_PLANTILLA_UK1";

ALTER TABLE "PAR_PRECIOS_SESION" ADD ("TARIFA_ID" integer);
ALTER TABLE "PAR_PRECIOS_SESION" DROP CONSTRAINT "PAR_PRECIOS_SESION_UK1";
ALTER TABLE "PAR_PRECIOS_SESION" ADD CONSTRAINT "PAR_PRECIOS_SESION_par_ta_fk2" FOREIGN KEY ("TARIFA_ID") 
	REFERENCES "PAR_TARIFAS" ("ID") ON UPDATE RESTRICT ON DELETE RESTRICT ENABLE;
ALTER TABLE "PAR_PRECIOS_SESION" ADD UNIQUE ("LOCALIZACION_ID", "SESION_ID", "TARIFA_ID");

INSERT INTO "PAR_TARIFAS" (ID, NOMBRE, ISPUBLICA, DEFECTO) values (1, 'General', 1, 1);
INSERT INTO "PAR_TARIFAS" (ID, NOMBRE, ISPUBLICA, DEFECTO) values (2, 'Descompte', 1, 0);
INSERT INTO "PAR_TARIFAS" (ID, NOMBRE, ISPUBLICA, DEFECTO) values (3, 'Invitació', 0, 0);
INSERT INTO "PAR_TARIFAS" (ID, NOMBRE, ISPUBLICA, DEFECTO) values (4, 'Aula Teatre', 1, 0);

INSERT INTO "PAR_CINES" (ID, CODIGO, NOMBRE, CIF, DIRECCION, COD_MUNICIPIO, NOM_MUNICIPIO, CP, EMPRESA, COD_REGISTRO, TFNO, IVA)
VALUES (1, '123', 'Paranimf', 'Q6250003H', 'Avinguda Sos Baynat S/N', 'TODO', 'Castelló de la Plana', '12071', 'TODO', 'TODO', '964 72 80 00', 0);

INSERT INTO "PAR_SALAS" (ID, CODIGO, NOMBRE, ASIENTOS, ASIENTO_DISC, ASIENTO_NORES, TIPO, FORMATO, SUBTITULO, CINE_ID)
VALUES (1, 'TODO', 'Paranimf', 641, 6, 0, 'COMERCIAL', 'DIGITAL', 'false', 1);

UPDATE "PAR_SESIONES" SET SALA_ID = 1;
UPDATE "PAR_LOCALIZACIONES" SET SALA_ID = 1;
UPDATE "PAR_PLANTILLAS" SET SALA_ID = 1;

UPDATE "PAR_BUTACAS" set TIPO = 1 where TIPO = 'normal';
UPDATE "PAR_BUTACAS" set TIPO = 2 where TIPO = 'descuento';
UPDATE "PAR_BUTACAS" set TIPO = 3 where TIPO = 'invitacion';
UPDATE "PAR_BUTACAS" set TIPO = 4 where tipo = 'aulaTeatro';

ALTER TABLE "PAR_SESIONES" ADD ("INCIDENCIA_ID" NUMBER);
update "PAR_SESIONES" set INCIDENCIA_ID = 0;


/*
 * para actualizar las plantillas y precios, hacer lo mismo para par_precios_sesion
 */
/*tarifa general*/
--insert into par_precios_plantilla (localizacion_id, plantilla_id, precio, tarifa_id) values (1,1653,9,5200);
--insert into par_precios_plantilla (localizacion_id, plantilla_id, precio, tarifa_id) values (2,1653,9,5200);
--insert into par_precios_plantilla (localizacion_id, plantilla_id, precio, tarifa_id) values (3,1653,9,5200);
--insert into par_precios_plantilla (localizacion_id, plantilla_id, precio, tarifa_id) values (5,1653,9,5200);
/*jubilats*/
--insert into par_precios_plantilla (localizacion_id, plantilla_id, precio, tarifa_id) values (1,1653,5,5201);
--insert into par_precios_plantilla (localizacion_id, plantilla_id, precio, tarifa_id) values (3,1653,5,5201);
--insert into par_precios_plantilla (localizacion_id, plantilla_id, precio, tarifa_id) values (2,1653,5,5201);
--insert into par_precios_plantilla (localizacion_id, plantilla_id, precio, tarifa_id) values (5,1653,5,5201);
/*invitacio*/
--insert into par_precios_plantilla (localizacion_id, plantilla_id, precio, tarifa_id) values (1,1653,0,5202);
--insert into par_precios_plantilla (localizacion_id, plantilla_id, precio, tarifa_id) values (2,1653,0,5202);
--insert into par_precios_plantilla (localizacion_id, plantilla_id, precio, tarifa_id) values (3,1653,0,5202);
--insert into par_precios_plantilla (localizacion_id, plantilla_id, precio, tarifa_id) values (5,1653,0,5202);
/*platea*/
--insert into par_precios_plantilla (localizacion_id, plantilla_id, precio, tarifa_id) values (5,1653,7,5203);
--insert into par_precios_plantilla (localizacion_id, plantilla_id, precio, tarifa_id) values (3,1653,7,5203);
--insert into par_precios_plantilla (localizacion_id, plantilla_id, precio, tarifa_id) values (2,1653,7,5203);
--insert into par_precios_plantilla (localizacion_id, plantilla_id, precio, tarifa_id) values (1,1653,7,5203);

ALTER TABLE "PAR_COMPRAS" ADD ("REFERENCIA_PAGO" VARCHAR(400));
