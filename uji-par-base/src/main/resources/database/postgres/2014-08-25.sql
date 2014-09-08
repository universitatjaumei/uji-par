CREATE TABLE par_compras_borradas
(
  id serial NOT NULL,
  compra_id integer,
  sesion_id integer,
  nombre character varying(255),
  apellidos character varying(255),
  direccion character varying(255),
  poblacion character varying(255),
  cp character varying(255),
  provincia character varying(255),
  tfno character varying(255),
  email character varying(255),
  info_periodica boolean DEFAULT false,
  fecha timestamp without time zone NOT NULL,
  taquilla boolean NOT NULL DEFAULT false,
  importe integer,
  codigo_pago_tarjeta character varying(255),
  pagada boolean DEFAULT false,
  uuid character varying(36),
  codigo_pago_pasarela character varying(255),
  reserva boolean DEFAULT false,
  desde timestamp without time zone,
  hasta timestamp without time zone,
  observaciones_reserva character varying(36),
  anulada boolean DEFAULT false,
  recibo_pinpad character varying(3500),
  caducada boolean DEFAULT false,
  referencia_pago character varying(255),
  compra_uuid character varying(100),
  fecha_borrado timestamp without time zone DEFAULT now(),
  codigo_error_banco character varying(255),
  CONSTRAINT par_compras_borradas_pkey PRIMARY KEY (id ),
  CONSTRAINT par_compras_borradas_id_key UNIQUE (id )
)
WITH (
  OIDS=FALSE
);


CREATE TABLE par_butacas_borradas
(
  id serial NOT NULL,
  butaca_id integer,
  sesion_id integer,
  localizacion_id integer,
  compra_id integer NOT NULL,
  fila character varying(255),
  numero character varying(255),
  tipo character varying(255),
  precio integer NOT NULL,
  anulada boolean DEFAULT false,
  presentada timestamp without time zone,
  fecha_borrada timestamp without time zone DEFAULT now(),
  CONSTRAINT par_butacas_borradas_pkey PRIMARY KEY (id ),
  CONSTRAINT par_butacas_borradas_id_key UNIQUE (id )
)
WITH (
  OIDS=FALSE
);