CREATE TABLE par_compras_borradas
(
  id number,
  compra_id number,
  sesion_id number,
  nombre varchar2(255),
  apellidos varchar2(255),
  direccion varchar2(255),
  poblacion varchar2(255),
  cp varchar2(255),
  provincia varchar2(255),
  tfno varchar2(255),
  email varchar2(255),
  info_periodica NUMBER(1,0) DEFAULT 0,
  fecha TIMESTAMP (6) NOT NULL,
  taquilla NUMBER(1,0) DEFAULT 0,
  importe number,
  codigo_pago_tarjeta varchar2(255),
  pagada NUMBER(1,0) DEFAULT 0,
  uuid varchar2(36),
  codigo_pago_pasarela varchar2(255),
  reserva NUMBER(1,0) DEFAULT 0,
  desde TIMESTAMP (6),
  hasta TIMESTAMP (6),
  observaciones_reserva varchar2(36),
  anulada NUMBER(1,0) DEFAULT 0,
  recibo_pinpad varchar2(3500),
  caducada NUMBER(1,0) DEFAULT 0,
  referencia_pago varchar2(255),
  compra_uuid varchar2(100),
  fecha_borrado TIMESTAMP (6) DEFAULT SYSDATE,
  codigo_error_banco varchar2(255),
  CONSTRAINT "PAR_COMPRAS_BORRADAS_PK" PRIMARY KEY ("ID"),
  CONSTRAINT "PAR_COMPRAS_BORRADAS_SE_FK1" FOREIGN KEY ("SESION_ID")
	  REFERENCES PAR_SESIONES ("ID") ON DELETE CASCADE ENABLE, 
  CONSTRAINT "PAR_COMPRAS_BORRADAS_CO_FK1" FOREIGN KEY ("COMPRA_ID")
  REFERENCES PAR_COMPRAS ("ID") ON DELETE CASCADE ENABLE
);


CREATE TABLE par_butacas_borradas
(
  id number,
  butaca_id number,
  sesion_id number,
  localizacion_id number,
  compra_id number NOT NULL,
  fila varchar2(255),
  numero varchar2(255),
  tipo varchar2(255),
  precio number NOT NULL,
  anulada NUMBER(1,0) DEFAULT 0,
  presentada TIMESTAMP (6),
  fecha_borrada TIMESTAMP (6) DEFAULT SYSDATE,
  CONSTRAINT "PAR_BUTACAS_BORRADAS_PK" PRIMARY KEY ("ID"),
  CONSTRAINT "PAR_BUTACAS_BORRADAS_SE_FK1" FOREIGN KEY ("SESION_ID")
	  REFERENCES PAR_SESIONES ("ID") ON DELETE CASCADE ENABLE
);