package ro.uti.ran.core.ws.fault;

import ro.uti.ran.core.exception.base.RanBusinessException;

public class RanFaultBean extends RanDefaultFaultBean {

    private String hint;

    public RanFaultBean() {
    }

    public RanFaultBean(Throwable t) {
        setMessage(t.getMessage());
    }

    public RanFaultBean(RanBusinessException e) {
        setCode(e.getCode());
        hint = e.getHint();
        setMessage(e.getMessage());
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    @Override
    protected RanFaultBean clone() {
        try {
            return (RanFaultBean) super.clone();
        } catch (CloneNotSupportedException e) {
            RanFaultBean faultBean = new RanFaultBean();
            faultBean.setCode(getCode());
            faultBean.setMessage(getMessage());
            faultBean.setHint(hint);
            return faultBean;
        }
    }

    @Override
    public void setMessage(String mess) {
        super.setMessage(mess);

        String error = "Ran Fault Bean Message - (message=" + getMessage() + ")";
        error += getCode() != null ? " code: " + getCode() : "";
        error += hint != null ? " hint: " + hint : "";

        System.err.println(error);
    }
}
