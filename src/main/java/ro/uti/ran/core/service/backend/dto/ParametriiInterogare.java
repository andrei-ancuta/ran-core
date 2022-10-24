package ro.uti.ran.core.service.backend.dto;

import org.apache.commons.lang.StringUtils;
import ro.uti.ran.core.service.backend.utils.TipCapitol;

/**
 * Created by Dan on 25-Feb-16.
 */
public class ParametriiInterogare {

    //required parameters
    private final Integer codSirutaUAT;
    private final String identificatorGospodarie;
    private final TipCapitol tipCapitol;
    //optional parameters
    private final Integer an;
    private final Byte semestru;

    private ParametriiInterogare(ParametriiInterogareBuilder builder) {
        this.codSirutaUAT = builder.codSirutaUAT;
        this.identificatorGospodarie = builder.identificatorGospodarie;
        this.tipCapitol = builder.tipCapitol;
        this.an = builder.an;
        this.semestru = builder.semestru;
    }

    public Integer getCodSirutaUAT() {
        return codSirutaUAT;
    }

    public String getIdentificatorGospodarie() {
        return identificatorGospodarie;
    }

    public TipCapitol getTipCapitol() {
        return tipCapitol;
    }

    public Integer getAn() {
        return an;
    }

    public Byte getSemestru() {
        return semestru;
    }

    //Builder Class
    public static class ParametriiInterogareBuilder {
        //required parameters
        private final Integer codSirutaUAT;
        private final String identificatorGospodarie;
        private final TipCapitol tipCapitol;
        //optional parameters
        private Integer an;
        private Byte semestru;

        /**
         * @param codSirutaUAT            identificator unic UAT
         * @param identificatorGospodarie identificator unic alocat gospodariei in cadrul UAT-ului
         * @param tipCapitol              parte solicitata de informatie despre gospodarie (vezi nom_capitol)
         */
        public ParametriiInterogareBuilder(Integer codSirutaUAT, String identificatorGospodarie, TipCapitol tipCapitol) {
            this.codSirutaUAT = codSirutaUAT;
            this.identificatorGospodarie = identificatorGospodarie;
            this.tipCapitol = tipCapitol;
        }

        /**
         * @param an an raportare
         * @return builder
         */
        public ParametriiInterogareBuilder an(Integer an) {
            this.an = an;
            return this;
        }

        /**
         * @param semestru semestru raportare
         * @return builder
         */
        public ParametriiInterogareBuilder semestru(Byte semestru) {
            this.semestru = semestru;
            return this;
        }

        public ParametriiInterogare build() {
            ParametriiInterogare parametriiInterogare = new ParametriiInterogare(this);
            //validari
            if (parametriiInterogare.getCodSirutaUAT() == null) {
                throw new IllegalArgumentException("CodSirutaUAT nedefinit");
            }
            if (StringUtils.isEmpty(parametriiInterogare.getIdentificatorGospodarie())) {
                throw new IllegalArgumentException("IdentificatorGospodarie nedefinit");
            }
            if (parametriiInterogare.getTipCapitol() == null) {
                throw new IllegalArgumentException("TipCapitol nedefinit");
            }
            //
            return parametriiInterogare;
        }

    }

}
