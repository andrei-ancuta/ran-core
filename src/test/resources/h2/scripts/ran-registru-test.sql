DROP TABLE IF EXISTS NOM_TIP_ACT;

create table NOM_TIP_ACT
(
  id_nom_tip_act     NUMBER not null,
  cod                VARCHAR2(10 ) not null,
  denumire           VARCHAR2(100 ) not null,
  descriere          VARCHAR2(500 ),
  data_start         DATE not null,
  data_stop          DATE,
  base_id            NUMBER not null,
  last_modified_date TIMESTAMP not null,
  primary key (ID_NOM_TIP_ACT)
);
create index IDX_NOM_TIP_ACT_BASE_ID on NOM_TIP_ACT (BASE_ID);
create index IDX_NOM_TIP_ACT_COD on NOM_TIP_ACT (COD);
create index IDX_NOM_TIP_ACT_DAT_START_STOP on NOM_TIP_ACT (DATA_START, DATA_STOP);
create index IDX_NOM_TIP_ACT_LMD on NOM_TIP_ACT (LAST_MODIFIED_DATE);

DROP TABLE IF EXISTS ACT;

create table ACT
(
  id_act             NUMBER not null,
  fk_nom_tip_act     NUMBER not null,
  fk_nom_judet       NUMBER,
  numar_act          VARCHAR2(20 ) not null,
  data_act           DATE not null,
  emitent            VARCHAR2(200 ),
  last_modified_date TIMESTAMP not null,
  primary key (ID_ACT)
);
create index IDX_ACT_FK_NOM_TIP_ACT on ACT (FK_NOM_TIP_ACT);
create index IDX_ACT_LMD on ACT (LAST_MODIFIED_DATE);
alter table ACT add foreign key (FK_NOM_TIP_ACT) references NOM_TIP_ACT (ID_NOM_TIP_ACT);

DROP TABLE IF EXISTS NOM_JUDET;
create table NOM_JUDET
(
  id_nom_judet       NUMBER not null,
  cod_alfa           VARCHAR2(2 ) not null,
  cod_siruta         NUMBER(6) not null,
  denumire           VARCHAR2(50 ) not null,
  data_start         DATE not null,
  data_stop          DATE,
  base_id            NUMBER not null,
  uid_cms            NUMBER,
  last_modified_date TIMESTAMP not null,
  primary key (ID_NOM_JUDET)
);
create index IDX_NOM_JUDET_BASE_ID on NOM_JUDET (BASE_ID);
create index IDX_NOM_JUDET_COD_ALFA on NOM_JUDET (COD_ALFA);
create index IDX_NOM_JUDET_COD_SIRUTA on NOM_JUDET (COD_SIRUTA);
create index IDX_NOM_JUDET_DATA_START_STOP on NOM_JUDET (DATA_START, DATA_STOP);
create index IDX_NOM_JUDET_LMD on NOM_JUDET (LAST_MODIFIED_DATE);
create index IDX_NOM_JUDET_UID_CMS on NOM_JUDET (UID_CMS);

DROP TABLE IF EXISTS NOM_UAT;
create table NOM_UAT
(
  id_nom_uat         NUMBER not null,
  fk_nom_judet       NUMBER not null,
  cod_siruta         NUMBER(6) not null,
  denumire           VARCHAR2(50 ) not null,
  data_start         DATE not null,
  data_stop          DATE,
  base_id            NUMBER not null,
  uid_cms            NUMBER,
  last_modified_date TIMESTAMP not null,
  primary key (ID_NOM_UAT)
);
create index IDX_NOM_UAT_BASE_ID on NOM_UAT (BASE_ID);
create index IDX_NOM_UAT_COD_SIRUTA on NOM_UAT (COD_SIRUTA);
create index IDX_NOM_UAT_DATA_START_STOP on NOM_UAT (DATA_START, DATA_STOP);
create index IDX_NOM_UAT_FK_NOM_JUDET on NOM_UAT (FK_NOM_JUDET);
create index IDX_NOM_UAT_LMD on NOM_UAT (LAST_MODIFIED_DATE);
create index IDX_NOM_UAT_UID_CMS on NOM_UAT (UID_CMS);

alter table NOM_UAT add foreign key (FK_NOM_JUDET) references NOM_JUDET (ID_NOM_JUDET);

DROP TABLE IF EXISTS NOM_LOCALITATE;
create table NOM_LOCALITATE
(
  id_nom_localitate  NUMBER not null,
  fk_nom_uat         NUMBER not null,
  tip                NUMBER(2) not null,
  cod_siruta         NUMBER(6) not null,
  denumire           VARCHAR2(50 ) not null,
  data_start         DATE not null,
  data_stop          DATE,
  base_id            NUMBER not null,
  uid_cms            NUMBER,
  last_modified_date TIMESTAMP not null,
  primary key (ID_NOM_LOCALITATE)
);
create index IDX_NOM_LOCALITATE_BASE_ID on NOM_LOCALITATE (BASE_ID);
create index IDX_NOM_LOCALITATE_COD_SIRUTA on NOM_LOCALITATE (COD_SIRUTA);
create index IDX_NOM_LOCALITATE_FK_NOM_UAT on NOM_LOCALITATE (FK_NOM_UAT);
create index IDX_NOM_LOCALITATE_LMD on NOM_LOCALITATE (LAST_MODIFIED_DATE);
create index IDX_NOM_LOCALITATE_UID_CMS on NOM_LOCALITATE (UID_CMS);
create index IDX_NOM_LOC_DATA_START_STOP on NOM_LOCALITATE (DATA_START, DATA_STOP);

alter table NOM_LOCALITATE add foreign key (FK_NOM_UAT) references NOM_UAT (ID_NOM_UAT);

DROP TABLE IF EXISTS NOM_TIP_DETINATOR;

create table NOM_TIP_DETINATOR
(
  id_nom_tip_detinator NUMBER not null,
  cod                  VARCHAR2(10 ) not null,
  denumire             VARCHAR2(100 ) not null,
  descriere            VARCHAR2(500 ),
  data_start           DATE not null,
  data_stop            DATE,
  base_id              NUMBER not null,
  last_modified_date   TIMESTAMP not null,
  primary key (ID_NOM_TIP_DETINATOR)
);
create index IDX_NOM_TIP_DETINATOR_BASE_ID on NOM_TIP_DETINATOR (BASE_ID);
create index IDX_NOM_TIP_DETINATOR_COD on NOM_TIP_DETINATOR (COD);
create index IDX_NOM_TIP_DETINATOR_LMD on NOM_TIP_DETINATOR (LAST_MODIFIED_DATE);
create index IDX_NOM_TIP_DET_DAT_START_STOP on NOM_TIP_DETINATOR (DATA_START, DATA_STOP);


DROP TABLE IF EXISTS NOM_TIP_EXPLOATATIE;
create table NOM_TIP_EXPLOATATIE
(
  id_nom_tip_exploatatie NUMBER not null,
  cod                    VARCHAR2(10 ),
  denumire               VARCHAR2(150 ) not null,
  descriere              VARCHAR2(500 ),
  cod_rand               NUMBER(2) not null,
  tip_persoana           NUMBER(1) not null,
  data_start             DATE not null,
  data_stop              DATE,
  base_id                NUMBER not null,
  last_modified_date     TIMESTAMP not null,
  primary key (ID_NOM_TIP_EXPLOATATIE),
  check (TIP_PERSOANA IN (1,2))
);
create index IDX_NOM_TIP_EXPLOATATIE_COD on NOM_TIP_EXPLOATATIE (COD);
create index IDX_NOM_TIP_EXPLOATIE_LMD on NOM_TIP_EXPLOATATIE (LAST_MODIFIED_DATE);
create index IDX_NOM_TIP_EXPLOAT_BASE_ID on NOM_TIP_EXPLOATATIE (BASE_ID);
create index IDX_NOM_TIP_EXPLOAT_COD_RAND on NOM_TIP_EXPLOATATIE (COD_RAND);
create index IDX_NOM_TIP_EXPLOAT_TIP_PERS on NOM_TIP_EXPLOATATIE (TIP_PERSOANA);
create index IDX_NOM_TIP_EXPL_DATA_STRT_STP on NOM_TIP_EXPLOATATIE (DATA_START, DATA_STOP);


DROP TABLE IF EXISTS GOSPODARIE;
create table GOSPODARIE
(
  id_gospodarie            NUMBER not null,
  fk_nom_judet             NUMBER not null,
  fk_nom_uat               NUMBER not null,
  fk_nom_localitate        NUMBER not null,
  fk_nom_tip_detinator     NUMBER not null,
  fk_nom_tip_exploatatie   NUMBER not null,
  identificator            VARCHAR2(30 ) not null,
  ident_volum              VARCHAR2(20 ) not null,
  ident_pozitie_curenta    NUMBER(10) not null,
  ident_pozitie_anterioara NUMBER(10) not null,
  ident_rol_nominal_unic   NUMBER(10),
  cod_exploatatie          VARCHAR2(30 ),
  nr_unic_identificare     VARCHAR2(30 ),
  is_activ                 NUMBER(1) not null,
  insert_date              TIMESTAMP not null,
  last_modified_date       TIMESTAMP not null,
  primary key (ID_GOSPODARIE),
  check (IS_ACTIV IN (0,1))
);
create index IDX_GOSPODARIE_COD_EXPLOATATIE on GOSPODARIE (COD_EXPLOATATIE);
create index IDX_GOSPODARIE_FK_NOM_JUDET on GOSPODARIE (FK_NOM_JUDET);
create index IDX_GOSPODARIE_FK_NOM_LOCALIT on GOSPODARIE (FK_NOM_LOCALITATE);
create index IDX_GOSPODARIE_FK_NOM_TIP_DET on GOSPODARIE (FK_NOM_TIP_DETINATOR);
create index IDX_GOSPODARIE_FK_NOM_TIP_EXPL on GOSPODARIE (FK_NOM_TIP_EXPLOATATIE);
create index IDX_GOSPODARIE_FK_NOM_UAT on GOSPODARIE (FK_NOM_UAT);
create index IDX_GOSPODARIE_IDENTIFICATOR on GOSPODARIE (IDENTIFICATOR);
--create bitmap index IDX_GOSPODARIE_IS_ACTIV on GOSPODARIE (IS_ACTIV)create index IDX_GOSPODARIE_LMD on GOSPODARIE (LAST_MODIFIED_DATE);
create index IDX_GOSPODARIE_NR_UNIC_IDENTIF on GOSPODARIE (NR_UNIC_IDENTIFICARE);

alter table GOSPODARIE add foreign key (FK_NOM_JUDET)  references NOM_JUDET (ID_NOM_JUDET);
alter table GOSPODARIE add foreign key (FK_NOM_LOCALITATE)  references NOM_LOCALITATE (ID_NOM_LOCALITATE);
alter table GOSPODARIE add foreign key (FK_NOM_TIP_DETINATOR) references NOM_TIP_DETINATOR (ID_NOM_TIP_DETINATOR);
alter table GOSPODARIE add foreign key (FK_NOM_TIP_EXPLOATATIE) references NOM_TIP_EXPLOATATIE (ID_NOM_TIP_EXPLOATATIE);
alter table GOSPODARIE add foreign key (FK_NOM_UAT) references NOM_UAT (ID_NOM_UAT);

DROP TABLE IF EXISTS NOM_CAPITOL;
create table NOM_CAPITOL
(
  id_nom_capitol NUMBER not null,
  cod            VARCHAR2(20 ) not null,
  denumire       VARCHAR2(200 ) not null,
  descriere      VARCHAR2(500 ),
  alias          VARCHAR2(50 ),
  data_start     DATE not null,
  data_stop      DATE,
  base_id        NUMBER not null,
  primary key (ID_NOM_CAPITOL)
);
create index IDX_NOM_CAPITOL_BASE_ID on NOM_CAPITOL (BASE_ID);
create index IDX_NOM_CAPITOL_COD on NOM_CAPITOL (COD);
create index IDX_NOM_CAP_DATA_START_STOP on NOM_CAPITOL (DATA_START, DATA_STOP);

DROP TABLE IF EXISTS NOM_CATEGORIE_FOLOSINTA;

create table NOM_CATEGORIE_FOLOSINTA
(
  id_nom_categorie_folosinta NUMBER not null,
  fk_nom_categorie_folosinta NUMBER,
  cod                        VARCHAR2(10 ) not null,
  denumire                   VARCHAR2(100 ) not null,
  descriere                  VARCHAR2(500 ),
  primary key (ID_NOM_CATEGORIE_FOLOSINTA),
  unique UK_NOM_CATEGORIE_FOLOSINTA_COD (COD)
);
create index IDX_NOM_CAT_FOL_FK_NOM_CAT_FOL on NOM_CATEGORIE_FOLOSINTA (FK_NOM_CATEGORIE_FOLOSINTA);

DROP TABLE IF EXISTS CAP_CATEGORIE_FOLOSINTA;
create table CAP_CATEGORIE_FOLOSINTA
(
  id_cap_categorie_folosinta NUMBER not null,
  fk_cap_categorie_folosinta NUMBER,
  fk_nom_capitol             NUMBER not null,
  fk_nom_categorie_folosinta NUMBER,
  cod                        VARCHAR2(10 ),
  denumire                   VARCHAR2(100 ) not null,
  descriere                  VARCHAR2(500 ),
  cod_rand                   NUMBER(2) not null,
  is_formula                 NUMBER(1) not null,
  tip_formula_relatie        NUMBER(1) not null,
  data_start                 DATE not null,
  data_stop                  DATE,
  base_id                    NUMBER not null,
  last_modified_date         TIMESTAMP not null,
  primary key (ID_CAP_CATEGORIE_FOLOSINTA),
  check (IS_FORMULA IN (0,1)),
  check (TIP_FORMULA_RELATIE IN (-1,0,1))
);
create index IDX_CAP_CATEG_FOLOSINTA_COD on CAP_CATEGORIE_FOLOSINTA (COD);
create index IDX_CAP_CATEG_FOLOSINTA_LMD on CAP_CATEGORIE_FOLOSINTA (LAST_MODIFIED_DATE);
create index IDX_CAP_CATEG_FOLOS_BASE_ID on CAP_CATEGORIE_FOLOSINTA (BASE_ID);
create index IDX_CAP_CATEG_FOLOS_COD_RAND on CAP_CATEGORIE_FOLOSINTA (COD_RAND);
create index IDX_CAP_CATEG_FOL_FK_NOM_CAP on CAP_CATEGORIE_FOLOSINTA (FK_NOM_CAPITOL);
create index IDX_CAP_CAT_FOL_DAT_START_STOP on CAP_CATEGORIE_FOLOSINTA (DATA_START, DATA_STOP);
create index IDX_CAP_CAT_FOL_FK_CAP_CAT_FOL on CAP_CATEGORIE_FOLOSINTA (FK_CAP_CATEGORIE_FOLOSINTA);
create index IDX_CAP_CAT_FOL_FK_NOM_CAT_FOL on CAP_CATEGORIE_FOLOSINTA (FK_NOM_CATEGORIE_FOLOSINTA);

alter table CAP_CATEGORIE_FOLOSINTA add foreign key (FK_NOM_CAPITOL)  references NOM_CAPITOL (ID_NOM_CAPITOL);
alter table CAP_CATEGORIE_FOLOSINTA add foreign key (FK_CAP_CATEGORIE_FOLOSINTA)  references CAP_CATEGORIE_FOLOSINTA (ID_CAP_CATEGORIE_FOLOSINTA);
alter table CAP_CATEGORIE_FOLOSINTA add foreign key (FK_NOM_CATEGORIE_FOLOSINTA)  references NOM_CATEGORIE_FOLOSINTA (ID_NOM_CATEGORIE_FOLOSINTA);

DROP TABLE IF EXISTS  NOM_MODALITATE_DETINERE;

create table NOM_MODALITATE_DETINERE
(
  id_nom_modalitate_detinere NUMBER not null,
  cod                        VARCHAR2(10 ) not null,
  denumire                   VARCHAR2(100 ) not null,
  descriere                  VARCHAR2(500 ),
  data_start                 DATE not null,
  data_stop                  DATE,
  base_id                    NUMBER not null,
  last_modified_date         TIMESTAMP not null,
  primary key (ID_NOM_MODALITATE_DETINERE)
);
create index IDX_NOM_MODALITAT_DETINERE_COD on NOM_MODALITATE_DETINERE (COD);
create index IDX_NOM_MODALITAT_DETINERE_LMD on NOM_MODALITATE_DETINERE (LAST_MODIFIED_DATE);
create index IDX_NOM_MODAL_DETINERE_BASE_ID on NOM_MODALITATE_DETINERE (BASE_ID);
create index IDX_NOM_MOD_DET_DAT_START_STOP on NOM_MODALITATE_DETINERE (DATA_START, DATA_STOP);

DROP TABLE IF EXISTS PARCELA_TEREN;

