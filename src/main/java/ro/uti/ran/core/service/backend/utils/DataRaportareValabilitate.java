package ro.uti.ran.core.service.backend.utils;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.util.Date;

/**
 * Created by Stanciu Neculai on 13-Apr-16.
 */
public class DataRaportareValabilitate extends java.util.Date {
    private Date dataRaportareValabilitate;

    public DataRaportareValabilitate() {
        dataRaportareValabilitate = DataRaportareHelper.getDataOra_23_59_00_000(new Date());
    }

    public DataRaportareValabilitate(Integer an) {
        dataRaportareValabilitate = DataRaportareHelper.getUltimaZiDinAnulDeRaportare(an);
    }

    public DataRaportareValabilitate(Integer an, Integer semestru) {
        dataRaportareValabilitate = DataRaportareHelper.getUltimaZiDinAnulSemestrulDeRaportare(an, semestru);
    }

    public Date getDataRaportareValabilitate() {
        return dataRaportareValabilitate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DataRaportareValabilitate that = (DataRaportareValabilitate) o;

        return dataRaportareValabilitate.equals(that.dataRaportareValabilitate);

    }

    @Override
    public int hashCode() {
        return dataRaportareValabilitate.hashCode();
    }
}
