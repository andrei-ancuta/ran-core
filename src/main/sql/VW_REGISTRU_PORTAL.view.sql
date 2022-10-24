-- schema registru:
GRANT SELECT ON nom_stare_registru TO ran_portal;


-- schema portal:
CREATE SYNONYM nom_stare_registru_core FOR ran_registru_v2.nom_stare_registru;

CREATE OR REPLACE VIEW VW_REGISTRU_PORTAL AS
SELECT
  rp.ID_REGISTRU AS ID_REGISTRU,
  rp.INDEX_REGISTRU AS INDEX_REGISTRU,
  rp.DATA_REGISTRU AS DATA_REGISTRU,
  rp.DENUMIRE_FISIER AS DENUMIRE_FISIER,
  rp.FK_INCARCARE AS FK_INCARCARE,
  rp.IS_RECIPISA_SEMNATA AS IS_RECIPISA_SEMNATA,
  srp.COD AS COD_SRP,
  srp.DENUMIRE AS DENUMIRE_SRP,
  frp.DATA_STARE AS DATA_STARE_FRP,
  frp.MESAJ_STARE AS MESAJ_STARE_FRP,
  src.COD AS COD_SRC,
  src.DENUMIRE AS DENUMIRE_SRC
FROM REGISTRU rp
  LEFT JOIN NOM_STARE_REGISTRU srp ON (rp.fk_nom_stare_registru = srp.id_nom_stare_registru)
  LEFT JOIN FLUX_REGISTRU frp ON (frp.fk_registru = rp.id_registru and frp.fk_nom_stare_registru = rp.fk_nom_stare_registru)
  LEFT JOIN REGISTRU_CORE rc ON (rp.INDEX_REGISTRU = rc.INDEX_REGISTRU)
  LEFT JOIN NOM_STARE_REGISTRU_CORE src ON (rc.fk_nom_stare_registru = src.id_nom_stare_registru)
;