create table PARCELA_TEREN
(
  id_parcela_teren           NUMBER not null,
  fk_gospodarie              NUMBER not null,
  fk_cap_categorie_folosinta NUMBER not null,
  fk_nom_modalitate_detinere NUMBER,
  fk_act_instrainare         NUMBER,
  fk_nom_judet               NUMBER,
  an                         NUMBER(4) not null,
  denumire                   VARCHAR2(100 ) not null,
  cod_rand                   NUMBER(2) not null,
  intravilan_extravilan      NUMBER(1) not null,
  suprafata                  NUMBER(15) not null,
  nr_bloc_fizic              VARCHAR2(20 ),
  mentiune                   VARCHAR2(500 ),
  last_modified_date         TIMESTAMP not null,
  primary key (ID_PARCELA_TEREN),
  check (INTRAVILAN_EXTRAVILAN IN (1,2))
);
create index IDX_PARCELA_TEREN_AN on PARCELA_TEREN (AN);
create index IDX_PARCELA_TEREN_FK_GOSPOD on PARCELA_TEREN (FK_GOSPODARIE);
create index IDX_PARCELA_TEREN_LMD on PARCELA_TEREN (LAST_MODIFIED_DATE);
create index IDX_PARC_TEREN_FK_ACT_INSTRAIN on PARCELA_TEREN (FK_ACT_INSTRAINARE);
create index IDX_PARC_TEREN_FK_CAP_CAT_FOL on PARCELA_TEREN (FK_CAP_CATEGORIE_FOLOSINTA);
create index IDX_PARC_TEREN_FK_NOM_MOD_DET on PARCELA_TEREN (FK_NOM_MODALITATE_DETINERE);

alter table PARCELA_TEREN add foreign key (FK_ACT_INSTRAINARE) references ACT (ID_ACT);
alter table PARCELA_TEREN add foreign key (FK_GOSPODARIE)  references GOSPODARIE (ID_GOSPODARIE);
alter table PARCELA_TEREN add foreign key (FK_CAP_CATEGORIE_FOLOSINTA)  references CAP_CATEGORIE_FOLOSINTA (ID_CAP_CATEGORIE_FOLOSINTA);
alter table PARCELA_TEREN add foreign key (FK_NOM_MODALITATE_DETINERE)  references NOM_MODALITATE_DETINERE (ID_NOM_MODALITATE_DETINERE);

DROP TABLE IF EXISTS ACT_DETINERE;
create table ACT_DETINERE
(
  id_act_detinere    NUMBER not null,
  fk_parcela_teren   NUMBER not null,
  fk_act             NUMBER not null,
  fk_nom_judet       NUMBER,
  last_modified_date TIMESTAMP not null,
  primary key (ID_ACT_DETINERE)
);
create index IDX_ACT_DETINERE_FK_ACT on ACT_DETINERE (FK_ACT);
create index IDX_ACT_DETINERE_LMD on ACT_DETINERE (LAST_MODIFIED_DATE);
create index IDX_ACT_DETIN_FK_PARCELA_TEREN on ACT_DETINERE (FK_PARCELA_TEREN);

alter table ACT_DETINERE add foreign key (FK_ACT)  references ACT (ID_ACT);
alter table ACT_DETINERE add foreign key (FK_PARCELA_TEREN) references PARCELA_TEREN (ID_PARCELA_TEREN);

DROP TABLE IF EXISTS  NOM_TARA;
create table NOM_TARA
(
  id_nom_tara        NUMBER not null,
  cod_alfa_2         VARCHAR2(2 ),
  cod_alfa_3         VARCHAR2(3 ),
  cod_numeric        NUMBER(3),
  denumire           VARCHAR2(100 ) not null,
  data_start         DATE not null,
  data_stop          DATE,
  base_id            NUMBER not null,
  last_modified_date TIMESTAMP not null,
  primary key (ID_NOM_TARA)
);
create index IDX_NOM_TARA_BASE_ID on NOM_TARA (BASE_ID);
create index IDX_NOM_TARA_COD_ALFA_2 on NOM_TARA (COD_ALFA_2);
create index IDX_NOM_TARA_COD_ALFA_3 on NOM_TARA (COD_ALFA_3);
create index IDX_NOM_TARA_COD_NUMERIC on NOM_TARA (COD_NUMERIC);
create index IDX_NOM_TARA_DATA_START_STOP on NOM_TARA (DATA_START, DATA_STOP);
create index IDX_NOM_TARA_LMD on NOM_TARA (LAST_MODIFIED_DATE);


DROP TABLE IF EXISTS ADRESA;
create table ADRESA
(
  id_adresa           NUMBER not null,
  fk_nom_tara         NUMBER not null,
  fk_nom_judet        NUMBER,
  fk_nom_uat          NUMBER,
  fk_nom_localitate   NUMBER,
  uid_renns           VARCHAR2(30 ),
  strada              VARCHAR2(50 ),
  nr_strada           VARCHAR2(10 ),
  bloc                VARCHAR2(10 ),
  scara               VARCHAR2(10 ),
  etaj                NUMBER(3),
  apartament          VARCHAR2(10 ),
  exceptie_adresa     VARCHAR2(500 ),
  data_start          DATE not null,
  data_stop           DATE,
  base_id             NUMBER not null,
  last_modified_date  TIMESTAMP not null,
  renns_modified_date TIMESTAMP,
  primary key (ID_ADRESA)
);
create index IDX_ADRESA_BASE_ID on ADRESA (BASE_ID);
create index IDX_ADRESA_DATA_START_STOP on ADRESA (DATA_START, DATA_STOP);
create index IDX_ADRESA_FK_NOM_JUDET on ADRESA (FK_NOM_JUDET);
create index IDX_ADRESA_FK_NOM_LOCALITATE on ADRESA (FK_NOM_LOCALITATE);
create index IDX_ADRESA_FK_NOM_TARA on ADRESA (FK_NOM_TARA);
create index IDX_ADRESA_FK_NOM_UAT on ADRESA (FK_NOM_UAT);
create index IDX_ADRESA_LMD on ADRESA (LAST_MODIFIED_DATE);
create index IDX_ADRESA_UID_RENNS on ADRESA (UID_RENNS);

alter table ADRESA add foreign key (FK_NOM_JUDET)  references NOM_JUDET (ID_NOM_JUDET);
alter table ADRESA add foreign key (FK_NOM_LOCALITATE)  references NOM_LOCALITATE (ID_NOM_LOCALITATE);
alter table ADRESA add foreign key (FK_NOM_TARA)  references NOM_TARA (ID_NOM_TARA);
alter table ADRESA add foreign key (FK_NOM_UAT) references NOM_UAT (ID_NOM_UAT);

DROP TABLE IF EXISTS NOM_TIP_ADRESA;
create table NOM_TIP_ADRESA
(
  id_nom_tip_adresa NUMBER not null,
  cod               VARCHAR2(10 ) not null,
  denumire          VARCHAR2(100 ) not null,
  primary key (ID_NOM_TIP_ADRESA),
  unique UK_TIP_ADRESA_COD (COD)
);

DROP TABLE IF EXISTS  ADRESA_GOSPODARIE;
create table ADRESA_GOSPODARIE
(
  id_adresa_gospodarie NUMBER not null,
  fk_gospodarie        NUMBER not null,
  fk_nom_tip_adresa    NUMBER not null,
  fk_adresa            NUMBER not null,
  fk_nom_judet         NUMBER,
  last_modified_date   TIMESTAMP not null,
  primary key (ID_ADRESA_GOSPODARIE)
);
create index IDX_ADRESA_GOSPOD_FK_ADRESA on ADRESA_GOSPODARIE (FK_ADRESA);
create index IDX_ADRESA_GOSPOD_FK_GOSPOD on ADRESA_GOSPODARIE (FK_GOSPODARIE);
create index IDX_ADRESA_GOSP_FK_NOM_TIP_ADR on ADRESA_GOSPODARIE (FK_NOM_TIP_ADRESA);

alter table ADRESA_GOSPODARIE add foreign key (FK_ADRESA)  references ADRESA (ID_ADRESA);
alter table ADRESA_GOSPODARIE add foreign key (FK_GOSPODARIE)  references GOSPODARIE (ID_GOSPODARIE);
alter table ADRESA_GOSPODARIE add foreign key (FK_NOM_TIP_ADRESA)  references NOM_TIP_ADRESA (ID_NOM_TIP_ADRESA);

DROP TABLE IF EXISTS NOM_SPECIE_ANIMAL;

create table NOM_SPECIE_ANIMAL
(
  id_nom_specie_animal NUMBER not null,
  fk_nom_specie_animal NUMBER,
  cod                  VARCHAR2(10 ) not null,
  denumire             VARCHAR2(100 ) not null,
  descriere            VARCHAR2(500 ),
  last_modified_date   TIMESTAMP not null,
  primary key (ID_NOM_SPECIE_ANIMAL),
  unique UK_NOM_SPECIE_ANIMAL_COD (COD)
);
create index IDX_NOM_SPECIE_ANIMAL_LMD on NOM_SPECIE_ANIMAL (LAST_MODIFIED_DATE);
create index IDX_NOM_SP_ANIM_FK_NOM_SP_ANIM on NOM_SPECIE_ANIMAL (FK_NOM_SPECIE_ANIMAL);

DROP TABLE IF EXISTS NOM_TIP_UNITATE_MASURA;
create table NOM_TIP_UNITATE_MASURA
(
  id_nom_tip_unitate_masura NUMBER not null,
  cod                       VARCHAR2(10 ) not null,
  denumire                  VARCHAR2(100 ) not null,
  primary key (ID_NOM_TIP_UNITATE_MASURA),
  unique UK_NOM_TIP_UNITATE_MASURA_COD (COD)
);

DROP TABLE IF EXISTS NOM_UNITATE_MASURA;

create table NOM_UNITATE_MASURA
(
  id_nom_unitate_masura     NUMBER not null,
  fk_nom_tip_unitate_masura NUMBER not null,
  cod                       VARCHAR2(10 ) not null,
  denumire                  VARCHAR2(100 ) not null,
  primary key (ID_NOM_UNITATE_MASURA),
  unique UK_NOM_UNITATE_MASURA_COD (COD)
);
create index IDX_NOM_UN_MAS_FK_NOM_TIP_UN_M on NOM_UNITATE_MASURA (FK_NOM_TIP_UNITATE_MASURA);

alter table NOM_UNITATE_MASURA add foreign key (FK_NOM_TIP_UNITATE_MASURA) references NOM_TIP_UNITATE_MASURA (ID_NOM_TIP_UNITATE_MASURA);

DROP TABLE IF EXISTS CAP_ANIMAL_PROD;

create table CAP_ANIMAL_PROD
(
  id_cap_animal_prod    NUMBER not null,
  fk_cap_animal_prod    NUMBER,
  fk_nom_capitol        NUMBER not null,
  fk_nom_unitate_masura NUMBER,
  fk_nom_specie_animal  NUMBER,
  cod                   VARCHAR2(10 ),
  denumire              VARCHAR2(100 ) not null,
  descriere             VARCHAR2(500 ),
  cod_rand              NUMBER(2),
  is_formula            NUMBER(1) not null,
  tip_operand_relatie   NUMBER(1),
  ordin_multiplicare    NUMBER(5),
  data_start            DATE not null,
  data_stop             DATE,
  base_id               NUMBER not null,
  last_modified_date    TIMESTAMP not null,
  primary key (ID_CAP_ANIMAL_PROD),
  check (IS_FORMULA IN (0,1)),
  check (TIP_OPERAND_RELATIE IS NULL OR (TIP_OPERAND_RELATIE IN (1,2)))
);
create index IDX_CAP_ANIMAL_PROD_BASE_ID on CAP_ANIMAL_PROD (BASE_ID);
create index IDX_CAP_ANIMAL_PROD_COD on CAP_ANIMAL_PROD (COD);
create index IDX_CAP_ANIMAL_PROD_COD_RAND on CAP_ANIMAL_PROD (COD_RAND);
create index IDX_CAP_ANIMAL_PROD_FK_NOM_CAP on CAP_ANIMAL_PROD (FK_NOM_CAPITOL);
create index IDX_CAP_ANIMAL_PROD_LMD on CAP_ANIMAL_PROD (LAST_MODIFIED_DATE);
create index IDX_CAP_ANIM_PROD_FK_NOM_SP_AN on CAP_ANIMAL_PROD (FK_NOM_SPECIE_ANIMAL);
create index IDX_CAP_ANIM_PR_DAT_START_STOP on CAP_ANIMAL_PROD (DATA_START, DATA_STOP);
create index IDX_CAP_ANIM_PR_FK_CAP_ANIM_PR on CAP_ANIMAL_PROD (FK_CAP_ANIMAL_PROD);
create index IDX_CAP_ANIM_PR_FK_NOM_UN_MAS on CAP_ANIMAL_PROD (FK_NOM_UNITATE_MASURA);

alter table CAP_ANIMAL_PROD add foreign key (FK_NOM_CAPITOL) references NOM_CAPITOL (ID_NOM_CAPITOL);
alter table CAP_ANIMAL_PROD add foreign key (FK_CAP_ANIMAL_PROD)  references CAP_ANIMAL_PROD (ID_CAP_ANIMAL_PROD);
alter table CAP_ANIMAL_PROD add foreign key (FK_NOM_SPECIE_ANIMAL)  references NOM_SPECIE_ANIMAL (ID_NOM_SPECIE_ANIMAL);
alter table CAP_ANIMAL_PROD add foreign key (FK_NOM_UNITATE_MASURA)  references NOM_UNITATE_MASURA (ID_NOM_UNITATE_MASURA);


DROP TABLE IF EXISTS ANIMAL_PROD;
create table ANIMAL_PROD
(
  id_animal_prod     NUMBER not null,
  fk_nom_uat         NUMBER not null,
  fk_cap_animal_prod NUMBER not null,
  fk_nom_judet       NUMBER,
  an                 NUMBER(4) not null,
  valoare            NUMBER(15,2) not null,
  last_modified_date TIMESTAMP not null,
  primary key (ID_ANIMAL_PROD)
);
create index IDX_ANIMAL_PROD_AN on ANIMAL_PROD (AN);
create index IDX_ANIMAL_PROD_FK_NOM_UAT on ANIMAL_PROD (FK_NOM_UAT);
create index IDX_ANIMAL_PROD_LMD on ANIMAL_PROD (LAST_MODIFIED_DATE);
create index IDX_ANIM_PROD_FK_CAP_ANIM_PROD on ANIMAL_PROD (FK_CAP_ANIMAL_PROD);

alter table ANIMAL_PROD add foreign key (FK_CAP_ANIMAL_PROD)  references CAP_ANIMAL_PROD (ID_CAP_ANIMAL_PROD);
alter table ANIMAL_PROD add foreign key (FK_NOM_UAT)  references NOM_UAT (ID_NOM_UAT);

DROP TABLE IF EXISTS CAP_APLICARE_INGRASAMANT;

create table CAP_APLICARE_INGRASAMANT
(
  id_cap_aplicare_ingrasamant NUMBER not null,
  fk_cap_aplicare_ingrasamant NUMBER,
  fk_nom_capitol              NUMBER not null,
  cod                         VARCHAR2(10 ),
  denumire                    VARCHAR2(100 ) not null,
  descriere                   VARCHAR2(500 ),
  cod_rand                    NUMBER(2) not null,
  is_formula                  NUMBER(1) not null,
  tip_formula_relatie         NUMBER(1) not null,
  data_start                  DATE not null,
  data_stop                   DATE,
  base_id                     NUMBER not null,
  last_modified_date          TIMESTAMP not null,
  primary key (ID_CAP_APLICARE_INGRASAMANT),
  check (IS_FORMULA IN (0,1)),
  check (TIP_FORMULA_RELATIE IN (-1,0,1))
);
create index IDX_CAP_APLIC_INGRASAMANT_COD on CAP_APLICARE_INGRASAMANT (COD);
create index IDX_CAP_APLIC_INGRASAMANT_LMD on CAP_APLICARE_INGRASAMANT (LAST_MODIFIED_DATE);
create index IDX_CAP_APLIC_INGRAS_BASE_ID on CAP_APLICARE_INGRASAMANT (BASE_ID);
create index IDX_CAP_APLIC_INGRAS_COD_RAND on CAP_APLICARE_INGRASAMANT (COD_RAND);
create index IDX_CAP_APLIC_INGR_FK_NOM_CAP on CAP_APLICARE_INGRASAMANT (FK_NOM_CAPITOL);
create index IDX_CAP_APL_ING_DAT_START_STOP on CAP_APLICARE_INGRASAMANT (DATA_START, DATA_STOP);
create index IDX_CAP_APL_ING_FK_CAP_APL_ING on CAP_APLICARE_INGRASAMANT (FK_CAP_APLICARE_INGRASAMANT);

alter table CAP_APLICARE_INGRASAMANT add foreign key (FK_NOM_CAPITOL) references NOM_CAPITOL (ID_NOM_CAPITOL);
alter table CAP_APLICARE_INGRASAMANT add foreign key (FK_CAP_APLICARE_INGRASAMANT) references CAP_APLICARE_INGRASAMANT (ID_CAP_APLICARE_INGRASAMANT);

DROP TABLE IF EXISTS APLICARE_INGRASAMANT;
create table APLICARE_INGRASAMANT
(
  id_aplicare_ingrasamant     NUMBER not null,
  fk_gospodarie               NUMBER not null,
  fk_cap_aplicare_ingrasamant NUMBER not null,
  fk_nom_judet                NUMBER,
  an                          NUMBER(4) not null,
  suprafata                   NUMBER(15) not null,
  cantitate                   NUMBER(10) not null,
  last_modified_date          TIMESTAMP not null,
  primary key (ID_APLICARE_INGRASAMANT)
);
create index IDX_APLICARE_INGRASAMANT_AN on APLICARE_INGRASAMANT (AN);
create index IDX_APLICARE_INGRASAMANT_LMD on APLICARE_INGRASAMANT (LAST_MODIFIED_DATE);
create index IDX_APLIC_INGRASAMENT_FK_GOSP on APLICARE_INGRASAMANT (FK_GOSPODARIE);
create index IDX_APL_INGRAS_FK_CAP_APL_INGR on APLICARE_INGRASAMANT (FK_CAP_APLICARE_INGRASAMANT);

