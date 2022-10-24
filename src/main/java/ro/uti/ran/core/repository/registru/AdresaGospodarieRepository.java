package ro.uti.ran.core.repository.registru;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ro.uti.ran.core.model.registru.AdresaGospodarie;
import ro.uti.ran.core.repository.base.RanRepository;

/**
 * Created by Dan on 08-Jan-16.
 */
public interface AdresaGospodarieRepository extends RanRepository<AdresaGospodarie, Long> {

    @Query("SELECT a FROM AdresaGospodarie a where a.gospodarie.idGospodarie = :idGospodarie AND a.nomTipAdresa.cod = :codTipAdresa")
    AdresaGospodarie findByCodTipAdresaAndGospodarie(@Param("codTipAdresa") String codTipAdresa, @Param("idGospodarie") Long idGospodarie);
}
