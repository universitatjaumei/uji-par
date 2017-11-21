ALTER TABLE par_cines
    ADD COLUMN passbook_activado BOOLEAN DEFAULT FALSE;

INSERT INTO par_version_bbdd (VERSION) VALUES ('2017-10-16.SQL');