alter table APLICARE_INGRASAMANT add foreign key (FK_GOSPODARIE)  references GOSPODARIE (ID_GOSPODARIE);
alter table APLICARE_INGRASAMANT add foreign key (FK_CAP_APLICARE_INGRASAMANT)  references CAP_APLICARE_INGRASAMANT (ID_CAP_APLICARE_INGRASAMANT);

DROP TABLE IF EXISTS ATESTAT;
create table ATESTAT
(
  id_atestat              NUMBER not null,
  fk_gospodarie           NUMBER not null,
  fk_act_aviz_consultativ NUMBER,
  fk_nom_judet            NUMBER,
  serie_numar_atestat     VARCHAR2(20 ) not null,
  data_eliberare_atestat  DATE not null,
  last_modified_date      TIMESTAMP not null,
  primary key (ID_ATESTAT)
);
create index IDX_ATESTAT_FK_ACT_AVIZ_CONS on ATESTAT (FK_ACT_AVIZ_CONSULTATIV);
create index IDX_ATESTAT_FK_GOSPODARIE on ATESTAT (FK_GOSPODARIE);
create index IDX_ATESTAT_LMD on ATESTAT (LAST_MODIFIED_DATE);

alter table ATESTAT add foreign key (FK_ACT_AVIZ_CONSULTATIV)  references ACT (ID_ACT);
alter table ATESTAT add foreign key (FK_GOSPODARIE)  references GOSPODARIE (ID_GOSPODARIE);

DROP TABLE IF EXISTS ATESTAT_PRODUS;
create table ATESTAT_PRODUS
(
  id_atestat_produs  NUMBER not null,
  fk_atestat         NUMBER not null,
  denumire_produs    VARCHAR2(100 ) not null,
  last_modified_date TIMESTAMP not null,
  primary key (ID_ATESTAT_PRODUS)
);

alter table ATESTAT_PRODUS add foreign key (FK_ATESTAT)  references ATESTAT (ID_ATESTAT);

DROP TABLE IF EXISTS ATESTAT_VIZA;
create table ATESTAT_VIZA
(
  id_atestat_viza    NUMBER not null,
  fk_atestat         NUMBER not null,
  numar_viza         NUMBER(2) not null,
  data_viza          DATE not null,
  last_modified_date TIMESTAMP not null,
  primary key (ID_ATESTAT_VIZA)
);
create index IDX_ATESTAT_VIZA_LMD on ATESTAT_VIZA (LAST_MODIFIED_DATE);

alter table ATESTAT_VIZA add foreign key (FK_ATESTAT)  references ATESTAT (ID_ATESTAT);

DROP TABLE IF EXISTS CAP_CATEGORIE_ANIMAL;
create table CAP_CATEGORIE_ANIMAL
(
  id_cap_categorie_animal NUMBER not null,
  fk_cap_categorie_animal NUMBER,
  fk_nom_capitol          NUMBER not null,
  fk_nom_specie_animal    NUMBER,
  cod                     VARCHAR2(10 ),
  denumire                VARCHAR2(250 ) not null,
  descriere               VARCHAR2(500 ),
  cod_rand                NUMBER(2) not null,
  is_formula              NUMBER(1) not null,
  tip_formula_relatie     NUMBER(1) not null,
  data_start              DATE not null,
  data_stop               DATE,
  base_id                 NUMBER not null,
  last_modified_date      TIMESTAMP not null,
  primary key (ID_CAP_CATEGORIE_ANIMAL),
  check (IS_FORMULA IN (0,1)),
  check (TIP_FORMULA_RELATIE IN (-1,0,1))
);
create index IDX_CAP_CATEGORIE_ANIMAL_LMD on CAP_CATEGORIE_ANIMAL (LAST_MODIFIED_DATE);
create index IDX_CAP_CATEG_ANIMAL_BASE_ID on CAP_CATEGORIE_ANIMAL (BASE_ID);
create index IDX_CAP_CATEG_ANIMAL_COD_RAND on CAP_CATEGORIE_ANIMAL (COD_RAND);
create index IDX_CAP_CAT_ANIMAL_FK_NOM_CAP on CAP_CATEGORIE_ANIMAL (FK_NOM_CAPITOL);
create index IDX_CAP_CAT_ANIM_DT_START_STOP on CAP_CATEGORIE_ANIMAL (DATA_START, DATA_STOP);
create index IDX_CAP_CAT_ANIM_FK_CAP_CAT_AN on CAP_CATEGORIE_ANIMAL (FK_CAP_CATEGORIE_ANIMAL);
create index IDX_CAP_CAT_AN_FK_NOM_SPEC_AN on CAP_CATEGORIE_ANIMAL (FK_NOM_SPECIE_ANIMAL);

alter table CAP_CATEGORIE_ANIMAL add foreign key (FK_NOM_CAPITOL)  references NOM_CAPITOL (ID_NOM_CAPITOL);
alter table CAP_CATEGORIE_ANIMAL add foreign key (FK_NOM_SPECIE_ANIMAL)  references NOM_SPECIE_ANIMAL (ID_NOM_SPECIE_ANIMAL);
alter table CAP_CATEGORIE_ANIMAL add foreign key (FK_CAP_CATEGORIE_ANIMAL)  references CAP_CATEGORIE_ANIMAL (ID_CAP_CATEGORIE_ANIMAL);

DROP TABLE IF EXISTS NOM_PLANTA_CULTURA;
create table NOM_PLANTA_CULTURA
(
  id_nom_planta_cultura NUMBER not null,
  fk_nom_planta_cultura NUMBER,
  cod                   VARCHAR2(10 ) not null,
  denumire              VARCHAR2(100 ) not null,
  descriere             VARCHAR2(500 ),
  last_modified_date    TIMESTAMP not null,
  primary key (ID_NOM_PLANTA_CULTURA),
  unique UK_NOM_PLANTA_CULTURA_COD (COD)
);
create index IDX_NOM_PL_CULT_FK_NOM_PL_CULT on NOM_PLANTA_CULTURA (FK_NOM_PLANTA_CULTURA);

alter table NOM_PLANTA_CULTURA add foreign key (FK_NOM_PLANTA_CULTURA)  references NOM_PLANTA_CULTURA (ID_NOM_PLANTA_CULTURA);

DROP TABLE IF EXISTS CAP_CULTURA;
create table CAP_CULTURA
(
  id_cap_cultura        NUMBER not null,
  fk_cap_cultura        NUMBER,
  fk_nom_capitol        NUMBER not null,
  fk_nom_planta_cultura NUMBER,
  cod                   VARCHAR2(10 ),
  denumire              VARCHAR2(250 ) not null,
  descriere             VARCHAR2(500 ),
  cod_rand              NUMBER(3) not null,
  is_formula            NUMBER(1) not null,
  tip_formula_relatie   NUMBER(1) not null,
  data_start            DATE not null,
  data_stop             DATE,
  base_id               NUMBER not null,
  last_modified_date    TIMESTAMP not null,
  primary key (ID_CAP_CULTURA),
  check (IS_FORMULA IN (0,1)),
  check (TIP_FORMULA_RELATIE IN (-1,0,1))
);
create index IDX_CAP_CULTURA_COD on CAP_CULTURA (COD);
create index IDX_CAP_CULTURA_COD_RAND on CAP_CULTURA (COD_RAND);
create index IDX_CAP_CULTURA_FK_CAP_CULTURA on CAP_CULTURA (FK_CAP_CULTURA);
create index IDX_CAP_CULTURA_FK_NOM_CAPITOL on CAP_CULTURA (FK_NOM_CAPITOL);
create index IDX_CAP_CULTURA_LMD on CAP_CULTURA (LAST_MODIFIED_DATE);
create index IDX_CAP_CULT_DATA_START_STOP on CAP_CULTURA (DATA_START, DATA_STOP);
create index IDX_CAP_CULT_FK_NOM_PLANT_CULT on CAP_CULTURA (FK_NOM_PLANTA_CULTURA);

alter table CAP_CULTURA add foreign key (FK_CAP_CULTURA)  references CAP_CULTURA (ID_CAP_CULTURA);
alter table CAP_CULTURA add foreign key (FK_NOM_CAPITOL)  references NOM_CAPITOL (ID_NOM_CAPITOL);
alter table CAP_CULTURA add foreign key (FK_NOM_PLANTA_CULTURA)  references NOM_PLANTA_CULTURA (ID_NOM_PLANTA_CULTURA);

DROP TABLE IF EXISTS CAP_CULTURA_PROD;
create table CAP_CULTURA_PROD
(
  id_cap_cultura_prod   NUMBER not null,
  fk_cap_cultura_prod   NUMBER,
  fk_nom_capitol        NUMBER not null,
  fk_nom_unitate_masura NUMBER not null,
  fk_nom_planta_cultura NUMBER,
  cod                   VARCHAR2(10 ),
  denumire              VARCHAR2(250 ) not null,
  descriere             VARCHAR2(500 ),
  cod_rand              NUMBER(3) not null,
  is_formula            NUMBER(1) not null,
  tip_formula_relatie   NUMBER(1) not null,
  is_suprafata          NUMBER(1) not null,
  is_prod_medie         NUMBER(1) not null,
  is_prod_total         NUMBER(1) not null,
  data_start            DATE not null,
  data_stop             DATE,
  base_id               NUMBER not null,
  last_modified_date    TIMESTAMP not null,
  primary key (ID_CAP_CULTURA_PROD),
  check (IS_FORMULA IN (0,1)),
  check (IS_SUPRAFATA IN (0,1)),
  check (IS_PROD_MEDIE IN (0,1)),
  check (IS_PROD_TOTAL IN (0,1)),
  check (TIP_FORMULA_RELATIE IN (-1,0,1))
);
create index IDX_CAP_CULTURA_PROD_COD on CAP_CULTURA_PROD (COD);
create index IDX_CAP_CULTURA_PROD_COD_RAND on CAP_CULTURA_PROD (COD_RAND);
create index IDX_CAP_CULTURA_PROD_LMD on CAP_CULTURA_PROD (LAST_MODIFIED_DATE);
create index IDX_CAP_CULTURA_PR_FK_NOM_CAP on CAP_CULTURA_PROD (FK_NOM_CAPITOL);
create index IDX_CAP_CULTURA_PR_FK_NOM_UN_M on CAP_CULTURA_PROD (FK_NOM_UNITATE_MASURA);
create index IDX_CAP_CULT_PR_DAT_START_STOP on CAP_CULTURA_PROD (DATA_START, DATA_STOP);
create index IDX_CAP_CULT_PR_FK_CAP_CULT_PR on CAP_CULTURA_PROD (FK_CAP_CULTURA_PROD);
create index IDX_CAP_CULT_PR_FK_NOM_PL_CULT on CAP_CULTURA_PROD (FK_NOM_PLANTA_CULTURA);

alter table CAP_CULTURA_PROD add foreign key (FK_NOM_CAPITOL)  references NOM_CAPITOL (ID_NOM_CAPITOL);
alter table CAP_CULTURA_PROD add foreign key (FK_CAP_CULTURA_PROD)  references CAP_CULTURA_PROD (ID_CAP_CULTURA_PROD);
alter table CAP_CULTURA_PROD add foreign key (FK_NOM_PLANTA_CULTURA)  references NOM_PLANTA_CULTURA (ID_NOM_PLANTA_CULTURA);
alter table CAP_CULTURA_PROD add foreign key (FK_NOM_UNITATE_MASURA)  references NOM_UNITATE_MASURA (ID_NOM_UNITATE_MASURA);

DROP TABLE IF EXISTS NOM_POM_ARBUST;
create table NOM_POM_ARBUST
(
  id_nom_pom_arbust  NUMBER not null,
  fk_nom_pom_arbust  NUMBER,
  cod                VARCHAR2(10 ) not null,
  denumire           VARCHAR2(100 ) not null,
  descriere          VARCHAR2(500 ),
  last_modified_date TIMESTAMP not null,
  primary key (ID_NOM_POM_ARBUST),
  unique UK_NOM_POM_ARBUST_COD (COD)
);

create index IDX_NOM_POM_ARB_FK_NOM_POM_ARB on NOM_POM_ARBUST (FK_NOM_POM_ARBUST);

alter table NOM_POM_ARBUST add foreign key (FK_NOM_POM_ARBUST)  references NOM_POM_ARBUST (ID_NOM_POM_ARBUST);

DROP TABLE IF EXISTS CAP_FRUCT_PROD;
create table CAP_FRUCT_PROD
(
  id_cap_fruct_prod        NUMBER not null,
  fk_cap_fruct_prod        NUMBER,
  fk_nom_capitol           NUMBER not null,
  fk_nom_pom_arbust        NUMBER,
  cod                      VARCHAR2(10 ),
  denumire                 VARCHAR2(100 ) not null,
  descriere                VARCHAR2(500 ),
  cod_rand                 NUMBER(2) not null,
  is_formula               NUMBER(1) not null,
  tip_formula_relatie      NUMBER(1) not null,
  is_nr_pom_razlet         NUMBER(1) not null,
  is_prod_medie_pom_razlet NUMBER(1) not null,
  is_prod_total_pom_razlet NUMBER(1) not null,
  is_suprafata_livada      NUMBER(1) not null,
  is_prod_medie_livada     NUMBER(1) not null,
  is_prod_total_livada     NUMBER(1) not null,
  data_start               DATE not null,
  data_stop                DATE,
  base_id                  NUMBER not null,
  last_modified_date       TIMESTAMP not null,
  primary key (ID_CAP_FRUCT_PROD),
  check (TIP_FORMULA_RELATIE IN (-1,0,1)),
  check (IS_FORMULA IN (0,1)),
  check (IS_SUPRAFATA_LIVADA IN (0,1)),
  check (IS_NR_POM_RAZLET IN (0,1)),
  check (IS_PROD_MEDIE_LIVADA IN (0,1)),
  check (IS_PROD_TOTAL_LIVADA IN (0,1)) ,
  check (IS_PROD_MEDIE_POM_RAZLET IN (0,1)),
  check (IS_PROD_TOTAL_POM_RAZLET IN (0,1))
);
create index IDX_CAP_FRUCT_PROD_COD on CAP_FRUCT_PROD (COD);
create index IDX_CAP_FRUCT_PROD_COD_RAND on CAP_FRUCT_PROD (COD_RAND);
create index IDX_CAP_FRUCT_PROD_FK_NOM_CAP on CAP_FRUCT_PROD (FK_NOM_CAPITOL);
create index IDX_CAP_FRUCT_PROD_LMD on CAP_FRUCT_PROD (LAST_MODIFIED_DATE);
create index IDX_CAP_FRUCT_PR_FK_CAP_FR_PR on CAP_FRUCT_PROD (FK_CAP_FRUCT_PROD);
create index IDX_CAP_FR_PR_DATA_START_STOP on CAP_FRUCT_PROD (DATA_START, DATA_STOP);
create index IDX_CAP_FR_PR_FK_NOM_POM_ARB on CAP_FRUCT_PROD (FK_NOM_POM_ARBUST);

alter table CAP_FRUCT_PROD add foreign key (FK_CAP_FRUCT_PROD)  references CAP_FRUCT_PROD (ID_CAP_FRUCT_PROD);
alter table CAP_FRUCT_PROD add foreign key (FK_NOM_CAPITOL)  references NOM_CAPITOL (ID_NOM_CAPITOL);
alter table CAP_FRUCT_PROD add foreign key (FK_NOM_POM_ARBUST)  references NOM_POM_ARBUST (ID_NOM_POM_ARBUST);


DROP TABLE IF EXISTS CAP_MOD_UTILIZARE;
create table CAP_MOD_UTILIZARE
(
  id_cap_mod_utilizare NUMBER not null,
  fk_cap_mod_utilizare NUMBER,
  fk_nom_capitol       NUMBER not null,
  cod                  VARCHAR2(10 ),
  denumire             VARCHAR2(150 ) not null,
  descriere            VARCHAR2(500 ),
  cod_rand             NUMBER(2) not null,
  is_formula           NUMBER(1) not null,
  tip_formula_relatie  NUMBER(1) not null,
  data_start           DATE not null,
  data_stop            DATE,
  base_id              NUMBER not null,
  last_modified_date   TIMESTAMP not null,
  primary key (ID_CAP_MOD_UTILIZARE),
  check (IS_FORMULA IN (0,1)),
  check (TIP_FORMULA_RELATIE IN (-1,0,1))
);
create index IDX_CAP_MOD_UTILIZARE_COD on CAP_MOD_UTILIZARE (COD);
create index IDX_CAP_MOD_UTILIZARE_COD_RAND on CAP_MOD_UTILIZARE (COD_RAND);
create index IDX_CAP_MOD_UTILIZARE_LMD on CAP_MOD_UTILIZARE (LAST_MODIFIED_DATE);
create index IDX_CAP_MOD_UTIL_DATA_STRT_STP on CAP_MOD_UTILIZARE (DATA_START, DATA_STOP);
create index IDX_CAP_MOD_UTIL_FK_CAP_MOD_UT on CAP_MOD_UTILIZARE (FK_CAP_MOD_UTILIZARE);
create index IDX_CAP_MOD_UTIL_FK_NOM_CAP on CAP_MOD_UTILIZARE (FK_NOM_CAPITOL);

