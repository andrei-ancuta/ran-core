DROP TABLE IF EXISTS APP_COMPONENTA;

create table APP_COMPONENTA
(
  id_app_componenta NUMBER not null,
  cod               VARCHAR2(10 ) not null,
  denumire          VARCHAR2(100 ) not null,
  descriere         VARCHAR2(500 ),
  primary key (ID_APP_COMPONENTA),
  unique UK_APP_COMPONENTA_COD (COD)
);

DROP TABLE IF EXISTS APP_CONTEXT;
create table APP_CONTEXT
(
  id_app_context NUMBER not null,
  cod            VARCHAR2(10 ) not null,
  denumire       VARCHAR2(100 ) not null,
  descriere      VARCHAR2(500 ),
  primary key (ID_APP_CONTEXT),
  unique  UK_APP_CONTEXT_COD (COD)
);

DROP TABLE IF EXISTS APP_UTILIZATOR;

create table APP_UTILIZATOR
(
  id_app_utilizator NUMBER not null,
  nume_utilizator   VARCHAR2(50 ) not null,
  nume              VARCHAR2(50 ),
  prenume           VARCHAR2(50 ),
  email             VARCHAR2(50 ),
  cnp               VARCHAR2(13 ),
  observatii        VARCHAR2(500 ),
  is_activ          NUMBER(1) not null,
  nif               VARCHAR2(13 ),
  primary key (ID_APP_UTILIZATOR),
  unique UK_APP_UTILIZATOR_NUME_UTILIZ (NUME_UTILIZATOR)
);

DROP TABLE IF EXISTS APP_SESIUNE;
create table APP_SESIUNE
(
  id_app_sesiune    NUMBER not null,
  fk_app_utilizator NUMBER not null,
  fk_app_context    NUMBER not null,
  uid_sesiune_http  VARCHAR2(500 ) not null,
  adresa_ip         VARCHAR2(50 ) not null,
  data_start        DATE not null,
  data_stop         DATE,
  primary key (ID_APP_SESIUNE),
);

create index IDX_APP_SESIUNE_FK_APP_CONTEXT on APP_SESIUNE(FK_APP_CONTEXT);
create index IDX_APP_SESIUNE_FK_APP_UTILIZ on APP_SESIUNE (FK_APP_UTILIZATOR);
create index IDX_APP_SES_DATA_START_STOP on APP_SESIUNE (DATA_START, DATA_STOP);

alter table APP_SESIUNE add foreign key (FK_APP_CONTEXT) references APP_CONTEXT (ID_APP_CONTEXT);
alter table APP_SESIUNE add foreign key (FK_APP_UTILIZATOR) references APP_UTILIZATOR (ID_APP_UTILIZATOR);

DROP TABLE IF EXISTS  APP_TIP_OPERATIE;

create table APP_TIP_OPERATIE
(
  id_app_tip_operatie NUMBER not null,
  cod                 VARCHAR2(10 ) not null,
  denumire            VARCHAR2(100 ) not null,
  descriere           VARCHAR2(500 ),
  primary key (ID_APP_TIP_OPERATIE),
  unique UK_APP_TIP_OPERATIE_COD (COD)
);

DROP TABLE IF EXISTS APP_OPERATIE_SESIUNE;

create table APP_OPERATIE_SESIUNE
(
  id_app_operatie_sesiune NUMBER not null,
  fk_app_sesiune          NUMBER not null,
  fk_app_tip_operatie     NUMBER not null,
  data_operatie           DATE not null,
  descriere               VARCHAR2(500 ) not null,
  descriere_complet       CLOB,
  primary key (ID_APP_OPERATIE_SESIUNE)
);
create index IDX_APP_OP_SESIUNE_DATA_OP on APP_OPERATIE_SESIUNE (DATA_OPERATIE);
create index IDX_APP_OP_SESIUNE_FK_APP_SES on APP_OPERATIE_SESIUNE (FK_APP_SESIUNE);
create index IDX_APP_OP_SES_FK_APP_TIP_SES on APP_OPERATIE_SESIUNE (FK_APP_TIP_OPERATIE);
alter table APP_OPERATIE_SESIUNE add foreign key (FK_APP_SESIUNE) references APP_SESIUNE (ID_APP_SESIUNE);
alter table APP_OPERATIE_SESIUNE add foreign key (FK_APP_TIP_OPERATIE) references APP_TIP_OPERATIE (ID_APP_TIP_OPERATIE);

DROP TABLE IF EXISTS  APP_PARAMETRU;

create table APP_PARAMETRU
(
  id_app_parametru  NUMBER not null,
  cod               VARCHAR2(10 ) not null,
  denumire          VARCHAR2(100 ) not null,
  descriere         VARCHAR2(500 ),
  valoare           VARCHAR2(100 ) not null,
  valoare_implicita VARCHAR2(100 ) not null,
  primary key (ID_APP_PARAMETRU),
  unique UK_APP_PARAMETRU_COD (COD)
);

