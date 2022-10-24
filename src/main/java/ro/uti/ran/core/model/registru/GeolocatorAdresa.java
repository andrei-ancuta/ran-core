package ro.uti.ran.core.model.registru;

import javax.persistence.*;
import java.io.Serializable;


/**
 * The persistent class for the GEOLOCATOR_ADRESA database table.
 */
@Entity
@Table(name = "GEOLOCATOR_ADRESA")
@NamedQuery(name = "GeolocatorAdresa.findAll", query = "SELECT g FROM GeolocatorAdresa g")
public class GeolocatorAdresa implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "GEOLOCATOR_ADRESA_IDGEOLOCATOR_ADRESA_GENERATOR", sequenceName = "SEQ_GEOLOCATOR_ADRESA", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GEOLOCATOR_ADRESA_IDGEOLOCATOR_ADRESA_GENERATOR")
    @Column(name = "ID_GEOLOCATOR_ADRESA")
    private Long idGeolocatorAdresa;

    @Column(name = "FK_NOM_JUDET")
    private Long fkNomJudet;


    @OneToOne
    @JoinColumn(name = "FK_ADRESA")
    private Adresa adresa;

    public GeolocatorAdresa() {
    }

    public Long getIdGeolocatorAdresa() {
        return this.idGeolocatorAdresa;
    }

    public void setIdGeolocatorAdresa(Long idGeolocatorAdresa) {
        this.idGeolocatorAdresa = idGeolocatorAdresa;
    }

    public Long getFkNomJudet() {
        return this.fkNomJudet;
    }

    public void setFkNomJudet(Long fkNomJudet) {
        this.fkNomJudet = fkNomJudet;
    }

    public Adresa getAdresa() {
        return this.adresa;
    }

    public void setAdresa(Adresa adresa) {
        this.adresa = adresa;
    }

}