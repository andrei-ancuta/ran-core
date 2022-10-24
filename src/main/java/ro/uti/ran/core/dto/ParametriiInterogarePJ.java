package ro.uti.ran.core.dto;

import org.apache.commons.lang.StringUtils;

public class ParametriiInterogarePJ {

    //required parameters
    private final String cui;
    private final String denumire;
    private final Long idNomFormaOrganizareRc;
    private final Long idNomUat;
    //optional parameters
    private final String cif;

    private ParametriiInterogarePJ(ParametriiInterogarePJBuilder builder) {
        this.cui = builder.cui;
        this.denumire = builder.denumire;
        this.idNomFormaOrganizareRc = builder.idNomFormaOrganizareRc;
        this.idNomUat = builder.idNomUat;
        this.cif = builder.cif;
    }

    public String getCui() {
        return cui;
    }

    public String getDenumire() {
        return denumire;
    }

    public Long getIdNomFormaOrganizareRc() {
        return idNomFormaOrganizareRc;
    }

    public Long getIdNomUat() {
        return idNomUat;
    }

    public String getCif() {
        return cif;
    }

    //Builder Class
    public static class ParametriiInterogarePJBuilder {

        //required parameters
        private final String cui;
        private final String denumire;
        private final Long idNomFormaOrganizareRc;
        private final Long idNomUat;
        //optional parameters
        private String cif;

        public ParametriiInterogarePJBuilder(String cui, String denumire, Long idNomFormaOrganizareRc, Long idNomUat) {
            this.cui = cui;
            this.denumire = denumire;
            this.idNomFormaOrganizareRc = idNomFormaOrganizareRc;
            this.idNomUat = idNomUat;
        }

        public ParametriiInterogarePJBuilder cif(String cif) {
            this.cif = cif;
            return this;
        }

        public ParametriiInterogarePJ build() {
            ParametriiInterogarePJ parametriiInterogare = new ParametriiInterogarePJ(this);
            //validari
            if (StringUtils.isEmpty(parametriiInterogare.getCui())) {
                throw new IllegalArgumentException("Cui nedefinit");
            }
            if (StringUtils.isEmpty(parametriiInterogare.getDenumire())) {
                throw new IllegalArgumentException("Denumire nedefinit");
            }
            if (parametriiInterogare.getIdNomFormaOrganizareRc() == null) {
                throw new IllegalArgumentException("idNomFormaOrganizareRc nedefinit");
            }
            if (parametriiInterogare.getIdNomUat() == null) {
                throw new IllegalArgumentException("idNomUat nedefinit");
            }
            //
            return parametriiInterogare;
        }
    }
}

