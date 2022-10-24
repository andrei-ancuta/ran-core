package ro.uti.ran.core.repository.registru;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ro.uti.ran.core.model.registru.MembruPf;
import ro.uti.ran.core.repository.base.RanRepository;

import java.util.List;

/**
 * Created by Dan on 27-Oct-15.
 */
public interface MembruPfRepository extends RanRepository<MembruPf, Long> {

    @Query("SELECT a1 FROM MembruPf a1 where a1.detinatorPf.gospodarie.idGospodarie = :idGospodarie AND a1.nomLegaturaRudenie.cod = :cod")
    List<MembruPf> findByCodLegaturaRudenieAndGospodarie(@Param("cod") String cod, @Param("idGospodarie") Long idGospodarie);

    @Query("SELECT  m FROM MembruPf m " +
            "join m.detinatorPf.gospodarie.detinatorPfs p " +
             "where m.detinatorPf.idDetinatorPf = :idDetinatorPf AND (p.persoanaFizica.cnp = :cnp)")
    List<MembruPf> findMembersCnpByMainCnpAndFkOfDetinatorPf(@Param("idDetinatorPf") Long idDetinatorPf, @Param("cnp") String cnp);

    @Query("SELECT  m FROM MembruPf m " +
            "join m.detinatorPf.gospodarie.detinatorPfs p " +
            "where m.detinatorPf.idDetinatorPf = :idDetinatorPf AND (p.persoanaFizica.nif = :nif)")
    List<MembruPf> findMembersNifByMainNifAndFkOfDetinatorPf(@Param("idDetinatorPf") Long idDetinatorPf, @Param("nif") String nif);

    @Query("SELECT a1.detinatorPf.gospodarie.identificator FROM MembruPf a1 where a1.detinatorPf.gospodarie.isActiv = 1 AND a1.detinatorPf.gospodarie.nomUat.codSiruta = :codSiruta AND a1.detinatorPf.persoanaFizica.cnp = :cnp")
    List<String> unicitateByCnp(@Param("cnp") String cnp, @Param("codSiruta") Integer codSiruta);

    @Query("SELECT a1.detinatorPf.gospodarie.identificator FROM MembruPf a1 where a1.detinatorPf.gospodarie.isActiv = 1 AND a1.detinatorPf.gospodarie.nomUat.codSiruta = :codSiruta AND a1.detinatorPf.persoanaFizica.nif = :nif")
    List<String> unicitateByNif(@Param("nif") String nif, @Param("codSiruta") Integer codSiruta);
}
