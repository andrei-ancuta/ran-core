package ro.uti.ran.core.utils;

/**
 * Created by Admin on 11/24/2015.
 */
public class CnpValidator {

    public static boolean isValid(String cnp){

        if(cnp.length() == 13){

            int []cnpArray = new int[cnp.length()];

            for(int i =0 ; i < cnp.length(); i++){
                cnpArray[i] = Integer.parseInt(String.valueOf(cnp.charAt(i)));
            }

            //Ultima cifră din CNP (cifra numărul 13) este una de control (al validităţii CNP-ului). Această cifră rezultă
            // în urma unui calcul matematic foarte simplu. Pentru a afla această cifră, pe baza primelor 12 cifre al
            // CNP-ului, se foloseşte numărul 279146358279. Fiecare cifră a CNP-ului se înmulţeşte cu cifra corespondentă
            // din acest număr, rezultatele se adună, iar suma rezultată se împarte la 11. Restul împărţirii dă cifra de control.
            // Dacă restul este 10, cifra de control este 1.

            long sum = cnpArray[0] * 2 + cnpArray[1] * 7 + cnpArray[2] * 9 + cnpArray[3] * 1 + cnpArray[4] * 4 + cnpArray[5] * 6
                    + cnpArray[6] * 3 + cnpArray[7] * 5 + cnpArray[8] * 8 + cnpArray[9] * 2 + cnpArray[10] * 7 + cnpArray[11] * 9;

            long rest = sum % 11;

            if(((rest < 10) && (rest == cnpArray[12])) || ((rest == 10) && (cnpArray[12] == 1))){
                return true;
            }
        }

        return false;
    }
}
