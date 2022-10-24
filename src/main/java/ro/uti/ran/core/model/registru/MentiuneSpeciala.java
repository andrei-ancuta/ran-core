package ro.uti.ran.core.model.registru;

import javax.persistence.*;
import java.io.Serializable;


/**
 * The persistent class for the MENTIUNE_SPECIALA database table.
 */
@Entity
@Table(name = "MENTIUNE_SPECIALA")
@NamedQuery(name = "MentiuneSpeciala.findAll", query = "SELECT m FROM MentiuneSpeciala m")
public class MentiuneSpeciala implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "MENTIUNE_SPECIALA_IDMENTIUNESPECIALA_GENERATOR", sequenceName = "SEQ_MENTIUNE_SPECIALA", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MENTIUNE_SPECIALA_IDMENTIUNESPECIALA_GENERATOR")
    @Column(name = "ID_MENTIUNE_SPECIALA")
    private Long idMentiuneSpeciala;

    @Column(name = "FK_NOM_JUDET")
    private Long fkNomJudet;


    private String mentiune;

    //bi-directional many-to-one association to Gospodarie
    @ManyToOne
    @JoinColumn(name = "FK_GOSPODARIE")
    private Gospodarie gospodarie;

    public MentiuneSpeciala() {
    }

    public Long getIdMentiuneSpeciala() {
        return this.idMentiuneSpeciala;
    }

    public void setIdMentiuneSpeciala(Long idMentiuneSpeciala) {
        this.idMentiuneSpeciala = idMentiuneSpeciala;
    }

    public Long getFkNomJudet() {
        return this.fkNomJudet;
    }

    public void setFkNomJudet(Long fkNomJudet) {
        this.fkNomJudet = fkNomJudet;
    }


    public String getMentiune() {
        return this.mentiune;
    }

    public void setMentiune(String mentiune) {
        this.mentiune = mentiune;
    }

    public Gospodarie getGospodarie() {
        return this.gospodarie;
    }

    public void setGospodarie(Gospodarie gospodarie) {
        this.gospodarie = gospodarie;
    }

}