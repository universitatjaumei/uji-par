-- Este select permite saber cuantos eventos tienen sesiones con diferentes formatos (debería ser 0)

--select pe.*, ps.* from par_eventos pe join par_sesiones ps on ps.evento_id = pe.id where pe.formato <> ps.formato;

ALTER TABLE PAR_EVENTOS ADD COLUMN FORMATO VARCHAR(400);
UPDATE PAR_EVENTOS SET FORMATO = PAR_SESIONES.FORMATO FROM PAR_SESIONES WHERE PAR_SESIONES.EVENTO_ID = PAR_EVENTOS.ID;
ALTER TABLE PAR_SESIONES DROP COLUMN FORMATO;

ALTER TABLE PAR_EVENTOS_MULTISESION ADD COLUMN VER_LING VARCHAR(400);