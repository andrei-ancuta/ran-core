package ro.uti.ran.core.repository.portal;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ro.uti.ran.core.model.portal.RegistruPortal;
import ro.uti.ran.core.repository.base.RanRepository;

import java.util.List;

/**
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-11-05 11:51
 */
public interface RegistruPortalRepository extends RanRepository<RegistruPortal, Long> {

    @Query("select rp from RegistruPortal rp where rp.incarcare.id = :idIncarcare")
    List<RegistruPortal> findByIncarcare_Id(@Param("idIncarcare") Long idIncarcare);

    RegistruPortal findByIndexRegistru(String indexRegistru);

    @Query("select rp from RegistruPortal rp where rp.indexRegistru in (:registruIndices) order by rp.nomCapitolPortal.cod ASC")
    List<RegistruPortal> findAllByRegistruIndices(@Param("registruIndices") List<String> registruIndices);

    @Query(nativeQuery=true, value="select * from VW_UNPROCESSED")
    List<RegistruPortal> findAllUnProcessed();

}
