-- Este select permite saber cuantos eventos tienen sesiones con diferentes formatos (debería ser 0)

--select s.* from par_sesiones s, par_sesiones ss where ss.evento_id = s.evento_id and s.formato <> ss.formato;

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