package ro.uti.ran.core.service.backend.nomenclator;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import ro.uti.ran.core.model.registru.NomCapitol;
import ro.uti.ran.core.service.backend.nomenclator.model.ExportableNomenclator;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Stanciu Neculai on 12.Feb.2016.
 */
@Component
public class GenericCapitolExportableNom<T> implements CapitolExportableNom<T> {
    private static final String DISTINCT_NOM_CAPITOL_QUERY=" SELECT DISTINCT FK_NOM_CAPITOL FROM {0} ";
    private static final String GET_ALL_BY_NOM_CAPITOL=" SELECT e FROM {0} e WHERE e.nomCapitol.id = {1} ";

    @PersistenceContext(unitName = "ran-registru")
    private EntityManager entityManager;

    private ExportableNomenclator exportableNomenclator;

    @Override
    public List<Long> getDistinctNomCapitol() {
        return entityManager.createNativeQuery(MessageFormat.format(DISTINCT_NOM_CAPITOL_QUERY,getExportableNomenclator().getTableName())).getResultList();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<T> getAllByNomCapitol(NomCapitol capitol, Pageable pageable) {
        Query query = entityManager.createQuery(MessageFormat.format(GET_ALL_BY_NOM_CAPITOL,getExportableNomenclator().getName(),capitol.getId()),getExportableNomenclator().getNomType().getClazz());
        query.setFirstResult((pageable.getPageNumber())*pageable.getPageSize());
        query.setMaxResults(pageable.getPageSize());
        return query.getResultList();
    }
    private ExportableNomenclator getExportableNomenclator() {
        return exportableNomenclator;
    }

    public void setExportableNomenclator(ExportableNomenclator exportableNomenclator) {
        this.exportableNomenclator = exportableNomenclator;
    }
}
