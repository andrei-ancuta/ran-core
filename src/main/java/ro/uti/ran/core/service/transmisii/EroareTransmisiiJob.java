package ro.uti.ran.core.service.transmisii;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ro.uti.ran.core.business.scheduler.annotations.Cluster;
import ro.uti.ran.core.exception.base.RanBusinessException;
import ro.uti.ran.core.model.portal.Parametru;
import ro.uti.ran.core.model.registru.Registru;
import ro.uti.ran.core.repository.registru.RegistruRepository;
import ro.uti.ran.core.service.parametru.ParametruService;
import ro.uti.ran.core.ws.internal.transmitere.ProcesareDateRegistruAsync;

import java.util.List;

/**
 * Created by Stanciu Neculai on 31-Mar-16.
 */
@Service
public class EroareTransmisiiJob {
    private static final Logger log = LoggerFactory.getLogger(EroareTransmisiiJob.class);

    public static final String TRANSMISII_ERROR_MAX_ITEM_TO_PROCESS = "transmisii.error.maxItemToProcess";
    public static final String TRANSMISII_ERROR_RETRY_NB = "transmisii.error.retryNo";
    public static final int DEFAULT_NB_OF_ITEMS_TO_PROCESS = 30000;
    public static final int DEFAULT_TRANSMISII_RETRY_NO = 3;

    @Autowired
    private RegistruRepository registruRepository;

    @Autowired
    @Qualifier("jmsMessageSender")
    private ProcesareDateRegistruAsync procesareDateRegistruAsync;



    @Autowired
    private ParametruService parametruService;

    @Cluster
    @Scheduled(cron = "0 0 22 ? * MON-FRI")
    @Async("eroareTransmisiiJobExecutor")
    public void processErrorRegistryEntry() {
        log.info("#### Start job eroareTransmisiiJobExecutor ...");
        Integer nbOfItemsToProcess = getNrOfItemToProcess();
        Integer nbOfRetries = getRetryNb();
        //PageRequest pageRequest = new PageRequest(0,nbOfItemsToProcess.intValue());
        List<Registru> registruList = registruRepository.findAllByStareRegistruAndTotalProcesare(nbOfRetries);
        log.info("#### Nr de elemente trimise spre coada: "+registruList.size());
        int contor = 0;
        for(Registru registru: registruList){
            try {
                if(registru != null && registru.getIdRegistru() != null) {
                    contor++;
                    StringBuilder sb = new StringBuilder(20);
                    sb.append("#### ")
                            .append(contor)
                            .append(" Trimis spre procesare in coada elementul cu id-ul:")
                            .append(registru.getIdRegistru())
                            .append(" si indexRegistru: ")
                            .append(registru.getIndexRegistru());
                    log.debug(sb.toString());
                    procesareDateRegistruAsync.procesareDateRegistru(registru.getIdRegistru());
                }
            } catch (Exception e) {
                log.error(e.getMessage(),e);
            }
        }
        log.info("#### Stop job eroareTransmisiiJobExecutor ...");
    }


    @Scheduled(cron = "0 0 21 ? * MON-FRI")
    @Async("procesareDateRegistruErori")
    public void procesareDateRegistruErori() throws RanBusinessException {

        log.info("Rulare job procesareDateRegistruErori");

        List<Registru> registruData = registruRepository.getAllErrorUnprocessedData();

        log.info("A fost incarcata lista cu elemente. Total count: "+registruData.size());
        for(Registru registru : registruData) {
            log.info("Procesare incarcare cu id: "+registru.getIdRegistru());
            try {
                procesareDateRegistruAsync.procesareDateRegistru(registru.getIdRegistru());
            } catch (Exception e) {
                log.error(e.getMessage(),e);
            }
        }
    }


    private Integer getRetryNb() {
        Parametru parametru = parametruService.getParametru(TRANSMISII_ERROR_RETRY_NB);
        String paramValue = getParametruValue(parametru);
        if (paramValue != null) {
            return Integer.valueOf(paramValue);
        } else {
            return DEFAULT_TRANSMISII_RETRY_NO;
        }
    }

    private Integer getNrOfItemToProcess() {
        Parametru parametru = parametruService.getParametru(TRANSMISII_ERROR_MAX_ITEM_TO_PROCESS);
        String paramValue = getParametruValue(parametru);
        if (paramValue != null) {
            return Integer.valueOf(paramValue);
        } else {
            return DEFAULT_NB_OF_ITEMS_TO_PROCESS;
        }
    }

    private String getParametruValue(Parametru parametru) {
        if (parametru != null) {
            if (parametru.getValoare() != null && !parametru.getValoare().isEmpty()) {
                return parametru.getValoare();
            }
            if (parametru.getValoareImplicita() != null && !parametru.getValoareImplicita().isEmpty()) {
                return parametru.getValoareImplicita();
            }
            return null;
        } else {
            return null;
        }
    }


}
