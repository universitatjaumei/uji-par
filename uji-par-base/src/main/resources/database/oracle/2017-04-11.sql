ALTER TABLE PAR_CINES ADD (LANGS VARCHAR(250));
ALTER TABLE PAR_CINES ADD (DEFAULT_LANG VARCHAR(2));

INSERT INTO par_version_bbdd (VERSION) VALUES ('2017-04-11.SQL');