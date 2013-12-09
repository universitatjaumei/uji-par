--------------------------------------------------------
--  File created - viernes-diciembre-28-2012   
--------------------------------------------------------
--------------------------------------------------------
--  DDL for Sequence HIBERNATE_SEQUENCE
--------------------------------------------------------

CREATE SEQUENCE  HIBERNATE_SEQUENCE  MINVALUE 1 INCREMENT BY 1;
   
--------------------------------------------------------
--  DDL for Table PAR_TIPOS_EVENTO
--------------------------------------------------------

  CREATE TABLE PAR_TIPOS_EVENTO 
   (    
    ID SERIAL UNIQUE, 
    NOMBRE_ES VARCHAR(50) NOT NULL, 
    NOMBRE_VA VARCHAR(50)
   ) ;
   
--------------------------------------------------------
--  DDL for Table PAR_EVENTOS
--------------------------------------------------------

  CREATE TABLE PAR_EVENTOS
   (    
    ID SERIAL UNIQUE, 
    TITULO_ES VARCHAR(255) NOT NULL, 
    DESCRIPCION_ES VARCHAR(3500), 
    COMPANYIA_ES VARCHAR(300), 
    INTERPRETES_ES VARCHAR(3500), 
    DURACION_ES VARCHAR(255), 
    PREMIOS_ES VARCHAR(3500), 
    CARACTERISTICAS_ES VARCHAR(3500), 
    COMENTARIOS_ES VARCHAR(3500), 
    IMAGEN OID, 
    TIPO_EVENTO_ID INTEGER NOT NULL, 
    IMAGEN_CONTENT_TYPE VARCHAR(255), 
    IMAGEN_SRC VARCHAR(255), 
    TITULO_VA VARCHAR(255), 
    DESCRIPCION_VA VARCHAR(3500), 
    COMPANYIA_VA VARCHAR(300), 
    INTERPRETES_VA VARCHAR(3500), 
    DURACION_VA VARCHAR(255), 
    PREMIOS_VA VARCHAR(3500), 
    CARACTERISTICAS_VA VARCHAR(3500), 
    COMENTARIOS_VA VARCHAR(3500), 
    ASIENTOS_NUMERADOS BOOLEAN DEFAULT FALSE, 
    PORCENTAJE_IVA REAL, 
    IVA_SGAE REAL, 
    RETENCION_SGAE REAL
   ) ;
   
--------------------------------------------------------
--  DDL for Table PAR_LOCALIZACIONES
--------------------------------------------------------

  CREATE TABLE PAR_LOCALIZACIONES 
   (    
    ID SERIAL UNIQUE,
    CODIGO VARCHAR(255) UNIQUE NOT NULL,  
    NOMBRE_ES VARCHAR(255) NOT NULL, 
    TOTAL_ENTRADAS INTEGER, 
    NOMBRE_VA VARCHAR(255)
   );
   
--------------------------------------------------------
--  DDL for Table PAR_PLANTILLAS
--------------------------------------------------------

  CREATE TABLE PAR_PLANTILLAS 
   (    
    ID SERIAL UNIQUE, 
    NOMBRE VARCHAR(255) UNIQUE NOT NULL
   ) ;
   
   INSERT INTO PAR_PLANTILLAS (ID, NOMBRE) VALUES ('-1', '- Sense plantilla');
   

--------------------------------------------------------
--  DDL for Table PAR_PRECIOS_PLANTILLA
--------------------------------------------------------

  CREATE TABLE PAR_PRECIOS_PLANTILLA 
   (    
    ID SERIAL UNIQUE, 
    LOCALIZACION_ID INTEGER NOT NULL, 
    PLANTILLA_ID INTEGER NOT NULL, 
    PRECIO INTEGER, 
    DESCUENTO INTEGER, 
    INVITACION INTEGER
   ) ;
   
   ALTER TABLE PAR_PRECIOS_PLANTILLA ADD CONSTRAINT PAR_PRECIOS_PLANTILLA_UK1 UNIQUE (LOCALIZACION_ID, PLANTILLA_ID);   

--------------------------------------------------------
--  DDL for Table PAR_PRECIOS_SESION
--------------------------------------------------------

  CREATE TABLE PAR_PRECIOS_SESION 
   (    
    ID SERIAL UNIQUE, 
    LOCALIZACION_ID INTEGER NOT NULL, 
    SESION_ID INTEGER NOT NULL, 
    PRECIO INTEGER, 
    DESCUENTO INTEGER, 
    INVITACION INTEGER
   ) ;
   
  ALTER TABLE PAR_PRECIOS_SESION ADD CONSTRAINT PAR_PRECIOS_SESION_UK1 UNIQUE (SESION_ID, LOCALIZACION_ID);   
   
