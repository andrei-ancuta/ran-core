package ro.uti.ran.core.service.backend.renns;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.uti.ran.core.exception.DateRegistruValidationException;
import ro.uti.ran.core.exception.base.RanBusinessException;
import ro.uti.ran.core.model.registru.Adresa;
import ro.uti.ran.core.repository.registru.AdresaRepository;
import ro.uti.ran.core.service.backend.GeometrieService;

import java.util.Date;
import java.util.List;

/**
 * Created by Dan on 29-Jan-16.
 */
@Service("adresaService")
@Transactional(value = "registruTransactionManager", rollbackFor = RanBusinessException.class)
public class AdresaServiceImpl implements AdresaService {
    private static final Logger logger = LoggerFactory.getLogger(AdresaServiceImpl.class);
    @Autowired
    AdresaRepository adresaRepository;
    @Autowired
    private GeometrieService geometrieService;

    @Override
    public void updateFromRenns(Adresa renns) throws RanBusinessException {
        try {
            List<Adresa> lstAdrese = adresaRepository.findByUidRenns(renns.getUidRenns());
            for (Adresa adresa : lstAdrese) {
                populeazaAdresaFromRenns(adresa, renns);
                //un flag care marcheaza faptul ca este o actualizare din RENNS
                adresa.setRennsModifiedDate(new Date());
                //
                adresaRepository.save(adresa);
                //geometrie
                if (StringUtils.isNotEmpty(renns.getGeometrieGML())) {
                    geometrieService.insertAdresaGIS(adresa.getIdAdresa(), renns.getGeometrieGML(), adresa.getNomJudet().getId());
                }
            }
        } catch (Exception e) {
            throw new DateRegistruValidationException(e.getMessage(), e);
        }
    }

    /**
     * @param adresa destinatie
     * @param renns  sursa
     */
    private void populeazaAdresaFromRenns(Adresa adresa, Adresa renns) {
        //strada
        adresa.setStrada(renns.getStrada());
        //numar strada
        adresa.setNrStrada(renns.getNrStrada());
    }
}
