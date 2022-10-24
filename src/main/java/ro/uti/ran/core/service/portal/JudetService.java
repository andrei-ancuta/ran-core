package ro.uti.ran.core.service.portal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import ro.uti.ran.core.model.portal.Judet;
import ro.uti.ran.core.repository.portal.JudetRepository;

/**
 * Created by Stanciu Neculai on 18-Apr-16.
 */
@Service
public class JudetService {

    @Autowired
    private JudetRepository judetRepository;

    @Cacheable(value = "nomenclatoare",key = "'NomJudet_'.concat(#idJudet?.hashCode())")
    public Judet findOne(Long idJudet){
        return judetRepository.findOne(idJudet);
    }


}
