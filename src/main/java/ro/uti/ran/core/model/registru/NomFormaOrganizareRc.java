package ro.uti.ran.core.model.registru;

import javax.persistence.*;


/**
 * The persistent class for the NOM_FORMA_ORGANIZARE_RC database table.
 */
@Entity
@Table(name = "NOM_FORMA_ORGANIZARE_RC")
@NamedQuery(name = "NomFormaOrganizareRc.findAll", query = "SELECT n FROM NomFormaOrganizareRc n")
public class NomFormaOrganizareRc extends Nomenclator implements NomRegistrulAgricol{
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "NOM_FORMA_ORGANIZARE_RC_ID_GENERATOR", sequenceName = "SEQ_NOM_FORMA_ORGANIZARE_RC", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "NOM_FORMA_ORGANIZARE_RC_ID_GENERATOR")
    @Column(name = "ID_NOM_FORMA_ORGANIZARE_RC", updatable = false)
    private Long id;

    private String cod;

    private String denumire;

    @Column(name = "IS_PERSONALITATE_JURIDICA")
    private Integer isPersonalitateJuridica;

    public NomFormaOrganizareRc() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCod() {
        return this.cod;
    }

    public void setCod(String cod) {
        this.cod = cod;
    }

    public String getDenumire() {
        return this.denumire;
    }

    public void setDenumire(String denumire) {
        this.denumire = denumire;
    }

    public Integer getIsPersonalitateJuridica() {
        return isPersonalitateJuridica;
    }

    public void setIsPersonalitateJuridica(Integer isPersonalitateJuridica) {
        this.isPersonalitateJuridica = isPersonalitateJuridica;
    }
}