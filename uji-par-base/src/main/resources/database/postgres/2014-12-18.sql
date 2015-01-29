CREATE TABLE PAR_TPVS
(
  ID serial NOT NULL,
  NOMBRE character varying(255) not null,
  CODE character varying(255) not null,
  CURRENCY character varying(255) not null,
  TERMINAL character varying(255) not null,
  TRANSACTION_CODE character varying(255) not null,
  ORDER_PREFIX character varying(255) not null,
  LANG_CA_CODE character varying(255) not null,
  LANG_ES_CODE character varying(255) not null,
  URL character varying(255) not null,
  WSDL_URL character varying(255) not null,
  SECRET character varying(255) not null,
  DEFAULT_TPV boolean DEFAULT false,
  CONSTRAINT PAR_TPVS_PKEY PRIMARY KEY (ID)
)
WITH (
  OIDS=FALSE
);

ALTER TABLE PAR_EVENTOS ADD COLUMN TPV_ID integer DEFAULT 1;

--Insert de el TPV de Benicassim, Vila-real con ID = 1 para ponerlo por defecto en la relación entre evento y TPV
INSERT INTO PAR_TPVS (ID, NOMBRE, CODE, CURRENCY, TERMINAL, TRANSACTION_CODE, ORDER_PREFIX, LANG_CA_CODE, LANG_ES_CODE, URL, WSDL_URL, SECRET, DEFAULT_TPV)
              VALUES (1, 'ILUSTRISIMO AYUNTAMIENTO', '055136832', '978', '001', '0', '0000', '003', '001', 'https://sis-t.redsys.es:25443/sis/realizarPago', 'http://wifi.benicassim.es/japps/par-public/services/InotificacionSIS', '01234', true);

ALTER TABLE PAR_EVENTOS ADD CONSTRAINT PAR_EVENTOS_TPVS_FK1 FOREIGN KEY (TPV_ID) REFERENCES PAR_TPVS (ID);

INSERT INTO par_version_bbdd (VERSION) VALUES ('2014-12-18.SQL');