alter table CAP_MOD_UTILIZARE add foreign key (FK_NOM_CAPITOL)  references NOM_CAPITOL (ID_NOM_CAPITOL);
alter table CAP_MOD_UTILIZARE add foreign key (FK_CAP_MOD_UTILIZARE)  references CAP_MOD_UTILIZARE (ID_CAP_MOD_UTILIZARE);

DROP TABLE IF EXISTS CAP_PLANTATIE;
create table CAP_PLANTATIE
(
  id_cap_plantatie    NUMBER not null,
  fk_cap_plantatie    NUMBER,
  fk_nom_capitol      NUMBER not null,
  fk_nom_pom_arbust   NUMBER,
  cod                 VARCHAR2(10 ),
  denumire            VARCHAR2(150 ) not null,
  descriere           VARCHAR2(500 ),
  cod_rand            NUMBER(2) not null,
  is_formula          NUMBER(1) not null,
  tip_formula_relatie NUMBER(1) not null,
  data_start          DATE not null,
  data_stop           DATE,
  base_id             NUMBER not null,
  last_modified_date  TIMESTAMP not null,
  primary key (ID_CAP_PLANTATIE),
  check (IS_FORMULA IN (0,1)),
  check (TIP_FORMULA_RELATIE IN (-1,0,1))
);

create index IDX_CAP_PLANTATIE_COD on CAP_PLANTATIE (COD);
create index IDX_CAP_PLANTATIE_COD_RAND on CAP_PLANTATIE (COD_RAND);
create index IDX_CAP_PLANTATIE_FK_CAP_PLANT on CAP_PLANTATIE (FK_CAP_PLANTATIE);
create index IDX_CAP_PLANTATIE_FK_NOM_CAP on CAP_PLANTATIE (FK_NOM_CAPITOL);
create index IDX_CAP_PLANTATIE_LMD on CAP_PLANTATIE (LAST_MODIFIED_DATE);
create index IDX_CAP_PLANT_DATA_START_STOP on CAP_PLANTATIE (DATA_START, DATA_STOP);
create index IDX_CAP_PLANT_FK_NOM_POM_ARB on CAP_PLANTATIE (FK_NOM_POM_ARBUST);

alter table CAP_PLANTATIE add foreign key (FK_CAP_PLANTATIE)  references CAP_PLANTATIE (ID_CAP_PLANTATIE);
alter table CAP_PLANTATIE add foreign key (FK_NOM_CAPITOL)  references NOM_CAPITOL (ID_NOM_CAPITOL);
alter table CAP_PLANTATIE add foreign key (FK_NOM_POM_ARBUST)  references NOM_POM_ARBUST (ID_NOM_POM_ARBUST);

DROP TABLE IF EXISTS CAP_PLANTATIE_PROD;
create table CAP_PLANTATIE_PROD
(
  id_cap_plantatie_prod NUMBER not null,
  fk_cap_plantatie_prod NUMBER,
  fk_nom_capitol        NUMBER not null,
  fk_nom_pom_arbust     NUMBER,
  cod                   VARCHAR2(10 ),
  denumire              VARCHAR2(150 ) not null,
  descriere             VARCHAR2(500 ),
  cod_rand              NUMBER(2) not null,
  is_formula            NUMBER(1) not null,
  tip_formula_relatie   NUMBER(1) not null,
  is_suprafata          NUMBER(1) not null,
  is_prod_medie         NUMBER(1) not null,
  is_prod_total         NUMBER(1) not null,
  data_start            DATE not null,
  data_stop             DATE,
  base_id               NUMBER not null,
  last_modified_date    TIMESTAMP not null,
  primary key (ID_CAP_PLANTATIE_PROD),
  check (IS_FORMULA IN (0,1)),
  check (IS_PROD_MEDIE IN (0,1)),
  check (IS_PROD_TOTAL IN (0,1)),
  check (IS_SUPRAFATA IN (0,1)),
  check (TIP_FORMULA_RELATIE IN (-1,0,1))
);

create index IDX_CAP_PLANTATIE_PROD_LMD on CAP_PLANTATIE_PROD (LAST_MODIFIED_DATE);
create index IDX_CAP_PLANT_PROD_BASE_ID on CAP_PLANTATIE_PROD (BASE_ID);
create index IDX_CAP_PLANT_PROD_COD_RAND on CAP_PLANTATIE_PROD (COD_RAND);
create index IDX_CAP_PLANT_PR_FK_CAP_PL_PR on CAP_PLANTATIE_PROD (FK_CAP_PLANTATIE_PROD);
create index IDX_CAP_PLANT_PR_FK_NOM_CAP on CAP_PLANTATIE_PROD (FK_NOM_CAPITOL);
create index IDX_CAP_PL_PR_DATA_START_STOP on CAP_PLANTATIE_PROD (DATA_START, DATA_STOP);
create index IDX_CAP_PL_PR_FK_NOM_POM_ARB on CAP_PLANTATIE_PROD (FK_NOM_POM_ARBUST);

alter table CAP_PLANTATIE_PROD add foreign key (FK_CAP_PLANTATIE_PROD)  references CAP_PLANTATIE_PROD (ID_CAP_PLANTATIE_PROD);
alter table CAP_PLANTATIE_PROD add foreign key (FK_NOM_CAPITOL)  references NOM_CAPITOL (ID_NOM_CAPITOL);
alter table CAP_PLANTATIE_PROD add foreign key (FK_NOM_POM_ARBUST)  references NOM_POM_ARBUST (ID_NOM_POM_ARBUST);


DROP TABLE IF EXISTS CAP_POM_RAZLET;
create table CAP_POM_RAZLET
(
  id_cap_pom_razlet   NUMBER not null,
  fk_cap_pom_razlet   NUMBER,
  fk_nom_capitol      NUMBER not null,
  fk_nom_pom_arbust   NUMBER,
  cod                 VARCHAR2(10 ),
  denumire            VARCHAR2(100 ) not null,
  descriere           VARCHAR2(500 ),
  cod_rand            NUMBER(2) not null,
  is_formula          NUMBER(1) not null,
  tip_formula_relatie NUMBER(1) not null,
  data_start          DATE not null,
  data_stop           DATE,
  base_id             NUMBER not null,
  last_modified_date  TIMESTAMP not null,
  check (IS_FORMULA IN (0,1)),
  check (TIP_FORMULA_RELATIE IN (-1,0,1))
);

create index IDX_CAP_POM_RAZLET_COD on CAP_POM_RAZLET (COD);
create index IDX_CAP_POM_RAZLET_COD_RAND on CAP_POM_RAZLET (COD_RAND);
create index IDX_CAP_POM_RAZLET_FK_NOM_CAP on CAP_POM_RAZLET (FK_NOM_CAPITOL);
create index IDX_CAP_POM_RAZLET_LMD on CAP_POM_RAZLET (LAST_MODIFIED_DATE);
create index IDX_CAP_POM_RAZ_DAT_START_STOP on CAP_POM_RAZLET (DATA_START, DATA_STOP);
create index IDX_CAP_POM_RAZ_FK_CAP_POM_RAZ on CAP_POM_RAZLET (FK_CAP_POM_RAZLET);
create index IDX_CAP_POM_RAZ_FK_NOM_POM_ARB on CAP_POM_RAZLET (FK_NOM_POM_ARBUST);

alter table CAP_POM_RAZLET add foreign key (FK_NOM_CAPITOL)  references NOM_CAPITOL (ID_NOM_CAPITOL);
alter table CAP_POM_RAZLET add foreign key (FK_CAP_POM_RAZLET)  references CAP_POM_RAZLET (ID_CAP_POM_RAZLET);
alter table CAP_POM_RAZLET add foreign key (FK_NOM_POM_ARBUST)  references NOM_POM_ARBUST (ID_NOM_POM_ARBUST);

DROP TABLE IF EXISTS CAP_SISTEM_TEHNIC;
create table CAP_SISTEM_TEHNIC
(
  id_cap_sistem_tehnic NUMBER not null,
  fk_nom_capitol       NUMBER not null,
  cod                  VARCHAR2(10 ),
  denumire             VARCHAR2(100 ) not null,
  descriere            VARCHAR2(500 ),
  cod_rand             NUMBER(2) not null,
  data_start           DATE not null,
  data_stop            DATE,
  base_id              NUMBER not null,
  last_modified_date   TIMESTAMP not null,
  primary key (ID_CAP_SISTEM_TEHNIC)
);

create index IDX_CAP_SISTEM_TEHNIC_COD on CAP_SISTEM_TEHNIC (COD);
create index IDX_CAP_SISTEM_TEHNIC_COD_RAND on CAP_SISTEM_TEHNIC (COD_RAND);
create index IDX_CAP_SISTEM_TEHNIC_LMD on CAP_SISTEM_TEHNIC (LAST_MODIFIED_DATE);
create index IDX_CAP_SISTEM_TEH_FK_NOM_CAP on CAP_SISTEM_TEHNIC (FK_NOM_CAPITOL);
create index IDX_CAP_SIS_TEH_DAT_START_STOP on CAP_SISTEM_TEHNIC (DATA_START, DATA_STOP);

alter table CAP_SISTEM_TEHNIC add foreign key (FK_NOM_CAPITOL)  references NOM_CAPITOL (ID_NOM_CAPITOL);

DROP TABLE IF EXISTS CAP_SUBSTANTA_CHIMICA;
create table CAP_SUBSTANTA_CHIMICA
(
  id_cap_substanta_chimica NUMBER not null,
  fk_cap_substanta_chimica NUMBER,
  fk_nom_capitol           NUMBER not null,
  cod                      VARCHAR2(10 ),
  denumire                 VARCHAR2(100 ) not null,
  descriere                VARCHAR2(500 ),
  cod_rand                 NUMBER(2) not null,
  is_formula               NUMBER(1) not null,
  tip_formula_relatie      NUMBER(1) not null,
  data_start               DATE not null,
  data_stop                DATE,
  base_id                  NUMBER not null,
  last_modified_date       TIMESTAMP not null,
  primary key (ID_CAP_SUBSTANTA_CHIMICA),
  check (IS_FORMULA IN (0,1)),
  check (TIP_FORMULA_RELATIE IN (-1,0,1))
);

create index IDX_CAP_SUBSTANTA_CHIMICA_LMD on CAP_SUBSTANTA_CHIMICA (LAST_MODIFIED_DATE);
create index IDX_CAP_SUBST_CHIMICA_BASE_ID on CAP_SUBSTANTA_CHIMICA (BASE_ID);
create index IDX_CAP_SUBST_CHIMICA_COD_RAND on CAP_SUBSTANTA_CHIMICA (COD_RAND);
create index IDX_CAP_SUBST_CHIM_FK_NOM_CAP on CAP_SUBSTANTA_CHIMICA (FK_NOM_CAPITOL);
create index IDX_CAP_SUB_CHI_DAT_START_STOP on CAP_SUBSTANTA_CHIMICA (DATA_START, DATA_STOP);
create index IDX_CAP_SUB_CHI_FK_CAP_SUB_CHI on CAP_SUBSTANTA_CHIMICA (FK_CAP_SUBSTANTA_CHIMICA);

alter table CAP_SUBSTANTA_CHIMICA add foreign key (FK_NOM_CAPITOL)  references NOM_CAPITOL (ID_NOM_CAPITOL);
alter table CAP_SUBSTANTA_CHIMICA add foreign key (FK_CAP_SUBSTANTA_CHIMICA)  references CAP_SUBSTANTA_CHIMICA (ID_CAP_SUBSTANTA_CHIMICA);

DROP TABLE IF EXISTS CAP_TEREN_IRIGAT;
create table CAP_TEREN_IRIGAT
(
  id_cap_teren_irigat NUMBER not null,
  fk_cap_teren_irigat NUMBER,
  fk_nom_capitol      NUMBER not null,
  cod                 VARCHAR2(10 ),
  denumire            VARCHAR2(100 ) not null,
  descriere           VARCHAR2(500 ),
  cod_rand            NUMBER(2) not null,
  is_formula          NUMBER(1) not null,
  tip_formula_relatie NUMBER(1) not null,
  data_start          DATE not null,
  data_stop           DATE,
  base_id             NUMBER not null,
  last_modified_date  TIMESTAMP not null,
  primary key (ID_CAP_TEREN_IRIGAT),
  check (IS_FORMULA IN (0,1)),
  check (TIP_FORMULA_RELATIE IN (-1,0,1))
);

create index IDX_CAP_TEREN_IRIGAT_COD on CAP_TEREN_IRIGAT (COD);
create index IDX_CAP_TEREN_IRIGAT_COD_RAND on CAP_TEREN_IRIGAT (COD_RAND);
create index IDX_CAP_TEREN_IRIGAT_LMD on CAP_TEREN_IRIGAT (LAST_MODIFIED_DATE);
create index IDX_CAP_TEREN_IRIG_FK_NOM_CAP on CAP_TEREN_IRIGAT (FK_NOM_CAPITOL);
create index IDX_CAP_TER_IRG_FK_CAP_TER_IRG on CAP_TEREN_IRIGAT (FK_CAP_TEREN_IRIGAT);
create index IDX_CAP_TER_IRIG_DATA_STRT_STP on CAP_TEREN_IRIGAT (DATA_START, DATA_STOP);

alter table CAP_TEREN_IRIGAT add foreign key (FK_NOM_CAPITOL)  references NOM_CAPITOL (ID_NOM_CAPITOL);
alter table CAP_TEREN_IRIGAT add foreign key (FK_CAP_TEREN_IRIGAT)  references CAP_TEREN_IRIGAT (ID_CAP_TEREN_IRIGAT);

DROP TABLE IF EXISTS CATEGORIE_ANIMAL;
create table CATEGORIE_ANIMAL
(
  id_categorie_animal     NUMBER not null,
  fk_gospodarie           NUMBER not null,
  fk_cap_categorie_animal NUMBER not null,
  fk_nom_judet            NUMBER,
  an                      NUMBER(4) not null,
  semestru                NUMBER(1) not null,
  nr_cap                  NUMBER(10) not null,
  last_modified_date      TIMESTAMP not null,
  primary key (ID_CATEGORIE_ANIMAL)
);
create index IDX_CATEGORIE_ANIMAL_LMD on CATEGORIE_ANIMAL (LAST_MODIFIED_DATE);
create index IDX_CATEG_ANIMAL_FK_GOSPODARIE on CATEGORIE_ANIMAL (FK_GOSPODARIE);
create index IDX_CAT_ANIMAL_FK_CAP_CAT_ANIM on CATEGORIE_ANIMAL (FK_CAP_CATEGORIE_ANIMAL);

alter table CATEGORIE_ANIMAL add foreign key (FK_GOSPODARIE)  references GOSPODARIE (ID_GOSPODARIE);
alter table CATEGORIE_ANIMAL add foreign key (FK_CAP_CATEGORIE_ANIMAL)  references CAP_CATEGORIE_ANIMAL (ID_CAP_CATEGORIE_ANIMAL);

DROP TABLE IF EXISTS CERERE;
create table CERERE
(
  id_cerere     NUMBER not null,
  fk_gospodarie NUMBER not null,
  fk_nom_judet  NUMBER,
  data_cerere   DATE not null,
  titlu         VARCHAR2(100 ) not null,
  uid_cmis      VARCHAR2(20 ),
  primary key (ID_CERERE)
);
create index IDX_CERERE_FK_GOSPODARIE on CERERE (FK_GOSPODARIE);
create index IDX_CERERE_UID_CMIS on CERERE (UID_CMIS);

alter table CERERE add foreign key (FK_GOSPODARIE)  references GOSPODARIE (ID_GOSPODARIE);

DROP TABLE IF EXISTS CERERE_DETALIU;
create table CERERE_DETALIU
(
  id_cerere_detaliu NUMBER not null,
  fk_cerere         NUMBER not null,
  fk_nom_capitol    NUMBER,
  fk_nom_judet      NUMBER,
  rand_capitol      VARCHAR2(250 ),
  coloana_capitol   VARCHAR2(100 ),
  continut          CLOB,
  primary key (ID_CERERE_DETALIU)
);
create index IDX_CERERE_FK_CERERE on CERERE_DETALIU (FK_CERERE);
create index IDX_CERERE_FK_NOM_CAPITOL on CERERE_DETALIU (FK_NOM_CAPITOL);

alter table CERERE_DETALIU add foreign key (FK_CERERE)  references CERERE (ID_CERERE);
alter table CERERE_DETALIU add foreign key (FK_NOM_CAPITOL)  references NOM_CAPITOL (ID_NOM_CAPITOL);

DROP TABLE IF EXISTS CERTIFICAT_COM;
create table CERTIFICAT_COM
(
  id_certificat_com  NUMBER not null,
  fk_atestat         NUMBER not null,
  serie              VARCHAR2(10 ) not null,
  data_eliberare     DATE not null,
  last_modified_date TIMESTAMP not null,
  primary key (ID_CERTIFICAT_COM)
);
create index IDX_CERTIFICAT_COM_FK_ATESTAT on CERTIFICAT_COM (FK_ATESTAT);
create index IDX_CERTIFICAT_COM_LMD on CERTIFICAT_COM (LAST_MODIFIED_DATE);

alter table CERTIFICAT_COM add foreign key (FK_ATESTAT)  references ATESTAT (ID_ATESTAT);