--------------------------------------------------------
--  DDL for Table PAR_SESIONES
--------------------------------------------------------

  CREATE TABLE PAR_SESIONES 
   (    
    ID SERIAL UNIQUE, 
    EVENTO_ID INTEGER NOT NULL, 
    FECHA_CELEBRACION TIMESTAMP NOT NULL, 
    FECHA_INICIO_VENTA_ONLINE TIMESTAMP, 
    FECHA_FIN_VENTA_ONLINE TIMESTAMP, 
    HORA_APERTURA VARCHAR(10), 
    CANAL_INTERNET BOOLEAN DEFAULT TRUE, 
    CANAL_TAQUILLA BOOLEAN DEFAULT TRUE, 
    PLANTILLA_ID INTEGER
   );

--------------------------------------------------------
--  DDL for Table PAR_USUARIOS
--------------------------------------------------------

  CREATE TABLE PAR_USUARIOS 
   (    
    ID SERIAL UNIQUE, 
    NOMBRE VARCHAR(255) NOT NULL, 
    USUARIO VARCHAR(255) UNIQUE NOT NULL, 
    MAIL VARCHAR(255) NOT NULL
   ) ;
   
--------------------------------------------------------
--  DDL for Table PAR_BUTACAS
--------------------------------------------------------

  CREATE TABLE PAR_BUTACAS 
   (    
    ID SERIAL UNIQUE,
    SESION_ID INTEGER, 
    LOCALIZACION_ID INTEGER,
    COMPRA_ID INTEGER NOT NULL,  
    FILA VARCHAR(255), 
    NUMERO VARCHAR(255),
    TIPO VARCHAR(255),
    PRECIO INTEGER NOT NULL,
    ANULADA BOOLEAN DEFAULT FALSE
   ) ;
   
--------------------------------------------------------
--  DDL for Table PAR_COMPRAS
--------------------------------------------------------

    CREATE TABLE PAR_COMPRAS 
   (    
    ID SERIAL UNIQUE, 
    SESION_ID INTEGER, 
    NOMBRE VARCHAR(255), 
    APELLIDOS VARCHAR(255),
    DIRECCION VARCHAR(255),
    POBLACION VARCHAR(255),
    CP VARCHAR(255),
    PROVINCIA VARCHAR(255),
    TFNO VARCHAR(255), 
    EMAIL VARCHAR(255),
    INFO_PERIODICA BOOLEAN DEFAULT FALSE, 
    FECHA TIMESTAMP NOT NULL, 
    TAQUILLA BOOLEAN DEFAULT FALSE NOT NULL,
    IMPORTE INTEGER,
    CODIGO_PAGO_TARJETA VARCHAR(255),
    PAGADA BOOLEAN DEFAULT FALSE,
    UUID VARCHAR(36),
    CODIGO_PAGO_PASARELA VARCHAR(255),
    RESERVA BOOLEAN DEFAULT FALSE,
    DESDE TIMESTAMP,
    HASTA TIMESTAMP,
    OBSERVACIONES_RESERVA VARCHAR(36),
    ANULADA BOOLEAN DEFAULT FALSE
   );
   
--------------------------------------------------------
--  DDL for Table PAR_MAILS
--------------------------------------------------------
    CREATE TABLE PAR_MAILS 
   (    
    ID SERIAL UNIQUE, 
    PARA VARCHAR(255) NOT NULL, 
    DE VARCHAR(255) NOT NULL,
    TITULO TEXT,
    TEXTO TEXT,
    FECHA_CREADO TIMESTAMP, 
    FECHA_ENVIADO TIMESTAMP
   );

--------------------------------------------------------
--  Ref Constraints for Table PAR_EVENTOS
--------------------------------------------------------

  ALTER TABLE PAR_EVENTOS ADD CONSTRAINT PAR_EVENTOS_PAR_TIPOS_EVE_FK1 FOREIGN KEY (TIPO_EVENTO_ID)
      REFERENCES PAR_TIPOS_EVENTO (ID);
--------------------------------------------------------
--  Ref Constraints for Table PAR_PRECIOS_PLANTILLA
--------------------------------------------------------

  ALTER TABLE PAR_PRECIOS_PLANTILLA ADD CONSTRAINT PAR_PRECIOS_PLANTILLA_PAR_FK1 FOREIGN KEY (LOCALIZACION_ID)
      REFERENCES PAR_LOCALIZACIONES (ID);
 
  ALTER TABLE PAR_PRECIOS_PLANTILLA ADD CONSTRAINT PAR_PRECIOS_PLANTILLA_PAR_FK2 FOREIGN KEY (PLANTILLA_ID)
      REFERENCES PAR_PLANTILLAS (ID) ON DELETE CASCADE;
