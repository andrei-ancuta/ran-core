package ro.uti.ran.core.model.registru;

import ro.uti.ran.core.model.Editable;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the NOM_CATEGORIE_FOLOSINTA database table.
 */
@Entity
@Table(name = "NOM_CATEGORIE_FOLOSINTA")
@NamedQuery(name = "NomCategorieFolosinta.findAll", query = "SELECT n FROM NomCategorieFolosinta n")
public class NomCategorieFolosinta extends Nomenclator implements Editable, NomRegistrulAgricol{
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "NOM_CATEGORIE_FOLOSINTA_ID_GENERATOR", sequenceName = "SEQ_NOM_CATEGORIE_FOLOSINTA", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "NOM_CATEGORIE_FOLOSINTA_ID_GENERATOR")
    @Column(name = "ID_NOM_CATEGORIE_FOLOSINTA", updatable = false)
    private Long id;

    private String cod;

    private String denumire;

    private String descriere;

    @Column(name = "FK_NOM_CATEGORIE_FOLOSINTA")
    private Long fkNomCategorieFolosinta;

    //bi-directional many-to-one association to CapCategorieFolosinta
    @OneToMany(mappedBy = "nomCategorieFolosinta")
    private List<CapCategorieFolosinta> capCategorieFolosintas = new ArrayList<>();


    public NomCategorieFolosinta() {
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

    public String getDescriere() {
        return this.descriere;
    }

    public void setDescriere(String descriere) {
        this.descriere = descriere;
    }

    public Long getFkNomCategorieFolosinta() {
        return this.fkNomCategorieFolosinta;
    }

    public void setFkNomCategorieFolosinta(Long fkNomCategorieFolosinta) {
        this.fkNomCategorieFolosinta = fkNomCategorieFolosinta;
    }

    public List<CapCategorieFolosinta> getCapCategorieFolosintas() {
        return this.capCategorieFolosintas;
    }

    public void setCapCategorieFolosintas(List<CapCategorieFolosinta> capCategorieFolosintas) {
        this.capCategorieFolosintas = capCategorieFolosintas;
    }

    public CapCategorieFolosinta addCapCategorieFolosinta(CapCategorieFolosinta capCategorieFolosinta) {
        getCapCategorieFolosintas().add(capCategorieFolosinta);
        capCategorieFolosinta.setNomCategorieFolosinta(this);

        return capCategorieFolosinta;
    }

    public CapCategorieFolosinta removeCapCategorieFolosinta(CapCategorieFolosinta capCategorieFolosinta) {
        getCapCategorieFolosintas().remove(capCategorieFolosinta);
        capCategorieFolosinta.setNomCategorieFolosinta(null);

        return capCategorieFolosinta;
    }

    @Override
    public String getCodePrim() {
        return this.cod;
    }

    @Override
    public String getCodePrimName() {
        return "cod";
    }

    @Override
    public Integer getCodeSec() {
        return null;
    }

    @Override
    public String getCodeSecName() {
        return null;
    }

    @Override
    public Date getStarting(){
        return null;
    }

}