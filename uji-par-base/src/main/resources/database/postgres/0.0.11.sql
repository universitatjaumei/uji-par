ALTER TABLE PAR_PRECIOS_PLANTILLA ADD COLUMN AULA_TEATRO INTEGER;
ALTER TABLE PAR_PRECIOS_SESION ADD COLUMN AULA_TEATRO INTEGER;

UPDATE PAR_PRECIOS_PLANTILLA SET AULA_TEATRO=0 WHERE AULA_TEATRO IS null;
UPDATE PAR_PRECIOS_SESION SET AULA_TEATRO=0 WHERE AULA_TEATRO IS null;