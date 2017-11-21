ALTER TABLE par_cines
  ADD passbook_activado NUMBER(1) DEFAULT 0;

INSERT INTO par_version_bbdd (VERSION) VALUES ('2017-10-16.SQL');