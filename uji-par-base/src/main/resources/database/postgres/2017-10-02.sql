ALTER TABLE PAR_EVENTOS ADD COLUMN IMAGEN_UUID VARCHAR(255);
ALTER TABLE PAR_EVENTOS ADD COLUMN IMAGEN_PUBLI_UUID VARCHAR(255);

INSERT INTO par_version_bbdd (VERSION) VALUES ('2017-10-02.SQL');