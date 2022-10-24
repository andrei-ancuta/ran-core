package ro.uti.ran.core.model.portal;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(RegistruPortalDetails.class)
public class RegistruPortalDetails_ {
    public static volatile SingularAttribute<RegistruPortalDetails,Long> id;
    public static volatile SingularAttribute<RegistruPortalDetails,Incarcare> incarcare;
}
