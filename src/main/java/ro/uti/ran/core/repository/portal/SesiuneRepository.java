package ro.uti.ran.core.repository.portal;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ro.uti.ran.core.model.portal.Sesiune;
import ro.uti.ran.core.repository.base.RanRepository;

import java.util.List;

/**
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-12-22 11:14
 */
public interface SesiuneRepository extends RanRepository<Sesiune,Long> {

    @Query("SELECT s from Sesiune s where s.uidSesiuneHttp = :token order by s.id desc")
    List<Sesiune> findByToken(@Param("token") String token);
}
