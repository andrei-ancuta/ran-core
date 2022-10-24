package ro.uti.ran.core.audit;

import ro.uti.ran.core.audit.descriereOperatii.DescarcaNomenclatorOperatieSesiuneGenericBuilder;
import ro.uti.ran.core.audit.descriereOperatii.DescriereOperatieSesiuneGenericBuilder;

import static ro.uti.ran.core.audit.TipTranzactieEnum.TRANSACTION_UNIT_PORTAL;
import static ro.uti.ran.core.audit.TipTranzactieEnum.TRANSACTION_UNIT_REGISTRU;

/**
 * Created by Anastasia cea micuta on 1/17/2016.
 */
public enum AuditOpType {

    /*interfata utilizator anonim*/
    /*TBD*/LOGIN_USERNAME_PASSWORD("AUTH-PASS", "audit.autentificare_username_password.descriere_sumara", "audit.autentificare_username_password.descriere_completa"),
    /*TBD*/LOGIN_CERTIFICATE("AUTH-CERT", "audit.autentificare_username_password.descriere_sumara", "audit.autentificare_username_password.descriere_completa"),


    /*interfata utilizator autentificat*/
    /*N/A*/DESCARCARE_NOMENCLATOR("NOM-DOWN", DescarcaNomenclatorOperatieSesiuneGenericBuilder.class, "audit.decarcare_nom.descriere_sumara", "audit.decarcare_nom.descriere_completa"),
    /*TBD*/LOGOUT("LOGOUT", "audit.autentificare_username_password.descriere_sumara", "audit.autentificare_username_password.descriere_completa"),
    VERIFICARE_UTILIZATOR("CHECK-USER", "audit.autentificare_username_password.descriere_sumara", "audit.autentificare_username_password.descriere_completa"),
    SALVARE_UTILIZATOR("SAVE-USER", "audit.autentificare_username_password.descriere_sumara", "audit.autentificare_username_password.descriere_completa"),
    SALVARE_GOSPODAR("SAVE-GOSP", "audit.autentificare_username_password.descriere_sumara", "audit.autentificare_username_password.descriere_completa"),
    ASIGNARE_ROL("ASG-ROL", "audit.autentificare_username_password.descriere_sumara", "audit.autentificare_username_password.descriere_completa"),
    REVOCARE_ROL("RMV-ROL", "audit.autentificare_username_password.descriere_sumara", "audit.autentificare_username_password.descriere_completa"),
    SALVARE_TOKEN_APLICATIE("SAVE-TOKEN", "audit.salvare.token.descriere_sumara", "audit.salvare.token.descriere_completa"),
    SALVARE_PREFERINTE("SAVE-PREF", "audit.autentificare_username_password.descriere_sumara", "audit.autentificare_username_password.descriere_completa"),
    /*TBD*/SALVARE_PARAMETRU_SISTEM("SAVE-PARAM", "audit.autentificare_username_password.descriere_sumara", "audit.autentificare_username_password.descriere_completa"),
    SALVARE_INREGISTRARE_NOMENCLATOR("NOM-SAVE", "audit.autentificare_username_password.descriere_sumara", "audit.autentificare_username_password.descriere_completa"),
    SALVARE_VERSIUNE_NOMENCLATOR("NOM-V-SAVE", "audit.autentificare_username_password.descriere_sumara", "audit.autentificare_username_password.descriere_completa"),
    CREARE_VERSIUNE_NOMENCLATOR("NOM-V-NEW", "audit.autentificare_username_password.descriere_sumara", "audit.autentificare_username_password.descriere_completa"),
    INCHIDERE_VERSIUNE_NOMENCLATOR("CLOSE_V", "audit.autentificare_username_password.descriere_sumara", "audit.autentificare_username_password.descriere_completa"),
    INACTIVARE_INREGISTRARE_NOMENCLATOR("NOM-RMV", "audit.autentificare_username_password.descriere_sumara", "audit.autentificare_username_password.descriere_completa"),
    ACTUALIZARE_INVENTAR_GOSPODARII("INV-GOSP", "audit.autentificare_username_password.descriere_sumara", "audit.autentificare_username_password.descriere_completa", TRANSACTION_UNIT_REGISTRU),
    UPLOAD("UPLOAD", "audit.autentificare_username_password.descriere_sumara", "audit.autentificare_username_password.descriere_completa"),
    
    SCHIMBARE_PAROLA("UPD-PASS", "audit.edit.passwd.user", "audit.edit.passwd.user"),
    SCHIMBARE_PAROLA_DE_CATRE_UTILIZATOR("UPD-U-PASS", "audit.edit.passwd.user.my", "audit.edit.passwd.user.my"),
    RESETEAZA_PAROLA("RESET-PSWD","audit.reset.passwd.user","audit.reset.passwd.user"),
    RESETEAZA_PAROLA_DE_CATRE_UTILIZATOR("RESET-PSWU","audit.reset.passwd.user.my","audit.reset.passwd.user.my"),
    
