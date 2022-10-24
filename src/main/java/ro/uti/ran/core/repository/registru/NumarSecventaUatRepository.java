package ro.uti.ran.core.repository.registru;

import java.math.BigDecimal;
import java.sql.SQLException;

public interface NumarSecventaUatRepository {

	public BigDecimal getSecventaByBaseId(Long baseId) throws SQLException;
	
	public void resetSecventa(Long baseId, BigDecimal value) throws SQLException;

	public BigDecimal viewSecventaByBaseId(Long baseId) throws SQLException;

	public long viewMaxNrCerereByUat(Long idUat) throws SQLException;
	
}
