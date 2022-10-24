package ro.uti.ran.core.exception.codes;

import ro.uti.ran.core.exception.base.MessageHolder;
import ro.uti.ran.core.exception.base.MessageProvider;
import ro.uti.ran.core.exception.hint.DateRegistruValidationHints;

/**
 * Created by Dan on 16-Nov-15.
 */
public enum DateRegistruValidationCodes implements MessageProvider {

    GOSPODARIE_NOT_FOUND(1, "Gospodarie inexistenta cu identificator '%2$s' si cod siruta UAT '%1$s'.", DateRegistruValidationHints.GOSPODARIE_NOT_FOUND_HINT),
    UAT_JUDET_NECORESPUNZATOR(2, "UAT cu cod siruta '%s' nu apartine judetului cu cod siruta '%s'."),
    LOCALITATE_UAT_NECORESPUNZATOR(3, "Localitatea cu cod siruta '%s' nu apartine UAT cu cod siruta '%s'."),
    SECTIUNE_CAPITOL_DUPLICATA(4, "In capitol nu este unica sectiunea: '%s' cu '%s' = '%s'."),
    COD_CAPITOL_INVALID(5, "Valoare cod capitol '%s' invalida. Inlocuiti valoarea cu  '%s'."),
    UAT_DIFERIT(6, "Cod siruta = '%s' al UAT care transmite nu este identic cu cod siruta = '%s' al UAT pentru care se inregistreaza datele."),
    ELEMENTE_JURIDICE_NECORESPUNZATOR(7, "Campul 'elementeJuridice' se completeaza (obligatoriu) doar pentru tip detinator '%s' si tip exploatatie '%s'"),
    COD_NOMENCLATOR_INVALID(8, "Valoarea '%s' pentru codNomenclator e invalida pentru '%s' cu codRand '%s'"),
    NOMENCLATOR_RECORD_NOT_FOUND_AT_DATE(9, "'%1$s' cu '%2$s' = '%3$s' nu exista in sistem la data %4$td-%4$tm-%4$tY."),
    NOMENCLATOR_RECORD_NOT_FOUND(10, "'%s' cu '%s' = '%s' nu exista in sistem."),
    ATESTAT_DUBLICAT(11, "In capitol nu este unica sectiunea 'atestat_producator' cu 'Serie numar' = '%s'."),
    MENTIUNE_CERERE_SUC_DUBLICAT(12, "In capitol nu este unica sectiunea 'cerere_sau_sesizare' cu 'Data inregistrare' = %1$td-%1$tm-%1$tY si cu 'Nr' = '%2$s'."),
    MENTIUNE_CERERE_SUC_SUCCESIBIL_DUBLICAT(13, "In sectiunea 'cerere_sau_sesizare' cu 'Data inregistrare' = %1$td-%1$tm-%1$tY si cu 'Nr' = '%2$s' nu este unic 'succesibil' cu 'Nume' = '%3$s' si cu 'Prenume' = '%4$s'."),
    PREEMPTIUNE_DUBLICATE(14, "In capitol nu este unica sectiunea 'oferta_vanzare' cu 'Nr. Oferta Vanzare' = '%s'."),
    PREEMPTIUNE_PERSOANA_PREEMPTIUNE_DUBLICATE(15, "In sectiunea 'oferta_vanzare' cu 'Data Oferta Vanzare' = %1$td-%1$tm-%1$tY si cu 'Nr. Oferta Vanzare' = '%2$s' nu este unic 'cumparator/preemptor/vanzator' cu 'CNP/CUI' = '%3$s'. "),
    CATEGORIE_ANIMAL_CROTALIE_DUBLICATE(16, "In sectiunea 'categorie_animale' cu 'Cod Rand' = '%s' nu este unic 'crotalie' cu 'Cod Identificare' = '%s'."),
    ATESTAT_ATESTAT_PRODUS_DUBLICATE(17, "In sectiunea 'atestat_producator' cu 'Data Eliberare' = %1$td-%1$tm-%1$tY si cu 'Serie Numar' = '%2$s' nu este unic 'produs' cu 'denumire' = '%3$s'."),
    ATESTAT_ATESTAT_VIZA_DUBLICATE(18, "In sectiunea 'atestat_producator' cu 'Data Eliberare' = %1$td-%1$tm-%1$tY si cu 'Serie Numar' = '%2$s' nu este unic 'viza' cu 'numarViza' = '%3$s'."),
    ATESTAT_CERTIFICAT_COM_DUBLICATE(19, "In sectiunea 'atestat_producator' cu 'Data Eliberare' = %1$td-%1$tm-%1$tY si cu 'Serie Numar' = '%2$s' nu este unic  'certificatComercializare' cu 'Serie' = '%3$s' si cu 'Data Eliberare' = %4$td-%4$tm-%4$tY."),
    PARCELA_TEREN_LOCALIZARE_DUBLICATE(20, "In sectiunea 'identificare_teren' cu 'Cod Rand' = '%s' nu este unic 'localizare' cu 'codTip' = '%s'."),
    NOM_CULTURA_RECORD_NOT_FOUND_AT_DATE(21, "'%1$s' cu '%2$s' = '%3$s' si cu '%4$s' = '%5$s'  nu exista in sistem la data %6$td-%6$tm-%6$tY pentru capitolul '%7$s'."),
    NOMENCLATOR_IS_FORMULA(22, "'%s' cu '%s' = '%s' nu este acceptat de sistem. Valoarea respectiva se calculeaza de catre sistem, nu se transmite."),
    DETINATOR_PF_NOT_FOUND(23, "CAP1 se accepta numai daca anterior s-a facut o transmisie cu CAP0_12 pentru gospodaria cu UAT_COD_SIRUTA = '%s' si cu identificator = '%s'."),
    COD_RAND_INVALID(24, "Nu s-a gasit o inregistrare valida in '%1$s' pentru COD= '%2s4' si COD_RAND='%3$s' la data= %4$td-%4$tm-%4$tY."),
    REFERINTA_GEO_XML_INVALID(25, "'referintaGeoXml' este invalida: '%s'"),
    SECTIUNI_NEDEFINITE(26, "Nu au fost definite sectiuni pentru capitolul '%s'"),
    UAT_ADRESA_GOSPODARIE_INVALID(27, "Cod siruta UAT '%s' din adresa gospodarie nu coincide cu cod siruta UAT '%s' din header."),
    INDICATIV_HEADER_BODY_NECORESPUNZATOR(28, "Continut 'body' necorespunzator cu 'indicativ' '%s' din header."),
    DEZ_RE_ACTIVARE_CODCAPITOL_NECORESPUNZATOR(29, "Campul 'codCapitol' din elementul '%s' poate fi 'CAP0_12' sau 'CAP0_34'"),
    TOTAL_NEGATIV(30, "Totalul '%s' calculat de sistem, pe baza datelor trimise, pentru valoarea '%s' este negativ."),
    ANULARE_CAP13(31, "CAP13 nu suporta anulare la nivel de sectiune. Se permite doar anulare la nivel de capitol"),
    GOSPODAR_INITIALA_TATA(32, "Campul 'initialaTata' este obligatoriu pentru 'gospodar'"),
    PRODUCTIE_MEDIE_INVALIDA(32, "Se incearca trimiterea unei productii medii pentru un rand (cod='%s' si codRand='%s') care nu permite acest lucru pentru capitolul '%s'!"),
    PRODUCTIE_TOTALA_INVALIDA(33, "Se incearca trimiterea unei productii totale pentru un rand (cod='%s' si codRand='%s') care nu permite acest lucru pentru capitolul '%s'!"),
    PRODUCTIE_MEDIE_OBLIGATORIE(34, "Se incearca completarea unui rand (cod='%s' si codRand='%s') pentru capitolul '%s', dar productia medie nu este specificata!"),
    PRODUCTIE_TOTALA_OBLIGATORIE(35, "Se incearca completarea unui rand (cod='%s' si codRand='%s') pentru capitolul '%s', dar productia totala nu este specificata!"),
    COD_RAND_PRODUCTIE_INVALID(36, "Nu s-a gasit o inregistrare valida in '%1$s' pentru COD= '%2$s' si COD_RAND='%3$s' la data= %4$td-%4$tm-%4$tY cu IS_PROD=1."),
    RESTRICTIE_ADRESA_RENNS(37, "In sectiunea '%s' exista o adresa (din RO) care nu are completata campul 'cua'!"),
    RESTRICTIE_TOTAL_UAT_GOSPODARIE(38, "Numarul total de gospodarii existente in UAT nu a fost completat pentru anul '%s'!"),
    RESTRICTIE_TOTAL_UAT_DECLARATIE(39, "Numarul total de declaratii pe gospodarie depuse la nivel de UAT este mai mare decat numarul total de gospodarii existente in UAT  pentru anul '%s'!"),
    FORMAORGANIZARERC_NECORESPUNZATOR_012(40, "In 'elementeJuridice' 'formaOrganizareRC' se completeaza doar cu 'PFA', 'II', 'IF'! Valoarea transmisa este '%s'."),
    FORMAORGANIZARERC_NECORESPUNZATOR_034(41, "In 'persoanaJuridica' 'formaOrganizareRC' nu se completeaza cu 'PFA', 'II', 'IF'! Valoarea transmisa este '%s'."),
    SECTIUNE_NU_EXISTA(42, "In capitolul transmis nu exista sectiunea: '%s' cu '%s' = '%s' si denumire = '%s'!"),
    TOTAL_CAMP_RAND_NECORESPUNZATOR(43, "Valoarea '%s', exprimata in '%s', nu corespunde cu valoarea calculata '%s' pentru campul '%s' din sectiunea: '%s' cu '%s' = '%s' si denumire = '%s'!"),
    SECTIUNE_IS_FORMULA_0_NU_EXISTA(44, "In capitol exista doar subtotaluri/totaluri fara elemente care sa le compuna!"),
    CNP_INVALID(45, "Valoarea '%s' pentru campul 'cnp' este invalida!"),
    GENERIC_FIELD_MANDATORY(46, "Se incearca completarea unui rand (cod='%s' si codRand='%s') pentru capitolul '%s', dar '%s' nu este specificat!"),
    GENERIC_FIELD_INVALID(47, "Se incearca transmiterea sectiunii '%s' pentru un rand (cod='%s' si codRand='%s') care nu permite acest lucru pentru capitolul '%s'!"),
    NOMENCLATOR_RECORD_COD_AND_CODRAND_NOT_FOUND_AT_DATE(48, "'%1$s' cu codNomenclator = '%3$s' si codRand = '%2$s' nu exista in sistem la data %4$td-%4$tm-%4$tY."),
    CAP0_XX_UPDATE_CAP0_YY(49, "Aceasta gospodarie a fost adaugata prin transmiterea unui capitol de tip '%s'. Nu poate fi '%s' prin transmiterea unui capitol de tip '%s'."),
    GOSPODARIE_DEZACTIVATA(50, "Aceasta gospodarie este dezactivata. Se accepta doar transmisii cu indicativ '%s'!"),
    GOSPODARIA_ARE_CAPITOLE(51, "Aceasta gospodarie are capitole transmise anterior neanulate!", DateRegistruValidationHints.GOSPODARIA_ARE_CAPITOLE_HINT),
    CNP_CAP012_CAP1(52, "Cnp gospodar '%s' transmis prin cap0_12 nu coincide cu cnp cap gospodarie '%s' transmis prin cap1."),
    CAP_GOSPODARIE_OBLIGATORIU(53, "Cap gospodarie este obligatoriu de transmis!"),
    TOTAL_2a_RAND_NECORESPUNZATOR(54, "Valorile, exprimate in '%s', 'totalARI+totalHA'= '%s' si 'altelocARI + altelocHA + localARI + localHA' = '%s' nu coincid  in sectiunea: 'categorie_teren' cu 'COD_RAND' = '%s' si denumire = '%s'!"),
    AN_RAPORTARE_VS_AN_TRANSMISIE(55, "'anRaportare' este '%s'. Se poate transmite doar din anul curent si din anii anteriori."),
    NIF_CAP012_CAP1(56, "Nif gospodar '%s' transmis prin cap0_12 nu coincide cu nif cap gospodarie '%s' transmis prin cap1."),
    ANULARE_CAP_AN(57, "Pentru anulare capitol '%s' este necesar an raportare."),
    ANULARE_CAP_SEMESTRU_AN(58, "Pentru anulare capitol '%s' sunt necesari ca parametrii: an raportare, semestru raportare."),
    TOTAL_10b_HA_RAND_NECORESPUNZATOR(59, "Valorile 'nrHAazotoase+nrHAfosfatice+nrHApotasice'= '%s' si 'totalHA' = '%s' nu coincid  in sectiunea: 'culturi_ingrasaminte_chimice' cu 'COD_RAND' = '%s' si denumire = '%s'!"),
    TOTAL_10b_KG_RAND_NECORESPUNZATOR(60, "Valorile 'nrKGazotoase+nrKGfosfatice+nrKGpotasice'= '%s' si 'totalKG' = '%s' nu coincid  in sectiunea: 'culturi_ingrasaminte_chimice' cu 'COD_RAND' = '%s' si denumire = '%s'!"),
    AN_RAPORTARE_VS_DATA_LIMITA_PARAM(61, "'anRaportare' este '%s'.Se poate transmite doar din anul curent si din anul anterior. Din anul anterior se poate transmite numai pana la data '%s'"),
    EROARE_VALIDARE_GEOMETRIE_UAT_LIMIT(62, "Geometria aplicata nu se incadreaza in limita UAT cod siruta '%s'"),
    PROPRIETAR_PARCELA_OBLIGATORIU(63, "Sectiunea 'identificare_teren' cu denumire='%s' si codRand='%s' nu are completat 'proprietar'!"),
    PROPRIETAR_PARCELA_NU_ESTE_MEMBRU_GOSPODARIE(64, "'Proprietar' cu CNP/NIF = '%s' avand nume = '%s', initiala tata = '%s' si prenume = '%s'  din sectiunea 'identificare_teren' cu denumire='%s' si codRand='%s' nu face parte din membri declarati ai gospodariei!"),
    EROARE_CONSISTENTA_GEOMETRIE(65, "Geometria aplicata nu este valida pentru a fi acceptata de sistem. Motiv: '%s'"),
    GEOMETRIE_CULTURA_RAND_TOTAL(66, "Nu se accepta geometrie pentru randurile de tip total(ex: '%1$s' cu 'codNomenclator' = '%2$s' si cu 'codRand' = '%3$s')"),
    GEOMETRIE_CAP3(67, "Se accepta geometrie doar pentru randurile care nu sunt de tip total cu semnificatia 'terenuri primite'. Randul cu 'codNomenclator' = '%1$s' si cu 'codRand' = '%2$s' nu indeplineste acest criteriu."),
    RESTRICTIE_ADRESA_CUA_AND_GEOMETRIE(68, "In sectiunea '%s' exista o adresa (din RO) care are completate ambele campuri 'cua' si 'referintaGeoXml'!", DateRegistruValidationHints.RESTRICTIE_ADRESA_CUA_AND_GEOMETRIE_HINT),
    PROD_MEDIE_RAND_NECORESPUNZATOR(69,"Valoarea '%1$s', exprimata in '%2$s',  trebuie sa fie cuprinsa intre  '%3$s' si '%4$s' pentru campul '%5$s' cu 'codRand' = '%6$s' si denumire = '%7$s'!"),
    VALOARE_RAND_NECORESPUNZATOR(70,"Valoarea '%1$s' nu este valida pentru elementul '%2$s'  din randul cu 'codRand' = '%3$s' si denumire = '%4$s'!"),
    CNP_NIF_DETINATOR_PF_UNIC(71,"In UAT cu cod siruta '%1$s' persoana cu CNP/NIF '%2$s' nu poate detine mai multe gospodarii ('%3$s')!"),
    CNP_NIF_MEMBRU_UNIC(72,"In UAT cu cod siruta '%1$s' persoana cu CNP/NIF '%2$s' nu poate fi membru in mai multe gospodarii ('%3$s')!"),
    CUI_DETINATOR_PJ_UNIC(73,"In UAT cu cod siruta '%1$s' persoana juridica avand CUI '%2$s' nu poate detine mai multe gospodarii ('%3$s')!"),
    ADEV_VANZARE_LIBERA(74,"Numar si data sunt obligatorii in 'Adeverinta de vanzare libera'!"),
    EROARE_VALIDARE_GEOMETRIE_TYPE(75,"Geometria trebuie sa fie de tip '%1$s'!"),
    EROARE_VALIDARE_GEOMETRIE_EPSG(76,"EPSG geometrie trebuie sa fie '%1$s'!"),
    EROARE_VALIDARE_GEOMETRIE_CONFIG(77,"Problema de configurare, contactati administratorul aplicatiei!"),
    AN_RAPORTARE_NEDEFINIT(78,"'anRaportare' este obligatoriu de transmis!"),
    AN_RAPORTARE_ERONAT(79,"Valoarea campului 'anRaportare' nu este valida!"),
    CONSTRANGERE_LUNGIME_CAMP_DEPASITA(80 , "Valoarea campului %s este prea lunga, ati introdus %s caractere, limita este de %s caractere"),
    NUMAR_AVIZ_CONSULTATIV_NEDEFINIT(81, "'nrAvizConsultativ' este obligatoriu de transmis!"),
    COD_RAND_ERONAT(82, "Codul de rand are o lungimea prea mare, limita este de 2 cifre!"),
    ANUL_TERMINARII_ERONAT(83, "Anul terminarii trebuie sa aiba 4 cifre!"),
    SUPRAFATA_SOL_ERONATA(84, "Suprafata sol are o lungimea prea mare, limita este de 5 cifre!");

    private MessageHolder provider;

    private DateRegistruValidationCodes(int secondaryCode, String message, String hint) {
        provider = new MessageHolder(secondaryCode, message, hint);
    }

    private DateRegistruValidationCodes(int secondaryCode, String message) {
        this(secondaryCode, message, null);
    }

    public MessageHolder getMessageHolder() {
        return provider;
    }
}
