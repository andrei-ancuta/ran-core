package ro.uti.ran.core.model.portal;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(Utilizator.class)
public class Utilizator_ {
    public static volatile SingularAttribute<Utilizator,Long> id;
    public static volatile SingularAttribute<Utilizator,String> numeUtilizator;
}
