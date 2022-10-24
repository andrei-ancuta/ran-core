package ro.uti.ran.core.repository.portal;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ro.uti.ran.core.model.portal.UtilizatorGospodarie;
import ro.uti.ran.core.repository.base.RanRepository;

import java.util.List;

/**
 * Created by adrian.boldisor on 2/4/2016.
 */
public interface UtilizatorGospodarieRepository extends RanRepository<UtilizatorGospodarie, Long> {

    List<UtilizatorGospodarie> findByIdUtilizatorGospodarie(Long IdUtilizatorGospodarie);

    List<UtilizatorGospodarie> findByidUtilizator(Long idUtilizator);


    List<UtilizatorGospodarie>findByidGospodarie(Long idGospodarie);



    @Query("SELECT  g FROM UtilizatorGospodarie g where g.idUtilizator = :idUser and g.idGospodarie = :idGosp")
    List<UtilizatorGospodarie> findByIdUserAndIdGospodariePj(@Param("idUser") Long idUser, @Param("idGosp") Long idGosp );



}