DROP TABLE IF EXISTS NOM_DESTINATIE_CLADIRE;
create table NOM_DESTINATIE_CLADIRE
(
  id_nom_destinatie_cladire NUMBER not null,
  cod                       VARCHAR2(10 ) not null,
  denumire                  VARCHAR2(100 ) not null,
  descriere                 VARCHAR2(500 ),
  data_start                DATE not null,
  data_stop                 DATE,
  base_id                   NUMBER not null,
  last_modified_date        TIMESTAMP not null,
  primary key (ID_NOM_DESTINATIE_CLADIRE)
);

create index IDX_NOM_DESTINATIE_CLADIRE_COD on NOM_DESTINATIE_CLADIRE (COD);
create index IDX_NOM_DESTINATIE_CLADIRE_LMD on NOM_DESTINATIE_CLADIRE (LAST_MODIFIED_DATE);
create index IDX_NOM_DEST_CLADIRE_BASE_ID on NOM_DESTINATIE_CLADIRE (BASE_ID);
create index IDX_NOM_DST_CLD_DAT_START_STOP on NOM_DESTINATIE_CLADIRE (DATA_START, DATA_STOP);


DROP TABLE IF EXISTS NOM_TIP_CLADIRE;

create table NOM_TIP_CLADIRE
(
  id_nom_tip_cladire NUMBER not null,
  cod                VARCHAR2(10 ) not null,
  denumire           VARCHAR2(300 ) not null,
  descriere          VARCHAR2(500 ),
  data_start         DATE not null,
  data_stop          DATE,
  base_id            NUMBER not null,
  last_modified_date TIMESTAMP not null,
  primary key (ID_NOM_TIP_CLADIRE)
);

create index IDX_NOM_TIP_CLADIRE_BASE_ID on NOM_TIP_CLADIRE (BASE_ID);
create index IDX_NOM_TIP_CLADIRE_COD on NOM_TIP_CLADIRE (COD);
create index IDX_NOM_TIP_CLADIRE_LMD on NOM_TIP_CLADIRE (LAST_MODIFIED_DATE);
create index IDX_NOM_TIP_CLD_DAT_START_STOP on NOM_TIP_CLADIRE (DATA_START, DATA_STOP);


DROP TABLE IF EXISTS CLADIRE;
create table CLADIRE
(
  id_cladire                NUMBER not null,
  fk_gospodarie             NUMBER not null,
  fk_nom_tip_cladire        NUMBER not null,
  fk_nom_destinatie_cladire NUMBER not null,
  fk_adresa                 NUMBER not null,
  fk_nom_judet              NUMBER,
  identificator             VARCHAR2(20 ) not null,
  an                        NUMBER(4) not null,
  zona                      VARCHAR2(50 ) not null,
  suprafata_desfasurata     NUMBER(5) not null,
  suprafata_sol             NUMBER(5),
  an_terminare              NUMBER(4) not null,
  identificator_cadastral   VARCHAR2(20 ),
  last_modified_date        TIMESTAMP not null,
  primary key (ID_CLADIRE)
);

create index IDX_CLADIRE_FK_ADRESA on CLADIRE (FK_ADRESA);
create index IDX_CLADIRE_FK_GOSPODARIE on CLADIRE (FK_GOSPODARIE);
create index IDX_CLADIRE_FK_NOM_DEST_CLAD on CLADIRE (FK_NOM_DESTINATIE_CLADIRE);
create index IDX_CLADIRE_FK_NOM_TIP_CLADIRE on CLADIRE (FK_NOM_TIP_CLADIRE);
create index IDX_CLADIRE_IDENTIFICATOR on CLADIRE (IDENTIFICATOR);
create index IDX_CLADIRE_IDENT_CADASTRAL on CLADIRE (IDENTIFICATOR_CADASTRAL);
create index IDX_CLADIRE_LMD on CLADIRE (LAST_MODIFIED_DATE);

alter table CLADIRE add foreign key (FK_ADRESA)  references ADRESA (ID_ADRESA);
alter table CLADIRE add foreign key (FK_GOSPODARIE)  references GOSPODARIE (ID_GOSPODARIE);
alter table CLADIRE add foreign key (FK_NOM_DESTINATIE_CLADIRE)  references NOM_DESTINATIE_CLADIRE (ID_NOM_DESTINATIE_CLADIRE);
alter table CLADIRE add foreign key (FK_NOM_TIP_CLADIRE)  references NOM_TIP_CLADIRE (ID_NOM_TIP_CLADIRE);

DROP TABLE IF EXISTS NOM_TIP_CONTRACT;

create table NOM_TIP_CONTRACT
(
  id_nom_tip_contract NUMBER not null,
  cod                 VARCHAR2(10 ) not null,
  denumire            VARCHAR2(100 ) not null,
  primary key (ID_NOM_TIP_CONTRACT),
  unique UK_TIP_CONTRACT_COD (COD)
);

DROP TABLE IF EXISTS PERSOANA_FIZICA;

create table PERSOANA_FIZICA
(
  id_persoana_fizica NUMBER not null,
  cnp                VARCHAR2(13 ),
  nume               VARCHAR2(60 ) not null,
  initiala_tata      VARCHAR2(10 ) not null,
  prenume            VARCHAR2(60 ) not null,
  nif                VARCHAR2(13 ),
  data_start         DATE not null,
  data_stop          DATE,
  base_id            NUMBER not null,
  last_modified_date TIMESTAMP not null,
  primary key (ID_PERSOANA_FIZICA),
  check (CNP IS NOT NULL OR NIF IS NOT NULL)
);

create index IDX_PERSOANA_FIZICA_BASE_ID on PERSOANA_FIZICA (BASE_ID);
create index IDX_PERSOANA_FIZICA_CNP on PERSOANA_FIZICA (CNP);
create index IDX_PERSOANA_FIZICA_LMD on PERSOANA_FIZICA (LAST_MODIFIED_DATE);
create index IDX_PERSOANA_FIZICA_NIF on PERSOANA_FIZICA (NIF);
create index IDX_PERS_FIZICA_DAT_START_STOP on PERSOANA_FIZICA (DATA_START, DATA_STOP);

DROP TABLE IF EXISTS NOM_FORMA_ORGANIZARE_RC;

create table NOM_FORMA_ORGANIZARE_RC
(
  id_nom_forma_organizare_rc NUMBER not null,
  cod                        VARCHAR2(10 ) not null,
  denumire                   VARCHAR2(100 ) not null,
  is_personalitate_juridica  NUMBER(1) not null,
  primary key (ID_NOM_FORMA_ORGANIZARE_RC),
  unique UK_NOM_FORMA_ORGANIZARE_RC_COD (COD),
  check (IS_PERSONALITATE_JURIDICA IN (0,1))
);

create index IDX_NOM_FRM_ORG_RC_IS_PERS_JUR on NOM_FORMA_ORGANIZARE_RC (IS_PERSONALITATE_JURIDICA);

DROP TABLE IF EXISTS PERSOANA_RC;
create table PERSOANA_RC
(
  id_persoana_rc             NUMBER not null,
  fk_nom_forma_organizare_rc NUMBER not null,
  denumire                   VARCHAR2(150 ) not null,
  cui                        VARCHAR2(20 ) not null,
  cif                        VARCHAR2(20 ),
  data_start                 DATE not null,
  data_stop                  DATE,
  base_id                    NUMBER not null,
  last_modified_date         TIMESTAMP not null,
  primary key (ID_PERSOANA_RC)
);

create index IDX_PERSOANA_RC_BASE_ID on PERSOANA_RC (BASE_ID);
create index IDX_PERSOANA_RC_CIF on PERSOANA_RC (CIF);
create index IDX_PERSOANA_RC_CUI on PERSOANA_RC (CUI);
create index IDX_PERSOANA_RC_LMD on PERSOANA_RC (LAST_MODIFIED_DATE);
create index IDX_PERS_RC_DATA_START_STOP on PERSOANA_RC (DATA_START, DATA_STOP);
create index IDX_PERS_RC_FK_NOM_FORM_ORG_RC on PERSOANA_RC (FK_NOM_FORMA_ORGANIZARE_RC);

alter table PERSOANA_RC add foreign key (FK_NOM_FORMA_ORGANIZARE_RC)  references NOM_FORMA_ORGANIZARE_RC (ID_NOM_FORMA_ORGANIZARE_RC);

DROP TABLE IF EXISTS CONTRACT;
create table CONTRACT
(
  id_contract                NUMBER not null,
  fk_gospodarie              NUMBER not null,
  fk_nom_tip_contract        NUMBER not null,
  fk_nom_categorie_folosinta NUMBER,
  fk_persoana_fizica         NUMBER,
  fk_persoana_rc             NUMBER,
  fk_nom_judet               NUMBER,
  nr_crt                     NUMBER(5) not null,
  nr_contract                VARCHAR2(20 ) not null,
  data_contract              DATE not null,
  suprafata                  NUMBER(15) not null,
  data_start                 DATE not null,
  data_stop                  DATE not null,
  redeventa                  NUMBER(15,2) not null,
  redeventa_completa         VARCHAR2(100 ),
  last_modified_date         TIMESTAMP not null,
  primary key (ID_CONTRACT)
);

create index IDX_CONTRACT_DATA_START_STOP on CONTRACT (DATA_START, DATA_STOP);
create index IDX_CONTRACT_FK_GOSPODARIE on CONTRACT (FK_GOSPODARIE);
create index IDX_CONTRACT_FK_NOM_CAT_FOL on CONTRACT (FK_NOM_CATEGORIE_FOLOSINTA);
create index IDX_CONTRACT_FK_NOM_TIP_CONTR on CONTRACT (FK_NOM_TIP_CONTRACT);
create index IDX_CONTRACT_FK_PERSOANA_RC on CONTRACT (FK_PERSOANA_RC);
create index IDX_CONTRACT_FK_PERS_FIZICA on CONTRACT (FK_PERSOANA_FIZICA);
create index IDX_CONTRACT_LMD on CONTRACT (LAST_MODIFIED_DATE);

alter table CONTRACT add foreign key (FK_GOSPODARIE)  references GOSPODARIE (ID_GOSPODARIE);
alter table CONTRACT add foreign key (FK_NOM_CATEGORIE_FOLOSINTA)  references NOM_CATEGORIE_FOLOSINTA (ID_NOM_CATEGORIE_FOLOSINTA);
alter table CONTRACT add foreign key (FK_NOM_TIP_CONTRACT)  references NOM_TIP_CONTRACT (ID_NOM_TIP_CONTRACT);
alter table CONTRACT add foreign key (FK_PERSOANA_FIZICA)  references PERSOANA_FIZICA (ID_PERSOANA_FIZICA);
alter table CONTRACT add foreign key (FK_PERSOANA_RC)  references PERSOANA_RC (ID_PERSOANA_RC);

DROP TABLE IF EXISTS CROTALIE;
create table CROTALIE
(
  id_crotalie         NUMBER not null,
  fk_categorie_animal NUMBER not null,
  cod_identificare    VARCHAR2(20 ) not null,
  last_modified_date  TIMESTAMP not null,
  primary key (ID_CROTALIE)
);

create index IDX_CROTALIE_COD_IDENTIFICARE on CROTALIE (COD_IDENTIFICARE);
create index IDX_CROTALIE_FK_CATEG_ANIMAL on CROTALIE (FK_CATEGORIE_ANIMAL);
create index IDX_CROTALIE_LMD on CROTALIE (LAST_MODIFIED_DATE);

alter table CROTALIE add foreign key (FK_CATEGORIE_ANIMAL)  references CATEGORIE_ANIMAL (ID_CATEGORIE_ANIMAL);

DROP TABLE IF EXISTS NOM_TIP_SPATIU_PROT;
create table NOM_TIP_SPATIU_PROT
(
  id_nom_tip_spatiu_prot NUMBER not null,
  cod                    VARCHAR2(10 ) not null,
  denumire               VARCHAR2(100 ) not null,
  primary key (ID_NOM_TIP_SPATIU_PROT),
  unique UK_NOM_TIP_SPATIU_PROT_COD (COD)
);

DROP TABLE IF EXISTS CULTURA;
create table CULTURA
(
  id_cultura             NUMBER not null,
  fk_gospodarie          NUMBER not null,
  fk_cap_cultura         NUMBER not null,
  fk_nom_tip_spatiu_prot NUMBER,
  fk_nom_judet           NUMBER,
  an                     NUMBER(4) not null,
  suprafata              NUMBER(15) not null,
  last_modified_date     TIMESTAMP not null,
  primary key (ID_CULTURA)
);

create index IDX_CULTURA_AN on CULTURA (AN);
create index IDX_CULTURA_FK_CAP_CULTURA on CULTURA (FK_CAP_CULTURA);
create index IDX_CULTURA_FK_GOSPODARIE on CULTURA (FK_GOSPODARIE);
create index IDX_CULTURA_FK_NOM_TIP_SP_PROT on CULTURA (FK_NOM_TIP_SPATIU_PROT);
create index IDX_CULTURA_LMD on CULTURA (LAST_MODIFIED_DATE);

alter table CULTURA add foreign key (FK_CAP_CULTURA)  references CAP_CULTURA (ID_CAP_CULTURA);
alter table CULTURA add foreign key (FK_GOSPODARIE)  references GOSPODARIE (ID_GOSPODARIE);
alter table CULTURA add foreign key (FK_NOM_TIP_SPATIU_PROT)  references NOM_TIP_SPATIU_PROT (ID_NOM_TIP_SPATIU_PROT);

DROP TABLE IF EXISTS CULTURA_PROD;
create table CULTURA_PROD
(
  id_cultura_prod        NUMBER not null,
  fk_nom_uat             NUMBER not null,
  fk_cap_cultura_prod    NUMBER not null,
  fk_nom_tip_spatiu_prot NUMBER,
  fk_nom_judet           NUMBER,
  an                     NUMBER(4) not null,
  suprafata              NUMBER(15),
  prod_medie             NUMBER(10),
  prod_total             NUMBER(15),
  last_modified_date     TIMESTAMP not null,
  primary key (ID_CULTURA_PROD)
);
create index IDX_CULTURA_PROD_AN on CULTURA_PROD (AN);
create index IDX_CULTURA_PROD_FK_NOM_UAT on CULTURA_PROD (FK_NOM_UAT);
create index IDX_CULTURA_PROD_LMD on CULTURA_PROD (LAST_MODIFIED_DATE);
create index IDX_CULT_PROD_FK_CAP_CULT_PROD on CULTURA_PROD (FK_CAP_CULTURA_PROD);
create index IDX_CULT_PROD_FK_NOM_TIP_SP_PR on CULTURA_PROD (FK_NOM_TIP_SPATIU_PROT);

alter table CULTURA_PROD add foreign key (FK_NOM_UAT)  references NOM_UAT (ID_NOM_UAT);
alter table CULTURA_PROD add foreign key (FK_CAP_CULTURA_PROD)  references CAP_CULTURA_PROD (ID_CAP_CULTURA_PROD);
alter table CULTURA_PROD add foreign key (FK_NOM_TIP_SPATIU_PROT)  references NOM_TIP_SPATIU_PROT (ID_NOM_TIP_SPATIU_PROT);

DROP TABLE IF EXISTS DETINATOR_PF;


create table DETINATOR_PF
(
  id_detinator_pf    NUMBER not null,
  fk_gospodarie      NUMBER not null,
  fk_persoana_fizica NUMBER not null,
  fk_persoana_rc     NUMBER,
  fk_nom_judet       NUMBER,
  last_modified_date TIMESTAMP not null,
  primary key (ID_DETINATOR_PF)
);

create index IDX_DETINATOR_PF_FK_GOSPODARIE on DETINATOR_PF (FK_GOSPODARIE);
create index IDX_DETINATOR_PF_FK_PERS_FIZ on DETINATOR_PF (FK_PERSOANA_FIZICA);
create index IDX_DETINATOR_PF_FK_PERS_RC on DETINATOR_PF (FK_PERSOANA_RC);
create index IDX_DETINATOR_PF_LMD on DETINATOR_PF (LAST_MODIFIED_DATE);

alter table DETINATOR_PF add foreign key (FK_PERSOANA_RC)  references PERSOANA_RC (ID_PERSOANA_RC);
alter table DETINATOR_PF add foreign key (FK_PERSOANA_FIZICA)  references PERSOANA_FIZICA (ID_PERSOANA_FIZICA);
alter table DETINATOR_PF add foreign key (FK_GOSPODARIE)  references GOSPODARIE (ID_GOSPODARIE);

DROP TABLE IF EXISTS DETINATOR_PJ;


create table DETINATOR_PJ
(
  id_detinator_pj       NUMBER not null,
  fk_gospodarie         NUMBER not null,
  fk_persoana_rc        NUMBER not null,
  fk_persoana_fizica    NUMBER not null,
  fk_nom_judet          NUMBER,
  denumire_subdiviziune VARCHAR2(150 ),
  last_modified_date    TIMESTAMP not null,
  primary key (ID_DETINATOR_PJ)
);

create index IDX_DETINATOR_PJ_FK_GOSPODARIE on DETINATOR_PJ (FK_GOSPODARIE);
create index IDX_DETINATOR_PJ_FK_PERS_FIZIC on DETINATOR_PJ (FK_PERSOANA_FIZICA);
create index IDX_DETINATOR_PJ_FK_PERS_RC on DETINATOR_PJ (FK_PERSOANA_RC);
create index IDX_DETINATOR_PJ_LMD on DETINATOR_PJ (LAST_MODIFIED_DATE);


alter table DETINATOR_PJ add foreign key (FK_PERSOANA_RC)  references PERSOANA_RC (ID_PERSOANA_RC);
alter table DETINATOR_PJ add foreign key (FK_PERSOANA_FIZICA)  references PERSOANA_FIZICA (ID_PERSOANA_FIZICA);
alter table DETINATOR_PJ add foreign key (FK_GOSPODARIE)  references GOSPODARIE (ID_GOSPODARIE);

