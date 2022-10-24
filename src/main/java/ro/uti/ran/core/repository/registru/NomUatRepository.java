package ro.uti.ran.core.repository.registru;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ro.uti.ran.core.model.registru.NomUat;

import java.util.List;

/**
 * Created by Anastasia cea micuta on 10/20/2015.
 */
public interface NomUatRepository extends JpaRepository<NomUat, Long> {

	NomUat findByCodSiruta(Integer codSiruta);

	@Query(nativeQuery = true,value = " SELECT COUNT(*) FROM nom_uat" +
			" where fk_nom_judet = ?1")
	Long totalUatDinJudet(Long idJudet);

	List<NomUat> findAll();

	@Query(nativeQuery = true,value = " SELECT * FROM nom_uat" +
			" where fk_nom_judet = ?1")
	List<NomUat> nomUatDinJudet(Long idJudet);
}