package ro.uti.ran.core.model.registru.view;

import javax.persistence.IdClass;
import java.io.Serializable;

@IdClass(ViewUatGospodariePK.class)
public class ViewUatGospodariePK implements Serializable {

    private static final long serialVersionUID = 1848613327348014302L;
    private Long uat;
    private Integer an;
    public ViewUatGospodariePK() {
    }

    public Long getUat() {
        return uat;
    }

    public void setUat(Long uat) {
        this.uat = uat;
    }

    public Integer getAn() {
        return an;
    }

    public void setAn(Integer an) {
        this.an = an;
    }

    @Override
    public boolean equals(Object obj) {
        return getUat() == ((ViewUatGospodarie) obj).getUat() && getAn() == ((ViewUatGospodarie) obj).getAn();
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

}

