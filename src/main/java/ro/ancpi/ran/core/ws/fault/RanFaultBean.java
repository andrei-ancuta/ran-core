package ro.ancpi.ran.core.ws.fault;

import ro.uti.ran.core.exception.base.RanBusinessException;

import javax.xml.bind.annotation.XmlType;


@XmlType(namespace="core.ran.ancpi.ro", name = "RanFaultBean")
public class RanFaultBean extends ro.uti.ran.core.ws.fault.RanFaultBean {

    public RanFaultBean() {
    }

    public RanFaultBean(Throwable t) {
        setMessage(t.getMessage());
    }

    public RanFaultBean(RanBusinessException e) {
        setCode(e.getCode());
        setHint(e.getHint());
        setMessage(e.getMessage());
    }

}
