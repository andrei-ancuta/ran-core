package ro.uti.ran.core.service.registru;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.uti.ran.core.exception.base.RanBusinessException;
import ro.uti.ran.core.model.registru.FluxRegistru;
import ro.uti.ran.core.model.registru.NomStareRegistru;
import ro.uti.ran.core.model.registru.Registru;
import ro.uti.ran.core.model.utils.RanConstants;
import ro.uti.ran.core.repository.registru.FluxRegistruRepository;

import java.text.MessageFormat;
import java.util.Date;

/**
 * Created by Stanciu Neculai on 17.Dec.2015.
 */
@Service
public class FluxRegistruServiceImpl implements FluxRegistruService {
    private static final String MESAJ_STARE_R = "Fisier receptionat cu succes, urmeaza validarea.";
    private static final String MESAJ_STARE_V = "Fisier validat cu succes, urmeaza salvarea.";
    private static final String MESAJ_STARE_I = "Fisier respins pe serviciul de transmitere. Motiv:{0}";
    private static final String MESAJ_STARE_S = "Fisier salvat cu succes.";
    private static final String MESAJ_STARE_E = "Fisier respins pe serviciul de transmitere. Motiv:{0}";

    @Autowired
    private FluxRegistruRepository fluxRegistruRepository;

    @Transactional(value = "registruTransactionManager")
    public void saveFluxRegistru(Registru registru,NomStareRegistru nomStareRegistru, String respinsParam) {
        FluxRegistru fluxRegistru = new FluxRegistru();
        fluxRegistru.setDataStare(new Date());
        fluxRegistru.setMesajStare(getStareMesaj(nomStareRegistru.getCod(), respinsParam));
        fluxRegistru.setNomStareRegistru(nomStareRegistru);
        fluxRegistru.setRegistru(registru);
        fluxRegistruRepository.save(fluxRegistru);
    }

    private String getStareMesaj(String stareRegistruCode, String respinsParam) {
        if (stareRegistruCode.equals(RanConstants.STARE_REGISTRU_RECEPTIONATA_COD)) {
            return MESAJ_STARE_R;
        } else if (stareRegistruCode.equals(RanConstants.STARE_REGISTRU_VALIDATA_COD)) {
            return MESAJ_STARE_V;
        } else if(stareRegistruCode.equals(RanConstants.STARE_REGISTRU_INVALIDATA_COD)){
            return MessageFormat.format(MESAJ_STARE_I, respinsParam);
        } else if (stareRegistruCode.equals(RanConstants.STARE_REGISTRU_SALVATA_COD)){
            return MESAJ_STARE_S;
        } else {
            return MessageFormat.format(MESAJ_STARE_E, respinsParam);
        }
    }

    public String extractRespinsMesaj(RanBusinessException e) {
        StringBuilder sb = new StringBuilder();
        sb.append(" Cod:")
                .append(e.getCode())
                .append(" Mesaj:")
                .append(e.getMessage())
                .append(" Detalii:")
                .append(e.getResourceKey())
                .append(", ")
                .append(e.getSecondaryCode())
                .append(" Hint:")
                .append(e.getHint());
        return sb.toString();
    }
}