DROP TABLE IF EXISTS APP_ROL;

create table APP_ROL
(
  id_app_rol        NUMBER not null,
  fk_app_context    NUMBER not null,
  fk_app_componenta NUMBER not null,
  cod               VARCHAR2(10 ) not null,
  denumire          VARCHAR2(100 ) not null,
  descriere         VARCHAR2(500 ),
  is_activ          NUMBER(1) default 1 not null,
  primary key (ID_APP_ROL),
  unique UK_APP_ROL_COD (COD)
);

create index IDX_APP_ROL_FK_APP_COMPONENTA on APP_ROL (FK_APP_COMPONENTA);
create index IDX_APP_ROL_FK_APP_CONTEXT on APP_ROL (FK_APP_CONTEXT);
alter table APP_ROL add foreign key (FK_APP_COMPONENTA) references APP_COMPONENTA (ID_APP_COMPONENTA);
alter table APP_ROL add foreign key (FK_APP_CONTEXT) references APP_CONTEXT (ID_APP_CONTEXT);

DROP TABLE IF EXISTS  NOM_TIP_INSTITUTIE;

create table NOM_TIP_INSTITUTIE
(
  id_nom_tip_institutie NUMBER not null,
  cod                   VARCHAR2(10 ) not null,
  denumire              VARCHAR2(100 ) not null,
  primary key (ID_NOM_TIP_INSTITUTIE),
  unique UK_NOM_TIP_INSTITUTIE_COD (COD)
);

DROP TABLE IF EXISTS NOM_INSTITUTIE;

create table NOM_INSTITUTIE
(
  id_nom_institutie     NUMBER not null,
  cod                   VARCHAR2(10 ) not null,
  denumire              VARCHAR2(100 ) not null,
  fk_nom_tip_institutie NUMBER not null,
  primary key (ID_NOM_INSTITUTIE),
  unique UK_NOM_INSTITUTIE_COD (COD)
);
create index IDX_NOM_INST_FK_NOM_TIP_INST on NOM_INSTITUTIE (FK_NOM_TIP_INSTITUTIE);
alter table NOM_INSTITUTIE add foreign key (FK_NOM_TIP_INSTITUTIE) references NOM_TIP_INSTITUTIE (ID_NOM_TIP_INSTITUTIE);

DROP TABLE IF EXISTS APP_ROL_UTILIZATOR;
create table APP_ROL_UTILIZATOR
(
  id_app_rol_utilizator NUMBER not null,
  fk_app_rol            NUMBER not null,
  fk_app_utilizator     NUMBER not null,
  fk_nom_institutie     NUMBER,
  fk_nom_judet          NUMBER,
  fk_nom_uat            NUMBER,
  primary key (ID_APP_ROL_UTILIZATOR)
);
create index IDX_APP_ROL_UTILIZ_FK_APP_ROL on APP_ROL_UTILIZATOR (FK_APP_ROL);
create index IDX_APP_ROL_UTILIZ_FK_APP_UTIL on APP_ROL_UTILIZATOR (FK_APP_UTILIZATOR);
create index IDX_APP_ROL_UTILIZ_FK_NOM_INST on APP_ROL_UTILIZATOR (FK_NOM_INSTITUTIE);
create index IDX_APP_ROL_UTILIZ_FK_NOM_JUD on APP_ROL_UTILIZATOR (FK_NOM_JUDET);
create index IDX_APP_ROL_UTILIZ_FK_NOM_UAT on APP_ROL_UTILIZATOR (FK_NOM_UAT);
--alter table APP_ROL_UTILIZATOR add foreign key (FK_NOM_JUDET)references RAN_REGISTRU_DEV.NOM_JUDET (ID_NOM_JUDET);
alter table APP_ROL_UTILIZATOR add foreign key (FK_APP_ROL) references APP_ROL (ID_APP_ROL);
alter table APP_ROL_UTILIZATOR add foreign key (FK_APP_UTILIZATOR) references APP_UTILIZATOR (ID_APP_UTILIZATOR);
alter table APP_ROL_UTILIZATOR add foreign key (FK_NOM_INSTITUTIE) references NOM_INSTITUTIE (ID_NOM_INSTITUTIE);
--alter table APP_ROL_UTILIZATOR add foreign key (FK_NOM_UAT) references RAN_REGISTRU_DEV.NOM_UAT (ID_NOM_UAT);

DROP TABLE IF EXISTS APP_SISTEM;

