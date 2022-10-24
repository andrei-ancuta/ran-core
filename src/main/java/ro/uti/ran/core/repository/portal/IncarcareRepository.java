package ro.uti.ran.core.repository.portal;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ro.uti.ran.core.model.portal.Incarcare;
import ro.uti.ran.core.model.portal.StareIncarcare;
import ro.uti.ran.core.repository.base.RanRepository;

import java.util.List;


/**
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-09-24 13:41
 */
public interface IncarcareRepository extends RanRepository<Incarcare, Long> {

    Incarcare findByIndexIncarcare(Long indexIncarcare);

    Incarcare findFirstByStareIncarcare(StareIncarcare stareIncarcare);

    @Query(nativeQuery=true, value="select * from VW_INCARCARE ")
    List<Incarcare> findAllUnProcessedLoad();

    @Query("Select i from Incarcare i where i.id in (Select r.incarcare.id from RegistruPortal r where r.id = :idRegistru)")
    Incarcare getIncarcareByIdRegistru(@Param("idRegistru") Long  idRegistru);

    //@Query(nativeQuery=true, value="SELECT * FROM incarcare WHERE id_incarcare = (SELECT fk_incarcare FROM registru  WHERE id_registru = 41501618);")
}
