package ro.uti.ran.core.service.registru;

import java.math.BigDecimal;

import ro.uti.ran.core.ws.fault.RanRuntimeException;
import ro.uti.ran.core.ws.internal.RanAuthorization;
import ro.uti.ran.core.ws.internal.UatRanAuthorization;

public interface NumarSecventaUatService {

	public Long getNumarCerere(UatRanAuthorization ranAuthorization) throws NumarSecventaNotFoundException, RanRuntimeException;
	
	public void resetSecventa(UatRanAuthorization ranAuthorization) throws NumarSecventaNotFoundException, RanRuntimeException, NumarSecventaInvalidException;

	public void setSecventa(UatRanAuthorization ranAuthorization, BigDecimal value) throws NumarSecventaNotFoundException, RanRuntimeException;

	public Long vizualizeazaNumarCerere(UatRanAuthorization ranAuthorization) throws NumarSecventaNotFoundException, RanRuntimeException;
	
}
