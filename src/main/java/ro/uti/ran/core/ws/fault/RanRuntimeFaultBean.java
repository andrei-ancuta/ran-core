package ro.uti.ran.core.ws.fault;

/**
 * Created with IntelliJ IDEA.
 * User: bogdan.ardeleanu
 * Date: 12/9/13
 * Time: 4:26 PM
 * To change this template use File | Settings | File Templates.
 */
public class RanRuntimeFaultBean extends RanDefaultFaultBean {

    public RanRuntimeFaultBean() {
    }

    public RanRuntimeFaultBean(Throwable t) {
        setMessage(t.getMessage());
    }

    @Override
    protected RanRuntimeFaultBean clone() {
        try {
            return (RanRuntimeFaultBean) super.clone();
        } catch (CloneNotSupportedException e) {
            RanRuntimeFaultBean faultBean = new RanRuntimeFaultBean();
            faultBean.setCode(getCode());
            faultBean.setMessage(getMessage());
            return faultBean;
        }
    }
    
    @Override
    public void setMessage(String mess) {
		super.setMessage(mess);
		
		String error = "Ran Runtime Fault Bean Message - (message=" + getMessage() + ")";
		error += getCode() != null ? " code: " + getCode() : "";
		
        System.err.println(error);
       
	}

}
