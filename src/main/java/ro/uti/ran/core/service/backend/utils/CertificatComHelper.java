package ro.uti.ran.core.service.backend.utils;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import ro.uti.ran.core.model.registru.CertificatCom;

/**
 * Created by Dan on 10-Nov-15.
 */
public class CertificatComHelper {
    private long dataEliberare;
    private String serie;

    public CertificatComHelper(CertificatCom certificatCom) {
        if (certificatCom == null) {
            throw new IllegalArgumentException("CertificatCom  nedefinit!");
        }
        this.dataEliberare = DataRaportareHelper.getDataOra_00_00_00_000(certificatCom.getDataEliberare()).getTime();
        this.serie = certificatCom.getSerie() != null ? certificatCom.getSerie().toLowerCase() : null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        CertificatComHelper that = (CertificatComHelper) o;

        return new EqualsBuilder()
                .append(dataEliberare, that.dataEliberare)
                .append(serie, that.serie)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(dataEliberare)
                .append(serie)
                .toHashCode();
    }

    public static boolean isEquals(CertificatCom a1, CertificatCom a2) {
        if (a1 == null) {
            throw new IllegalArgumentException("CertificatCom a1 nedefinit!");
        }
        if (a2 == null) {
            throw new IllegalArgumentException("CertificatCom a2 nedefinit!");
        }
        return (new CertificatComHelper(a1)).equals(new CertificatComHelper(a2));
    }
}
