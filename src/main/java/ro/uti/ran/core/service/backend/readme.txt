1 hectar (ha) = 100 ari = 10.000 mp;
1 arie = 100 mp

- adauga, update, anulare se face la nivel de sectiune

. Capitol_0_12  date_identificare_gospodarie + PF-gospodar;
- unicitate dupa NOM_UAT.COD_SIRUTA + GOSPODARIE.IDENTIFICATOR
v2
- corespondenta DB: GOSPODARIE, DETINATOR_PF, ADRESA
- nomenclatoare DB: NOM_UAT, NOM_TIP_DETINATOR,NOM_TIP_EXPLOATATIE, PERSOANA_FIZICA, NOM_TARA, NOM_TIP_ADRESA, NOM_JUDET
v3
- corespondenta DB:
- nomenclatoare DB:

. Capitol_0_34 date_identificare_gospodarie + PJ
- unicitate dupa NOM_UAT.COD_SIRUTA + GOSPODARIE.IDENTIFICATOR
v2
- corespondenta DB: DETINATOR_PJ, ADRESA
- nomenclatoare DB:
v3
- corespondenta DB:
- nomenclatoare DB:

. Capitol_1 are o LISTA de membru_gospodarie
- unicitate dupa MEMBRU_PF.CNP
v2
- corespondenta DB: MEMBRU_PF, DETINATOR_PF
- nomenclatoare DB: NOM_LEGATURA_RUDENIE
v3
- corespondenta DB:
- nomenclatoare DB:

. Capitol_2a are o LISTA de categorie_teren
v2
- unicitate dupa an + NOM_CATEGORIE_FOLOSINTA.COD_RAND
- corespondenta DB: SUPRAFATA_CATEGORIE
- nomenclatoare DB: NOM_CATEGORIE_FOLOSINTA
v3
- unicitate dupa an + CAP_CATEGORIE_FOLOSINTA.COD_RAND
- corespondenta DB: SUPRAFATA_CATEGORIE
- nomenclatoare DB: CAP_CATEGORIE_FOLOSINTA + FK_NOM_CAPITOL = 2a

. Capitol_2b are o LISTA de identificare_teren (parcela/tarla/sola)
v2
- unicitate dupa an + PARCELA_TEREN.COD_RAND
- corespondenta DB: PARCELA_TEREN, ACT_DETINERE, PARCELA_LOCALIZARE
- nomenclatoare DB: NOM_TIP_ACT_DETINERE,NOM_CATEGORIE_FOLOSINTA, NOM_MODALITATE_DETINERE, NOM_TIP_ACT_INSTRAINARE, PERSOANA_FIZICA
v3
- unicitate dupa an + PARCELA_TEREN.COD_RAND
- corespondenta DB: PARCELA_TEREN, ACT, ACT_DETINERE, PARCELA_LOCALIZARE
- nomenclatoare DB: NOM_TIP_ACT, NOM_MODALITATE_DETINERE, PERSOANA_FIZICA, CAP_CATEGORIE_FOLOSINTA + FK_NOM_CAPITOL = 2b

. Capitol_3 are o LISTA de mod_utilizare_suprafete_agricole
v2
- unicitate dupa an + NOM_MOD_UTILIZARE.COD_RAND
- corespondenta DB: SUPRAFATA_UTILIZARE
- nomenclatoare DB: NOM_MOD_UTILIZARE
v3
- unicitate dupa an + CAP_MOD_UTILIZARE.COD_RAND
- corespondenta DB: SUPRAFATA_UTILIZARE
- nomenclatoare DB: CAP_MOD_UTILIZARE + FK_NOM_CAPITOL = 3

. Capitol_4a
v2
- unicitate dupa cap + an + NOM_CULTURA.COD_RAND
- corespondenta DB: CULTURA
- nomenclatoare DB: NOM_CULTURA
v3
- unicitate dupa cap + an + CAP_CULTURA.COD_RAND
- corespondenta DB: CULTURA
- nomenclatoare DB: CAP_CULTURA + FK_NOM_CAPITOL = 4a

. Capitol_4a1
v2
- unicitate dupa cap + an + NOM_CULTURA.COD_RAND
- corespondenta DB: CULTURA
- nomenclatoare DB: NOM_CULTURA
v3
- unicitate dupa cap + an + CAP_CULTURA.COD_RAND
- corespondenta DB: CULTURA
- nomenclatoare DB: CAP_CULTURA + FK_NOM_CAPITOL = 4a1


. Capitol_4b1
v2
- unicitate dupa an + NOM_CULTURA_SPATIU_PROT.COD_RAND
- corespondenta DB: CULTURA_SPATIU_PROT
- nomenclatoare DB: NOM_TIP_SPATIU_PROT, NOM_CULTURA_SPATIU_PROT
v3
- unicitate dupa an + CAP_CULTURA.COD_RAND
- corespondenta DB: CULTURA
- nomenclatoare DB: NOM_TIP_SPATIU_PROT, CAP_CULTURA + FK_NOM_CAPITOL = 4b1 + FK_NOM_TIP_SPATIU_PROT = SERA