create table APP_SISTEM
(
  id_app_sistem         NUMBER not null,
  fk_nom_institutie     NUMBER not null,
  fk_nom_judet          NUMBER,
  fk_nom_uat            NUMBER,
  cod                   VARCHAR2(10 ),
  denumire              VARCHAR2(100 ) not null,
  cod_licenta           VARCHAR2(60 ) not null,
  data_generare_licenta DATE not null,
  is_activ              NUMBER(1) not null,
  primary key (ID_APP_SISTEM),
  unique UK_APP_SISTEM_COD (COD),
  check (IS_ACTIV IN (0,1))
);

create index IDX_APP_SISTEM_COD_LICENTA on APP_SISTEM (COD_LICENTA);
create index IDX_APP_SISTEM_FK_NOM_INSTIT on APP_SISTEM (FK_NOM_INSTITUTIE);
create index IDX_APP_SISTEM_FK_NOM_JUDET on APP_SISTEM (FK_NOM_JUDET);
create index IDX_APP_SISTEM_FK_NOM_UAT on APP_SISTEM (FK_NOM_UAT);
--create bitmap index IDX_APP_SISTEM_IS_ACTIV on APP_SISTEM (IS_ACTIV);

alter table APP_SISTEM add foreign key (FK_NOM_INSTITUTIE) references NOM_INSTITUTIE (ID_NOM_INSTITUTIE);
--alter table APP_SISTEM add foreign key (FK_NOM_JUDET) references RAN_REGISTRU_DEV.NOM_JUDET (ID_NOM_JUDET);
--alter table APP_SISTEM add foreign key (FK_NOM_UAT) references RAN_REGISTRU_DEV.NOM_UAT (ID_NOM_UAT);

DROP TABLE IF EXISTS APP_UTILIZATOR_GOSPODARIE;

create table APP_UTILIZATOR_GOSPODARIE
(
  id_app_utilizator_gospodarie NUMBER not null,
  fk_app_utilizator            NUMBER not null,
  fk_gospodarie                NUMBER not null,
  primary key (ID_APP_UTILIZATOR_GOSPODARIE)
);
alter table APP_UTILIZATOR_GOSPODARIE  add foreign key (FK_APP_UTILIZATOR) references APP_UTILIZATOR (ID_APP_UTILIZATOR);

DROP TABLE IF EXISTS  NOM_STARE_REGISTRU;
create table NOM_STARE_REGISTRU
(
  id_nom_stare_registru NUMBER not null,
  cod                   VARCHAR2(10 ) not null,
  denumire              VARCHAR2(100 ) not null,
  primary key (ID_NOM_STARE_REGISTRU),
  unique UK_NOM_STARE_REGISTRU_COD (COD)
);

DROP TABLE IF EXISTS NOM_STARE_INCARCARE;

create table NOM_STARE_INCARCARE
(
  id_nom_stare_incarcare NUMBER not null,
  cod                    VARCHAR2(10 ) not null,
  denumire               VARCHAR2(100 ) not null,
  primary key (ID_NOM_STARE_INCARCARE),
  unique UK_NOM_STARE_INCARCARE_COD (COD)
);

DROP TABLE IF EXISTS INCARCARE;

create table INCARCARE
(
  id_incarcare           NUMBER not null,
  fk_app_utilizator      NUMBER not null,
  fk_nom_uat             NUMBER not null,
  fk_nom_stare_incarcare NUMBER not null,
  index_incarcare        VARCHAR2(36 ) not null,
  data_incarcare         DATE not null,
  denumire_fisier        VARCHAR2(100 ) not null,
  continut_fisier        BLOB,
  primary key (ID_INCARCARE)
);
create index IDX_INCARCARE_DATA_INCARCARE on INCARCARE (DATA_INCARCARE);
create index IDX_INCARCARE_FK_APP_UTILIZAT on INCARCARE (FK_APP_UTILIZATOR);
create index IDX_INCARCARE_FK_NOM_ST_INCARC on INCARCARE (FK_NOM_STARE_INCARCARE);
create index IDX_INCARCARE_FK_NOM_UAT on INCARCARE (FK_NOM_UAT);
create index IDX_INCARCARE_INDEX_INCARCARE on INCARCARE (INDEX_INCARCARE);
alter table INCARCARE add foreign key (FK_APP_UTILIZATOR)references APP_UTILIZATOR (ID_APP_UTILIZATOR);
alter table INCARCARE add foreign key (FK_NOM_STARE_INCARCARE) references NOM_STARE_INCARCARE (ID_NOM_STARE_INCARCARE);
--alter table INCARCARE add foreign key (FK_NOM_UAT) references RAN_REGISTRU_DEV.NOM_UAT (ID_NOM_UAT);

DROP TABLE IF EXISTS REGISTRU;

