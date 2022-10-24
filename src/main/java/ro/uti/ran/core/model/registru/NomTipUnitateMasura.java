package ro.uti.ran.core.model.registru;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


/**
 * The persistent class for the NOM_TIP_UNITATE_MASURA database table.
 */
@Entity
@Table(name = "NOM_TIP_UNITATE_MASURA")
@NamedQuery(name = "NomTipUnitateMasura.findAll", query = "SELECT n FROM NomTipUnitateMasura n")
public class NomTipUnitateMasura extends Nomenclator implements NomRegistrulAgricol {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "NOM_TIP_UNITATE_MASURA_ID_GENERATOR", sequenceName = "SEQ_NOM_TIP_UNITATE_MASURA", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "NOM_TIP_UNITATE_MASURA_ID_GENERATOR")
    @Column(name = "ID_NOM_TIP_UNITATE_MASURA", updatable = false)
    private Long id;

    private String cod;

    private String denumire;

    //bi-directional many-to-one association to NomUnitateMasura
    @OneToMany(mappedBy = "nomTipUnitateMasura")
    private List<NomUnitateMasura> nomUnitateMasuras = new ArrayList<NomUnitateMasura>();

    public NomTipUnitateMasura() {
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

    public List<NomUnitateMasura> getNomUnitateMasuras() {
        return this.nomUnitateMasuras;
    }

    public void setNomUnitateMasuras(List<NomUnitateMasura> nomUnitateMasuras) {
        this.nomUnitateMasuras = nomUnitateMasuras;
    }

    public NomUnitateMasura addNomUnitateMasura(NomUnitateMasura nomUnitateMasura) {
        getNomUnitateMasuras().add(nomUnitateMasura);
        nomUnitateMasura.setNomTipUnitateMasura(this);

        return nomUnitateMasura;
    }

    public NomUnitateMasura removeNomUnitateMasura(NomUnitateMasura nomUnitateMasura) {
        getNomUnitateMasuras().remove(nomUnitateMasura);
        nomUnitateMasura.setNomTipUnitateMasura(null);

        return nomUnitateMasura;
    }

}