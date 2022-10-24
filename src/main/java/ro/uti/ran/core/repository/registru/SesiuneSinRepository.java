package ro.uti.ran.core.repository.registru;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ro.uti.ran.core.model.registru.SesiuneSin;
import ro.uti.ran.core.repository.base.RanRepository;

/**
 * Created by Stanciu Neculai on 07.Jan.2016.
 */
public interface SesiuneSinRepository extends RanRepository<SesiuneSin, Long> {

    @Query("SELECT s from SesiuneSin s where s.uidSesiuneHttp = :token")
    SesiuneSin findByToken(@Param("token") String token);

}
