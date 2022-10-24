package ro.uti.ran.core.model.utils;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Dan on 26-Oct-15.
 */
public interface RanConstants {

    Integer GOSPODARIE_IS_ACTIV_DA = 1;
    Integer GOSPODARIE_IS_ACTIV_NU = 0;

    Integer NOM_TARA_COD_NUMERIC_ROMANIA = 642;

    Integer PARCELA_TEREN_INTRAVILAN = 1;
    Integer PARCELA_TEREN_EXTRAVILAN = 2;

    String NOM_TIP_SPATIU_PROT_COD_SERA = "SE";
    String NOM_TIP_SPATIU_PROT_COD_SOLAR = "SO";

    Integer NOM_IS_FORMULA_DA = 1;
    Integer NOM_IS_FORMULA_NU = 0;
    Integer NOM_VALOARE_DA = 1;

    String LOCATIE_RECIPISA_REGISTRU_IN = "locatie.recipisa.registru.in";
    String LOCATIE_RECIPISA_REGISTRU_OUT = "locatie.recipisa.registru.out";

    String STARE_REGISTRU_RECEPTIONATA_COD = "R";
    String STARE_REGISTRU_VALIDATA_COD = "V";
    String STARE_REGISTRU_INVALIDATA_COD = "I";
    String STARE_REGISTRU_SALVATA_COD = "S";
    String STARE_REGISTRU_EROARE_LA_SALVARE_COD = "E";

    //Messages
    String MESSAGE_PATH = "messages.path";
    String MESSAGE_STORE_LOCAL = "messages.local";
    String MESSAGE_FAULT_STACKTRACE_NBLINES = "messages.fault.stacktrace.lines";

    String UM_KG = "KG";
    String UM_HA = "HA";
    String UM_MP = "MP";
    String UM_NR_CAPETE = "NR. CAPETE";
    String UM_NR_POMI = "NR. POMI";


    //Proxy orders
    int TRANSACTION_MANAGEMENT_ORDER = 100;
    int AUDIT_ORDER = 201;
    int SCHEDULER_ORDER = 199;
    
    //forgot password
    String FORGOT_PASSWORD_REDIRECT_QUERY="?fp=true&id={0}";
    String ACTIVATE_USER_REDIRECT_QUERY="?activateUser=true&id={0}";
    
    String DEFAULT_PASSWORD = "12345678";

    //Params keys
    String URL_LOGIN_PARAM_KEY = "url.login";
    String URL_BASE_PARAM_KEY = "url.base";
    
	Set<String> MAX_PRIORIRTY_CHAPTERS = new HashSet<String>() {
     
		private static final long serialVersionUID = 7763216432064906815L;

		{ 
    		add("CAP0_12"); 
    		add("CAP0_34");
    	}
    };


    Set<Long> MAX_PRIORIRTY_CHAPTERS_INT = new HashSet<Long>() {
        private static final long serialVersionUID = 7763216432064906815L;
        {
            add((long) 27);
            add((long) 28);
        }
    };
}
