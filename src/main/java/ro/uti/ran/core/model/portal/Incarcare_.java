package ro.uti.ran.core.model.portal;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import java.util.Date;

@StaticMetamodel(Incarcare.class)
public class Incarcare_ {
    public static volatile SingularAttribute<Incarcare,Long> id;
    public static volatile SingularAttribute<Incarcare,String> indexIncarcare;
    public static volatile SingularAttribute<Incarcare,Date> dataIncarcare;
    public static volatile SingularAttribute<Incarcare,String> denumireFisier;
    public static volatile SingularAttribute<Incarcare,Utilizator> utilizator;
    public static volatile SingularAttribute<Incarcare,UAT> uat;
}