--------------------------------------------------------
--  Ref Constraints for Table PAR_PRECIOS_SESION
--------------------------------------------------------

  ALTER TABLE PAR_PRECIOS_SESION ADD CONSTRAINT PAR_PRECIOS_SESION_PAR_LO_FK1 FOREIGN KEY (LOCALIZACION_ID)
      REFERENCES PAR_LOCALIZACIONES (ID);
 
  ALTER TABLE PAR_PRECIOS_SESION ADD CONSTRAINT PAR_PRECIOS_SESION_PAR_SE_FK1 FOREIGN KEY (SESION_ID)
      REFERENCES PAR_SESIONES (ID) ON DELETE CASCADE;
--------------------------------------------------------
--  Ref Constraints for Table PAR_SESIONES
--------------------------------------------------------

  ALTER TABLE PAR_SESIONES ADD CONSTRAINT PAR_SESIONES_PAR_EVENTOS_FK1 FOREIGN KEY (EVENTO_ID)
      REFERENCES PAR_EVENTOS (ID) ON DELETE CASCADE;
 
  ALTER TABLE PAR_SESIONES ADD CONSTRAINT PAR_SESIONES_PAR_PLANTILL_FK1 FOREIGN KEY (PLANTILLA_ID)
      REFERENCES PAR_PLANTILLAS (ID);

--------------------------------------------------------
--  Ref Constraints for Table PAR_COMPRAS
-------------------------------------------------------
      
  ALTER TABLE PAR_COMPRAS ADD CONSTRAINT PAR_COMPRAS_SE_FK1 FOREIGN KEY (SESION_ID)
      REFERENCES PAR_SESIONES (ID) ON DELETE CASCADE;    
          
--------------------------------------------------------
--  Ref Constraints for Table PAR_BUTACAS
--------------------------------------------------------

  ALTER TABLE PAR_BUTACAS ADD CONSTRAINT PAR_BUTACAS_LO_FK1 FOREIGN KEY (LOCALIZACION_ID)
      REFERENCES PAR_LOCALIZACIONES (ID);
 
  ALTER TABLE PAR_BUTACAS ADD CONSTRAINT PAR_BUTACAS_SE_FK1 FOREIGN KEY (SESION_ID)
      REFERENCES PAR_SESIONES (ID) ON DELETE CASCADE;    
      
  ALTER TABLE PAR_BUTACAS ADD CONSTRAINT PAR_BUTACAS_CO_FK1 FOREIGN KEY (COMPRA_ID)
      REFERENCES PAR_COMPRAS (ID) ON DELETE CASCADE;        
      
    
--------------------------------------------------------
--  Constraints for Table PAR_BUTACAS
--------------------------------------------------------

-- Permite unique por (sesion, localizacion, fila, numero) pero sin dar problemas cuando fila=null y numero=null (las butacas no numeradas)
CREATE UNIQUE INDEX PAR_BUTACAS_UK ON PAR_BUTACAS (SESION_ID, LOCALIZACION_ID, (CASE WHEN (FILA IS NULL) THEN CAST(ID+1000 AS VARCHAR) ELSE FILA END), (CASE WHEN (NUMERO IS NULL) THEN CAST(ID+1000 AS VARCHAR) ELSE NUMERO END), (CASE WHEN (ANULADA=TRUE) THEN ID+1000 ELSE 1 END));


Insert into PAR_LOCALIZACIONES (ID,NOMBRE_ES,TOTAL_ENTRADAS,NOMBRE_VA,CODIGO) values (1,'Platea nivel 2',225,'Platea nivell 2','platea2');
Insert into PAR_LOCALIZACIONES (ID,NOMBRE_ES,TOTAL_ENTRADAS,NOMBRE_VA,CODIGO) values (2,'Discapacitados platea nivel 1',3,'Discapacitats platea nivell 1','discapacitados1');
Insert into PAR_LOCALIZACIONES (ID,NOMBRE_ES,TOTAL_ENTRADAS,NOMBRE_VA,CODIGO) values (3,'Discapacitados platea nivel 2',3,'Discapacitats platea nivell 2','discapacitados2');
Insert into PAR_LOCALIZACIONES (ID,NOMBRE_ES,TOTAL_ENTRADAS,NOMBRE_VA,CODIGO) values (4,'Discapacitados anfiteatro',1,'Discapacitats anfiteatre','discapacitados3');
Insert into PAR_LOCALIZACIONES (ID,NOMBRE_ES,TOTAL_ENTRADAS,NOMBRE_VA,CODIGO) values (5,'Anfiteatro',144,'Amfiteatre','anfiteatro');
Insert into PAR_LOCALIZACIONES (ID,NOMBRE_ES,TOTAL_ENTRADAS,NOMBRE_VA,CODIGO) values (6,'Platea nivel 1',266,'Platea nivell 1','platea1');