create table REGISTRU
(
  id_registru           NUMBER not null,
  fk_incarcare          NUMBER not null,
  fk_nom_stare_registru NUMBER not null,
  fk_nom_capitol        NUMBER,
  index_registru        VARCHAR2(36 ),
  data_registru         DATE,
  denumire_fisier       VARCHAR2(100 ) not null,
  continut_fisier       CLOB not null,
  recipisa              BLOB,
  is_recipisa_semnata   NUMBER(1) not null,
  primary key (ID_REGISTRU),
  check (IS_RECIPISA_SEMNATA IN (0,1))
);
create index IDX_REGISTRU_DATA_REGISTRU on REGISTRU (DATA_REGISTRU);
create index IDX_REGISTRU_FK_INCARCARE on REGISTRU (FK_INCARCARE);
create index IDX_REGISTRU_FK_NOM_CAPITOL on REGISTRU (FK_NOM_CAPITOL);
create index IDX_REGISTRU_FK_NOM_STARE_REG on REGISTRU (FK_NOM_STARE_REGISTRU);
create index IDX_REGISTRU_INDEX_REGISTRU on REGISTRU (INDEX_REGISTRU);
--create bitmap index IDX_REGISTRU_IS_RECIPISA_SEMN on REGISTRU (IS_RECIPISA_SEMNATA);
--alter table REGISTRU add foreign key (FK_NOM_CAPITOL)references RAN_REGISTRU_DEV.NOM_CAPITOL (ID_NOM_CAPITOL);
alter table REGISTRU add foreign key (FK_INCARCARE) references INCARCARE (ID_INCARCARE);
alter table REGISTRU add foreign key (FK_NOM_STARE_REGISTRU) references NOM_STARE_REGISTRU (ID_NOM_STARE_REGISTRU);

  
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
create index IDX_FLUX_REGISTRU_DATA_STARE on FLUX_REGISTRU (DATA_STARE);
create index IDX_FLUX_REGISTRU_FK_REGISTRU on FLUX_REGISTRU (FK_REGISTRU);
create index IDX_FLUX_REGIST_FK_NOM_ST_REG on FLUX_REGISTRU (FK_NOM_STARE_REGISTRU);
alter table FLUX_REGISTRU add foreign key (FK_REGISTRU) references REGISTRU (ID_REGISTRU);
alter table FLUX_REGISTRU add foreign key (FK_NOM_STARE_REGISTRU) references NOM_STARE_REGISTRU (ID_NOM_STARE_REGISTRU);

DROP TABLE IF EXISTS UAT_CONFIG;

create table UAT_CONFIG
(
  id_uat_config             NUMBER not null,
  fk_nom_uat                NUMBER not null,
  is_notificare_rap         NUMBER(1) not null,
  fk_nom_sursa_registru     NUMBER,
  is_mod_transmitere_manual NUMBER(1) default 0 not null,
  primary key (ID_UAT_CONFIG),
  unique UK_UAT_CONFIG_FK_NOM_UAT (FK_NOM_UAT),
  check (IS_MOD_TRANSMITERE_MANUAL IN (0,1))
);

create index IDX_UAT_CONFIG_IS_MOD_TRAN_MAN on UAT_CONFIG (IS_MOD_TRANSMITERE_MANUAL);
--alter table UAT_CONFIG add foreign key (FK_NOM_SURSA_REGISTRU) references RAN_REGISTRU_DEV.NOM_SURSA_REGISTRU (ID_NOM_SURSA_REGISTRU);
--alter table UAT_CONFIG add foreign key (FK_NOM_UAT) references RAN_REGISTRU_DEV.NOM_UAT (ID_NOM_UAT);

create sequence SEQ_APP_OPERATIE_SESIUNE
minvalue 1
maxvalue 999999999999
start with 1193
increment by 1;

create sequence SEQ_APP_ROL_UTILIZATOR
minvalue 1
maxvalue 999999999999
start with 1209
increment by 1;

create sequence SEQ_APP_SESIUNE
minvalue 1
maxvalue 999999999999
start with 1259
increment by 1;

create sequence SEQ_APP_SISTEM
minvalue 1
maxvalue 999999999999
start with 1009
increment by 1;

create sequence SEQ_APP_UTILIZATOR
minvalue 1
maxvalue 999999999999
start with 1073
increment by 1;

create sequence SEQ_APP_UTILIZATOR_GOSPODARIE
minvalue 1
maxvalue 999999999999
start with 1000
increment by 1;

create sequence SEQ_FLUX_REGISTRU
minvalue 1
maxvalue 999999999999
start with 1197
increment by 1;

create sequence SEQ_INCARCARE
minvalue 1
maxvalue 999999999999
start with 1030
increment by 1;

create sequence SEQ_REGISTRU
minvalue 1
maxvalue 999999999999
start with 1148
increment by 1;

create sequence SEQ_UAT_CONFIG
minvalue 1
maxvalue 999999999999
start with 1005
increment by 1;
