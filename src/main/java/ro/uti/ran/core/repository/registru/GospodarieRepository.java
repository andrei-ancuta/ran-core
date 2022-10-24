package ro.uti.ran.core.repository.registru;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ro.uti.ran.core.model.registru.Gospodarie;
import ro.uti.ran.core.repository.base.RanRepository;

import java.util.List;

/**
 * Created by Dan on 16-Oct-15.
 */
public interface GospodarieRepository extends RanRepository<Gospodarie, Long> {


    @Query("SELECT g FROM Gospodarie g where LOWER(g.identificator) = LOWER(:identificator) AND g.nomUat.codSiruta = :codSirutaUAT")
    Gospodarie findByUatAndIdentificator(@Param("codSirutaUAT") Integer codSirutaUAT, @Param("identificator") String identificator);

    @Query("SELECT G from Gospodarie G where G.idGospodarie = :idGosp AND G.nomUat.id = :idUat")
    Gospodarie findByiIdGospodarieAndUat(@Param("idGosp") Long idGosp, @Param("idUat")Long idUat);

    @Query("SELECT G from Gospodarie G where G.idGospodarie in (:idGosp) AND G.nomUat.id = :idUat")
   List<Gospodarie> findByIdGospodarieListAndUat(@Param("idGosp") List idGosp, @Param("idUat")Long idUat);

}