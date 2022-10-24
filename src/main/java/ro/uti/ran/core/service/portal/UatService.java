package ro.uti.ran.core.service.portal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import ro.uti.ran.core.model.portal.UAT;
import ro.uti.ran.core.repository.portal.UatRepository;

/**
 * Created by Stanciu Neculai on 18-Apr-16.
 */
@Service
public class UatService {

    @Autowired
    private UatRepository uatRepository;

    @Cacheable(value = "nomenclatoare",key = "'NomUat_'.concat(#idUat?.hashCode())")
    public UAT findOne(Long idUat){
        return uatRepository.findOne(idUat);
    }
}
