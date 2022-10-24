package ro.uti.ran.core.repository.portal;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ro.uti.ran.core.model.portal.RolUtilizator;
import ro.uti.ran.core.repository.base.RanRepository;

import java.util.List;

/**
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-09-16 14:22
 */
public interface RolUtilizatorRepository extends RanRepository<RolUtilizator, Long> {

    RolUtilizator findByUtilizator_IdAndRol_Id(Long idUtilizator, Long idRol);

    RolUtilizator findByUtilizator_IdAndRol_Cod(Long idUtilizator, String codRol);

    List<RolUtilizator> findByUtilizator_Id(Long idUtilizator);

    @Query("select ru from RolUtilizator ru where ru.utilizator.id = :idUtilizator and ru.id in (:idRolUtilizator)")
    List<RolUtilizator> findByUtilizator_IdAndIdRolUtilizator(@Param("idUtilizator")Long idUtilizator, @Param("idRolUtilizator") List<Long> idRolUtilizator);

    List<RolUtilizator> findByUtilizator_NumeUtilizator(String numeUtilizator);

    List<RolUtilizator> findByUtilizator_NumeUtilizatorAndRol_Context_CodAndRol_Activ(String numeUtilizator, String codContext, Boolean active);

    List<RolUtilizator> findByRol_CodAndRol_ActivAndUat_CodSirutaAndUtilizator_Activ(String codRol, Boolean rolActiv, Integer codSiruta, Boolean utilizatorActiv);

    List<RolUtilizator> findByRol_CodAndRol_ActivAndUtilizator_CnpAndUtilizator_Activ(String codRol, Boolean rolActiv, String cnp, Boolean utilizatorActiv);

    List<RolUtilizator> findByRol_CodAndRol_ActivAndUtilizator_NifAndUtilizator_Activ(String codRol, Boolean rolActiv, String nif, Boolean utilizatorActiv);
}