. Capitol_4b2
v2
- unicitate dupa an + NOM_CULTURA_SPATIU_PROT.COD_RAND
- corespondenta DB: CULTURA_SPATIU_PROT
- nomenclatoare DB: NOM_TIP_SPATIU_PROT, NOM_CULTURA_SPATIU_PROT
v3
- unicitate dupa an + CAP_CULTURA.COD_RAND
- corespondenta DB: CULTURA
- nomenclatoare DB: NOM_TIP_SPATIU_PROT, CAP_CULTURA + FK_NOM_CAPITOL = 4b2  + FK_NOM_TIP_SPATIU_PROT = SOLAR


. Capitol_4c
v2
- unicitate dupa cap + an + NOM_CULTURA.COD_RAND
- corespondenta DB: CULTURA
- nomenclatoare DB: NOM_CULTURA
v3
- unicitate dupa cap + an + CAP_CULTURA.COD_RAND
- corespondenta DB: CULTURA
- nomenclatoare DB: CAP_CULTURA + FK_NOM_CAPITOL = 4c

. Capitol_5a
v2
- unicitate dupa an + NOM_POM_RAZLET.COD_RAND
- corespondenta DB: POM_RAZLET
- nomenclatoare DB: NOM_POM_RAZLET
v3
- unicitate dupa an + CAP_POM_RAZLET.COD_RAND
- corespondenta DB: POM_RAZLET
- nomenclatoare DB: CAP_POM_RAZLET  + FK_NOM_CAPITOL = 5a


. Capitol_5b
v2
- unicitate dupa an + NOM_POM_PLANTATIE_POM.COD_RAND
- corespondenta DB: POM_PLANTATIE_POM
- nomenclatoare DB: NOM_POM_PLANTATIE_POM
v3
- unicitate dupa an + CAP_PLANTATIE.COD_RAND
- corespondenta DB: PLANTATIE
- nomenclatoare DB: CAP_PLANTATIE + FK_NOM_CAPITOL = 5b


. Capitol_5c
v2
- unicitate dupa an + NOM_POM_ALT_PLANTATIE_POM.COD_RAND
- corespondenta DB: POM_ALT_PLANTATIE_POM
- nomenclatoare DB: NOM_POM_ALT_PLANTATIE_POM
v3
- unicitate dupa an + CAP_PLANTATIE.COD_RAND
- corespondenta DB: PLANTATIE
- nomenclatoare DB: CAP_PLANTATIE + FK_NOM_CAPITOL = 5c


. Capitol_5d
v2
- unicitate dupa an + NOM_VIE_HAMEI.COD_RAND
- corespondenta DB: VIE_HAMEI
- nomenclatoare DB: NOM_VIE_HAMEI
v3
- unicitate dupa an + CAP_PLANTATIE.COD_RAND
- corespondenta DB: PLANTATIE
- nomenclatoare DB: CAP_PLANTATIE + FK_NOM_CAPITOL = 5d


. Capitol_6
v2
- unicitate dupa an + NOM_TEREN_IRIGAT.COD_RAND
- corespondenta DB: TEREN_IRIGAT
- nomenclatoare DB: NOM_TEREN_IRIGAT
v3
- unicitate dupa an + CAP_TEREN_IRIGAT.COD_RAND
- corespondenta DB: TEREN_IRIGAT
- nomenclatoare DB: CAP_TEREN_IRIGAT  + FK_NOM_CAPITOL = 6

. Capitol_7
v2
- unicitate dupa an + semestru + NOM_CATEGORIE_ANIMAL.COD_RAND
- corespondenta DB: CATEGORIE_ANIMAL, CROTALIE
- nomenclatoare DB: NOM_CATEGORIE_ANIMAL
v3
- unicitate dupa an + semestru + CAP_CATEGORIE_ANIMAL.COD_RAND
- corespondenta DB: CATEGORIE_ANIMAL, CROTALIE
- nomenclatoare DB: CAP_CATEGORIE_ANIMAL + FK_NOM_CAPITOL = 7

. Capitol_8
v2
- unicitate dupa an + semestru + NOM_CATEGORIE_ANIMAL_EVOL.COD_RAND
- corespondenta DB: CATEGORIE_ANIMAL_EVOL
- nomenclatoare DB: NOM_CATEGORIE_ANIMAL_EVOL
v3
- unicitate dupa an + semestru + CAP_CATEGORIE_ANIMAL.COD_RAND
- corespondenta DB: CATEGORIE_ANIMAL
- nomenclatoare DB: CAP_CATEGORIE_ANIMAL + FK_NOM_CAPITOL = 8

