package ro.uti.ran.core.repository.portal;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ro.uti.ran.core.model.portal.FluxRegistruCore;
import ro.uti.ran.core.repository.base.RanRepository;

/**
 * Created by Stanciu Neculai on 05.Feb.2016.
 */
public interface FluxRegistruCoreRepository extends RanRepository<FluxRegistruCore,Long> {
    @Query("select f from FluxRegistruCore as f where f.registru.idRegistru = :idRegistru and f.nomStareRegistru.cod = :codStareRegistru")
    List<FluxRegistruCore> findByIdRegistruAndStareRegistru(@Param("idRegistru") long idRegistru,@Param("codStareRegistru") String codStareRegistru);
}
