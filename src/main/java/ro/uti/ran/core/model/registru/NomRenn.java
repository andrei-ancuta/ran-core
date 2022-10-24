package ro.uti.ran.core.model.registru;

import javax.persistence.*;


/**
 * The persistent class for the NOM_RENNS database table.
 */
@Entity
@Table(name = "NOM_RENNS")
@NamedQuery(name = "NomRenn.findAll", query = "SELECT n FROM NomRenn n")
public class NomRenn extends Nomenclator {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "NOM_RENNS_ID_GENERATOR", sequenceName = "SEQ_NOM_RENNS", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "NOM_RENNS_ID_GENERATOR")
    @Column(name = "ID_NOM_RENNS", updatable = false)
    private Long id;

    private String apartament;

    private String bloc;

    private String etaj;

    @Column(name = "FK_NOM_JUDET")
    private Long fkNomJudet;

    @Column(name = "FK_NOM_LOCALITATE")
    private Long fkNomLocalitate;

    @Column(name = "FK_NOM_UAT")
    private Long fkNomUat;

    @Column(name = "NUMAR_POSTAL")
    private String numarPostal;

    @Column(name = "NUMAR_STRADA")
    private String numarStrada;

    private String scara;

    @Lob
    private byte[] shapefile;

    private String strada;

    @Column(name = "UID_RENNS")
    private String uidRenns;

    public NomRenn() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getApartament() {
        return this.apartament;
    }

    public void setApartament(String apartament) {
        this.apartament = apartament;
    }

    public String getBloc() {
        return this.bloc;
    }

    public void setBloc(String bloc) {
        this.bloc = bloc;
    }

    public String getEtaj() {
        return this.etaj;
    }

    public void setEtaj(String etaj) {
        this.etaj = etaj;
    }

    public Long getFkNomJudet() {
        return fkNomJudet;
    }

    public void setFkNomJudet(Long fkNomJudet) {
        this.fkNomJudet = fkNomJudet;
    }

    public Long getFkNomLocalitate() {
        return fkNomLocalitate;
    }

    public void setFkNomLocalitate(Long fkNomLocalitate) {
        this.fkNomLocalitate = fkNomLocalitate;
    }

    public Long getFkNomUat() {
        return fkNomUat;
    }

    public void setFkNomUat(Long fkNomUat) {
        this.fkNomUat = fkNomUat;
    }

    public String getNumarPostal() {
        return this.numarPostal;
    }

    public void setNumarPostal(String numarPostal) {
        this.numarPostal = numarPostal;
    }

    public String getNumarStrada() {
        return this.numarStrada;
    }

    public void setNumarStrada(String numarStrada) {
        this.numarStrada = numarStrada;
    }

    public String getScara() {
        return this.scara;
    }

    public void setScara(String scara) {
        this.scara = scara;
    }

    public byte[] getShapefile() {
        return this.shapefile;
    }

    public void setShapefile(byte[] shapefile) {
        this.shapefile = shapefile;
    }

    public String getStrada() {
        return this.strada;
    }

    public void setStrada(String strada) {
        this.strada = strada;
    }

    public String getUidRenns() {
        return this.uidRenns;
    }

    public void setUidRenns(String uidRenns) {
        this.uidRenns = uidRenns;
    }

}