. Capitol_9
v2
- unicitate dupa an + NOM_SISTEM_TEHNIC.COD_RAND
- corespondenta DB: SISTEM_TEHNIC
- nomenclatoare DB: NOM_SISTEM_TEHNIC
v3
- unicitate dupa an + CAP_SISTEM_TEHNIC.COD_RAND
- corespondenta DB: SISTEM_TEHNIC
- nomenclatoare DB: CAP_SISTEM_TEHNIC + FK_NOM_CAPITOL = 9

. Capitol_10a
v2
- unicitate dupa an + NOM_UTILIZARE_INGRASAMANT.COD_RAND
- corespondenta DB: UTILIZARE_INGRASAMANT
- nomenclatoare DB: NOM_UTILIZARE_INGRASAMANT
v3
- unicitate dupa an + CAP_APLICARE_INGRASAMANT.COD_RAND
- corespondenta DB: APLICARE_INGRASAMANT
- nomenclatoare DB: CAP_APLICARE_INGRASAMANT + FK_NOM_CAPITOL = 10a

. Capitol_10b
v2
- unicitate dupa an + NOM_SUBSTANTA_CHIMICA.COD_RAND
- corespondenta DB: SUBSTANTA_CHIMICA
- nomenclatoare DB: NOM_SUBSTANTA_CHIMICA
v3
- unicitate dupa an + CAP_SUBSTANTA_CHIMICA.COD_RAND
- corespondenta DB: SUBSTANTA_CHIMICA
- nomenclatoare DB: CAP_SUBSTANTA_CHIMICA + FK_NOM_CAPITOL = 10b

. Capitol_11
v2
- unicitate dupa an + fk_gospodarie + identificator
- corespondenta DB: CLADIRE
- nomenclatoare DB: NOM_DESTINATIE_CLADIRE, NOM_TIP_CLADIRE, NOM_LOCALITATE
v3
- unicitate dupa an + fk_gospodarie + identificator
- corespondenta DB: CLADIRE, GEOMETRIE_CLADIRE
- nomenclatoare DB: NOM_DESTINATIE_CLADIRE, NOM_TIP_CLADIRE,

. Capitol_12
v2
- unicitate dupa: ATESTAT.SERIE_NUMAR_ATESTAT
- corespondenta DB: ATESTAT, ATESTAT_PRODUS, ATESTAT_VIZA, CERTIFICAT_COM
- nomenclatoare DB:
v3
- unicitate dupa: ATESTAT.SERIE_NUMAR_ATESTAT
- corespondenta DB: ATESTAT, ATESTAT_PRODUS, ATESTAT_VIZA, CERTIFICAT_COM, ACT
- nomenclatoare DB:

. Capitol_13
- unicitate dupa: MENTIUNE_CERERE_SUC.NR_INREGISTRARE + MENTIUNE_CERERE_SUC.DATA_INREGISTRARE
v2
- corespondenta DB: MENTIUNE_CERERE_SUC, SUCCESIBIL
- nomenclatoare DB: NOM_TARA, NOM_JUDET, NOM_UAT, NOM_LOCALITATE
v3
- corespondenta DB: MENTIUNE_CERERE_SUC, SUCCESIBIL, ADRESA, PERSOANA_FIZICA
- nomenclatoare DB: NOM_TARA, NOM_JUDET, NOM_UAT, NOM_LOCALITATE

. Capitol_14
- unicitate dupa: PREEMPTIUNE.NR_OFERTA_VANZARE
v2
- corespondenta DB: PREEMPTIUNE, PERSOANA_PREEMPTIUNE
- nomenclatoare DB: NOM_TIP_REL_PREEMPTIUNE
v3
- corespondenta DB: PREEMPTIUNE, ACT, PERSOANA_PREEMPTIUNE, PERSOANA_FIZICA, PERSOANA_RC
- nomenclatoare DB:NOM_TIP_REL_PREEMPTIUNE

. Capitol_15a
- unicitate dupa: CONTRACT.NR_CRT
v2
- corespondenta DB: CONTRACT
- nomenclatoare DB: NOM_TIP_CONTRACT, NOM_CATEGORIE_FOLOSINTA
v3
- corespondenta DB: CONTRACT, PERSOANA_FIZICA, PERSOANA_RC
- nomenclatoare DB: NOM_TIP_CONTRACT, NOM_CATEGORIE_FOLOSINTA

. Capitol_15b
- unicitate dupa: CONTRACT.NR_CRT
v2
- corespondenta DB: CONTRACT
- nomenclatoare DB: NOM_TIP_CONTRACT, NOM_CATEGORIE_FOLOSINTA
v3
- corespondenta DB: CONTRACT, PERSOANA_FIZICA, PERSOANA_RC
- nomenclatoare DB: NOM_TIP_CONTRACT, NOM_CATEGORIE_FOLOSINTA

. Capitol_16
v2
- corespondenta DB: MENTIUNE_SPECIALA
- nomenclatoare DB:
v3
- corespondenta DB:
- nomenclatoare DB:




