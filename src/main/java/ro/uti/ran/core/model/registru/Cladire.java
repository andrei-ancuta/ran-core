package ro.uti.ran.core.model.registru;

import javax.persistence.*;
import java.io.Serializable;


/**
 * The persistent class for the CLADIRE database table.
 */
@Entity
@NamedQuery(name = "Cladire.findAll", query = "SELECT c FROM Cladire c")
public class Cladire implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "CLADIRE_IDCLADIRE_GENERATOR", sequenceName = "SEQ_CLADIRE", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CLADIRE_IDCLADIRE_GENERATOR")
    @Column(name = "ID_CLADIRE")
    private Long idCladire;

    @Column(name = "\"AN\"")
    private Integer an;

    @Column(name = "AN_TERMINARE")
    private Integer anTerminare;

    @Column(name = "FK_NOM_JUDET")
    private Long fkNomJudet;

    @Transient
    private String geometrieGML;

    private String identificator;

    @Column(name = "IDENTIFICATOR_CADASTRAL")
    private String identificatorCadastral;


    @Column(name = "SUPRAFATA_DESFASURATA")
    private Integer suprafataDesfasurata;

    @Column(name = "SUPRAFATA_SOL")
    private Integer suprafataSol;

    private String zona;

    //bi-directional many-to-one association to Adresa
    @ManyToOne
    @JoinColumn(name = "FK_ADRESA")
    private Adresa adresa;

    //bi-directional many-to-one association to Gospodarie
    @ManyToOne
    @JoinColumn(name = "FK_GOSPODARIE")
    private Gospodarie gospodarie;

    //bi-directional many-to-one association to NomDestinatieCladire
    @ManyToOne
    @JoinColumn(name = "FK_NOM_DESTINATIE_CLADIRE")
    private NomDestinatieCladire nomDestinatieCladire;

    //bi-directional many-to-one association to NomTipCladire
    @ManyToOne
    @JoinColumn(name = "FK_NOM_TIP_CLADIRE")
    private NomTipCladire nomTipCladire;

    //bi-directional one-to-one association to GeometrieCladire
    @OneToOne(mappedBy = "cladire", cascade = CascadeType.REMOVE)
    GeometrieCladire geometrieCladire;

    public Cladire() {
    }

    public Long getIdCladire() {
        return this.idCladire;
    }

    public void setIdCladire(Long idCladire) {
        this.idCladire = idCladire;
    }

    public Integer getAn() {
        return this.an;
    }

    public void setAn(Integer an) {
        this.an = an;
    }

    public Integer getAnTerminare() {
        return this.anTerminare;
    }

    public void setAnTerminare(Integer anTerminare) {
        this.anTerminare = anTerminare;
    }

    public Long getFkNomJudet() {
        return this.fkNomJudet;
    }

    public void setFkNomJudet(Long fkNomJudet) {
        this.fkNomJudet = fkNomJudet;
    }

    public String getIdentificator() {
        return this.identificator;
    }

    public void setIdentificator(String identificator) {
        this.identificator = identificator;
    }

    public String getIdentificatorCadastral() {
        return this.identificatorCadastral;
    }

    public void setIdentificatorCadastral(String identificatorCadastral) {
        this.identificatorCadastral = identificatorCadastral;
    }


    public Integer getSuprafataDesfasurata() {
        return this.suprafataDesfasurata;
    }

    public void setSuprafataDesfasurata(Integer suprafataDesfasurata) {
        this.suprafataDesfasurata = suprafataDesfasurata;
    }

    public Integer getSuprafataSol() {
        return this.suprafataSol;
    }

    public void setSuprafataSol(Integer suprafataSol) {
        this.suprafataSol = suprafataSol;
    }

    public String getZona() {
        return this.zona;
    }

    public void setZona(String zona) {
        this.zona = zona;
    }

    public Adresa getAdresa() {
        return this.adresa;
    }

    public void setAdresa(Adresa adresa) {
        this.adresa = adresa;
    }

    public Gospodarie getGospodarie() {
        return this.gospodarie;
    }

    public void setGospodarie(Gospodarie gospodarie) {
        this.gospodarie = gospodarie;
    }

    public NomDestinatieCladire getNomDestinatieCladire() {
        return this.nomDestinatieCladire;
    }

    public void setNomDestinatieCladire(NomDestinatieCladire nomDestinatieCladire) {
        this.nomDestinatieCladire = nomDestinatieCladire;
    }

    public NomTipCladire getNomTipCladire() {
        return this.nomTipCladire;
    }

    public void setNomTipCladire(NomTipCladire nomTipCladire) {
        this.nomTipCladire = nomTipCladire;
    }


    public GeometrieCladire getGeometrieCladire() {
        return geometrieCladire;
    }

    public void setGeometrieCladire(GeometrieCladire geometrieCladire) {
        this.geometrieCladire = geometrieCladire;
    }

    public String getGeometrieGML() {
        return geometrieGML;
    }

    public void setGeometrieGML(String geometrieGML) {
        this.geometrieGML = geometrieGML;
    }
}