DROP TABLE IF EXISTS DOC_CERERE;
create table DOC_CERERE
(
  id_doc_cerere NUMBER not null,
  fk_cerere     NUMBER not null,
  fk_nom_judet  NUMBER,
  data_doc      DATE not null,
  continut      BLOB not null,
  is_activ      NUMBER(1) not null,
  uid_cmis      VARCHAR2(20 ),
  primary key (ID_DOC_CERERE),
  check (IS_ACTIV IN (0,1))
);
create index IDX_DOC_CERERE_DATA_DOC on DOC_CERERE (DATA_DOC);
create index IDX_DOC_CERERE_FK_CERERE on DOC_CERERE (FK_CERERE);
--create bitmap index IDX_DOC_CERERE_IS_ACTIV on DOC_CERERE (IS_ACTIV);
create index IDX_DOC_CERERE_UID_CMIS on DOC_CERERE (UID_CMIS);

alter table DOC_CERERE add foreign key (FK_CERERE)  references CERERE (ID_CERERE);

DROP TABLE IF EXISTS NOM_REZOLUTIE_CERERE;
create table NOM_REZOLUTIE_CERERE
(
  id_nom_rezolutie_cerere NUMBER not null,
  cod                     VARCHAR2(10 ) not null,
  denumire                VARCHAR2(100 ) not null,
  primary key (ID_NOM_REZOLUTIE_CERERE),
  unique UK_NOM_REZOLUTIE_CERERE_COD (COD)
);


DROP TABLE IF EXISTS NOM_STARE_CERERE;
create table NOM_STARE_CERERE
(
  id_nom_stare_cerere NUMBER not null,
  cod                 VARCHAR2(10 ) not null,
  denumire            VARCHAR2(100 ) not null,
  primary key (ID_NOM_STARE_CERERE),
  unique UK_NOM_STARE_CERERE_COD (COD)
);

DROP TABLE IF EXISTS FLUX_CERERE;
create table FLUX_CERERE
(
  id_flux_cerere          NUMBER not null,
  fk_cerere               NUMBER not null,
  fk_app_utilizator       NUMBER not null,
  fk_nom_stare_cerere     NUMBER not null,
  fk_nom_rezolutie_cerere NUMBER,
  fk_nom_judet            NUMBER,
  data_stare              DATE not null,
  descriere               VARCHAR2(500 ) not null,
  primary key (ID_FLUX_CERERE)
);
create index IDX_FLUX_CERERE_DATA_STARE on FLUX_CERERE (DATA_STARE);
create index IDX_FLUX_CERERE_FK_APP_UTILIZ on FLUX_CERERE (FK_APP_UTILIZATOR);
create index IDX_FLUX_CERERE_FK_CERERE on FLUX_CERERE (FK_CERERE);
create index IDX_FLUX_CERERE_FK_NOM_REZ_CER on FLUX_CERERE (FK_NOM_REZOLUTIE_CERERE);
create index IDX_FLUX_CERERE_FK_NOM_ST_CER on FLUX_CERERE (FK_NOM_STARE_CERERE);

--alter table FLUX_CERERE add foreign key (FK_APP_UTILIZATOR)  references RAN_PORTAL_DEV.APP_UTILIZATOR (ID_APP_UTILIZATOR);
alter table FLUX_CERERE add foreign key (FK_CERERE)  references CERERE (ID_CERERE);
alter table FLUX_CERERE add foreign key (FK_NOM_REZOLUTIE_CERERE)  references NOM_REZOLUTIE_CERERE (ID_NOM_REZOLUTIE_CERERE);
alter table FLUX_CERERE add foreign key (FK_NOM_STARE_CERERE)  references NOM_STARE_CERERE (ID_NOM_STARE_CERERE);

DROP TABLE IF EXISTS NOM_STARE_REGISTRU;
create table NOM_STARE_REGISTRU
(
  id_nom_stare_registru NUMBER not null,
  cod                   VARCHAR2(10 ) not null,
  denumire              VARCHAR2(100 ) not null,
  primary key (ID_NOM_STARE_REGISTRU),
  unique UK_NOM_STARE_REGISTRU_COD (COD)
);

DROP TABLE IF EXISTS NOM_INDICATIV_XML;
create table NOM_INDICATIV_XML
(
  id_nom_indicativ_xml NUMBER not null,
  cod                  VARCHAR2(10 ) not null,
  denumire             VARCHAR2(100 ) not null,
  primary key (ID_NOM_INDICATIV_XML),
  unique UK_NOM_INDICATIV_XML_COD (COD)
);

DROP TABLE IF EXISTS NOM_SURSA_REGISTRU;
create table NOM_SURSA_REGISTRU
(
  id_nom_sursa_registru NUMBER not null,
  cod                   VARCHAR2(10 ) not null,
  denumire              VARCHAR2(100 ) not null,
  primary key (ID_NOM_SURSA_REGISTRU),
  unique UK_NOM_SURSA_REGISTRU_COD (COD)
);

DROP TABLE IF EXISTS REGISTRU;
create table REGISTRU
(
  id_registru              NUMBER not null,
  fk_nom_uat               NUMBER not null,
  fk_nom_stare_registru    NUMBER not null,
  fk_nom_sursa_registru    NUMBER not null,
  fk_nom_indicativ_xml     NUMBER not null,
  fk_nom_capitol           NUMBER not null,
  fk_gospodarie            NUMBER,
  fk_nom_judet             NUMBER,
  modalitate_transmitere   NUMBER(1) default 3 not null,
  index_registru           VARCHAR2(36 ) not null,
  data_registru            DATE not null,
  data_export              DATE not null,
  identificator_gospodarie VARCHAR2(30 ),
  an                       NUMBER(4),
  continut                 CLOB not null,
  recipisa                 BLOB,
  is_recipisa_semnata      NUMBER(1) not null,
  stare_corelare           NUMBER(1) not null,
  primary key (ID_REGISTRU),
  check (IS_RECIPISA_SEMNATA IN (0,1)),
  check (MODALITATE_TRANSMITERE IN (1,2,3)),
  check (STARE_CORELARE IN (1,2,3))
);
create index IDX_REGISTRU_AN on REGISTRU (AN);
create index IDX_REGISTRU_DATA_REGISTRU on REGISTRU (DATA_REGISTRU);
create index IDX_REGISTRU_FK_GOSPODARIE on REGISTRU (FK_GOSPODARIE);
create index IDX_REGISTRU_FK_NOM_CAPITOL on REGISTRU (FK_NOM_CAPITOL);
create index IDX_REGISTRU_FK_NOM_INDIC_XML on REGISTRU (FK_NOM_INDICATIV_XML);
create index IDX_REGISTRU_FK_NOM_STARE_REG on REGISTRU (FK_NOM_STARE_REGISTRU);
create index IDX_REGISTRU_FK_NOM_SURSA_REG on REGISTRU (FK_NOM_SURSA_REGISTRU);
create index IDX_REGISTRU_FK_NOM_UAT on REGISTRU (FK_NOM_UAT);
create index IDX_REGISTRU_IDENT_GOSPODARIE on REGISTRU (IDENTIFICATOR_GOSPODARIE);
create index IDX_REGISTRU_MOD_TRANSMITERE on REGISTRU (MODALITATE_TRANSMITERE);
create index IDX_REGISTRU_STARE_CORELARE on REGISTRU (STARE_CORELARE);

alter table REGISTRU add foreign key (FK_GOSPODARIE)  references GOSPODARIE (ID_GOSPODARIE);
alter table REGISTRU add foreign key (FK_NOM_CAPITOL)  references NOM_CAPITOL (ID_NOM_CAPITOL);
alter table REGISTRU add foreign key (FK_NOM_INDICATIV_XML)  references NOM_INDICATIV_XML (ID_NOM_INDICATIV_XML);
alter table REGISTRU add foreign key (FK_NOM_STARE_REGISTRU)  references NOM_STARE_REGISTRU (ID_NOM_STARE_REGISTRU);
alter table REGISTRU add foreign key (FK_NOM_SURSA_REGISTRU)  references NOM_SURSA_REGISTRU (ID_NOM_SURSA_REGISTRU);
alter table REGISTRU add foreign key (FK_NOM_UAT)  references NOM_UAT (ID_NOM_UAT);

DROP TABLE IF EXISTS FLUX_REGISTRU;
create table FLUX_REGISTRU
(
  id_flux_registru      NUMBER not null,
  fk_registru           NUMBER not null,
  fk_nom_stare_registru NUMBER not null,
  data_stare            DATE not null,
  mesaj_stare           VARCHAR2(4000 ),
  primary key (ID_FLUX_REGISTRU)
);

alter table FLUX_REGISTRU add foreign key (FK_REGISTRU)  references REGISTRU (ID_REGISTRU);
alter table FLUX_REGISTRU add foreign key (FK_NOM_STARE_REGISTRU)  references NOM_STARE_REGISTRU (ID_NOM_STARE_REGISTRU);

DROP TABLE IF EXISTS FRUCT_PROD;
create table FRUCT_PROD
(
  id_fruct_prod             NUMBER not null,
  fk_nom_uat                NUMBER not null,
  fk_cap_fruct_prod         NUMBER not null,
  fk_nom_judet              NUMBER,
  an                        NUMBER(4) not null,
  nr_pom_razlet_rod         NUMBER(10),
  prod_medie_pom_razlet_rod NUMBER(5),
  prod_total_pom_razlet_rod NUMBER(10),
  suprafata_livada          NUMBER(15),
  prod_medie_livada         NUMBER(10),
  prod_total_livada         NUMBER(15),
  last_modified_date        TIMESTAMP not null,
  primary key (ID_FRUCT_PROD)
);

create index IDX_FRUCT_PROD_AN on FRUCT_PROD (AN);
create index IDX_FRUCT_PROD_FK_CAP_FRUCT_PR on FRUCT_PROD (FK_CAP_FRUCT_PROD);
create index IDX_FRUCT_PROD_FK_NOM_UAT on FRUCT_PROD (FK_NOM_UAT);
create index IDX_FRUCT_PROD_LMD on FRUCT_PROD (LAST_MODIFIED_DATE);

alter table FRUCT_PROD add foreign key (FK_CAP_FRUCT_PROD)  references CAP_FRUCT_PROD (ID_CAP_FRUCT_PROD);
alter table FRUCT_PROD add foreign key (FK_NOM_UAT)  references NOM_UAT (ID_NOM_UAT);

DROP TABLE IF EXISTS GEOLOCATOR_ADRESA;
create table GEOLOCATOR_ADRESA
(
  id_geolocator_adresa NUMBER not null,
  fk_adresa            NUMBER not null,
  fk_nom_judet         NUMBER,
  geometrie            GEOMETRY not null,
  primary key (ID_GEOLOCATOR_ADRESA)
);
alter table GEOLOCATOR_ADRESA add foreign key (FK_ADRESA)  references ADRESA (ID_ADRESA);

DROP TABLE IF EXISTS GEOMETRIE_CLADIRE;
create table GEOMETRIE_CLADIRE
(
  id_geometrie_cladire NUMBER not null,
  fk_cladire           NUMBER not null,
  fk_nom_judet         NUMBER,
  geometrie            GEOMETRY not null,
  primary key (ID_GEOMETRIE_CLADIRE)
);
create index IDX_GEOLOCAT_CLADIRE_FK_CLAD on GEOMETRIE_CLADIRE (FK_CLADIRE);

alter table GEOMETRIE_CLADIRE add foreign key (FK_CLADIRE)  references CLADIRE (ID_CLADIRE);

DROP TABLE IF EXISTS GEOMETRIE_FLUX_CERERE;
create table GEOMETRIE_FLUX_CERERE
(
  id_geometrie_flux_cerere NUMBER not null,
  fk_flux_cerere           NUMBER not null,
  geometrie                GEOMETRY not null,
  primary key (ID_GEOMETRIE_FLUX_CERERE)
);
create index IDX_GEOMETRIE_FL_CER_FK_FL_CER on GEOMETRIE_FLUX_CERERE (FK_FLUX_CERERE);
alter table GEOMETRIE_FLUX_CERERE add foreign key (FK_FLUX_CERERE)  references FLUX_CERERE (ID_FLUX_CERERE);

DROP TABLE IF EXISTS GEOMETRIE_PARCELA_TEREN;
create table GEOMETRIE_PARCELA_TEREN
(
  id_geometrie_parcela_teren NUMBER not null,
  fk_parcela_teren           NUMBER not null,
  fk_nom_judet               NUMBER,
  geometrie                  GEOMETRY not null,
  primary key (ID_GEOMETRIE_PARCELA_TEREN)
);
alter table GEOMETRIE_PARCELA_TEREN add foreign key (FK_PARCELA_TEREN)  references PARCELA_TEREN (ID_PARCELA_TEREN);

DROP TABLE IF EXISTS INVENTAR_GOSP_UAT;
create table INVENTAR_GOSP_UAT
(
  id_inventar_gosp_uat NUMBER not null,
  fk_nom_uat           NUMBER not null,
  an                   NUMBER(4) not null,
  valoare              NUMBER(7) not null,
  last_modified_date   TIMESTAMP not null,
  primary key (ID_INVENTAR_GOSP_UAT)
);

create index IDX_INVENTAR_GOSP_UAT_AN on INVENTAR_GOSP_UAT (AN);
create index IDX_INVENTAR_GOSP_UAT_LMD on INVENTAR_GOSP_UAT (LAST_MODIFIED_DATE);
create index IDX_INVENT_GOSP_UAT_FK_NOM_UAT on INVENTAR_GOSP_UAT (FK_NOM_UAT);

alter table INVENTAR_GOSP_UAT add foreign key (FK_NOM_UAT)  references NOM_UAT (ID_NOM_UAT);

DROP TABLE IF EXISTS NOM_LEGATURA_RUDENIE;
create table NOM_LEGATURA_RUDENIE
(
  id_nom_legatura_rudenie NUMBER not null,
  cod                     VARCHAR2(10 ) not null,
  denumire                VARCHAR2(100 ) not null,
  descriere               VARCHAR2(500 ),
  data_start              DATE not null,
  data_stop               DATE,
  base_id                 NUMBER not null,
  last_modified_date      TIMESTAMP not null,
  primary key (ID_NOM_LEGATURA_RUDENIE)
);

create index IDX_NOM_LEGATURA_RUDENIE_COD on NOM_LEGATURA_RUDENIE (COD);
create index IDX_NOM_LEGATURA_RUDENIE_LMD on NOM_LEGATURA_RUDENIE (LAST_MODIFIED_DATE);
create index IDX_NOM_LEG_RUDENIE_BASE_ID on NOM_LEGATURA_RUDENIE (BASE_ID);
create index IDX_NOM_LEG_RUD_DAT_START_STOP on NOM_LEGATURA_RUDENIE (DATA_START, DATA_STOP);

DROP TABLE IF EXISTS MEMBRU_PF;
create table MEMBRU_PF
(
  id_membru_pf            NUMBER not null,
  fk_detinator_pf         NUMBER not null,
  fk_nom_legatura_rudenie NUMBER not null,
  fk_persoana_fizica      NUMBER not null,
  fk_nom_judet            NUMBER,
  cod_rand                NUMBER(2) not null,
  mentiune                VARCHAR2(500 ),
  last_modified_date      TIMESTAMP not null,
  primary key (ID_MEMBRU_PF)
);

create index IDX_MEMBRU_PF_FK_DETINATOR_PF on MEMBRU_PF (FK_DETINATOR_PF);
create index IDX_MEMBRU_PF_FK_NOM_LEG_RUD on MEMBRU_PF (FK_NOM_LEGATURA_RUDENIE);
create index IDX_MEMBRU_PF_FK_PERS_FIZICA on MEMBRU_PF (FK_PERSOANA_FIZICA);
create index IDX_MEMBRU_PF_LMD on MEMBRU_PF (LAST_MODIFIED_DATE);

alter table MEMBRU_PF add foreign key (FK_DETINATOR_PF)  references DETINATOR_PF (ID_DETINATOR_PF);
alter table MEMBRU_PF add foreign key (FK_NOM_LEGATURA_RUDENIE)  references NOM_LEGATURA_RUDENIE (ID_NOM_LEGATURA_RUDENIE);
alter table MEMBRU_PF add foreign key (FK_PERSOANA_FIZICA)  references PERSOANA_FIZICA (ID_PERSOANA_FIZICA);

DROP TABLE IF EXISTS MENTIUNE_CERERE_SUC;
create table MENTIUNE_CERERE_SUC
(
  id_mentiune_cerere_suc NUMBER not null,
  fk_gospodarie          NUMBER not null,
  fk_persoana_fizica     NUMBER not null,
  fk_nom_judet           NUMBER,
  data_deces             DATE not null,
  nr_inregistrare        VARCHAR2(20 ) not null,
  data_inregistrare      DATE not null,
  spn_bin                VARCHAR2(100 ) not null,
  last_modified_date     TIMESTAMP not null,
  primary key (ID_MENTIUNE_CERERE_SUC)
);

create index IDX_MENTIUNE_CERERE_SUC_LMD on MENTIUNE_CERERE_SUC (LAST_MODIFIED_DATE);
create index IDX_MENT_CERERE_SUC_FK_GOSPOD on MENTIUNE_CERERE_SUC (FK_GOSPODARIE);
create index IDX_MENT_CER_SUC_FK_PERS_FIZ on MENTIUNE_CERERE_SUC (FK_PERSOANA_FIZICA);

