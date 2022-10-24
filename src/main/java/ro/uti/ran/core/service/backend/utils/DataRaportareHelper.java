package ro.uti.ran.core.service.backend.utils;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Dan on 12-Nov-15.
 */
public class DataRaportareHelper {
    /**
     * Valorile de nomenclator sa fie valabile la ultima zi din anul/semestrul de raportare.
     *
     * @return ultima zi din anul/semestrul de raportare.
     */
    public static Date getUltimaZiDinAnulSemestrulDeRaportare(Integer anRaportare, Integer semestruRaportare) {
        if (anRaportare == null) {
            throw new IllegalArgumentException("AnRaportare nedefinit!");
        }
        if (semestruRaportare == null) {
            throw new IllegalArgumentException("SemestruRaportare nedefinit!");
        }
        Calendar ultimaZi = Calendar.getInstance();
        ultimaZi.set(Calendar.YEAR, anRaportare);
        ultimaZi.set(Calendar.MILLISECOND, 0);
        ultimaZi.set(Calendar.SECOND, 0);
        ultimaZi.set(Calendar.MINUTE, 0);
        ultimaZi.set(Calendar.HOUR_OF_DAY, 0);
        switch (semestruRaportare) {
            case 1:
                ultimaZi.set(Calendar.MONTH, Calendar.JUNE);
                ultimaZi.set(Calendar.DAY_OF_MONTH, 30);
                break;
            case 2:
                ultimaZi.set(Calendar.MONTH, Calendar.DECEMBER);
                ultimaZi.set(Calendar.DAY_OF_MONTH, 31);
                break;
            default:
                throw new IllegalArgumentException("Valoarea " + semestruRaportare + "invalida pentru SemestruRaportare!");
        }
        return ultimaZi.getTime();
    }

    /**
     * Valorile de nomenclator sa fie valabile la ultima zi din anul de raportare.
     *
     * @return ultima zi din anul/semestrul de raportare.
     */
    public static Date getUltimaZiDinAnulDeRaportare(Integer anRaportare) {
        return getUltimaZiDinAnulSemestrulDeRaportare(anRaportare, 2);
    }

    /**
     * @param data data care se doreste prelucrata
     * @return data trimisa ca parametru cu ora 00:00:00 000
     */
    public static Date getDataOra_00_00_00_000(Date data) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(data);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        return calendar.getTime();
    }

    /**
     * @param data data care se doreste prelucrata
     * @return data trimisa ca parametru cu ora 23:59:00 000
     */
    public static Date getDataOra_23_59_00_000(Date data) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(data);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        return calendar.getTime();
    }
}
