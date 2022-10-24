package ro.uti.ran.core.model.portal;


import ro.uti.ran.core.model.Model;

import javax.persistence.*;

/**
 * Created by adrian.boldisor on 2/4/2016.
 */


@Entity
@Table(name = "APP_UTILIZATOR_GOSPODARIE")
public class UtilizatorGospodarie extends Model {

    @Id
    @GeneratedValue(generator = "UtilisatorGospodarieSeq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "UtilisatorGospodarieSeq", sequenceName = "SEQ_APP_UTILIZATOR_GOSPODARIE")
    @Column(name = "ID_APP_UTILIZATOR_GOSPODARIE")
    long idUtilizatorGospodarie;

    public void setIdUtilizatorGospodarie(long idUtilizatorGospodarie) {
        idUtilizatorGospodarie = idUtilizatorGospodarie;
    }

    public long getIdUtilizatorGospodarie() {
        return idUtilizatorGospodarie;
    }

    @Column(name = "FK_APP_UTILIZATOR")
    long idUtilizator;


    public long getIdUtilizator() {
        return idUtilizator;
    }

    public void setIdUtilizator(long idUtilizator) {
        this.idUtilizator = idUtilizator;
    }


    @Column(name = "FK_GOSPODARIE")
    long idGospodarie;


    public long getIdGospodarie() {
        return idGospodarie;
    }

    public void setIdGospodarie(long idGospodarie) {
        this.idGospodarie = idGospodarie;
    }
}
