package ro.uti.ran.core.service.parametru;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ro.uti.ran.core.config.Profiles;
import ro.uti.ran.core.model.portal.Parametru;
import ro.uti.ran.core.repository.portal.ParametruRepository;

import java.util.List;

/**
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-10-08 17:21
 */
@Component
@Primary
@Profile(Profiles.PRODUCTION)
@Transactional("portalTransactionManager")
public class ParametruServiceImpl implements ParametruService {

    @Autowired
    ParametruRepository parametruRepository;

    @Override
    @Cacheable(value = "parametrii", key = "#_parametru")
    public Parametru getParametru(String _parametru) {
        if(StringUtils.isEmpty(_parametru)){
            throw new IllegalArgumentException("Cod parametru nedefinit");
        }
        Parametru parametru = parametruRepository.findByCod(_parametru);
        
        if(parametru != null) {
        	return parametru;
        }
        
        return parametruRepository.findByDenumire(_parametru);
    }
    
    

    @Override
    public List<Parametru> getListaParametri() {
        return parametruRepository.findAll();
    }

    @Override
    public Parametru salveazaParametru(Parametru parametru) {
        if( parametru == null){
            throw new IllegalArgumentException("Parametru nedefinit");
        }

        //
        // todo: validarile de business
        //

        return parametruRepository.save(parametru);
    }
}
