package ro.uti.ran.core.service.backend;

import ro.uti.ran.core.model.registru.Nomenclator;

import java.util.Collection;
import java.util.List;

/**
 * Created by Dan on 19-Nov-15.
 */
public interface TotaluriService {

    <T extends Nomenclator> List<T> getParentsForChildrenIds(Collection<Long> childrenIds, String nomEntityName, String nomFieldParentName);

    Number getTotalForParent(Long idGospodarie, Long nomIdParent, String randEntityName, String randEntityFieldValoareName, String randEntityFieldNomName, String nomFieldParentName);

    Number getTotalForParent(Long idGospodarie, Integer an, Long nomIdParent, String randEntityName, String randEntityFieldValoareName, String randEntityFieldNomName, String nomFieldParentName);

    Number getTotalCentralizatorForParent(Integer codSiruta, Integer an, Long nomIdParent, String randEntityName, String randEntityFieldValoareName, String randEntityFieldNomName, String nomFieldParentName);

    Number getTotalForParent(Long idGospodarie, Integer an, Integer semestru, Long nomIdParent, String randEntityName, String randEntityFieldValoareName, String randEntityFieldNomName, String nomFieldParentName);

}