alter table MENTIUNE_CERERE_SUC add foreign key (FK_GOSPODARIE)  references GOSPODARIE (ID_GOSPODARIE);
alter table MENTIUNE_CERERE_SUC add foreign key (FK_PERSOANA_FIZICA)  references PERSOANA_FIZICA (ID_PERSOANA_FIZICA);

DROP TABLE IF EXISTS MENTIUNE_SPECIALA;
create table MENTIUNE_SPECIALA
(
  id_mentiune_speciala NUMBER not null,
  fk_gospodarie        NUMBER not null,
  fk_nom_judet         NUMBER,
  mentiune             VARCHAR2(500 ) not null,
  last_modified_date   TIMESTAMP not null,
  primary key (ID_MENTIUNE_SPECIALA)
);

create index IDX_MENTIUNE_SPECIALA_FK_GOSP on MENTIUNE_SPECIALA (FK_GOSPODARIE);
create index IDX_MENTIUNE_SPECIALA_LMD on MENTIUNE_SPECIALA (LAST_MODIFIED_DATE);

alter table MENTIUNE_SPECIALA add foreign key (FK_GOSPODARIE)  references GOSPODARIE (ID_GOSPODARIE);

DROP TABLE IF EXISTS MSG_OPERATION;
create table MSG_OPERATION
(
  id_operation        NUMBER not null,
  operation_type      VARCHAR2(100 ),
  ip_address          VARCHAR2(50 ),
  wsdl_operation_name VARCHAR2(60 ),
  wsdl_service_name   VARCHAR2(60 ),
  operation_time      DATE,
  host_name           VARCHAR2(60 ),
  primary key (ID_OPERATION)
);

DROP TABLE IF EXISTS MSG_REQUEST;
create table MSG_REQUEST
(
  id_msg       NUMBER not null,
  content_path VARCHAR2(500 ),
  primary key (ID_MSG)
);

DROP TABLE IF EXISTS MSG_RESPONSE;
create table MSG_RESPONSE
(
  id_msg            NUMBER not null,
  content_path      VARCHAR2(600 ),
  response_date     DATE,
  fault_msg         VARCHAR2(4000 ),
  fault_stack_trace VARCHAR2(4000 ),
  fault_code        VARCHAR2(255 ),
  primary key (ID_MSG)
);

DROP TABLE IF EXISTS NOM_RENNS;
create table NOM_RENNS
(
  id_nom_renns      NUMBER not null,
  fk_nom_judet      NUMBER not null,
  fk_nom_uat        NUMBER not null,
  fk_nom_localitate NUMBER not null,
  uid_renns         VARCHAR2(20 ),
  strada            VARCHAR2(100 ),
  numar_strada      VARCHAR2(10 ),
  bloc              VARCHAR2(10 ),
  scara             VARCHAR2(10 ),
  etaj              VARCHAR2(10 ),
  apartament        VARCHAR2(10 ),
  numar_postal      VARCHAR2(10 ),
  geometrie         GEOMETRY,
  shapefile         BLOB,
  primary key (ID_NOM_RENNS)
);

DROP TABLE IF EXISTS NOM_TIP_LOCALIZARE;
create table NOM_TIP_LOCALIZARE
(
  id_nom_tip_localizare NUMBER not null,
  cod                   VARCHAR2(10 ) not null,
  denumire              VARCHAR2(100 ) not null,
  descriere             VARCHAR2(500 ),
  grupa                 NUMBER(1) not null,
  data_start            DATE not null,
  data_stop             DATE,
  base_id               NUMBER not null,
  last_modified_date    TIMESTAMP not null,
  primary key (ID_NOM_TIP_LOCALIZARE),
  check (GRUPA IN (1,2))
);

create index IDX_NOM_TIP_LOCALIZARE_BASE_ID on NOM_TIP_LOCALIZARE (BASE_ID);
create index IDX_NOM_TIP_LOCALIZARE_COD on NOM_TIP_LOCALIZARE (COD);
create index IDX_NOM_TIP_LOCALIZARE_GRUPA on NOM_TIP_LOCALIZARE (GRUPA);
create index IDX_NOM_TIP_LOCALIZARE_LMD on NOM_TIP_LOCALIZARE (LAST_MODIFIED_DATE);
create index IDX_NOM_TIP_LOC_DAT_START_STOP on NOM_TIP_LOCALIZARE (DATA_START, DATA_STOP);

DROP TABLE IF EXISTS NOM_TIP_REL_PREEMPTIUNE;
create table NOM_TIP_REL_PREEMPTIUNE
(
  id_nom_tip_rel_preemptiune NUMBER not null,
  cod                        VARCHAR2(10 ) not null,
  denumire                   VARCHAR2(100 ) not null,
  primary key (ID_NOM_TIP_REL_PREEMPTIUNE),
  unique UK_NOM_TIP_REL_PREEMPTIUNE (COD)
);

DROP TABLE IF EXISTS PARCELA_LOCALIZARE;
create table PARCELA_LOCALIZARE
(
  id_parcela_localizare NUMBER not null,
  fk_parcela_teren      NUMBER not null,
  fk_nom_tip_localizare NUMBER not null,
  valoare               VARCHAR2(30 ) not null,
  last_modified_date    TIMESTAMP not null,
  primary key (ID_PARCELA_LOCALIZARE)
);

create index IDX_PARCELA_LOCALIZARE_LMD on PARCELA_LOCALIZARE (LAST_MODIFIED_DATE);
create index IDX_PARCELA_LOC_FK_NOM_TIP_LOC on PARCELA_LOCALIZARE (FK_NOM_TIP_LOCALIZARE);
create index IDX_PARCELA_LOC_FK_PARC_TEREN on PARCELA_LOCALIZARE (FK_PARCELA_TEREN);

alter table PARCELA_LOCALIZARE add foreign key (FK_NOM_TIP_LOCALIZARE)  references NOM_TIP_LOCALIZARE (ID_NOM_TIP_LOCALIZARE);
alter table PARCELA_LOCALIZARE add foreign key (FK_PARCELA_TEREN)  references PARCELA_TEREN (ID_PARCELA_TEREN);

DROP TABLE IF EXISTS PREEMPTIUNE;
create table PREEMPTIUNE
(
  id_preemptiune          NUMBER not null,
  fk_gospodarie           NUMBER not null,
  nr_oferta_vanzare       VARCHAR2(20 ) not null,
  fk_act_aviz_madr_dadr   NUMBER not null,
  fk_act_adev_vanzare_lib NUMBER,
  fk_nom_judet            NUMBER,
  data_oferta_vanzare     DATE not null,
  pret_oferta_vanzare     NUMBER(15,2) not null,
  suprafata               NUMBER(15) not null,
  nr_carte_funciara       VARCHAR2(20 ) not null,
  last_modified_date      TIMESTAMP not null,
  primary key (ID_PREEMPTIUNE)
);

create index IDX_PREEMPTIUNE_FK_GOSPODARIE on PREEMPTIUNE (FK_GOSPODARIE);
create index IDX_PREEMPTIUNE_LMD on PREEMPTIUNE (LAST_MODIFIED_DATE);
create index IDX_PREEMP_FK_ACT_ADEV_VNZ_LIB on PREEMPTIUNE (FK_ACT_ADEV_VANZARE_LIB);
create index IDX_PREEMP_FK_ACT_AV_MADR_DADR on PREEMPTIUNE (FK_ACT_AVIZ_MADR_DADR);

alter table PREEMPTIUNE add foreign key (FK_GOSPODARIE)  references GOSPODARIE (ID_GOSPODARIE);
alter table PREEMPTIUNE add foreign key (FK_ACT_ADEV_VANZARE_LIB)  references ACT (ID_ACT);
alter table PREEMPTIUNE add foreign key (FK_ACT_AVIZ_MADR_DADR)  references ACT (ID_ACT);

DROP TABLE IF EXISTS PERSOANA_PREEMPTIUNE;
create table PERSOANA_PREEMPTIUNE
(
  id_persoana_preemptiune    NUMBER not null,
  fk_preemptiune             NUMBER not null,
  fk_nom_tip_rel_preemptiune NUMBER not null,
  fk_persoana_fizica         NUMBER,
  fk_persoana_rc             NUMBER,
  last_modified_date         TIMESTAMP not null,
  primary key (ID_PERSOANA_PREEMPTIUNE)
);

create index IDX_PERSOANA_PREEMPTIUNE_LMD on PERSOANA_PREEMPTIUNE (LAST_MODIFIED_DATE);
create index IDX_PERS_PREEMPTIUNE_FK_PREEMP on PERSOANA_PREEMPTIUNE (FK_PREEMPTIUNE);
create index IDX_PERS_PREEMP_FK_PERS_FIZICA on PERSOANA_PREEMPTIUNE (FK_PERSOANA_FIZICA);
create index IDX_PERS_PREEMP_FK_PERS_RC on PERSOANA_PREEMPTIUNE (FK_PERSOANA_RC);
create index IDX_PERS_PR_FK_NOM_TIP_REL_PR on PERSOANA_PREEMPTIUNE (FK_NOM_TIP_REL_PREEMPTIUNE);

alter table PERSOANA_PREEMPTIUNE add foreign key (FK_PREEMPTIUNE)  references PREEMPTIUNE (ID_PREEMPTIUNE);
alter table PERSOANA_PREEMPTIUNE add foreign key (FK_PERSOANA_FIZICA)  references PERSOANA_FIZICA (ID_PERSOANA_FIZICA);
alter table PERSOANA_PREEMPTIUNE add foreign key (FK_PERSOANA_RC)  references PERSOANA_RC (ID_PERSOANA_RC);
alter table PERSOANA_PREEMPTIUNE add foreign key (FK_NOM_TIP_REL_PREEMPTIUNE)  references NOM_TIP_REL_PREEMPTIUNE (ID_NOM_TIP_REL_PREEMPTIUNE);

DROP TABLE IF EXISTS PLANTATIE;
create table PLANTATIE
(
  id_plantatie       NUMBER not null,
  fk_gospodarie      NUMBER not null,
  fk_cap_plantatie   NUMBER not null,
  fk_nom_judet       NUMBER,
  an                 NUMBER(4) not null,
  suprafata          NUMBER(15) not null,
  nr_pom_rod         NUMBER(10),
  last_modified_date TIMESTAMP not null,
  primary key (ID_PLANTATIE)
);
create index IDX_PLANTATIE_AN on PLANTATIE (AN);
create index IDX_PLANTATIE_FK_CAP_PLANT on PLANTATIE (FK_CAP_PLANTATIE);
create index IDX_PLANTATIE_FK_GOSPODARIE on PLANTATIE (FK_GOSPODARIE);
create index IDX_PLANTATIE_LMD on PLANTATIE (LAST_MODIFIED_DATE);

alter table PLANTATIE add foreign key (FK_CAP_PLANTATIE)  references CAP_PLANTATIE (ID_CAP_PLANTATIE);
alter table PLANTATIE add foreign key (FK_GOSPODARIE)  references GOSPODARIE (ID_GOSPODARIE);

DROP TABLE IF EXISTS PLANTATIE_PROD;
create table PLANTATIE_PROD
(
  id_plantatie_prod     NUMBER not null,
  fk_nom_uat            NUMBER not null,
  fk_cap_plantatie_prod NUMBER not null,
  fk_nom_judet          NUMBER,
  an                    NUMBER(4) not null,
  suprafata             NUMBER(15),
  prod_medie            NUMBER(10),
  prod_total            NUMBER(15),
  last_modified_date    TIMESTAMP not null,
  primary key (ID_PLANTATIE_PROD)
);

create index IDX_PLANTATIE_PROD_AN on PLANTATIE_PROD (AN);
create index IDX_PLANTATIE_PROD_FK_NOM_UAT on PLANTATIE_PROD (FK_NOM_UAT);
create index IDX_PLANTATIE_PROD_LMD on PLANTATIE_PROD (LAST_MODIFIED_DATE);
create index IDX_PLANT_PROD_FK_CAP_PLANT_PR on PLANTATIE_PROD (FK_CAP_PLANTATIE_PROD);

alter table PLANTATIE_PROD add foreign key (FK_NOM_UAT)  references NOM_UAT (ID_NOM_UAT);
alter table PLANTATIE_PROD add foreign key (FK_CAP_PLANTATIE_PROD)  references CAP_PLANTATIE_PROD (ID_CAP_PLANTATIE_PROD);

DROP TABLE IF EXISTS POM_RAZLET;
create table POM_RAZLET
(
  id_pom_razlet      NUMBER not null,
  fk_gospodarie      NUMBER not null,
  fk_cap_pom_razlet  NUMBER not null,
  fk_nom_judet       NUMBER,
  an                 NUMBER(4) not null,
  nr_pom_rod         NUMBER(10) not null,
  nr_pom_tanar       NUMBER(10) not null,
  last_modified_date TIMESTAMP not null,
  primary key (ID_POM_RAZLET)
);
create index IDX_POM_RAZLET_AN on POM_RAZLET (AN);
create index IDX_POM_RAZLET_FK_CAP_POM_RAZ on POM_RAZLET (FK_CAP_POM_RAZLET);
create index IDX_POM_RAZLET_FK_GOSPODARIE on POM_RAZLET (FK_GOSPODARIE);
create index IDX_POM_RAZLET_LMD on POM_RAZLET (LAST_MODIFIED_DATE);

alter table POM_RAZLET add foreign key (FK_CAP_POM_RAZLET)  references CAP_POM_RAZLET (ID_CAP_POM_RAZLET);
alter table POM_RAZLET add foreign key (FK_GOSPODARIE)  references GOSPODARIE (ID_GOSPODARIE);

DROP TABLE IF EXISTS PROPRIETAR_PARCELA;
create table PROPRIETAR_PARCELA
(
  id_proprietar_parcela NUMBER not null,
  fk_parcela_teren      NUMBER not null,
  fk_persoana_fizica    NUMBER not null,
  fk_nom_judet          NUMBER,
  last_modified_date    TIMESTAMP not null,
  primary key (ID_PROPRIETAR_PARCELA)
);

create index IDX_PROPRIETAR_PARCELA_LMD on PROPRIETAR_PARCELA (LAST_MODIFIED_DATE);
create index IDX_PROP_PARCELA_FK_PARC_TEREN on PROPRIETAR_PARCELA (FK_PARCELA_TEREN);
create index IDX_PROP_PARCELA_FK_PERS_FIZ on PROPRIETAR_PARCELA (FK_PERSOANA_FIZICA);

alter table PROPRIETAR_PARCELA add foreign key (FK_PARCELA_TEREN)  references PARCELA_TEREN (ID_PARCELA_TEREN);
alter table PROPRIETAR_PARCELA add foreign key (FK_PERSOANA_FIZICA)  references PERSOANA_FIZICA (ID_PERSOANA_FIZICA);

DROP TABLE IF EXISTS SISTEM_TEHNIC;
create table SISTEM_TEHNIC
(
  id_sistem_tehnic     NUMBER not null,
  fk_gospodarie        NUMBER not null,
  fk_cap_sistem_tehnic NUMBER not null,
  fk_nom_judet         NUMBER,
  an                   NUMBER(4) not null,
  numar                NUMBER(5) not null,
  last_modified_date   TIMESTAMP not null,
  primary key (ID_SISTEM_TEHNIC)
);
create index IDX_SISTEM_TEHNIC_AN on SISTEM_TEHNIC (AN);
create index IDX_SISTEM_TEHNIC_FK_GOSPOD on SISTEM_TEHNIC (FK_GOSPODARIE);
create index IDX_SISTEM_TEHNIC_LMD on SISTEM_TEHNIC (LAST_MODIFIED_DATE);
create index IDX_SIST_TEHN_FK_CAP_SIST_TEHN on SISTEM_TEHNIC (FK_CAP_SISTEM_TEHNIC);

alter table SISTEM_TEHNIC add foreign key (FK_GOSPODARIE)  references GOSPODARIE (ID_GOSPODARIE);
alter table SISTEM_TEHNIC add foreign key (FK_CAP_SISTEM_TEHNIC)  references CAP_SISTEM_TEHNIC (ID_CAP_SISTEM_TEHNIC);

DROP TABLE IF EXISTS SUBSTANTA_CHIMICA;
create table SUBSTANTA_CHIMICA
(
  id_substanta_chimica     NUMBER not null,
  fk_gospodarie            NUMBER not null,
  fk_cap_substanta_chimica NUMBER not null,
  fk_nom_judet             NUMBER,
  an                       NUMBER(4) not null,
  suprafata_total          NUMBER(15) not null,
  cantitate_total          NUMBER(10) not null,
  suprafata_azotoase       NUMBER(15) not null,
  cantitate_azotoase       NUMBER(10) not null,
  suprafata_fosfatice      NUMBER(15) not null,
  cantitate_fosfatice      NUMBER(10) not null,
  suprafata_potasice       NUMBER(15) not null,
  cantitate_potasice       NUMBER(10) not null,
  last_modified_date       TIMESTAMP not null,
  primary key (ID_SUBSTANTA_CHIMICA)
);
create index IDX_SUBSTANTA_CHIMICA_AN on SUBSTANTA_CHIMICA (AN);
create index IDX_SUBSTANTA_CHIMICA_LMD on SUBSTANTA_CHIMICA (LAST_MODIFIED_DATE);
create index IDX_SUBST_CHIMICA_FK_GOSPOD on SUBSTANTA_CHIMICA (FK_GOSPODARIE);
create index IDX_SUBST_CHIM_FK_CAP_SUB_CHIM on SUBSTANTA_CHIMICA (FK_CAP_SUBSTANTA_CHIMICA);

