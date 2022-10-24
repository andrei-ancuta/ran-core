package ro.uti.ran.core.dto;

import org.apache.commons.lang.StringUtils;

public class ParametriiInterogarePF {

    //required parameters
    private final String nume;
    private final String prenume;
    private final String initialaTata;
    private final Long idNomUat;
    //optional parameters
    private final String cnp;
    private final String nif;


    private ParametriiInterogarePF(ParametriiInterogarePFBuilder builder) {
        this.nume = builder.nume;
        this.prenume = builder.prenume;
        this.initialaTata = builder.initialaTata;
        this.idNomUat = builder.idNomUat;
        this.cnp = builder.cnp;
        this.nif = builder.nif;
    }


    public String getNume() {
        return nume;
    }

    public String getPrenume() {
        return prenume;
    }

    public String getInitialaTata() {
        return initialaTata;
    }

    public Long getIdNomUat() {
        return idNomUat;
    }

    public String getCnp() {
        return cnp;
    }

    public String getNif() {
        return nif;
    }

    //Builder Class
    public static class ParametriiInterogarePFBuilder {
        //required parameters
        private final String nume;
        private final String prenume;
        private final String initialaTata;
        private final Long idNomUat;
        //optional parameters
        private String cnp;
        private String nif;


        public ParametriiInterogarePFBuilder(String nume, String prenume, String initialaTata, Long idNomUat) {
            this.nume = nume;
            this.prenume = prenume;
            this.initialaTata = initialaTata;
            this.idNomUat = idNomUat;
        }


        public ParametriiInterogarePFBuilder cnp(String cnp) {
            this.cnp = cnp;
            return this;
        }


        public ParametriiInterogarePFBuilder nif(String nif) {
            this.nif = nif;
            return this;
        }

        public ParametriiInterogarePF build() {
            ParametriiInterogarePF parametriiInterogare = new ParametriiInterogarePF(this);
            //validari
            if (StringUtils.isEmpty(parametriiInterogare.getNume())) {
                throw new IllegalArgumentException("Nume nedefinit");
            }
            if (StringUtils.isEmpty(parametriiInterogare.getPrenume())) {
                throw new IllegalArgumentException("Prenume nedefinit");
            }
            if (StringUtils.isEmpty(parametriiInterogare.getInitialaTata())) {
                throw new IllegalArgumentException("Initiala tata nedefinit");
            }
            if (parametriiInterogare.getIdNomUat() == null) {
                throw new IllegalArgumentException("idNomUat nedefinit");
            }
            if (StringUtils.isEmpty(parametriiInterogare.getCnp()) && StringUtils.isEmpty(parametriiInterogare.getNif())) {
                throw new IllegalArgumentException("CNP si NIF nedefinite ambele");
            }
            if (StringUtils.isNotEmpty(parametriiInterogare.getCnp()) && StringUtils.isNotEmpty(parametriiInterogare.getNif())) {
                throw new IllegalArgumentException("CNP si NIF definite simultan");
            }
            //
            return parametriiInterogare;
        }

    }
}
