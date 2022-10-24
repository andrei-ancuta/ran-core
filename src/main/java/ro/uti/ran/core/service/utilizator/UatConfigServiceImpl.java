package ro.uti.ran.core.service.utilizator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.uti.ran.core.audit.Audit;
import ro.uti.ran.core.audit.AuditOpType;
import ro.uti.ran.core.model.portal.UAT;
import ro.uti.ran.core.model.portal.UATConfig;
import ro.uti.ran.core.repository.portal.UatConfigRepository;
import ro.uti.ran.core.repository.portal.UatRepository;
import ro.uti.ran.core.ws.internal.utilizator.UatConfig;

import static ro.uti.ran.core.audit.AuditOpType.SALVARE_PREFERINTE;

/**
 * Created by Anastasia cea micuta on 1/19/2016.
 */
@Service
@Transactional
public class UatConfigServiceImpl implements UatConfigService {

    @Autowired
    UatConfigRepository uatConfigRepository;

    @Autowired
    UatRepository uatRepository;


    @Override
    public UatConfig getUatConfig(Long idUat) {
        if (idUat == null) {
            throw new IllegalArgumentException("Parametru idUat nedefinit");
        }

        UATConfig repoConfig = uatConfigRepository.findByUat_Id(idUat);
        if (repoConfig == null) {
            repoConfig = new UATConfig();
            repoConfig.setNotificareRaportare(false);
            repoConfig.setTransmitereManuala(false);

            UAT uat = uatRepository.getOne(idUat);
            if (uat == null) {
                throw new IllegalArgumentException("Nu exista UAT in baza de date avand id " + idUat);
            }

            repoConfig.setUat(uat);

            repoConfig = uatConfigRepository.save(repoConfig);
        }

        UatConfig uatConfig = new UatConfig();
        uatConfig.setNotificareRaportare(repoConfig.isNotificareRaportare());
        uatConfig.setTransmitereManuala(repoConfig.isTransmitereManuala());
        return uatConfig;
    }

    @Audit(opType = SALVARE_PREFERINTE)
    @Override
    public void saveUatConfig(Long idUat, UatConfig uatConfig) {
        if (idUat == null) {
            throw new IllegalArgumentException("Parametru idUat nedefinit");
        }

        if (uatConfig == null) {
            throw new IllegalArgumentException("Parametru uatConfig nedefinit");
        }

        UATConfig repoConfig = uatConfigRepository.findByUat_Id(idUat);
        if (repoConfig == null) {
            repoConfig = new UATConfig();

            UAT uat = uatRepository.getOne(idUat);
            if (uat == null) {
                throw new IllegalArgumentException("Nu exista UAT in baza de date avand id " + idUat);
            }
            repoConfig.setUat(uat);

            repoConfig = uatConfigRepository.save(repoConfig);
        }

        repoConfig.setNotificareRaportare(uatConfig.isNotificareRaportare());
        repoConfig.setTransmitereManuala(uatConfig.isTransmitereManuala());

        uatConfigRepository.save(repoConfig);
    }
}
