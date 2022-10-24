package ro.uti.ran.core.repository.registru;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ro.uti.ran.core.model.registru.Registru;
import ro.uti.ran.core.model.registru.view.ViewRegistruNomStare;
import ro.uti.ran.core.repository.base.RanRepository;

import java.util.List;


/**
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-09-29 13:33
 */
public interface RegistruRepository extends RanRepository<Registru, Long> {

    //@Query("select r from ViewRegistruNomStare r where r.indexRegistru = :indexRegistru")
    //@Query(nativeQuery=true,  value="select ID_REGISTRU, COD, IS_RECIPISA_SEMNATA, RECIPISA, INDEX_REGISTRU from VW_REGISTRU_NOM_STARE  where INDEX_REGISTRU = TO_CHAR (?1)")
  //  ViewRegistruNomStare findByIndexRegistru(String indexRegistru);

    @Query("select count(r) from Registru r where r.nomUat.id = :idUat and (r.modalitateTransmitere = 1 or r.modalitateTransmitere = 2 ) ")
    long getNbTransmisionByUat(@Param("idUat") Long idUat);

    @Query(nativeQuery=true,  value="select * from Registru rp where rp.TOTAL_PROCESARE <= ?1 and rp.FK_NOM_STARE_REGISTRU = 5")
    List<Registru> findAllByStareRegistruAndTotalProcesare(int nbOfRetries);

    @Query(nativeQuery=true, value="select * from VW_ERROR_UNPROCESSED")
    List<Registru> getAllErrorUnprocessedData();
}

