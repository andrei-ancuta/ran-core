package ro.uti.ran.core.model.portal;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(RegistruPortal.class)
public class RegistruPortal_ {
    public static volatile SingularAttribute<RegistruPortal,Long> id;
    public static volatile SingularAttribute<RegistruPortal,Incarcare> incarcare;
}