    ASIGNARE_GOSPODARIE_PJ("ASG-GOS-PJ","audit.asignare.gospadarie.pj","audit.asignare.gospadarie.pj"),
    REVOCARE_GOSPODARIE_PJ("RMV-GOS-PJ","audit.revocare.gospadarie.pj","audit.revocare.gospadarie.pj"),
   
    /*INTERFATA AUTOMATA*/
    /*TBD*/NOTIFICARE_RAPOARTE("NOTIF-RAP", "audit.autentificare_username_password.descriere_sumara", "audit.autentificare_username_password.descriere_completa"),
    /*TBD*/NOTIFICARE_CERERI_COMPLETARE_DATE("NOTIF-COMP", "audit.autentificare_username_password.descriere_sumara", "audit.autentificare_username_password.descriere_completa"),
    /*TBD*/NOTIFICARE_CERERI_CORECTIE_DATE("NOTIF-COR", "audit.autentificare_username_password.descriere_sumara", "audit.autentificare_username_password.descriere_completa"),
    /*TBD*/INTEROGARE_LISTA_GOSPODARII_PF("GOSP-PF", "audit.autentificare_username_password.descriere_sumara", "audit.autentificare_username_password.descriere_completa"),
    /*TBD*/INTEROGARE_LISTA_GOSPODARII_PJ("GOSP-PJ", "audit.autentificare_username_password.descriere_sumara", "audit.autentificare_username_password.descriere_completa"),
    /*TBD*/INTEROGARE_CAPITOL_GOSPODARIE("REQ-CAP", "audit.autentificare_username_password.descriere_sumara", "audit.autentificare_username_password.descriere_completa"),
    /*TBD*/INTEROGARE_CAPITOL_CENTRALIZATOR("REQ-CENT", "audit.autentificare_username_password.descriere_sumara", "audit.autentificare_username_password.descriere_completa"),
    /*TBD*/TRANSMITERE_CAPITOL("SEND-CAP", "audit.autentificare_username_password.descriere_sumara", "audit.autentificare_username_password.descriere_completa"),
    /*TBD*/FTP_CONECTARE("FTP-CON", "audit.autentificare_username_password.descriere_sumara", "audit.autentificare_username_password.descriere_completa"),
    /*TBD*/FTP_DESCARCARE("FTP-DOWN", "audit.autentificare_username_password.descriere_sumara", "audit.autentificare_username_password.descriere_completa"),
    /*TBD*/TRANSMITERE_STATUS("SEND-STAT", "audit.autentificare_username_password.descriere_sumara", "audit.autentificare_username_password.descriere_completa");


    private String cod;

    private Class springBeanClass;

    private String codDescriereProperties = "";

    private String codDescriereCompletaProperties = "";

    private TipTranzactieEnum transactionUnit = TRANSACTION_UNIT_PORTAL;

    AuditOpType(String cod, Class springBeanClass, String codDescriereProperties, String codDescriereCompletaProperties, TipTranzactieEnum transactionUnit) {
        this.cod = cod;
        this.springBeanClass = springBeanClass;
        this.codDescriereProperties = codDescriereProperties;
        this.codDescriereCompletaProperties = codDescriereCompletaProperties;
        this.transactionUnit = transactionUnit;
    }

    AuditOpType(String cod, String codDescriereProperties, String codDescriereCompletaProperties, TipTranzactieEnum transactionUnit) {
        this.cod = cod;
        this.springBeanClass = DescriereOperatieSesiuneGenericBuilder.class;
        this.codDescriereProperties = codDescriereProperties;
        this.codDescriereCompletaProperties = codDescriereCompletaProperties;
        this.transactionUnit = transactionUnit;
    }

    AuditOpType(String cod, Class springBeanClass, String codDescriereProperties, String codDescriereCompletaProperties) {
        this(cod, springBeanClass, codDescriereProperties, codDescriereCompletaProperties, TRANSACTION_UNIT_PORTAL);
    }

    AuditOpType(String cod, String codDescriereProperties, String codDescriereCompletaProperties) {
        this(cod, codDescriereProperties, codDescriereCompletaProperties, TRANSACTION_UNIT_PORTAL);
    }

    public String getCodDescriereProperties() {
        return codDescriereProperties;
    }

    public String getCodDescriereCompletaProperties() {
        return codDescriereCompletaProperties;
    }

    public TipTranzactieEnum getTransactionUnit() {
        return transactionUnit;
    }

    public <T extends DescriereOperatieSesiune> Class<T> getSpringBeanClass() {
        return springBeanClass;
    }

    public String getCod() {
        return cod;
    }
}
