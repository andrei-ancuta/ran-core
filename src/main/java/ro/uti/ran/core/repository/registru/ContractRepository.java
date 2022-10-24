package ro.uti.ran.core.repository.registru;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ro.uti.ran.core.model.registru.Contract;
import ro.uti.ran.core.repository.base.RanRepository;

import java.util.List;

/**
 * Created by Dan on 09-Nov-15.
 */
public interface ContractRepository extends RanRepository<Contract, Long> {

    @Query("SELECT a1 FROM Contract a1 where a1.gospodarie.idGospodarie = :idGospodarie AND a1.nomTipContract.cod = :codTipContract AND a1.fkNomJudet = :fkNomJudet")
    List<Contract> findByTipContractAndGospodarieAndFkNomJudet(@Param("codTipContract") String codTipContract, @Param("idGospodarie") Long idGospodarie,@Param("fkNomJudet") Long fkNomJudet);

}