alter table SUBSTANTA_CHIMICA add foreign key (FK_GOSPODARIE)  references GOSPODARIE (ID_GOSPODARIE);
alter table SUBSTANTA_CHIMICA add foreign key (FK_CAP_SUBSTANTA_CHIMICA)  references CAP_SUBSTANTA_CHIMICA (ID_CAP_SUBSTANTA_CHIMICA);

DROP TABLE IF EXISTS SUCCESIBIL;
create table SUCCESIBIL
(
  id_succesibil          NUMBER not null,
  fk_mentiune_cerere_suc NUMBER not null,
  fk_adresa              NUMBER not null,
  fk_persoana_fizica     NUMBER not null,
  last_modified_date     TIMESTAMP not null,
  primary key (ID_SUCCESIBIL)
);
create index IDX_SUCCESIBIL_FK_ADRESA on SUCCESIBIL (FK_ADRESA);
create index IDX_SUCCESIBIL_FK_MENT_CER_SUC on SUCCESIBIL (FK_MENTIUNE_CERERE_SUC);
create index IDX_SUCCESIBIL_FK_PERS_FIZICA on SUCCESIBIL (FK_PERSOANA_FIZICA);
create index IDX_SUCCESIBIL_LMD on SUCCESIBIL (LAST_MODIFIED_DATE);

alter table SUCCESIBIL add foreign key (FK_ADRESA)  references ADRESA (ID_ADRESA);
alter table SUCCESIBIL add foreign key (FK_MENTIUNE_CERERE_SUC)  references MENTIUNE_CERERE_SUC (ID_MENTIUNE_CERERE_SUC);
alter table SUCCESIBIL add foreign key (FK_PERSOANA_FIZICA)  references PERSOANA_FIZICA (ID_PERSOANA_FIZICA);

DROP TABLE IF EXISTS SUPRAFATA_CATEGORIE;
create table SUPRAFATA_CATEGORIE
(
  id_suprafata_categorie     NUMBER not null,
  fk_gospodarie              NUMBER not null,
  fk_cap_categorie_folosinta NUMBER not null,
  fk_nom_judet               NUMBER,
  an                         NUMBER(4) not null,
  suprafata_total            NUMBER(15) not null,
  suprafata_local            NUMBER(15),
  suprafata_alt              NUMBER(15),
  last_modified_date         TIMESTAMP not null,
  primary key (ID_SUPRAFATA_CATEGORIE)
);

create index IDX_SUPRAFATA_CATEGORIE_AN on SUPRAFATA_CATEGORIE (AN);
create index IDX_SUPRAFATA_CATEGORIE_LMD on SUPRAFATA_CATEGORIE (LAST_MODIFIED_DATE);
create index IDX_SUPRAF_CATEG_FK_GOSPODARIE on SUPRAFATA_CATEGORIE (FK_GOSPODARIE);
create index IDX_SUPRAF_CAT_FK_CAP_CAT_FOL on SUPRAFATA_CATEGORIE (FK_CAP_CATEGORIE_FOLOSINTA);

alter table SUPRAFATA_CATEGORIE add foreign key (FK_CAP_CATEGORIE_FOLOSINTA)  references CAP_CATEGORIE_FOLOSINTA (ID_CAP_CATEGORIE_FOLOSINTA);
alter table SUPRAFATA_CATEGORIE add foreign key (FK_GOSPODARIE)  references GOSPODARIE (ID_GOSPODARIE);

DROP TABLE IF EXISTS SUPRAFATA_UTILIZARE;
create table SUPRAFATA_UTILIZARE
(
  id_suprafata_utilizare NUMBER not null,
  fk_gospodarie          NUMBER not null,
  fk_cap_mod_utilizare   NUMBER not null,
  fk_nom_judet           NUMBER,
  an                     NUMBER(4) not null,
  suprafata              NUMBER(15) not null,
  last_modified_date     TIMESTAMP not null,
  primary key (ID_SUPRAFATA_UTILIZARE)
);
create index IDX_SUPRAFATA_UTILIZARE_AN on SUPRAFATA_UTILIZARE (AN);
create index IDX_SUPRAFATA_UTILIZARE_LMD on SUPRAFATA_UTILIZARE (LAST_MODIFIED_DATE);
create index IDX_SUPRAF_UTILIZARE_FK_GOSPOD on SUPRAFATA_UTILIZARE (FK_GOSPODARIE);
create index IDX_SUPR_UTIL_FK_CAP_MOD_UTIL on SUPRAFATA_UTILIZARE (FK_CAP_MOD_UTILIZARE);

alter table SUPRAFATA_UTILIZARE add foreign key (FK_GOSPODARIE)  references GOSPODARIE (ID_GOSPODARIE);
alter table SUPRAFATA_UTILIZARE add foreign key (FK_CAP_MOD_UTILIZARE)  references CAP_MOD_UTILIZARE (ID_CAP_MOD_UTILIZARE);

DROP TABLE IF EXISTS TABLE_DELETE_LOG;
create table TABLE_DELETE_LOG
(
  table_name   VARCHAR2(30 ) not null,
  record_id    NUMBER not null,
  deleted_date TIMESTAMP not null
);
create index IDX_TABLE_DELETE_LOG_DEL_DATE on TABLE_DELETE_LOG (DELETED_DATE);
create index IDX_TABLE_DELETE_LOG_RECORD_ID on TABLE_DELETE_LOG (RECORD_ID);
create index IDX_TABLE_DEL_LOG_TABLE_NAME on TABLE_DELETE_LOG (TABLE_NAME);


DROP TABLE IF EXISTS TEREN_IRIGAT;
create table TEREN_IRIGAT
(
  id_teren_irigat     NUMBER not null,
  fk_gospodarie       NUMBER not null,
  fk_cap_teren_irigat NUMBER not null,
  fk_nom_judet        NUMBER,
  an                  NUMBER(4) not null,
  suprafata           NUMBER(15) not null,
  last_modified_date  TIMESTAMP not null,
  primary key (ID_TEREN_IRIGAT)
);
create index IDX_TEREN_IRIGAT_AN on TEREN_IRIGAT (AN);
create index IDX_TEREN_IRIGAT_FK_GOSPODARIE on TEREN_IRIGAT (FK_GOSPODARIE);
create index IDX_TEREN_IRIGAT_LMD on TEREN_IRIGAT (LAST_MODIFIED_DATE);
create index IDX_TEREN_IRIG_FK_CAP_TER_IRIG on TEREN_IRIGAT (FK_CAP_TEREN_IRIGAT);

alter table TEREN_IRIGAT add foreign key (FK_GOSPODARIE)  references GOSPODARIE (ID_GOSPODARIE);
alter table TEREN_IRIGAT add foreign key (FK_CAP_TEREN_IRIGAT)  references CAP_TEREN_IRIGAT (ID_CAP_TEREN_IRIGAT);

create sequence SEQ_ACT
minvalue 1
maxvalue 9999999999999
start with 1202
increment by 1;

create sequence SEQ_ACT_DETINERE
minvalue 1
maxvalue 9999999999999
start with 1014
increment by 1;

create sequence SEQ_ADRESA
minvalue 1
maxvalue 9999999999999
start with 1521
increment by 1;

create sequence SEQ_ADRESA_GOSPODARIE
minvalue 1
maxvalue 9999999999999
start with 1437
increment by 1;

create sequence SEQ_ANIMAL_PROD
minvalue 1
maxvalue 9999999999999
start with 1045
increment by 1;

create sequence SEQ_APLICARE_INGRASAMANT
minvalue 1
maxvalue 9999999999999
start with 1160
increment by 1;

create sequence SEQ_ATESTAT
minvalue 1
maxvalue 9999999999999
start with 1033
increment by 1;

create sequence SEQ_ATESTAT_PRODUS
minvalue 1
maxvalue 9999999999999
start with 1096
increment by 1;

create sequence SEQ_ATESTAT_VIZA
minvalue 1
maxvalue 9999999999999
start with 1032
increment by 1;

create sequence SEQ_CAP_ANIMAL_PROD
minvalue 1
maxvalue 9999999999999
start with 1010
increment by 1;

create sequence SEQ_CAP_APLICARE_INGRASAMANT
minvalue 1
maxvalue 9999999999999
start with 1010
increment by 1;

create sequence SEQ_CAP_CATEGORIE_ANIMAL
minvalue 1
maxvalue 9999999999999
start with 1007
increment by 1;

create sequence SEQ_CAP_CATEGORIE_FOLOSINTA
minvalue 1
maxvalue 9999999999999
start with 1017
increment by 1;

create sequence SEQ_CAP_CULTURA
minvalue 1
maxvalue 9999999999999
start with 1002
increment by 1;

create sequence SEQ_CAP_CULTURA_PROD
minvalue 1
maxvalue 9999999999999
start with 1002
increment by 1;

create sequence SEQ_CAP_FRUCT_PROD
minvalue 1
maxvalue 9999999999999
start with 1002
increment by 1;

create sequence SEQ_CAP_MOD_UTILIZARE
minvalue 1
maxvalue 9999999999999
start with 1003
increment by 1;

create sequence SEQ_CAP_PLANTATIE
minvalue 1
maxvalue 9999999999999
start with 1054
increment by 1;

create sequence SEQ_CAP_PLANTATIE_PROD
minvalue 1
maxvalue 9999999999999
start with 1004
increment by 1;

create sequence SEQ_CAP_POM_RAZLET
minvalue 1
maxvalue 9999999999999
start with 1006
increment by 1;

create sequence SEQ_CAP_SISTEM_TEHNIC
minvalue 1
maxvalue 9999999999999
start with 1003
increment by 1;

create sequence SEQ_CAP_SUBSTANTA_CHIMICA
minvalue 1
maxvalue 9999999999999
start with 1007
increment by 1;

create sequence SEQ_CAP_TEREN_IRIGAT
minvalue 1
maxvalue 9999999999999
start with 1002
increment by 1;

create sequence SEQ_CATEGORIE_ANIMAL
minvalue 1
maxvalue 9999999999999
start with 1186
increment by 1;

create sequence SEQ_CERERE
minvalue 1
maxvalue 9999999999999
start with 1000
increment by 1;

create sequence SEQ_CERERE_DETALIU
minvalue 1
maxvalue 9999999999999
start with 1000
increment by 1;

create sequence SEQ_CERTIFICAT_COM
minvalue 1
maxvalue 9999999999999
start with 1000
increment by 1;

create sequence SEQ_CLADIRE
minvalue 1
maxvalue 9999999999999
start with 1031
increment by 1;

create sequence SEQ_CONTRACT
minvalue 1
maxvalue 9999999999999
start with 1076
increment by 1;

create sequence SEQ_CROTALIE
minvalue 1
maxvalue 9999999999999
start with 1054
increment by 1;

create sequence SEQ_CULTURA
minvalue 1
maxvalue 9999999999999
start with 1229
increment by 1;

create sequence SEQ_CULTURA_PROD
minvalue 1
maxvalue 9999999999999
start with 1352
increment by 1;

create sequence SEQ_DETINATOR_PF
minvalue 1
maxvalue 9999999999999
start with 1163
increment by 1;

create sequence SEQ_DETINATOR_PJ
minvalue 1
maxvalue 9999999999999
start with 1021
increment by 1;

create sequence SEQ_DOC_CERERE
minvalue 1
maxvalue 9999999999999
start with 1000
increment by 1;

create sequence SEQ_FLUX_CERERE
minvalue 1
maxvalue 9999999999999
start with 1000
increment by 1;

create sequence SEQ_FLUX_REGISTRU
minvalue 1
maxvalue 9999999999999
start with 3681
increment by 1;

create sequence SEQ_FRUCT_PROD
minvalue 1
maxvalue 9999999999999
start with 1160
increment by 1;

create sequence SEQ_GEOLOCATOR_ADRESA
minvalue 1
maxvalue 9999999999999
start with 1000
increment by 1;

create sequence SEQ_GEOMETRIE_CLADIRE
minvalue 1
maxvalue 9999999999999
start with 1000
increment by 1;

create sequence SEQ_GEOMETRIE_PARCELA_TEREN
minvalue 1
maxvalue 9999999999999
start with 1000
increment by 1;

create sequence SEQ_GOSPODARIE
minvalue 1
maxvalue 9999999999999
start with 1197
increment by 1;

create sequence SEQ_INVENTAR_GOSP_UAT
minvalue 1
maxvalue 9999999999999
start with 1010
increment by 1;

create sequence SEQ_MEMBRU_PF
minvalue 1
maxvalue 9999999999999
start with 1204
increment by 1;

create sequence SEQ_MENTIUNE_CERERE_SUC
minvalue 1
maxvalue 9999999999999
start with 1056
increment by 1;


create sequence SEQ_MENTIUNE_SPECIALA
minvalue 1
maxvalue 9999999999999
start with 1021
increment by 1;


create sequence SEQ_NOM_CATEGORIE_FOLOSINTA
minvalue 1
maxvalue 9999999999999
start with 1003
increment by 1;

create sequence SEQ_NOM_DESTINATIE_CLADIRE
minvalue 1
maxvalue 9999999999999
start with 1004
increment by 1;

create sequence SEQ_NOM_JUDET
minvalue 1
maxvalue 9999999999999
start with 1004
increment by 1;

create sequence SEQ_NOM_LEGATURA_RUDENIE
minvalue 1
maxvalue 9999999999999
start with 1004
increment by 1;

create sequence SEQ_NOM_LOCALITATE
minvalue 1
maxvalue 9999999999999
start with 100003
increment by 1;

create sequence SEQ_NOM_MODALITATE_DETINERE
minvalue 1
maxvalue 9999999999999
start with 1002
increment by 1;

create sequence SEQ_NOM_PLANTA_CULTURA
minvalue 1
maxvalue 9999999999999
start with 1003
increment by 1;

create sequence SEQ_NOM_POM_ARBUST
minvalue 1
maxvalue 9999999999999
start with 1004
increment by 1;

create sequence SEQ_NOM_SPECIE_ANIMAL
minvalue 1
maxvalue 9999999999999
start with 1002
increment by 1;


create sequence SEQ_NOM_TARA
minvalue 1
maxvalue 9999999999999
start with 1001
increment by 1;

create sequence SEQ_NOM_TIP_ACT
minvalue 1
maxvalue 9999999999999
start with 1001
increment by 1;

create sequence SEQ_NOM_TIP_CLADIRE
minvalue 1
maxvalue 9999999999999
start with 1003
increment by 1;

create sequence SEQ_NOM_TIP_DETINATOR
minvalue 1
maxvalue 9999999999999
start with 1001
increment by 1;

create sequence SEQ_NOM_TIP_EXPLOATATIE
minvalue 1
maxvalue 9999999999999
start with 1004
increment by 1;

create sequence SEQ_NOM_TIP_LOCALIZARE
minvalue 1
maxvalue 9999999999999
start with 1001
increment by 1;

create sequence SEQ_NOM_UAT
minvalue 1
maxvalue 9999999999999
start with 10000
increment by 1;

create sequence SEQ_OPERATION
minvalue 1
maxvalue 9999999999999
start with 42567
increment by 1;

create sequence SEQ_PARCELA_LOCALIZARE
minvalue 1
maxvalue 9999999999999
start with 1058
increment by 1;

create sequence SEQ_PARCELA_TEREN
minvalue 1
maxvalue 9999999999999
start with 1031
increment by 1;

create sequence SEQ_PERSOANA_FIZICA
minvalue 1
maxvalue 9999999999999
start with 1105
increment by 1;

create sequence SEQ_PERSOANA_PREEMPTIUNE
minvalue 1
maxvalue 9999999999999
start with 1138
increment by 1;

create sequence SEQ_PERSOANA_RC
minvalue 1
maxvalue 9999999999999
start with 1014
increment by 1;

create sequence SEQ_PLANTATIE
minvalue 1
maxvalue 9999999999999
start with 1290
increment by 1;

create sequence SEQ_PLANTATIE_PROD
minvalue 1
maxvalue 9999999999999
start with 1063
increment by 1;

create sequence SEQ_POM_RAZLET
minvalue 1
maxvalue 9999999999999
start with 1041
increment by 1;

create sequence SEQ_PREEMPTIUNE
minvalue 1
maxvalue 9999999999999
start with 1071
increment by 1;

create sequence SEQ_PROPRIETAR_PARCELA
minvalue 1
maxvalue 9999999999999
start with 1028
increment by 1;

create sequence SEQ_REGISTRU
minvalue 1
maxvalue 9999999999999
start with 2381
increment by 1;

create sequence SEQ_SISTEM_TEHNIC
minvalue 1
maxvalue 9999999999999
start with 1170
increment by 1;

create sequence SEQ_SUBSTANTA_CHIMICA
minvalue 1
maxvalue 9999999999999
start with 1078
increment by 1;

create sequence SEQ_SUCCESIBIL
minvalue 1
maxvalue 9999999999999
start with 1055
increment by 1;

create sequence SEQ_SUPRAFATA_CATEGORIE
minvalue 1
maxvalue 9999999999999
start with 1132
increment by 1;

create sequence SEQ_SUPRAFATA_UTILIZARE
minvalue 1
maxvalue 9999999999999
start with 1255
increment by 1;

create sequence SEQ_TEREN_IRIGAT
minvalue 1
maxvalue 9999999999999
start with 1095
increment by 1;
